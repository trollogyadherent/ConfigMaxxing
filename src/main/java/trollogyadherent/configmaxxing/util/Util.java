package trollogyadherent.configmaxxing.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public static boolean isServer() {
        return FMLCommonHandler.instance().getSide() == Side.SERVER;
    }

    public static enum Color {
        DARKRED,
        RED,
        GOLD,
        YELLOW,
        DARKGREEN,
        GREEN,
        AQUA,
        DARKAQUA,
        DARKBLUE,
        BLUE,
        LIGHTPURPLE,
        DARKPURPLE,
        WHITE,
        GREY,
        DARKGREY,
        BLACK,

        RESET
    }

    public static String colorCode(Color color) {
        Map<Color, String> colorMap = new HashMap<Color, String>() {{
            put(Color.DARKRED, "4");
            put(Color.RED, "c");
            put(Color.GOLD, "6");
            put(Color.YELLOW, "e");
            put(Color.DARKGREEN, "2");
            put(Color.GREEN, "a");
            put(Color.AQUA, "b");
            put(Color.DARKAQUA, "3");
            put(Color.DARKBLUE, "1");
            put(Color.BLUE, "9");
            put(Color.LIGHTPURPLE, "d");
            put(Color.DARKPURPLE, "5");
            put(Color.WHITE, "f");
            put(Color.GREY, "7");
            put(Color.DARKGREY, "8");
            put(Color.BLACK, "0");
            put(Color.RESET, "r");

        }};
        return (char) 167 + colorMap.get(color);
    }

    public static Object[] addAtIndex(Object[] arr, int index, Object val) {
        if (index < 0 || index > arr.length) {
            return null;
        }
        Object[] res = new Object[arr.length + 1];
        for (int i = 0, j = 0; i < res.length; i ++) {
            if (i == index) {
                res[i] = val;
            } else {
                res[i] = arr[j];
                j ++;
            }
        }
        return res;
    }

    public static Object[] removeAtIndex(Object[] arr, int index) {
        if (index < 0 || index >= arr.length) {
            return null;
        }
        Object[] res = new Object[arr.length - 1];
        for (int i = 0, j = 0; i < arr.length; i ++) {
            if (i != index) {
                res[j] = arr[i];
                j ++;
            }
        }
        return res;
    }

    public static void drawItemStack(ItemStack p_146982_1_, int p_146982_2_, int p_146982_3_, String p_146982_4_)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);

        //GL11.glScalef(2.0F, 2.0F, 0.0F);

        //GL11.glTranslatef(-13, -30, 0);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);

        //GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        FontRenderer font = null;
        if (p_146982_1_ != null) font = p_146982_1_.getItem().getFontRenderer(p_146982_1_);
        if (font == null) font = Minecraft.getMinecraft().fontRenderer;
        RenderItem.getInstance().renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_);
        //RenderItem.getInstance().renderItemOverlayIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_, p_146982_4_);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }
}
