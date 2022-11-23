package trollogyadherent.configmaxxing.configpickers;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiEditArray;
import cpw.mods.fml.client.config.GuiEditArrayEntries;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import trollogyadherent.configmaxxing.ConfigMaxxing;
import trollogyadherent.configmaxxing.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

/* The widget containing the potion list entries. it contains the + - buttons and the potion buttons. */
public class GuiEditArrayEntriesCommon extends GuiEditArrayEntries {
    static GuiEditArrayCommon owningGuiReflected;
    public GuiEditArray parent;
    public GuiScreen parentScreen;
    public static GuiEditArrayCommon.ReturnInfo returnInfo;
    public static GuiEditArrayEntriesCommon this_;
    static EntryPoint entryPoint;

    public GuiEditArrayEntriesCommon(GuiScreen parentScreen, GuiEditArray parent, EntryPoint entryPoint, IConfigElement configElement, Object[] beforeValues, Object[] currentValues, GuiEditArrayCommon.ReturnInfo returnInfo) {
        super(parent, Minecraft.getMinecraft(), configElement, beforeValues, currentValues);
        ConfigMaxxing.debug("Constructing GuiEditArrayEntriesCommon");
        this.parentScreen = parentScreen;
        this.parent = parent;
        this.entryPoint = entryPoint;

        this.listEntries = new ArrayList<>();
        this.returnInfo = returnInfo;
        this_ = this;

        try {
            owningGuiReflected = (GuiEditArrayCommon) ConfigMaxxing.varInstanceClient.owningGuiField.get(this);
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Failed to reflect");
            e.printStackTrace();
            return;
        }
        int i;
        for (i = 0; i < currentValues.length; i++) {
            listEntries.add(new StringEntry(parent, this, configElement, currentValues[i], i, valueToDisplay(currentValues[i])));
        }
        //for (Object value : currentValues)

        if (!configElement.isListLengthFixed()) {
            listEntries.add(new BaseEntryExtended(owningGuiReflected, this, configElement, i));
        }
    }

    public String valueToDisplay(Object value) {
        if (entryPoint == null) {
            return "None";
        }
        return entryPoint.formatValue(value);
    }

    /*
    potion = PotionUtil.getPotionByName((String) value);
            if (potion == null) {
                displayValue = "None";
            } else {
                displayValue = value + ": " + I18n.format(potion.getName());
            }
    * */

    @Override
    public void addNewEntry(int index)
    {
        StringEntry stringEntry = new StringEntry(parent, this, this.configElement, "None", currentValues.length, "None");

        Object[] newArr = Util.addAtIndex(currentValues, index, "None");

        currentValues = newArr;
        returnInfo.values = newArr;
        listEntries.add(index, stringEntry);
        this.recalculateState();
        super.recalculateState();
        this.canAddMoreEntries = !configElement.isListLengthFixed()
                && (configElement.getMaxListLength() == -1 || this.listEntries.size() - 1 < configElement.getMaxListLength());
        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
    }

    @Override
    public void removeEntry(int index)
    {
        for (int i = 0; i < listEntries.size(); i ++) {
            if (i > index) {
                ((BaseEntryExtended)listEntries.get(i)).setIndex(((BaseEntryExtended)listEntries.get(i)).getIndex() - 1);
            }
        }
        Object[] newArr = Util.removeAtIndex(currentValues, index);
        currentValues = newArr;
        returnInfo.values = newArr;
        super.removeEntry(index);
        this.recalculateState();
        super.recalculateState();
    }

    public static class BaseEntryExtended extends BaseEntry {
        GuiEditArrayEntriesCommon owningEntriesPotion;
        private int index;
        GuiButtonExtExtended btn;

        public BaseEntryExtended(GuiEditArray owningScreen, GuiEditArrayEntriesCommon owningEntryList, IConfigElement configElement, int index) {
            super(owningScreen, owningEntryList, configElement);
            owningEntriesPotion = owningEntryList;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
            if (this.btn != null) {
                this.btn.index = index;
            }
        }

        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.btnAddNewEntryAbove.mousePressed(owningEntryList.mc, x, y)) {
                ConfigMaxxing.debug("adding new entry!");
                ConfigMaxxing.debug(Arrays.toString(owningEntryList.currentValues));
                btnAddNewEntryAbove.func_146113_a(owningEntryList.mc.getSoundHandler());
                (owningEntryList).addNewEntry(index);
                owningEntryList.recalculateState();
                ConfigMaxxing.debug("after adding:");
                ConfigMaxxing.debug(Arrays.toString(owningEntryList.currentValues));
                try {
                    Object commonSelectionGui = entryPoint.selectionGuiClass.getConstructors()[0].newInstance(index, returnInfo, entryPoint);
                    owningGuiReflected.mc.displayGuiScreen((GuiScreen) commonSelectionGui);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    ConfigMaxxing.error("Failed to instantiate picker: " + entryPoint.selectionGuiClass.getCanonicalName());
                }
                return true;
            } else if (this.btnRemoveEntry.mousePressed(owningEntryList.mc, x, y)) {
                ConfigMaxxing.debug("removing entry!");
                btnRemoveEntry.func_146113_a(owningEntryList.mc.getSoundHandler());
                owningEntryList.removeEntry(index);
                owningEntryList.recalculateState();
                return true;
            }
            return false;
        }
    }

    public static class GuiButtonExtExtended extends GuiButtonExt {
        GuiEditArray guiEditArrayPotionNameScreen;
        GuiEditArrayEntriesCommon owningEntryList;
        public int index;
        public GuiButtonExtExtended(int id, int xPos, int yPos, int width, int height, String displayString, GuiEditArray guiEditArrayPotionNameScreen, GuiEditArrayEntriesCommon owningEntryList, int index) {
            super(id, xPos, yPos, width, height, displayString);
            this.guiEditArrayPotionNameScreen = guiEditArrayPotionNameScreen;
            this.owningEntryList = owningEntryList;
            this.index = index;
        }

        @Override
        public void mouseReleased(int x, int y) {
            if (x >= this.xPosition && x <= this.xPosition + this.width && y >= this.yPosition && y <= this.yPosition + this.height) {
                ConfigMaxxing.debug("Clicked me!");

                ConfigMaxxing.debug("currentValues before saving:");
                ConfigMaxxing.debug(Arrays.toString(owningGuiReflected.currentValuesReflected));
                owningGuiReflected.actionSave();
                try {
                    Object commonSelectionGui = entryPoint.selectionGuiClass.getConstructors()[0].newInstance(index, returnInfo, entryPoint);
                    owningGuiReflected.mc.displayGuiScreen((GuiScreen) commonSelectionGui);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    ConfigMaxxing.error("Failed to instantiate picker: " + entryPoint.selectionGuiClass.getCanonicalName());
                }
                //guiEditArrayPotionNameScreen.mc.displayGuiScreen(new PotionSelectionGui(index, returnInfo));
            }
        }
    }

    public static class StringEntry extends BaseEntryExtended {
        protected final GuiTextField textFieldValue;
        String displayValue;
        public StringEntry(GuiEditArray owningScreen, GuiEditArrayEntriesCommon owningEntryList, IConfigElement configElement, Object value, int index, String displayValue) {
            super(owningScreen, owningEntryList, configElement, index);

            this.displayValue = displayValue;

            /*potion = PotionUtil.getPotionByName((String) value);
            if (potion == null) {
                displayValue = "None";
            } else {
                displayValue = value + ": " + I18n.format(potion.getName());
            }*/

            this.btn = new GuiButtonExtExtended(69, owningEntryList.width / 4 + 10, 0, owningEntryList.width / 3 + 1, 18, displayValue, owningScreen, owningEntryList, index);
            this.textFieldValue = new GuiTextField(owningEntryList.mc.fontRenderer, owningEntryList.width / 4 + 1, 0, owningEntryList.controlWidth - 3, 16);
            this.textFieldValue.setMaxStringLength(10000);
            this.textFieldValue.setText(value.toString());
            this.isValidated = configElement.getValidationPattern() != null;

            if (configElement.getValidationPattern() != null)
            {
                if (configElement.getValidationPattern().matcher(this.textFieldValue.getText().trim()).matches())
                    isValidValue = true;
                else
                    isValidValue = false;
            }

        }


        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            if (configElement.isListLengthFixed() || slotIndex != owningEntryList.listEntries.size() - 1)
            {
                this.textFieldValue.setVisible(true);
                this.textFieldValue.yPosition = y + 1;
                //this.textFieldValue.drawTextBox();
                //this.btn.displayString = this.ent;
                this.btn.visible = true;
                this.btn.yPosition = y;
                this.btn.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            }
            else
                this.textFieldValue.setVisible(false);
        }

        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            ConfigMaxxing.debug("index: " + getIndex());
            boolean enabledReflected;
            try {
                enabledReflected = (boolean) ConfigMaxxing.varInstanceClient.enabledField.get(owningScreen);
            } catch (IllegalAccessException e) {
                ConfigMaxxing.error("Failed to reflect");
                e.printStackTrace();
                return;
            }
            if (enabledReflected || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
                    || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
            {
                this.textFieldValue.textboxKeyTyped((enabledReflected ? eventChar : Keyboard.CHAR_NONE), eventKey);

                if (configElement.getValidationPattern() != null)
                {
                    if (configElement.getValidationPattern().matcher(this.textFieldValue.getText().trim()).matches())
                        isValidValue = true;
                    else
                        isValidValue = false;
                }
            }
        }

        @Override
        public void updateCursorCounter()
        {
            this.textFieldValue.updateCursorCounter();
        }

        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {
            this.textFieldValue.mouseClicked(x, y, mouseEvent);
            this.btn.mouseReleased(x, y);
        }



        @Override
        public Object getValue()
        {
            return this.textFieldValue.getText().trim();
        }
    }
}
