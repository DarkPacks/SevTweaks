package tv.darkosto.sevtweaks.common.compat.crafttweaker;

import stanhebben.zenscript.annotations.ZenClass;

enum Types {
    CONTAINER,
    DIMENSION,
    MOB,
    PACKAGE,
    RECIPE_NAME,
    TINKER_MATERIAL,
    TINKER_MODIFIER,
    TINKER_TOOL
}

@ZenClass("mods.sevtweaks.stager.StagedType")
public class StagedType {
    private String value;
    private String subValue;
    private Types type;

    StagedType(String stagedString, Types type, String subValue) {
        this.value = stagedString;
        this.type = type;
        this.subValue = subValue;
    }

    public String getValue() {
        return value;
    }

    String getSubValue() {
        return subValue;
    }

    Types getType() {
        return type;
    }
}