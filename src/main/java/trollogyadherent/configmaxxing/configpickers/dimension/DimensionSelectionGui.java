package trollogyadherent.configmaxxing.configpickers.dimension;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldProvider;
import org.lwjgl.opengl.GL11;
import trollogyadherent.configmaxxing.ConfigMaxxing;
import trollogyadherent.configmaxxing.configpickers.AvailableEntriesListGui;
import trollogyadherent.configmaxxing.configpickers.CommonSelectionGui;
import trollogyadherent.configmaxxing.configpickers.EntryPoint;
import trollogyadherent.configmaxxing.configpickers.GuiEditArrayCommon;
import trollogyadherent.configmaxxing.util.ClientUtil;
import trollogyadherent.configmaxxing.util.DimensionUtil;

public class DimensionSelectionGui extends CommonSelectionGui {
    public DimensionSelectionGui(int index, GuiEditArrayCommon.ReturnInfo returnInfo, EntryPoint entryPoint) {
        super(index, returnInfo, entryPoint);
        this.title = I18n.format("eyesintheshadows.dimension_selection_title");

    }

    @Override
    public void populateEntries() {
        for (int i : ConfigMaxxing.varInstanceClient.providers.keySet()) {
            WorldProvider worldProvider = null;
            try {
                if (ConfigMaxxing.varInstanceClient.providers.get(i) != null) {
                    worldProvider = ConfigMaxxing.varInstanceClient.providers.get(i).newInstance();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (worldProvider == null) {
                continue;
            }
            DimensionListEntry dimensionListEntry = new DimensionListEntry(this, new DimensionUtil.SimpleDimensionObj(worldProvider.dimensionId, worldProvider.getDimensionName(), worldProvider.getDepartMessage()));
            this.availableEntries.add(dimensionListEntry);
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        int entryHeight = 36;

        /* The list element */
        this.availableEntriesListGui = new AvailableEntriesListGui(60, this.height - 30, 0 /*this.width / 4 * 3*/, this.height, entryHeight, this.availableEntries, returnInfo, index, this);
        this.availableEntriesListGui.setSlotXBoundsFromLeft(this.width / 2);
        this.availableEntriesListGui.registerScrollButtons(7, 8);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //super.drawScreen(mouseX, mouseY, partialTicks);

        /* Drawing the background, obv */
        this.drawBackground(0);

        /* Setting the x coord of the list widget */
        this.availableEntriesListGui.left = 25;

        /* Setting the width of the list widget */
        this.availableEntriesListGui.right = this.width - 25;
        this.availableEntriesListGui.width = this.availableEntriesListGui.right - this.availableEntriesListGui.left;

        /* Setting the width of the list elements (basically where the selection area and the rectangle that draws around is) */
        this.availableEntriesListGui.setListWidth(this.availableEntriesListGui.width / 5 * 4);

        /* Drawing the list widget */
        this.availableEntriesListGui.drawScreen(mouseX, mouseY, partialTicks);

        /* Drawing the title of this gui */
        this.drawCenteredString(this.fontRendererObj, title, this.width / 2, 16, 16777215);

        /* additonal list widget footer, otherwise is buggy for some reason */
        drawFooter();

        /* Drawing the rest, esentially the search box and the done button */
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /* Draws the bottom of the list widget */
    public void drawFooter() {
        float opaqueColor = 0.25098039215F;
        GL11.glColor4f(opaqueColor, opaqueColor, opaqueColor, 1);
        ClientUtil.drawModalRectWithCustomSizedTexture(mc, optionsBackground, 0, this.height - 30, this.width, 30, this.width / 32, 1);
        GL11.glColor4f(1, 1, 1, 1);
    }
}
