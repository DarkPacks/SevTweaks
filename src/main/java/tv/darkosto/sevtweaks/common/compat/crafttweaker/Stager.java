package tv.darkosto.sevtweaks.common.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tv.darkosto.sevtweaks.common.util.Helper;

import java.util.*;

@ZenRegister
@ModOnly("itemstages")
@ZenClass("mods.sevtweaks.stager.Stager")
public class Stager {
    private static Map<String, Stage> stageMap = new HashMap<>();

    @ZenMethod
    public static Map<String, Stage> getStageMap() {
        return stageMap;
    }

    @ZenMethod
    public static Stage getStage(String stage) {
        return stageMap.get(stage);
    }

    @ZenMethod
    public static String getIngredientStage(IIngredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        for (Stage stage : stageMap.values()) {
            if (stage.isStaged(ingredient)) {
                return stage.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public static String getContainerStage(String container) {
        if (container == null || container.length() < 1) {
            return null;
        }

        for (Stage stage : stageMap.values()) {
            if (stage.isStaged(container)) {
                return stage.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public static String getRecipeNameStage(String container) {
        return getContainerStage(container);
    }

    @ZenMethod
    public static boolean isStaged(IIngredient ingredient) {
        return getIngredientStage(ingredient) != null;
    }

    @ZenMethod
    public static boolean isStaged(String string) {
        return getContainerStage(string) != null;
    }

    @ZenMethod
    public static Stage initStage(String name) {
        if (name == null || name.length() < 1) {
            CraftTweakerAPI.logError(String.format("[Stager] Name %s cannot be null or less than one!", name));

            return null;
        }
        name = name.toLowerCase();
        if (stageMap.containsKey(name)) {
            CraftTweakerAPI.logError(String.format("[Stager] Failed to create builder for %s due to name already existing.", name));

            return null;
        }

        Stage stage = new Stage(name);
        stageMap.put(name, stage);

        return stage;
    }

    @ZenMethod
    public static void checkConflicts() {
        CraftTweakerAPI.logInfo("[Stage Duplicate] Starting duplicate checks....");

        Map<String, List<String>> duplicateRecipes = new HashMap<>();
        Map<String, List<String>> duplicateContainers = new HashMap<>();
        Map<IIngredient, List<String>> duplicateIngredient = new HashMap<>();

        for (Stage currStage : stageMap.values()) {
            for (Stage checkStage : stageMap.values()) {
                if (currStage.getStage().equals(checkStage.getStage())) {
                    continue;
                }
                Helper.getDuplicates(duplicateRecipes, currStage.getStage(), currStage.getStagedRecipes(), checkStage.getStagedRecipes());
                Helper.getDuplicates(duplicateContainers, currStage.getStage(), currStage.getStagedContainers(), checkStage.getStagedContainers());
                Helper.getIngredientDuplicates(duplicateIngredient, currStage.getStage(), currStage.getStagedIngredients(), checkStage.getStagedIngredients());
            }
        }

        duplicateRecipes.forEach((recipe, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate recipe stage for `%s` for stages %s", recipe, stages)));
        duplicateContainers.forEach((recipe, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate container stage for `%s` for stages %s", recipe, stages)));
        duplicateIngredient.forEach((recipe, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate ingredient stage for `%s` for stages %s", recipe, stages)));

        CraftTweakerAPI.logInfo("[Stage Duplicate] Completed duplicate checks!");
    }

    @ZenMethod
    public static void buildAll() {
        stageMap.forEach((s, stage) -> stage.build());
    }
}
