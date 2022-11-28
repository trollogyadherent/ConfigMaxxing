package trollogyadherent.configmaxxing.configpickers.mob;

import net.minecraft.entity.EntityList;
import trollogyadherent.configmaxxing.configpickers.EntryPoint;
import trollogyadherent.configmaxxing.configpickers.GuiEditArrayCommon;

public class MobBlacklistSelectionGui extends MobSelectionGui{
    public MobBlacklistSelectionGui(int index, GuiEditArrayCommon.ReturnInfo returnInfo, EntryPoint entryPoint) {
        super(index, returnInfo, entryPoint);
    }

    @Override
    public void populateEntries() {
        for (Object e : EntityList.stringToClassMapping.keySet()) {
            MobListEntry entry = new MobListEntry(this, (String) e);
            this.availableEntries.add(entry);
        }
    }
}
