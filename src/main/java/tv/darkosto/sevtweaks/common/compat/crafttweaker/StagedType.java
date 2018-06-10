package tv.darkosto.sevtweaks.common.compat.crafttweaker;

enum Types {
    CONTAINER,
    DIMENSION,
    PACKAGE,
    RECIPE_NAME;

    public static Types getFromString(String toGet) {
        for (Types type : Types.values()) {
            if (type.name().equalsIgnoreCase(toGet)) {
                return type;
            }
        }

        return null;
    }
}

public class StagedType {
    private String value;
    private Types type;

    public StagedType(String stagedString, Types type) {
        this.value = stagedString;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public Types getType() {
        return type;
    }
}