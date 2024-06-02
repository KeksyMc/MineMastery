package com.keksy.minemastery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandHandler implements CommandExecutor {

    private final StatsManager statsManager;
    private final ConfigManager configManager;
    private final String messagePrefix;

    public CommandHandler(StatsManager statsManager, ConfigManager configManager, String messagePrefix) {
        this.statsManager = statsManager;
        this.configManager = configManager;
        this.messagePrefix = messagePrefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mm")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("stats")) {
                    handleStatsCommand(sender, args);
                    return true;
                } else if (args[0].equalsIgnoreCase("addpoints")) {
                    handleAddPointsCommand(sender, args);
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    handleReloadCommand(sender);
                    return true;
                }
            } else {
                sender.sendMessage(messagePrefix + ChatColor.RED + "Utilisation : /mm [stats|addpoints|reload]");
            }
        }
        return false;
    }

    private void handleStatsCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerUUID = player.getUniqueId().toString();
                int masteryPoints = statsManager.getStats().getInt(playerUUID + ".points", 0);
                player.sendMessage(messagePrefix + ChatColor.YELLOW + "Vous avez " + masteryPoints + " points de maîtrise.");
            } else {
                Set<String> playerUUIDs = statsManager.getStats().getKeys(false);
                if (playerUUIDs.isEmpty()) {
                    sender.sendMessage(messagePrefix + ChatColor.RED + "Aucun joueur enregistré trouvé.");
                } else {
                    sender.sendMessage(messagePrefix + ChatColor.YELLOW + "Statistiques de tous les joueurs :");
                    for (String uuid : playerUUIDs) {
                        String playerName = statsManager.getStats().getString(uuid + ".name");
                        int masteryPoints = statsManager.getStats().getInt(uuid + ".points", 0);
                        if (playerName != null) {
                            sender.sendMessage(messagePrefix + ChatColor.YELLOW + playerName + " a " + masteryPoints + " points de maîtrise.");
                        } else {
                            sender.sendMessage(messagePrefix + ChatColor.YELLOW + "UUID: " + uuid + " a " + masteryPoints + " points de maîtrise.");
                        }
                    }
                }
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                String targetUUID = target.getUniqueId().toString();
                int masteryPoints = statsManager.getStats().getInt(targetUUID + ".points", 0);
                sender.sendMessage(messagePrefix + ChatColor.YELLOW + target.getName() + " a " + masteryPoints + " points de maîtrise.");
            } else {
                sender.sendMessage(messagePrefix + ChatColor.RED + "Le joueur spécifié est introuvable.");
            }
        }
    }

    private void handleAddPointsCommand(CommandSender sender, String[] args) {
        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                try {
                    int pointsToAdd = Integer.parseInt(args[2]);
                    String targetUUID = target.getUniqueId().toString();
                    statsManager.addMasteryPointsByUUID(targetUUID, pointsToAdd);
                    sender.sendMessage(messagePrefix + ChatColor.GREEN + "Ajouté " + pointsToAdd + " points de maîtrise à " + target.getName() + ".");
                    target.sendMessage(messagePrefix + ChatColor.LIGHT_PURPLE + "Vous avez reçu " + ChatColor.DARK_PURPLE + pointsToAdd + " points de maîtrise " + ChatColor.LIGHT_PURPLE + "!");
                } catch (NumberFormatException e) {
                    sender.sendMessage(messagePrefix + ChatColor.RED + "Le nombre de points doit être un entier.");
                }
            } else {
                sender.sendMessage(messagePrefix + ChatColor.RED + "Le joueur spécifié est introuvable.");
            }
        } else {
            sender.sendMessage(messagePrefix + ChatColor.RED + "Utilisation : /mm addpoints [joueur] [points]");
        }
    }

    private void handleReloadCommand(CommandSender sender) {
        configManager.reloadConfig();
        sender.sendMessage(messagePrefix + ChatColor.GREEN + "Configuration rechargée avec succès !");
        displayConfig(sender);
    }

    private void displayConfig(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Configuration actuelle :");
        for (String type : configManager.getBlocksPerPoint().keySet()) {
            sender.sendMessage(ChatColor.YELLOW + "Type de maîtrise: " + type);
            sender.sendMessage(ChatColor.YELLOW + "  Blocs par point: " + configManager.getBlocksPerPoint(type));
            sender.sendMessage(ChatColor.YELLOW + "  Niveaux requis:");
            for (Map.Entry<Integer, Integer> entry : configManager.getLevelRequirements().get(type).entrySet()) {
                sender.sendMessage(ChatColor.YELLOW + "    Niveau " + entry.getKey() + ": " + entry.getValue() + " points");
            }
            sender.sendMessage(ChatColor.YELLOW + "  Récompenses:");
            for (Map.Entry<Integer, List<String>> entry : configManager.getRewards().get(type).entrySet()) {
                sender.sendMessage(ChatColor.YELLOW + "    Niveau " + entry.getKey() + ":");
                for (String reward : entry.getValue()) {
                    sender.sendMessage(ChatColor.YELLOW + "      - " + reward);
                }
            }
        }
    }
}
