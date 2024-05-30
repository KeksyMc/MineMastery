package com.keksy.minemastery.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.keksy.minemastery.MineMastery;
import com.keksy.minemastery.models.PlayerMastery;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

public class MiningListener implements Listener {
    private final MineMastery plugin;

    public MiningListener(MineMastery plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        player.sendMessage("Vous avez cassé un bloc : " + block.getType());

        // Récupérer l'instance de PlayerMastery pour ce joueur
        PlayerMastery playerMastery = plugin.getPlayerMastery(player.getUniqueId());
        
        // Ajouter un point de maîtrise
        playerMastery.addMasteryPoints(1);
        
        // Afficher un message au joueur avec les points mis à jour
        int updatedPoints = playerMastery.getMasteryPoints();
        player.sendMessage("§a+1 point de maîtrise pour avoir miné un bloc !");
        player.sendMessage("§aVos points de maîtrise : §b" + updatedPoints);
        
        
        
        // Autres logiques ici (mise à jour de l'affichage, vérification des récompenses, etc.)
    }
}