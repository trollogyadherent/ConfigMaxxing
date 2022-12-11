package trollogyadherent.configmaxxing.configpickers.multival2;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import trollogyadherent.configmaxxing.configpickers.CommonSelectionGui;
import trollogyadherent.configmaxxing.configpickers.EntryPoint;

public class MultiVal2EntryPoint extends EntryPoint {
    public MultiVal2EntryPoint(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement, MultiVal2SelectionGui.class);
    }

    @Override
    public String formatValue(Object val) {
        return null;
    }

    @Override
    public String getDescription(Object val) {
        return null;
    }

    @Override
    public String getDisplayStringStyled(Object val) {
        return null;
    }

    @Override
    public String getDisplayString(Object val) {
        return null;
    }
}
