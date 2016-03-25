package com.pickaxis.itemupdater;

import java.util.logging.Level;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter( AccessLevel.PRIVATE )
public class ItemUpdaterPlugin extends JavaPlugin
{
    @Getter
    @Setter( AccessLevel.PRIVATE )
    private static ItemUpdaterPlugin instance;
    
    private ItemUpdater updater;
    
    private boolean debug;
    
    public ItemUpdaterPlugin()
    {
        super();
        
        ItemUpdaterPlugin.setInstance( this );
    }
    
    @Override
    public void onEnable()
    {
        this.setDebug( this.getConfig().getBoolean( "debug", true ) );
        
        try
        {
            this.setUpdater( new ItemUpdater() );
            this.getServer().getPluginManager().registerEvents( new ItemUpdaterListener(), this );
        }
        catch( NoSuchFieldException | SecurityException ex )
        {
            this.getLogger().log( Level.SEVERE, "Couldn't initialize listener.", ex );
            this.getServer().getPluginManager().disablePlugin( this );
        }
    }
    
    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll( this );
    }
    
    /**
     * Log a debug message if debugging is enabled.
     * 
     * @param message The message to log.
     */
    public void debug( String message )
    {
        if( this.isDebug() )
        {
            this.getLogger().log( Level.INFO, message );
        }
    }
}
