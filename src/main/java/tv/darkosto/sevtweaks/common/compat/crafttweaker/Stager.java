package tv.darkosto.sevtweaks.common.compat.crafttweaker;

import com.blamejared.recipestages.handlers.Recipes;
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

    // TODO: Expose this to Zen?
    public static String getTypeStage(String value) {
        if (value == null || value.length() < 1) {
            return null;
        }

        for (Stage stage : stageMap.values()) {
            if (stage.isStaged(value)) {
                return stage.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public static String getDimensionStage(int dimension) {
        for (Stage stage : stageMap.values()) {
            if (stage.isStaged(dimension)) {
                return stage.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public static boolean isStaged(IIngredient ingredient) {
        return getIngredientStage(ingredient) != null;
    }

    @ZenMethod
    public static boolean isStaged(String string) {
        return getTypeStage(string) != null;
    }

    @ZenMethod
    public static boolean isStaged(int dimension) {
        return getDimensionStage(dimension) != null;
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
        Map<String, List<String>> duplicateDimensions = new HashMap<>();
        Map<IIngredient, List<String>> duplicateIngredient = new HashMap<>();

        for (Stage currStage : stageMap.values()) {
            for (Stage checkStage : stageMap.values()) {
                if (currStage.getStage().equals(checkStage.getStage())) {
                    continue;
                }
                Helper.getDuplicates(duplicateRecipes, currStage.getStage(), currStage.getStagedTypes(Types.RECIPE_NAME), checkStage.getStagedTypes(Types.RECIPE_NAME));
                Helper.getDuplicates(duplicateDimensions, currStage.getStage(), currStage.getStagedTypes(Types.DIMENSION), checkStage.getStagedTypes(Types.DIMENSION));
                Helper.getIngredientDuplicates(duplicateIngredient, currStage.getStage(), currStage.getStagedIngredients(), checkStage.getStagedIngredients());
            }
        }

        duplicateRecipes.forEach((recipe, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate recipe stage for `%s` for stages %s", recipe, stages)));
        duplicateDimensions.forEach((dimension, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate dimension stage for `%s` for stages %s", dimension, stages)));
        duplicateIngredient.forEach((ingredient, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate ingredient stage for `%s` for stages %s", ingredient, stages)));

        CraftTweakerAPI.logInfo("[Stage Duplicate] Completed duplicate checks!");
    }

    @ZenMethod
    public static void buildAll() {
        Map<Types, Map<String, List<String>>> stagedTypes = new HashMap<>();
        stagedTypes.put(Types.CONTAINER, new HashMap<>());
        stagedTypes.put(Types.PACKAGE, new HashMap<>());

        stageMap.forEach((s, stage) -> {
            getStagedTypes(Types.CONTAINER, stagedTypes.get(Types.CONTAINER), stage);
            getStagedTypes(Types.PACKAGE, stagedTypes.get(Types.PACKAGE), stage);

            stage.build();
        });

        // Stage the Containers based on the built stage mapping.
        Map<String, List<String>> stagedContainers = stagedTypes.get(Types.CONTAINER);
        stagedContainers.forEach((container, stages) -> {
            String[] forStages = new String[]{};
            forStages = stages.toArray(forStages);

            Recipes.setContainerStage(container, forStages);
        });

        // Stage the Packages based on the built stage mapping.
        Map<String, List<String>> stagedPackages = stagedTypes.get(Types.PACKAGE);
        stagedPackages.forEach((packageName, stages) -> {
            String[] forStages = new String[]{};
            forStages = stages.toArray(forStages);

            Recipes.setPackageStage(packageName, forStages);
        });
    }

    /**
     *
     */
    private static void getStagedTypes(Types type, Map<String, List<String>> stringListMap, Stage stage) {
        for (StagedType stagedType : stage.getStagedTypes(type)) {
            if (stringListMap.containsKey(stagedType.getValue())) {
                List<String> currentStages = stringListMap.get(stagedType.getValue());
                currentStages.add(stage.getStage());
            } else {
                List<String> stages = new ArrayList<>();
                stages.add(stage.getStage());
                stringListMap.put(stagedType.getValue(), stages);
            }
        }
    }
}
