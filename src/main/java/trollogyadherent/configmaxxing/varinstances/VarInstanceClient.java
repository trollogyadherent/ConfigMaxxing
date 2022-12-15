package trollogyadherent.configmaxxing.varinstances;

import cpw.mods.fml.client.config.GuiEditArrayEntries;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import trollogyadherent.configmaxxing.ConfigMaxxing;
import trollogyadherent.configmaxxing.configpickers.mob.MobRenderTicker;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class VarInstanceClient {
    public Field entryListField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "entryList");
    public Field currentValuesField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "currentValues");
    public Field beforeValuesField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "beforeValues");
    public Field enabledField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "enabled");
    public Field owningGuiField = ReflectionHelper.findField(GuiEditArrayEntries.class, "owningGui");
    //public Field owningGuiGuiEditArrayField = ReflectionHelper.findField(GuiEditArray.class, "owningGui");
    public Field btnDoneField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "btnDone");
    public Field btnDefaultField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "btnDefault");
    public Field btnUndoChangesField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "btnUndoChanges");
    public Field chkApplyGloballyField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiConfig.class, "chkApplyGlobally");

    public Field providersField = ReflectionHelper.findField(DimensionManager.class, "providers");
    public Hashtable<Integer, Class<? extends WorldProvider>> providers;


    public MobRenderTicker mobRenderTicker;

    public VarInstanceClient() {
        entryListField.setAccessible(true);
        currentValuesField.setAccessible(true);
        beforeValuesField.setAccessible(true);
        owningGuiField.setAccessible(true);
        btnDoneField.setAccessible(true);
        btnDefaultField.setAccessible(true);
        btnUndoChangesField.setAccessible(true);
        enabledField.setAccessible(true);
        chkApplyGloballyField.setAccessible(true);
        providersField.setAccessible(true);
    }

    public void postInitHook() {
        this.mobRenderTicker = new MobRenderTicker();
        this.mobRenderTicker.register();
        try {
            providers = (Hashtable<Integer, Class<? extends WorldProvider>>) providersField.get(null);
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Reflection failed");
            e.printStackTrace();
        }
    }
}
