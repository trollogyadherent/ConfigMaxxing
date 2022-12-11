package trollogyadherent.configmaxxing.util;

import net.minecraft.block.Block;

import java.util.Set;

public class BlockUtil {
    public static void test() {
        for (Object o : Block.blockRegistry.getKeys()) {
            System.out.println(Block.blockRegistry.getObject(o));
        }
    }

    public static Block getBlockByName(String name) {
        for (Object o : Block.blockRegistry.getKeys()) {
            Block b = (Block) Block.blockRegistry.getObject(o);
            if (b.getUnlocalizedName().equals(name)) {
                return b;
            }
        }
        return null;
    }
}
