package trollogyadherent.configmaxxing.multivalue;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import trollogyadherent.configmaxxing.ConfigMaxxing;
import trollogyadherent.configmaxxing.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiValueConfigProperty {
    public final String name;
    private MultiValueEntryPoint entryPoint;
    private final Property baseProperty;
    private ArrayList<Property> props;
    private Configuration configuration;

    JsonValue jsonVals;

    public MultiValueConfigProperty(String category, String name, String comment, Configuration configuration) {
        this.props = new ArrayList<>();
        this.name = name;
        this.baseProperty = configuration.get(category, name, "", "WARNING, do not break lines. " + comment);
        this.configuration = configuration;
        if (!Util.isServer()) {
            this.baseProperty.setConfigEntryClass(MultiValueEntryPoint.class);
        }
        MultiValueRegistry.registerMultiValProperty(this);
        try {
            this.jsonVals = Json.parse(desanitizeValue(baseProperty.getString()));
        } catch (Exception e) {
            this.jsonVals = new JsonObject();
            ConfigMaxxing.info("Failed to parse multival property " + baseProperty.getName());
        }
    }

    public void registerProp(Property property) {
        ConfigMaxxing.debug("registering prop " + property.getName());
        ConfigMaxxing.debug(this.baseProperty.getString());
        this.configuration.removeCategory(this.configuration.getCategory(MultiValueEntryPoint.MULTIVAL_CATEGORY));
        if (!property.getName().startsWith(this.getName())) {
            property.setName(this.getName() + "." + property.getName());
        }
        JsonObject obj = this.jsonVals.asObject();
        JsonValue val = obj.get(property.getName());
        if (val != null) {
            if (property.getType() == Property.Type.BOOLEAN) {
                if (property.isList()) {
                    if (val.isArray()) {
                        boolean[] arr = new boolean[val.asArray().size()];
                        for (int i = 0; i < val.asArray().size(); i ++) {
                            if (val.asArray().values().get(i).isBoolean()) {
                                arr[i] = val.asArray().values().get(i).asBoolean();
                            }
                        }
                        property.set(arr);
                    }
                } else {
                    if (val.isBoolean()) {
                        property.set(val.asBoolean());
                    }
                }
            } else if (property.getType() == Property.Type.INTEGER) {
                if (property.isList()) {
                    if (val.isArray()) {
                        int[] arr = new int[val.asArray().size()];
                        for (int i = 0; i < val.asArray().size(); i ++) {
                            if (val.asArray().values().get(i).isNumber()) {
                                arr[i] = val.asArray().values().get(i).asInt();
                            }
                        }
                        property.set(arr);
                    }
                } else {
                    if (val.isNumber()) {
                        property.set(val.asInt());
                    }
                }
            } else if (property.getType() == Property.Type.DOUBLE) {
                if (property.isList()) {
                    if (val.isArray()) {
                        double[] arr = new double[val.asArray().size()];
                        for (int i = 0; i < val.asArray().size(); i ++) {
                            if (val.asArray().values().get(i).isNumber()) {
                                arr[i] = val.asArray().values().get(i).asDouble();
                            }
                        }
                        property.set(arr);
                    }
                } else {
                    if (val.isNumber()) {
                        property.set(val.asDouble());
                    }
                }
            } else if (property.getType() == Property.Type.STRING) {
                if (property.isList()) {
                    if (val.isArray()) {
                        String[] arr = new String[val.asArray().size()];
                        for (int i = 0; i < val.asArray().size(); i ++) {
                            if (val.asArray().values().get(i).isString()) {
                                arr[i] = val.asArray().values().get(i).asString();
                            }
                        }
                        property.set(arr);
                    }
                } else {
                    if (val.isString()) {
                        property.set(val.asString());
                    }
                }
            }
        }
        props.add(property);
        this.baseProperty.set(getValueString());
    }

    public Property getBaseProperty() {
        return baseProperty;
    }

    public MultiValueEntryPoint getEntryPoint() {
        return entryPoint;
    }

    public void registerEntryPoint(MultiValueEntryPoint entryPoint) {
        this.entryPoint = entryPoint;
    }

    public ArrayList<Property> getProps() {
        return props;
    }

    public void setProps(ArrayList<Property> props) {
        this.props = props;
    }

    public String getName() {
        return this.baseProperty.getName();
    }

    public Object getPropVal(Property property) {
        if (property.getType() == Property.Type.BOOLEAN) {
            return property.getBoolean();
        } else if (property.getType() == Property.Type.DOUBLE) {
            return property.getDouble();
        } else if (property.getType() == Property.Type.INTEGER) {
            return property.getInt();
        } else {
            return property.getString();
        }
    }

    public Object getPropValDefault(Property property) {
        if (property.getType() == Property.Type.BOOLEAN) {
            return Boolean.valueOf(property.getDefault());
        } else if (property.getType() == Property.Type.DOUBLE) {
            return Double.parseDouble(property.getDefault());
        } else if (property.getType() == Property.Type.INTEGER) {
            return Integer.parseInt(property.getDefault());
        } else {
            return property.getDefault();
        }
    }

    public Object[] getPropValList(Property property) {
        if (property.getType() == Property.Type.BOOLEAN) {
            return new boolean[][]{property.getBooleanList()};
        } else if (property.getType() == Property.Type.DOUBLE) {
            return new double[][]{property.getDoubleList()};
        } else if (property.getType() == Property.Type.INTEGER) {
            return new int[][]{property.getIntList()};
        } else {
            return property.getStringList();
        }
    }

    public Object[] getPropValListDefault(Property property) {
        String[] defaultStr = property.getDefaults();
        if (property.getType() == Property.Type.BOOLEAN) {
            Boolean[] res = new Boolean[defaultStr.length];
            for (int i = 0; i < defaultStr.length; i ++) {
                res[i] = Boolean.valueOf(defaultStr[i]);
            }
            return res;
        } else if (property.getType() == Property.Type.DOUBLE) {
            Double[] res = new Double[defaultStr.length];
            for (int i = 0; i < defaultStr.length; i ++) {
                res[i] = Double.parseDouble(defaultStr[i]);
            }
            return res;
        } else if (property.getType() == Property.Type.INTEGER) {
            Integer[] res = new Integer[defaultStr.length];
            for (int i = 0; i < defaultStr.length; i ++) {
                res[i] = Integer.parseInt(defaultStr[i]);
            }
            return res;
        } else {
            return defaultStr;
        }
    }

    public HashMap<String, Property> getValues() {
        HashMap<String, Property> res = new HashMap<>();
        for (Property property : this.props) {
            res.put(property.getName().substring(this.getName().length() + 1), property);
        }
        return res;
    }

    public HashMap<String, Property> getDefaultValues() {
        HashMap<String, Property> res = new HashMap<>();
        for (Property property : this.props) {
            //res.put(property.getName().substring(this.getName().length() + 1), property.getDef);
        }
        return res;
    }

    String sanitizeValue(String val) {
        return val.replace("[", "\\[").replace("]", "\\]").
                replace("{", "\\{").replace("}", "\\}").
                replace(":", "\\:");
    }

    String desanitizeValue(String val) {
        return val.replace("\\[", "[").replace("\\]", "]").
                replace("\\{", "{").replace("\\}", "}").
                replace("\\:", ":");
    }

    public String getValueString() {
        JsonObject jsonObject = new JsonObject();

        for (int i = 0; i < this.props.size(); i ++) {
            Property property = this.props.get(i);

            if (property.getType() == Property.Type.BOOLEAN) {
                if (property.isList()) {
                    JsonArray jsonArray = new JsonArray();
                    for (Boolean val : (Boolean[]) getPropValList(property)) {
                        jsonArray.add(val);
                    }
                    jsonObject.add(property.getName(), jsonArray);
                } else {
                    jsonObject.add(property.getName(), property.getBoolean());
                }
            } else if (property.getType() == Property.Type.STRING) {
                if (property.isList()) {
                    JsonArray jsonArray = new JsonArray();
                    for (String val : (String[]) getPropValList(property)) {
                        jsonArray.add(val);
                    }
                    jsonObject.add(property.getName(), jsonArray);
                } else {
                    jsonObject.add(property.getName(), property.getString());
                }
            } else if (property.getType() == Property.Type.INTEGER) {
                if (property.isList()) {
                    JsonArray jsonArray = new JsonArray();
                    for (Integer val : (Integer[]) getPropValList(property)) {
                        jsonArray.add(val);
                    }
                    jsonObject.add(property.getName(), jsonArray);
                } else {
                    jsonObject.add(property.getName(), property.getInt());
                }
            } else if (property.getType() == Property.Type.DOUBLE) {
                if (property.isList()) {
                    JsonArray jsonArray = new JsonArray();
                    for (Double val : (Double[]) getPropValList(property)) {
                        jsonArray.add(val);
                    }
                    jsonObject.add(property.getName(), jsonArray);
                } else {
                    jsonObject.add(property.getName(), property.getDouble());
                }
            }
        }
        return jsonObject.toString().replace("\n", "");
    }
    /*
    public String getValueString() {
        String res = "[ ";
        for (int i = 0; i < this.props.size(); i ++) {
            Property property = this.props.get(i);
            String addition = "";
            if (property.getType() == Property.Type.BOOLEAN) {
                if (property.isList()) {
                    boolean[] propList = property.getBooleanList();
                    res += "{ ";
                    String listAddition = "";
                    for (int j = 0; j < propList.length; j ++) {
                        listAddition += propList[j];
                        listAddition = sanitizeValue(listAddition);
                        if (j != propList.length - 1) {
                            listAddition += " : ";
                        }
                    }
                    addition += listAddition;
                    res += " }";
                } else {
                    addition += property.getBoolean();
                }
            } else if (property.getType() == Property.Type.STRING) {
                if (property.isList()) {
                    String[] propList = property.getStringList();
                    res += "{ ";
                    String listAddition = "";
                    for (int j = 0; j < propList.length; j ++) {
                        listAddition += propList[j];
                        listAddition = sanitizeValue(listAddition);
                        if (j != propList.length - 1) {
                            listAddition += " : ";
                        }
                    }
                    addition += listAddition;
                    res += " }";
                } else {
                    addition += property.getString();
                }
            } else if (property.getType() == Property.Type.INTEGER) {
                if (property.isList()) {
                    int[] propList = property.getIntList();
                    res += "{ ";
                    String listAddition = "";
                    for (int j = 0; j < propList.length; j ++) {
                        listAddition += propList[j];
                        listAddition = sanitizeValue(listAddition);
                        if (j != propList.length - 1) {
                            listAddition += " : ";
                        }
                    }
                    addition += listAddition;
                    res += " }";
                } else {
                    addition += property.getInt();
                }
            } else if (property.getType() == Property.Type.DOUBLE) {
                if (property.isList()) {
                    double[] propList = property.getDoubleList();
                    res += "{ ";
                    String listAddition = "";
                    for (int j = 0; j < propList.length; j ++) {
                        listAddition += propList[j];
                        listAddition = sanitizeValue(listAddition);
                        if (j != propList.length - 1) {
                            listAddition += " : ";
                        }
                    }
                    addition += listAddition;
                    res += " }";
                } else {
                    addition += property.getDouble();
                }
            }

            if (!property.isList()) {
                addition = sanitizeValue(addition);
            }
            res += addition;
            if (i != this.props.size() - 1) {
                res += " : ";
            }
        }
        res += " ]";
        return res;
    }

     */
}
