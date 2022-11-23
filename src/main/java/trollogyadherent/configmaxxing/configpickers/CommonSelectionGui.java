package trollogyadherent.configmaxxing.configpickers;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import trollogyadherent.configmaxxing.ConfigMaxxing;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonSelectionGui extends GuiScreen {
    public EntryPoint entryPoint;
    public int lastKeyboardEventKey = -1;
    public GuiEditArrayCommon.ReturnInfo returnInfo;
    public int index;
    Object originalValue;
    public ArrayList<ListEntry> availableEntries;
    public ArrayList<ListEntry> hiddenList;
    public String title = "Changeme";
    public AvailableEntriesListGui availableEntriesListGui;

    public SearchBox searchBox;
    /* Counts added entries so they can get readded back in order when the search term changes */
    private int addCounter = 0;

    public CommonSelectionGui(int index, GuiEditArrayCommon.ReturnInfo returnInfo, EntryPoint entryPoint) {
        this.mc = Minecraft.getMinecraft();

        this.index = index;
        this.returnInfo = returnInfo;
        this.entryPoint = entryPoint;

        if (!entryPoint.getConfigElement().isList() && (index < 0 || index >= returnInfo.values.length)) {
            this.originalValue = null;
        } else {
            if (entryPoint.getConfigElement().isList()) {
                this.originalValue = returnInfo.values[index];
            } else {
                this.originalValue = entryPoint.getConfigElement().get();
            }
        }

        this.availableEntries = new ArrayList<>();
        this.hiddenList =  new ArrayList<>();
        populateEntries();
    }

    public abstract void populateEntries();

    boolean eventCharIsAlpha(char eventChar) {
        return ((eventChar >= 'A') && (eventChar <= 'Z')) || ((eventChar >= 'a') && (eventChar <= 'z'));
    }

    boolean eventCharIsNum(char eventChar) {
        return (eventChar >= '0') && (eventChar <= '9');
    }

    boolean eventCharIsAlnum(char eventChar) {
        return eventCharIsAlpha(eventChar) || eventCharIsNum(eventChar);
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            ConfigMaxxing.debug("hit escape");
            actionCancelValues();
            showParentScreen();
        } else if (eventKey == Keyboard.KEY_RETURN) {
            showParentScreen();
        } else if (eventKey == Keyboard.KEY_DOWN) {
            this.availableEntriesListGui.moveSelectionDown();
        } else if (eventKey == Keyboard.KEY_UP) {
            this.availableEntriesListGui.moveSelectionUp();
        } else if (eventKey == Keyboard.KEY_TAB || eventCharIsAlnum(eventChar)) {
            this.searchBox.setFocused(true);
            this.searchBox.clear();
            if (eventCharIsAlnum(eventChar)) {
                this.searchBox.textboxKeyTyped(eventChar, eventKey);
            }
        } else {
            ConfigMaxxing.debug("Index: " + index);
            ConfigMaxxing.debug("width: " + this.width + ", height: " + this.height);
            this.searchBox.textboxKeyTyped(eventChar, eventKey);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        /* Triggered when clicked on the "Done" button */
        if (button.id == 69) {
            showParentScreen();
        }
    }

    public void keyReleased(char eventChar, int eventKey) {

    }

    public void keyPressedOnce(char eventChar, int eventKey) {

    }

    @Override
    public void handleKeyboardInput() {
        char eventChar = Keyboard.getEventCharacter();
        int eventKey =  Keyboard.getEventKey();
        /* True means it is down, false means it's released */
        if (Keyboard.getEventKeyState()) {
            this.keyTyped(eventChar, eventKey);
            if (this.lastKeyboardEventKey != eventKey) {
                this.keyPressedOnce(eventChar, eventKey);
                this.lastKeyboardEventKey = eventKey;
            }
        } else {
            this.keyReleased(eventChar, eventKey);
            this.lastKeyboardEventKey = -1;
        }

        this.mc.func_152348_aa();
    }

    public void actionCancelValues() {
        if (entryPoint.getConfigElement().isList()) {
            returnInfo.values[index] = originalValue;
        } else {
            if (originalValue == null) {
                originalValue = entryPoint.getConfigElement().getDefault();
            }
            entryPoint.getConfigElement().set(originalValue);
        }
    }

    public void showParentScreen() {
        if (returnInfo.configElement.isList()) {
            mc.displayGuiScreen(new GuiEditArrayCommon(returnInfo.parentScreen, this.entryPoint, returnInfo.configElement, returnInfo.slotIndex, returnInfo.values, returnInfo.enabled));
        } else {
            entryPoint.updateValueButtonText();
            mc.displayGuiScreen(this.entryPoint.owningScreen);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        this.availableEntriesListGui.func_148179_a(x, y, button);
        this.searchBox.mouseClicked(x, y, button);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
        super.mouseMovedOrUp(mouseX, mouseY, which);
    }

    public static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        //list.add(new DummyConfigElement<>("modIDSelector", "FML", ConfigGuiType.MOD_ID, "bruh sneed"));
        return list;
    }

    public boolean hasListEntry(ListEntry potionListEntry)
    {
        return this.availableEntries.contains(potionListEntry);
    }

    public List probablyToRemove(ListEntry skinListEntry)
    {
        return this.hasListEntry(skinListEntry) ? this.availableEntries : null;
    }

    public List getAvailableEntries()
    {
        return this.availableEntries;
    }

    @Override
    public void initGui() {
        super.initGui();

        /* "Done" button */
        this.buttonList.add(new GuiButton(69, 20, this.height - 25, (this.width - 25) / 4, 20, I18n.format("gui.done")));
        this.searchBox = new SearchBox(mc.fontRenderer, 20, 30, 100, 20, this);

        //this.availableEntriesListGui.selectEntry(0, true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.searchBox.width = this.width / 4;
        this.searchBox.drawTextBox();
    }

    public int getEntryCounter() {
        int res = this.addCounter;
        this.addCounter ++;
        return res;
    }

    public void onEntrySelect(int index, boolean doScroll) {

    }
}
