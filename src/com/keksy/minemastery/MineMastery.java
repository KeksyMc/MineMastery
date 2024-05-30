package com.keksy.minemastery;

import com.keksy.minemastery.listeners.MiningListener;
import com.keksy.minemastery.models.PlayerMastery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MineMastery extends JavaPlugin {

    private Map<UUID, PlayerMastery> playerMasteryData;
    private MineMasteryConfig config;

    @Override
    public void onEnable() {
        getLogger().info("Le plugin MineMastery démarre...");
        this.config = new MineMasteryConfig(this);
        config.loadConfigurationFile();
        
        PluginManager pluginManager = getServer().getPluginManager();

        // Enregistrer l'écouteur MiningListener
        pluginManager.registerEvents(new MiningListener(this), this);
       

        int gemPointsPerBlock = config.getGemPointsPerBlock();
        getLogger().info("Gem Points per Block: " + gemPointsPerBlock);

        int gemLevel1 = config.getGemLevel(1);
        getLogger().info("Gem Level 1 Points: " + gemLevel1);

        List<String> gemRewards1 = config.getGemRewards(1);
        getLogger().info("Gem Rewards Level 1: " + gemRewards1);

        int metalPointsPerBlock = config.getMetalPointsPerBlock();
        getLogger().info("Metal Points per Block: " + metalPointsPerBlock);

        int metalLevel1 = config.getMetalLevel(1);
        getLogger().info("Metal Level 1 Points: " + metalLevel1);

        List<String> metalRewards1 = config.getMetalRewards(1);
        getLogger().info("Metal Rewards Level 1: " + metalRewards1);
        
        new MineMasteryPlaceholder(this).register();
        this.playerMasteryData = new HashMap<>();
        this.getCommand("minemastery").setExecutor(new MineMasteryCommandExecutor());
        
        getLogger().info("Le plugin MineMastery est prêt à l'utilisation !");
    }
    
    public PlayerMastery getPlayerMastery(UUID playerUUID) {
        return playerMasteryData.computeIfAbsent(playerUUID, PlayerMastery::new);
    }
    
    public class MineMasteryCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 0) {
                sender.sendMessage("Utilisez /minemastery stats pour voir vos statistiques.");
                return true;
            }

            if (args[0].equalsIgnoreCase("stats")) {
                if (args.length == 1) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        PlayerMastery playerMastery = getPlayerMastery(player.getUniqueId());
                        player.sendMessage("§aVos statistiques de maîtrise :");
                        player.sendMessage("§ePoints de maîtrise : §b" + playerMastery.getMasteryPoints());
                    } else {
                        sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
                    }
                } else if (args.length == 2 && sender.hasPermission("minemastery.admin")) {
                    Player target = getServer().getPlayer(args[1]);
                    if (target != null) {
                        PlayerMastery playerMastery = getPlayerMastery(target.getUniqueId());
                        sender.sendMessage("§aStatistiques de maîtrise de " + target.getName() + " :");
                        sender.sendMessage("§ePoints de maîtrise : §b" + playerMastery.getMasteryPoints());
                    } else {
                        sender.sendMessage("Le joueur spécifié est introuvable ou hors ligne.");
                    }
                } else {
                    sender.sendMessage("Usage: /minemastery stats [player]");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("top")) {
                // Code pour afficher les meilleurs joueurs
                return true;
            }

            if (args[0].equalsIgnoreCase("rewards")) {
                // Code pour afficher les récompenses
                return true;
            }

            if (args[0].equalsIgnoreCase("setpoints")) {
                if (args.length == 3 && sender.hasPermission("minemastery.admin")) {
                    // Code pour définir les points d'un joueur
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("addpoints")) {
                if (args.length == 3 && sender.hasPermission("minemastery.admin")) {
                    // Code pour ajouter des points à un joueur
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("removepoints")) {
                if (args.length == 3 && sender.hasPermission("minemastery.admin")) {
                    // Code pour retirer des points à un joueur
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("minemastery.admin")) {
                    // Code pour recharger la configuration
                    return true;
                }
            }

            return false;
        }
    }
}
