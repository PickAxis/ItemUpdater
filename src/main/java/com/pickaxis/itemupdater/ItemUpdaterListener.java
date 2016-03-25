package com.pickaxis.itemupdater;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ItemUpdaterListener implements Listener
{
    @EventHandler
    public void onInventoryOpen( InventoryOpenEvent event )
    {
        ItemUpdaterPlugin.getInstance().getUpdater().updateInventory( event.getInventory() );
    }
    
    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event )
    {
        ItemUpdaterPlugin.getInstance().getUpdater().updateInventory( event.getPlayer().getInventory() );
    }
}
