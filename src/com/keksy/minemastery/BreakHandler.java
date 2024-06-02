package com.keksy.minemastery;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class BreakHandler {

    private final StatsManager statsManager;
    private final ConfigManager configManager;
    private final String messagePrefix;
    private final JavaPlugin plugin;
    private final Set<Block> processedBlocks = new HashSet<>();
    private boolean explodeEventInProgress = false;

    public BreakHandler(StatsManager statsManager, ConfigManager configManager, String messagePrefix, JavaPlugin plugin) {
        this.statsManager = statsManager;
        this.configManager = configManager;
        this.messagePrefix = messagePrefix;
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (explodeEventInProgress || !isAllowedBlock(block.getType()) || processedBlocks.contains(block)) {
            return;
        }
        processBlockBreak(player, block, 1, false);
    }

    public void handleTEBlockExplodeEvent(TEBlockExplodeEvent event) {
        Player player = event.getPlayer();
        explodeEventInProgress = true;
        int totalBlocksBroken = 0;
        for (Block block : event.blockList()) {
            if (block != null && block.getType() != Material.AIR && isAllowedBlock(block.getType()) && !processedBlocks.contains(block)) {
                processedBlocks.add(block);
                processBlockBreak(player, block, 1, true);
                totalBlocksBroken++;
            }
        }
        processedBlocks.clear();
        explodeEventInProgress = false;
    }

    private void processBlockBreak(Player player, Block block, int increment, boolean isExplodeEvent) {
        statsManager.setProcessingEvent(player, true);
        statsManager.incrementBlockCount(player, "gem", increment);
        statsManager.setProcessingEvent(player, false);
        if (!isExplodeEvent) {
            //sendBlockBreakMessage(player, increment, "minage");
        }
    }

    private boolean isAllowedBlock(Material material) {
        // Add your logic here to determine if the block type is allowed
        // For now, let's assume all blocks are allowed
        return true;
    }

    private void sendBlockBreakMessage(Player player, int blocksBroken, String source) {
        player.sendMessage(messagePrefix + ChatColor.LIGHT_PURPLE + "Vous avez cassé " + ChatColor.DARK_PURPLE + blocksBroken + " blocs " + ChatColor.LIGHT_PURPLE + "par " + source + "!");
    }
}
