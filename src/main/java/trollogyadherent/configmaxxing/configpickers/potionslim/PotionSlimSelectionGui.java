package trollogyadherent.configmaxxing.configpickers.potionslim;

import net.minecraft.potion.Potion;
import trollogyadherent.configmaxxing.configpickers.AvailableEntriesListGui;
import trollogyadherent.configmaxxing.configpickers.EntryPoint;
import trollogyadherent.configmaxxing.configpickers.GuiEditArrayCommon;
import trollogyadherent.configmaxxing.configpickers.potion.PotionListEntry;
import trollogyadherent.configmaxxing.configpickers.potion.PotionSelectionGui;

public class PotionSlimSelectionGui extends PotionSelectionGui {
    public PotionSlimSelectionGui(int index, GuiEditArrayCommon.ReturnInfo returnInfo, EntryPoint entryPoint) {
        super(index, returnInfo, entryPoint);
    }

    @Override
    public void initGui() {
        super.initGui();

        int entryHeight = 18;

        /* The list element */
        this.availableEntriesListGui = new AvailableEntriesListGui(60, this.height - 30, 0 /*this.width / 4 * 3*/, this.height, entryHeight, this.availableEntries, returnInfo, index, this);
        this.availableEntriesListGui.setSlotXBoundsFromLeft(this.width / 2);
        this.availableEntriesListGui.registerScrollButtons(7, 8);
    }

    @Override
    public void populateEntries() {
        for (Potion p : Potion.potionTypes) {
            if (p != null) {
                PotionSlimListEntry entry = new PotionSlimListEntry(this, p);
                this.availableEntries.add(entry);
            }
        }
    }
}
