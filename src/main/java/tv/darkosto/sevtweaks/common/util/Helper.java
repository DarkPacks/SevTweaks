package tv.darkosto.sevtweaks.common.util;

import crafttweaker.api.item.IIngredient;
import tv.darkosto.sevtweaks.common.compat.crafttweaker.StagedIngredient;
import tv.darkosto.sevtweaks.common.compat.crafttweaker.StagedType;

import java.util.*;
import java.util.regex.Pattern;

public class Helper {
    public static void getDuplicates(Map<String, List<String>> duplicates, String stage, List<StagedType> currStage, List<StagedType> checkStage) {
        Collection<String> similar = new HashSet<>();
        Collection<String> checkSimilar = new HashSet<>();
        currStage.forEach((stagedType -> similar.add(stagedType.getValue())));
        checkStage.forEach((stagedType -> checkSimilar.add(stagedType.getValue())));
        similar.retainAll(checkSimilar);

        for (String s : similar) {
            List<String> list = new ArrayList<>();
            if (duplicates.containsKey(s)) {
                if (!duplicates.get(s).contains(stage)) {
                    list.addAll(duplicates.get(s));
                    list.add(stage);
                    duplicates.put(s, list);
                }
            } else {
                list.add(stage);
                duplicates.put(s, list);
            }
        }
    }

    public static void getIngredientDuplicates(Map<IIngredient, List<String>> duplicates, String stage, Map<IIngredient, StagedIngredient> currStage, Map<IIngredient, StagedIngredient> checkStage) {
        Collection<IIngredient> similar = new HashSet<>();
        Collection<IIngredient> checkSimilar = new HashSet<>();
        currStage.forEach(((ingredient, stagedIngredient) -> similar.add(stagedIngredient.getIngredient())));
        checkStage.forEach(((ingredient, stagedIngredient) -> checkSimilar.add(stagedIngredient.getIngredient())));
        similar.retainAll(checkSimilar);

        for (IIngredient s : similar) {
            List<String> list = new ArrayList<>();
            if (duplicates.containsKey(s)) {
                if (!duplicates.get(s).contains(stage)) {
                    list.addAll(duplicates.get(s));
                    list.add(stage);
                    duplicates.put(s, list);
                }
            } else {
                list.add(stage);
                duplicates.put(s, list);
            }
        }
    }

    public static String validateRecipeName(String name) {
        Pattern withoutModId = Pattern.compile("^([a-zA-Z_]*)$");
        if (withoutModId.matcher(name).matches()) {
            return String.format("minecraft:%s", name);
        }

        Pattern pattern = Pattern.compile("^([a-z-A-Z]\\w+):([a-zA-Z_]*)$");
        return pattern.matcher(name).matches() ? name : null;
    }
}
