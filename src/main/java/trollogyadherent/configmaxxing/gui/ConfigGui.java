package trollogyadherent.configmaxxing.gui;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import trollogyadherent.configmaxxing.Config;
import trollogyadherent.configmaxxing.ConfigMaxxing;
import trollogyadherent.configmaxxing.Tags;

public class ConfigGui extends GuiConfig {

    private static IConfigElement ceGeneral = new ConfigElement(Config.config.getCategory(Config.Categories.general));
    private static IConfigElement ceDebug = new ConfigElement(Config.config.getCategory(Config.Categories.debug));
    private static IConfigElement ceExamples = new ConfigElement(Config.config.getCategory(Config.Categories.examples));
    private static IConfigElement ceTest = new ConfigElement(Config.config.getCategory(Config.Categories.test));

    public ConfigGui(GuiScreen parent) {
        //this.parentScreen = parent;
        super(parent, ImmutableList.of(ceGeneral, ceDebug, ceExamples, ceTest), Tags.MODID, Tags.MODID, false, false, I18n.format(Tags.MODID + ".configgui.title"), ConfigMaxxing.confFile.getAbsolutePath());
        ConfigMaxxing.debug("Instantiating config gui");
    }

    @Override
    public void initGui()
    {
        // You can add buttons and initialize fields here
        super.initGui();
        ConfigMaxxing.debug("Initializing config gui");
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // You can do things like create animations, draw additional elements, etc. here
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        ConfigMaxxing.debug("Config button id " + b.id + " pressed");
        super.actionPerformed(b);
        /* "Done" button */
        if (b.id == 2000) {
            /* Syncing config */
            Config.synchronizeConfigurationClient(ConfigMaxxing.confFile, true, false);
        }
    }

}
