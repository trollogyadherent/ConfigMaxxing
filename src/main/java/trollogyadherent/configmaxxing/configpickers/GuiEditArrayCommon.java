package trollogyadherent.configmaxxing.configpickers;

import cpw.mods.fml.client.config.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import trollogyadherent.configmaxxing.ConfigMaxxing;

import java.lang.reflect.Method;
import java.util.Arrays;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

/* This is the gui with the list (contains the widget with the entry buttons, and + - buttons) */

public class GuiEditArrayCommon extends cpw.mods.fml.client.config.GuiEditArray {
    public Object[] beforeValuesReflected;
    public Object[] currentValuesReflected;
    Method saveListChangesMethod;
    public GuiConfig parentGuiConfigScreen;
    public Object[] thisCurrentValues;
    public EntryPoint entryPoint;

    public GuiEditArrayCommon(GuiConfig parentScreen, EntryPoint entryPoint, IConfigElement configElement, int slotIndex, Object[] currentValues, boolean enabled) {
        super(parentScreen, configElement, slotIndex, currentValues, enabled);
        parentGuiConfigScreen = parentScreen;
        thisCurrentValues = currentValues;
        this.entryPoint = entryPoint;

        try {
            GuiEditArrayEntriesCommon guiEditArrayEntriesCommon = new GuiEditArrayEntriesCommon(parentScreen, this, entryPoint, this.configElement, new Object[0], new Object[0], new ReturnInfo(parentGuiConfigScreen, configElement, slotIndex, enabled, thisCurrentValues));
            ConfigMaxxing.varInstanceClient.entryListField.set(this, guiEditArrayEntriesCommon);
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Reflection failed");
            e.printStackTrace();
        }
        ConfigMaxxing.debug("Constructing GuiEditArrayCommon");
    }

    public static class ReturnInfo {
        public GuiConfig parentScreen;
        public IConfigElement configElement;
        public int slotIndex;
        public boolean enabled;
        public Object[] values;

        public ReturnInfo(GuiConfig parentScreen, IConfigElement configElement, int slotIndex, boolean enabled, Object[] values) {
            this.parentScreen = parentScreen;
            this.configElement = configElement;
            this.slotIndex = slotIndex;
            this.enabled = enabled;
            this.values = values;
        }
    }

    @Override
    public void initGui() {
        int undoGlyphWidth = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
        int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
        int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
        int undoWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
        int resetWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;

        try {
            ConfigMaxxing.varInstanceClient.btnDoneField.set(this, new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
            this.buttonList.add(ConfigMaxxing.varInstanceClient.btnDoneField.get(this));

            ConfigMaxxing.varInstanceClient.btnDefaultField.set(this, new GuiUnicodeGlyphButton(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, " " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
            this.buttonList.add(ConfigMaxxing.varInstanceClient.btnDefaultField.get(this));

            ConfigMaxxing.varInstanceClient.btnUndoChangesField.set(this, new GuiUnicodeGlyphButton(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
            this.buttonList.add(ConfigMaxxing.varInstanceClient.btnUndoChangesField.get(this));
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Failed to reflect");
            e.printStackTrace();
            return;
        }

        try {
            ConfigMaxxing.debug("entries:");
            GuiEditArrayEntries entryList = (GuiEditArrayEntries) ConfigMaxxing.varInstanceClient.entryListField.get(this);
            if (entryList == null) {
                ConfigMaxxing.debug("entryList null");
                return;
            }
            if (entryList.listEntries == null) {
                ConfigMaxxing.debug("entryList.listEntries null");
                return;
            }
            for (GuiEditArrayEntries.IArrayEntry iArrayEntry : entryList.listEntries) {
                GuiEditArrayEntries.BaseEntry baseEntry = (GuiEditArrayEntries.BaseEntry) iArrayEntry;
                ConfigMaxxing.debug(String.valueOf(baseEntry.getValue()));
            }
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Failed to reflect fields! (3)");
            e.printStackTrace();
        }

        try {
            beforeValuesReflected = (Object[]) ConfigMaxxing.varInstanceClient.beforeValuesField.get(this);
            currentValuesReflected = (Object[]) ConfigMaxxing.varInstanceClient.currentValuesField.get(this);

            //this.entryList = new GuiEditArrayEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
            //super.entryList = new GuiEditArrayEntriesPotionId(this, this.mc, this.configElement, beforeValuesReflected, currentValuesReflected);

            /*  */
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Failed to reflect");
            e.printStackTrace();
            return;
        }

        try {
            GuiEditArrayEntriesCommon guiEditArrayEntriesCommon = new GuiEditArrayEntriesCommon(parentScreen, this, entryPoint, this.configElement, beforeValuesReflected, currentValuesReflected, new ReturnInfo(parentGuiConfigScreen, configElement, slotIndex, enabled, thisCurrentValues));
            ConfigMaxxing.varInstanceClient.entryListField.set(this, guiEditArrayEntriesCommon);
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Reflection failed");
            e.printStackTrace();
        }
    }

    public void actionResetToDefault() {
        ConfigMaxxing.debug("actionResetToDefault called");
        try {
            //this.currentValues = configElement.getDefaults();
            ConfigMaxxing.varInstanceClient.currentValuesField.set(this, configElement.getDefaults());
            //this.entryList = new GuiEditArrayEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
            beforeValuesReflected = (Object[]) ConfigMaxxing.varInstanceClient.beforeValuesField.get(this);
            currentValuesReflected = (Object[]) ConfigMaxxing.varInstanceClient.currentValuesField.get(this);
            /*  */
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Failed to reflect fields! (1)");
            e.printStackTrace();
        }

        try {
            GuiEditArrayEntriesCommon guiEditArrayEntriesCommon = new GuiEditArrayEntriesCommon(parentScreen, this, entryPoint, this.configElement, beforeValuesReflected, currentValuesReflected, new ReturnInfo(parentGuiConfigScreen, configElement, slotIndex, enabled, thisCurrentValues));
            ConfigMaxxing.varInstanceClient.entryListField.set(this, guiEditArrayEntriesCommon);
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Reflection failed");
            e.printStackTrace();
        }
    }

    public void actionSave() {
        ConfigMaxxing.debug("actionSave called");
        try {
            //this.entryList.saveListChanges();
            GuiEditArrayEntries entryList = (GuiEditArrayEntries) ConfigMaxxing.varInstanceClient.entryListField.get(this);
            saveListChangesMethod  = ReflectionHelper.findMethod(GuiEditArrayEntries.class, entryList, new String[]{"saveListChanges"});
            saveListChangesMethod.invoke(entryList);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void actionUndoChanges() {
        ConfigMaxxing.debug("actionUndoChanges called");
        try {
            //this.currentValues = Arrays.copyOf(beforeValues, beforeValues.length);
            Object[] beforeValues = (Object[]) ConfigMaxxing.varInstanceClient.beforeValuesField.get(this);
            ConfigMaxxing.varInstanceClient.currentValuesField.set(this, Arrays.copyOf(beforeValues, beforeValues.length));
            //this.entryList = new GuiEditArrayEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
            Object[] currentValues = (Object[]) ConfigMaxxing.varInstanceClient.currentValuesField.get(this);
            ConfigMaxxing.varInstanceClient.entryListField.set(this, new GuiEditArrayEntries(this, this.mc, this.configElement, beforeValues, currentValues));
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Failed to reflect fields! (2)");
            e.printStackTrace();
        }
    }

    public void actionDisplayPreviousScreen() {
        ConfigMaxxing.debug("actionDisplayPreviousScreen called");
        //((GuiConfig)this.parentScreen).needsRefresh = true;
        //this.entryPoint.getConfigElement().haschanged = true;
        this.configElement.set(thisCurrentValues);

        entryPoint.updateValueButtonText();
        this.mc.displayGuiScreen(this.parentScreen);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 2000) {
            ConfigMaxxing.debug("Pressed button ID 2000");
            actionSave();
            actionDisplayPreviousScreen();
        } else if (button.id == 2001) {
            ConfigMaxxing.debug("Pressed button ID 2001");
            actionResetToDefault();
        } else if (button.id == 2002) {
            ConfigMaxxing.debug("Pressed button ID 2002");
            actionUndoChanges();
        }
    }
}
