package trollogyadherent.configmaxxing.configpickers.mob;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import trollogyadherent.configmaxxing.configpickers.EntryPoint;
import trollogyadherent.configmaxxing.util.MobUtil;

public class MobBlacklistEntryPoint extends EntryPoint {

    public MobBlacklistEntryPoint(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement, MobBlacklistSelectionGui.class);
    }

    @Override
    public String formatValue(Object val) {
        return getDisplayStringStyled(val);
    }

    @Override
    public String getDescription(Object val) {
        String className = MobUtil.getClassByName((String) val);
        if (className != null) {
            return className;
        }
        return "";
    }

    @Override
    public String getDisplayStringStyled(Object val) {
        return getDisplayString(val);
    }

    @Override
    public String getDisplayString(Object val) {
        if (val == null) {
            return "None";
        }
        return (String) val;
    }
}
