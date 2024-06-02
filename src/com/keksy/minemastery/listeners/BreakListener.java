package com.keksy.minemastery.listeners;

import com.keksy.minemastery.BreakHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {

    private final BreakHandler breakHandler;

    public BreakListener(BreakHandler breakHandler) {
        this.breakHandler = breakHandler;
        Bukkit.getPluginManager().registerEvents(this, breakHandler.getPlugin());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        breakHandler.handleBlockBreakEvent(event);
    }
}
