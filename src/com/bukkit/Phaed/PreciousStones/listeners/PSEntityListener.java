package com.bukkit.Phaed.PreciousStones.listeners;

import java.util.LinkedList;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.bukkit.Phaed.PreciousStones.PreciousStones;
import com.bukkit.Phaed.PreciousStones.managers.SettingsManager.FieldSettings;
import com.bukkit.Phaed.PreciousStones.vectors.Field;

/**
 * PreciousStones entity listener
 * 
 * @author Phaed
 */
public class PSEntityListener extends EntityListener
{
    private final PreciousStones plugin;
    
    public PSEntityListener(PreciousStones plugin)
    {
	this.plugin = plugin;
    }
    
    @Override
    public void onEntityExplode(EntityExplodeEvent event)
    {
	for (Block block : event.blockList())
	{
	    if (plugin.settings.isUnbreakableType(block))
		event.setCancelled(true);
	    
	    if (plugin.settings.isFieldType(block))
		event.setCancelled(true);
	    
	    LinkedList<Field> fields = plugin.ffm.getSourceFields(block);
	    
	    for (Field field : fields)
	    {
		FieldSettings fieldsettings = plugin.settings.getFieldSettings(field);
		
		if (fieldsettings.preventExplosions)
		{
		    if (fieldsettings.guarddogMode && plugin.ffm.allowedAreOnline(field))
		    {
			// plugin.cm.notifyGuardDog((Player) event.getEntity(), field, "tnt explosion");
			plugin.cm.notifyGuardDog(null, field, "creeper explosion");
			continue;
		    }
		    
		    event.setCancelled(true);
		    break;
		}
	    }
	}
    }
    
    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
	// prevent pvp
	
	if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
	{
	    Player attacker = (Player) event.getDamager();
	    Player victim = (Player) event.getEntity();
	    
	    LinkedList<Field> fields = plugin.ffm.getSourceFields(victim);
	    
	    for (Field field : fields)
	    {
		FieldSettings fieldsettings = plugin.settings.getFieldSettings(field);
		
		if (fieldsettings.preventPvP)
		{
		    if (fieldsettings.guarddogMode && plugin.ffm.allowedAreOnline(field))
		    {
			plugin.cm.notifyGuardDog(attacker, field, "pvp");
			continue;
		    }
		    
		    if (PreciousStones.Permissions.has(attacker, "preciousstones.bypass.pvp"))
		    {
			plugin.cm.warnBypassPvP(attacker, victim, field);
		    }
		    else
		    {
			event.setCancelled(true);
			plugin.cm.warnPvP(attacker, victim, field);
		    }
		    break;
		}
	    }
	}
    }
}