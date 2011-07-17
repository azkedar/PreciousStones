package net.sacredlabyrinth.Phaed.PreciousStones.listeners;

import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import org.bukkit.event.server.ServerListener;

import net.sacredlabyrinth.register.payment.Methods;
import java.util.logging.Level;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

/**
 *
 * @author phaed
 */
public class PSServerListener extends ServerListener
{
    private final PreciousStones plugin;
    private Methods Methods;

    /**
     *
     * @param plugin
     */
    public PSServerListener(PreciousStones plugin)
    {
        this.plugin = plugin;
        Methods = new Methods();
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event)
    {
        if (Methods != null && Methods.hasMethod())
        {
            Boolean check = Methods.checkDisabled(event.getPlugin());

            if (check)
            {
                plugin.Method = null;
            }
        }
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event)
    {
        if (!Methods.hasMethod())
        {
            if (Methods.setMethod(event.getPlugin()))
            {
                plugin.Method = Methods.getMethod();
                PreciousStones.log(Level.INFO, "Payment method: {0} v{1}", plugin.Method.getName(), plugin.Method.getVersion());
            }
        }
    }
}
