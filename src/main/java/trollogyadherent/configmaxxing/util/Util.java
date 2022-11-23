package trollogyadherent.configmaxxing.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

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
}
