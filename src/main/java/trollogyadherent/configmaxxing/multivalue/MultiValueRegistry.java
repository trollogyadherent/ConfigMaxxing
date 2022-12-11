package trollogyadherent.configmaxxing.multivalue;

import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;

public class MultiValueRegistry {
    public static class vars {
        //public static ArrayList<Property> props = new ArrayList<>();
        public static ArrayList<MultiValueConfigProperty> multiValProps = new ArrayList<>();
        public static Configuration config = new Configuration();
    }

    public static void registerMultiValProperty(MultiValueConfigProperty property) {
        MultiValueConfigProperty toRemove = getProperty(property.getName());
        if (toRemove != null) {
            vars.multiValProps.remove(toRemove);
        }
        vars.multiValProps.add(property);
    }

    public static void registerEntryPoint(MultiValueEntryPoint entryPoint) {
        for (MultiValueConfigProperty property : vars.multiValProps) {

        }
    }

    public static MultiValueConfigProperty getProperty(String name) {
        for (MultiValueConfigProperty property : vars.multiValProps) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }
}
