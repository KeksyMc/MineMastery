package com.keksy.minemastery.listeners;

import com.keksy.minemastery.StatsManager;
import com.keksy.minemastery.MineMastery;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

public class TEListener implements Listener {

    private final StatsManager statsManager;

    public TEListener(MineMastery plugin, StatsManager statsManager) {
        this.statsManager = statsManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTEBlockExplode(TEBlockExplodeEvent event) {
        Player player = event.getPlayer();
        statsManager.setExplodeEventInProgress(true);
        for (Block block : event.blockList()) {
            if (block != null && block.getType() != Material.AIR) {
                statsManager.handleBlockBreak(player, block);
            }
        }
        statsManager.setExplodeEventInProgress(false);
    }
}
