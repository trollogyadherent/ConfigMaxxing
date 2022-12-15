package trollogyadherent.configmaxxing.multivalue;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Keyboard;
import trollogyadherent.configmaxxing.ConfigMaxxing;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class MultiValueEntryPoint extends GuiConfigEntries.StringEntry {
    public final static String MULTIVAL_CATEGORY = "multival";
    private ArrayList<Property> props = new ArrayList<>();
    private ArrayList<GuiConfigEntries.IConfigEntry> configEntries = new ArrayList<>();

    private MultiValueConfigProperty multiValueConfigProperty;
    GuiConfigEntries guiConfigEntries;
    GuiConfig owningScreen;

    String currentVal;
    int thisSlotHeight = 20;

    public MultiValueEntryPoint(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<?> configElement) {
        super(owningScreen, owningEntryList, configElement);
        this.multiValueConfigProperty = MultiValueRegistry.getProperty(this.getName());
        if (this.multiValueConfigProperty == null) {
            throw new NullPointerException("this.multiValueConfigProperty is null!");
        }
        this.multiValueConfigProperty.registerEntryPoint(this);
        guiConfigEntries = owningEntryList;
        this.owningScreen = owningScreen;

        for (Property property : this.multiValueConfigProperty.getProps()) {
            addProp(property);
        }
        currentVal = this.multiValueConfigProperty.getBaseProperty().getString();
        if (currentVal.length() == 0) {
            currentVal = this.multiValueConfigProperty.getValueString();
            this.multiValueConfigProperty.getBaseProperty().set(currentVal);
        }
        Field beforeValueField = ReflectionHelper.findField(GuiConfigEntries.StringEntry.class, "beforeValue");
        beforeValueField.setAccessible(true);
        try {
            beforeValueField.set(this, currentVal);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isDefault() {
        return this.multiValueConfigProperty.getBaseProperty().getDefault() != null ? this.multiValueConfigProperty.getBaseProperty().getDefault().equals(this.currentVal) :
                this.currentVal.trim().isEmpty();
    }

    @Override
    public Object getCurrentValue() {
        for (GuiConfigEntries.IConfigEntry ce : this.configEntries) {
            ce.saveConfigElement();
        }
        this.currentVal = this.multiValueConfigProperty.getValueString();
        return this.currentVal;
    }

    @Override
    public Object[] getCurrentValues() {
        return new Object[] { getCurrentValue() };
    }

    @Override
    public void setToDefault() {
        if (enabled()) {
            this.currentVal = this.multiValueConfigProperty.getBaseProperty().getDefault();
        }
    }

    @Override
    public boolean isChanged() {
        return beforeValue != null ? !this.beforeValue.equals(this.getCurrentValue()) : this.getCurrentValue() == null;
    }

    @Override
    public void undoChanges() {
        if (enabled()) {
            this.currentVal = this.beforeValue;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean saveConfigElement() {
        if (enabled()) {
            if (isChanged() && this.isValidValue) {
                this.configElement.set(this.getCurrentValue());
                return configElement.requiresMcRestart();
            } else if (isChanged() && !this.isValidValue) {
                this.configElement.setToDefault();
                return configElement.requiresMcRestart()
                        && beforeValue != null ? beforeValue.equals(this.multiValueConfigProperty.getBaseProperty().getDefault()) :
                        this.multiValueConfigProperty.getBaseProperty().getDefault() == null;
            }
        }
        return false;
    }

    @Override
    public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
        for (GuiConfigEntries.IConfigEntry configEntry : configEntries) {
            if (configEntry == null) {
                continue;
            }
            int i = configEntries.indexOf(configEntry);
            configEntry.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
        }
        return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
    }

    @Override
    public void mouseClicked(int x, int y, int mouseEvent)
    {
        for (GuiConfigEntries.IConfigEntry configEntry : configEntries) {
            if (configEntry == null) {
                continue;
            }
            configEntry.mouseClicked(x, y, mouseEvent);
        }
    }

    @Override
    public void keyTyped(char eventChar, int eventKey)
    {
        for (GuiConfigEntries.IConfigEntry configEntry : configEntries) {
            if (configEntry == null) {
                continue;
            }
            configEntry.keyTyped(eventChar, eventKey);
        }
    }

    public void drawSuper(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
        boolean isChanged = isChanged();

        if (drawLabel)
        {
            String label = (!isValidValue ? EnumChatFormatting.RED.toString() :
                    (isChanged ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString()))
                    + (isChanged ? EnumChatFormatting.ITALIC.toString() : "") + this.name;
            this.mc.fontRenderer.drawString(
                    label,
                    this.owningScreen.entryList.labelX,
                    y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2,
                    16777215);
        }

        this.btnUndoChanges.xPosition = this.owningEntryList.scrollBarX - 44;
        this.btnUndoChanges.yPosition = y;
        this.btnUndoChanges.enabled = enabled() && isChanged;
        this.btnUndoChanges.drawButton(this.mc, mouseX, mouseY);

        this.btnDefault.xPosition = this.owningEntryList.scrollBarX - 22;
        this.btnDefault.yPosition = y;
        this.btnDefault.enabled = enabled() && !isDefault();
        this.btnDefault.drawButton(this.mc, mouseX, mouseY);

        if (this.tooltipHoverChecker == null)
            this.tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, this.owningScreen.entryList.controlX - 8, 800);
        else
            this.tooltipHoverChecker.updateBounds(y, y + slotHeight, x, this.owningScreen.entryList.controlX - 8);
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
        drawSuper(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);

        /* draw string: content, x, y, color */
        String displayString = this.name + ":";
        int displayStringWidth = owningEntryList.mc.fontRenderer.getStringWidth(displayString);
        int xPos = (listWidth - x) / 2 - displayStringWidth / 2;
        owningEntryList.mc.fontRenderer.drawString(displayString, xPos , y, 16777215);


        //this.textFieldValue.xPosition = this.owningEntryList.controlX + 2;
        //this.textFieldValue.yPosition = y + 1;
        //this.textFieldValue.width = this.owningEntryList.controlWidth - 4;
        //this.textFieldValue.setEnabled(enabled());
        //this.textFieldValue.drawTextBox();

        for (GuiConfigEntries.IConfigEntry configEntry : configEntries) {
            if (configEntry == null) {
                return;
            }
            int index = configEntries.indexOf(configEntry);
            configEntry.drawEntry(slotIndex, x, y + slotHeight / (configEntries.size() + 1) + 2 + index * slotHeight / (configEntries.size() + 1), listWidth, slotHeight / (configEntries.size() + 1), tessellator, mouseX, mouseY, isSelected);
        }
    }

    public void addProp(Property property) {
        if (property == null) {
            return;
        }
        IConfigElement sneed = null;
        /*for (IConfigElement iConfigElement : owningScreen.configElements) {
            String propName = property.getName();
            String confElemName = iConfigElement.getName();
            String qualifiedName = iConfigElement.getQualifiedName();
            if (iConfigElement.getName().equals(property.getName())) {
                sneed = iConfigElement;
                break;
            }
        }*/
        sneed = new ConfigElement(property);
        if (sneed == null) {
            ConfigMaxxing.debug("iConfigElement not found");
            return;
        }
        props.add((Property) property);
        if (!property.showInGui()) {
            configEntries.add(null);
            return;
        }

        //System.out.println(guiConfigEntries);

        //this.configEntries.get(0)

        Field slotHeightField = ReflectionHelper.findField(GuiSlot.class, "slotHeight", "field_148149_f");
        slotHeightField.setAccessible(true);
        try {
            int currentSlotHeight = (int) slotHeightField.get(guiConfigEntries);
            if (thisSlotHeight + 20 > currentSlotHeight) {
                slotHeightField.set(guiConfigEntries, currentSlotHeight + 20);
                thisSlotHeight += 20;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //guiConfigEntries.slotHeight = 60;

        if (property.getConfigEntryClass() != null) {
            try {
                this.configEntries.add((GuiConfigEntries.IConfigEntry) property.getConfigEntryClass()
                        .getConstructor(GuiConfig.class, GuiConfigEntries.class, IConfigElement.class)
                        .newInstance(this.owningScreen, guiConfigEntries, sneed));
            } catch (Throwable e) {
                ConfigMaxxing.error("There was a critical error instantiating the custom IConfigEntry for config element " + property.getName() + ".");
                e.printStackTrace();
            }
        } else {
            if (property.isList()) {
                this.configEntries.add(new GuiConfigEntries.ArrayEntry(this.owningScreen, guiConfigEntries, new ConfigElement(property)));
            } else if (property.getType() == Property.Type.BOOLEAN) {
                Constructor<GuiConfigEntries.BooleanEntry> constructor = (Constructor<GuiConfigEntries.BooleanEntry>) GuiConfigEntries.BooleanEntry.class.getDeclaredConstructors()[0];
                constructor.setAccessible(true);
                try {
                    GuiConfigEntries.BooleanEntry booleanEntry = constructor.newInstance(this.owningScreen, guiConfigEntries, new ConfigElement(property));
                    this.configEntries.add(booleanEntry);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    ConfigMaxxing.error("Failed to instantiate GuiConfigEntries.BooleanEntry");
                    e.printStackTrace();
                }
            } else if (property.getType() == Property.Type.INTEGER) {
                this.configEntries.add(new GuiConfigEntries.IntegerEntry(this.owningScreen, guiConfigEntries, new ConfigElement(property)));
            } else if (property.getType() == Property.Type.DOUBLE) {
                this.configEntries.add(new GuiConfigEntries.DoubleEntry(this.owningScreen, guiConfigEntries, new ConfigElement(property)));
            } else if (property.getType() == Property.Type.STRING) {
                if (property.getValidValues() != null && property.getValidValues().length > 0) {
                    Constructor<GuiConfigEntries.CycleValueEntry> constructor = (Constructor<GuiConfigEntries.CycleValueEntry>) GuiConfigEntries.CycleValueEntry.class.getDeclaredConstructors()[0];
                    constructor.setAccessible(true);
                    try {
                        GuiConfigEntries.CycleValueEntry cycleEntry = constructor.newInstance(this.owningScreen, guiConfigEntries, new ConfigElement(property));
                        this.configEntries.add(cycleEntry);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        ConfigMaxxing.error("Failed to instantiate GuiConfigEntries.CycleValueEntry");
                        e.printStackTrace();
                    }
                } else {
                    this.configEntries.add(new GuiConfigEntries.StringEntry(this.owningScreen, guiConfigEntries, (IConfigElement<String>) property));
                }
            }
        }
    }
}
