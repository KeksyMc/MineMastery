package com.keksy.minemastery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MineMastery extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private File statsFile;
    private FileConfiguration stats;
    private Map<Player, Integer> blockCount;
    private Map<String, Integer> blocksPerPoint;
    private Map<String, Map<Integer, Integer>> levelRequirements;
    private Map<String, Map<Integer, List<String>>> rewards;

    @Override
    public void onEnable() {
        // Save default config and add comments
        saveDefaultConfig();
        config = getConfig();
        addConfigComments();

        // Load mastery configurations
        loadMasteryConfig();

        // Initialize stats file
        createStatsFile();

        // Register events
        getServer().getPluginManager().registerEvents(this, this);

        // Initialize block count
        blockCount = new HashMap<>();

        // Register commands
        PluginCommand command = this.getCommand("mm");
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public void onDisable() {
        saveStats();
    }

    @SuppressWarnings("deprecation")
    private void addConfigComments() {
        config.options().header("MineMastery Configuration\nConfigure the plugin settings here.");
        config.addDefault("exampleSetting", "exampleValue");
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void createStatsFile() {
        statsFile = new File(getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            try {
                statsFile.getParentFile().mkdirs();
                statsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stats = YamlConfiguration.loadConfiguration(statsFile);
    }

    private void saveStats() {
        try {
            stats.save(statsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMasteryConfig() {
        blocksPerPoint = new HashMap<>();
        levelRequirements = new HashMap<>();
        rewards = new HashMap<>();

        // Load mastery types from config
        Set<String> masteryTypes = config.getConfigurationSection("mastery").getKeys(false);
        for (String type : masteryTypes) {
            blocksPerPoint.put(type, config.getInt("mastery." + type + ".points-per-block"));

            Map<Integer, Integer> levels = new HashMap<>();
            Map<Integer, List<String>> levelRewards = new HashMap<>();

            // Load level requirements
            Set<String> levelKeys = config.getConfigurationSection("mastery." + type + ".levels").getKeys(false);
            for (String levelKey : levelKeys) {
                int level = Integer.parseInt(levelKey);
                levels.put(level, config.getInt("mastery." + type + ".levels." + levelKey));
            }
            levelRequirements.put(type, levels);

            // Load rewards
            Set<String> rewardKeys = config.getConfigurationSection("mastery." + type + ".rewards").getKeys(false);
            for (String rewardKey : rewardKeys) {
                int level = Integer.parseInt(rewardKey);
                levelRewards.put(level, config.getStringList("mastery." + type + ".rewards." + rewardKey));
            }
            rewards.put(type, levelRewards);
        }
    }

    private void addPlayerToStats(Player player) {
        String playerUUID = player.getUniqueId().toString();
        if (!stats.contains(playerUUID)) {
            stats.set(playerUUID + ".name", player.getName());
            stats.set(playerUUID + ".points", 0);
            saveStats();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        addPlayerToStats(player);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        incrementBlockCount(player, "gem", 1); // Assuming "gem" mastery type, adjust accordingly
    }

    @EventHandler
    public void onTEBlockExplode(TEBlockExplodeEvent event) {
        Player player = event.getPlayer();
        int blocksBroken = event.blockList().size();
        incrementBlockCount(player, "gem", blocksBroken); // Assuming "gem" mastery type, adjust accordingly
    }

    private void incrementBlockCount(Player player, String type, int blocksBroken) {
        int count = blockCount.getOrDefault(player, 0) + blocksBroken;
        int blocksPerPoint = this.blocksPerPoint.get(type);

        if (count >= blocksPerPoint) {
            int masteryPointsToAdd = count / blocksPerPoint;
            count %= blocksPerPoint;
            addMasteryPoints(player, type, masteryPointsToAdd);
        }
        blockCount.put(player, count);
    }

    private void addMasteryPoints(Player player, String type, int points) {
        String playerUUID = player.getUniqueId().toString();
        int currentPoints = stats.getInt(playerUUID + ".points", 0);
        stats.set(playerUUID + ".points", currentPoints + points);
        saveStats();
        player.sendMessage(ChatColor.GREEN + "Vous avez gagné " + points + " point(s) de maîtrise !");
        
        // Check for level rewards
        checkLevelRewards(player, type, currentPoints + points);
    }

    private void checkLevelRewards(Player player, String type, int totalPoints) {
        Map<Integer, Integer> levels = levelRequirements.get(type);
        Map<Integer, List<String>> typeRewards = rewards.get(type);

        for (Map.Entry<Integer, Integer> entry : levels.entrySet()) {
            int level = entry.getKey();
            int requiredPoints = entry.getValue();

            if (totalPoints >= requiredPoints) {
                List<String> rewardCommands = typeRewards.get(level);
                if (rewardCommands != null) {
                    for (String command : rewardCommands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                    }
                }
            }
        }
    }

    private void addMasteryPointsByUUID(String uuid, int points) {
        int currentPoints = stats.getInt(uuid + ".points", 0);
        stats.set(uuid + ".points", currentPoints + points);
        saveStats();
    }

    private void reloadConfigAndDisplay(CommandSender sender) {
        reloadConfig();
        config = getConfig();
        loadMasteryConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration rechargée avec succès !");
        displayConfig(sender);
    }

    private void displayConfig(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Configuration actuelle :");
        for (String type : blocksPerPoint.keySet()) {
            sender.sendMessage(ChatColor.YELLOW + "Type de maîtrise: " + type);
            sender.sendMessage(ChatColor.YELLOW + "  Blocs par point: " + blocksPerPoint.get(type));
            sender.sendMessage(ChatColor.YELLOW + "  Niveaux requis:");
            for (Map.Entry<Integer, Integer> entry : levelRequirements.get(type).entrySet()) {
                sender.sendMessage(ChatColor.YELLOW + "    Niveau " + entry.getKey() + ": " + entry.getValue() + " points");
            }
            sender.sendMessage(ChatColor.YELLOW + "  Récompenses:");
            for (Map.Entry<Integer, List<String>> entry : rewards.get(type).entrySet()) {
                sender.sendMessage(ChatColor.YELLOW + "    Niveau " + entry.getKey() + ":");
                for (String reward : entry.getValue()) {
                    sender.sendMessage(ChatColor.YELLOW + "      - " + reward);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mm")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("stats")) {
                    if (args.length == 1) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            String playerUUID = player.getUniqueId().toString();
                            int masteryPoints = stats.getInt(playerUUID + ".points", 0);
                            player.sendMessage(ChatColor.YELLOW + "Vous avez " + masteryPoints + " points de maîtrise.");
                        } else {
                            // Command executed from the console, display stats for all players
                            Set<String> playerUUIDs = stats.getKeys(false);
                            if (playerUUIDs.isEmpty()) {
                                sender.sendMessage(ChatColor.RED + "Aucun joueur enregistré trouvé.");
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + "Statistiques de tous les joueurs :");
                                for (String uuid : playerUUIDs) {
                                    String playerName = stats.getString(uuid + ".name");
                                    int masteryPoints = stats.getInt(uuid + ".points", 0);
                                    if (playerName != null) {
                                        sender.sendMessage(ChatColor.YELLOW + playerName + " a " + masteryPoints + " points de maîtrise.");
                                    } else {
                                        sender.sendMessage(ChatColor.YELLOW + "UUID: " + uuid + " a " + masteryPoints + " points de maîtrise.");
                                    }
                                }
                            }
                        }
                        return true;
                    } else if (args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            String targetUUID = target.getUniqueId().toString();
                            int masteryPoints = stats.getInt(targetUUID + ".points", 0);
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + " a " + masteryPoints + " points de maîtrise.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Le joueur spécifié est introuvable.");
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("addpoints")) {
                    if (args.length == 3) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            try {
                                int pointsToAdd = Integer.parseInt(args[2]);
                                String targetUUID = target.getUniqueId().toString();
                                addMasteryPointsByUUID(targetUUID, pointsToAdd);
                                sender.sendMessage(ChatColor.GREEN + "Ajouté " + pointsToAdd + " points de maîtrise à " + target.getName() + ".");
                                target.sendMessage(ChatColor.GREEN + "Vous avez reçu " + pointsToAdd + " points de maîtrise !");
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.RED + "Le nombre de points doit être un entier.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Le joueur spécifié est introuvable.");
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Utilisation : /mm addpoints [joueur] [points]");
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    reloadConfigAndDisplay(sender);
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Utilisation : /mm [stats|addpoints|reload]");
            }
        }
        return false;
    }
}
