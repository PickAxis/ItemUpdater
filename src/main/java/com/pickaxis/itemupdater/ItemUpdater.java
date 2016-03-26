package com.pickaxis.itemupdater;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.server.v1_9_R1.DataConverterPotionId;
import net.minecraft.server.v1_9_R1.DataConverterSpawnEgg;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Updates Potions and Spawn Eggs to their new 1.9 data format.
 */
@Getter( AccessLevel.PRIVATE )
public class ItemUpdater
{
    private final DataConverterPotionId potionConverter;
    
    private final DataConverterSpawnEgg spawnEggConerter;
    
    private final Field handleField;
    
    ItemUpdater() throws NoSuchFieldException, SecurityException
    {
        this.potionConverter = new DataConverterPotionId();
        this.spawnEggConerter = new DataConverterSpawnEgg();
        this.handleField = CraftItemStack.class.getDeclaredField( "handle" );
    }
    
    /**
     * Updates an ItemStack.
     * 
     * @param i The ItemStack to update.
     */
    public boolean updateItem( ItemStack i )
    {
        if( i.getDurability() == 0 || !( i.getType() == Material.POTION || i.getType() == Material.MONSTER_EGG ) )
        {
            return false;
        }
        
        try
        {
            NBTTagCompound tag = new NBTTagCompound();
            this.getHandleField().setAccessible( true );
            net.minecraft.server.v1_9_R1.ItemStack mi = ( (net.minecraft.server.v1_9_R1.ItemStack) this.getHandleField().get( i ) );
            this.getHandleField().setAccessible( false );
            mi.save( tag );
            
            switch( i.getType() )
            {
                case POTION:
                    mi.c( this.getPotionConverter().a( tag ) );
                    break;
                case MONSTER_EGG:
                    mi.c( this.getSpawnEggConerter().a( tag ) );
                    break;
            }
            
            if( ItemUpdaterPlugin.getInstance().isDebug() )
            {
                ItemUpdaterPlugin.getInstance().getLogger().log( Level.INFO, "Updated item: {0}", i.toString() );
            }
        }
        catch( IllegalAccessException | IllegalArgumentException | SecurityException ex )
        {
            Logger.getLogger( ItemUpdaterListener.class.getName() ).log( Level.SEVERE, null, ex );
        }
        
        return true;
    }
    
    /**
     * Updates all ItemStacks in an Inventory.
     * 
     * @param inv The Inventory to update.
     */
    public void updateInventory( Inventory inv )
    {
        long startTime = System.nanoTime();
        boolean updated = false;
        
        for( ItemStack i : inv.getContents() )
        {
            if( i != null )
            {
                updated = this.updateItem( i ) || updated;
            }
        }
        
        if( ItemUpdaterPlugin.getInstance().isDebug() && updated )
        {
            ItemUpdaterPlugin.getInstance().getLogger().log( Level.INFO, "{0} updated in {1}ns.", new Object[]{ inv.toString(), 
                                                                                                                System.nanoTime() - startTime } );
        }
    }
}
