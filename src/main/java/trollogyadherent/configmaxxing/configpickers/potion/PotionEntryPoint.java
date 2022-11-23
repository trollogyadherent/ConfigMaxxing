package trollogyadherent.configmaxxing.configpickers.potion;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import trollogyadherent.configmaxxing.configpickers.EntryPoint;
import trollogyadherent.configmaxxing.util.PotionUtil;
import trollogyadherent.configmaxxing.util.Util;

public class PotionEntryPoint extends EntryPoint {
    public PotionEntryPoint(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement, PotionSelectionGui.class);
    }

    @Override
    public String formatValue(Object val) {
        return getDisplayStringStyled(PotionUtil.getPotionByName((String) val));
    }

    /* Returns the string in the second row of the entry */
    @Override
    public String getDescription(Object val) {
        Potion potion = (Potion) val;
        if (potion == null) {
            return "";
        }
        return potion.getName() + " - id: " + potion.getId();
    }

    /* The name as displayed in the list */
    /* If the potion is a good effect, it will be displayed green, otherwise red */
    @Override
    public String getDisplayStringStyled(Object val) {
        Potion potion = (Potion) val;
        if (potion == null) {
            return "None";
        }
        String prefix = Util.colorCode(Util.Color.GREEN);
        if (potion.isBadEffect()) {
            prefix = Util.colorCode(Util.Color.RED);
        }
        return prefix + getDisplayString(val) + Util.colorCode(Util.Color.RESET);
    }

    @Override
    public String getDisplayString(Object val) {
        Potion potion = (Potion) val;
        if (potion == null) {
            return "None";
        }
        return I18n.format(potion.getName());
    }
}
