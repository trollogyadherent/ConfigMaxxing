package trollogyadherent.configmaxxing;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import trollogyadherent.configmaxxing.util.Util;

public class CommonProxy {
    protected int modEntityID = -1;

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) {
        ConfigMaxxing.confFile = event.getSuggestedConfigurationFile();
        if (Util.isServer()) {
            Config.synchronizeConfigurationServer(event.getSuggestedConfigurationFile(), false);
        } else {
            Config.synchronizeConfigurationClient(event.getSuggestedConfigurationFile(), false, true);
        }
        ConfigMaxxing.debug("I am " + Tags.MODNAME + " at version " + Tags.VERSION + " and group name " + Tags.GROUPNAME);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {

    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

    }

    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void serverStarted(FMLServerStartedEvent event) {

    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }

    public void serverStopped(FMLServerStoppedEvent event) {

    }

    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }
}
