package tv.darkosto.sevtweaks.common.compat.crafttweaker;

import com.blamejared.recipestages.handlers.Recipes;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import net.minecraftforge.fml.common.Optional.Method;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tv.darkosto.sevtweaks.common.util.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ZenRegister
@ZenClass("mods.sevtweaks.stager.Stager")
public class Stager {
    private static Map<String, Stage> stageMap = new HashMap<>();

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
    @Method(modid = "recipestages")
    public static String getRecipeNameStage(String recipeName) {
        for (Stage stage : stageMap.values()) {
            if (stage.getRecipeNameStage(recipeName) != null) {
                return stage.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    @Method(modid = "dimstages")
    public static String getDimensionStage(int dimension) {
        for (Stage stage : stageMap.values()) {
            if (stage.getDimensionStage(dimension) != null) {
                return stage.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    @Method(modid = "recipestages")
    public static List<String> getContainerStages(String container) {
        return getTypeStages(Types.CONTAINER, container);
    }

    @ZenMethod
    @Method(modid = "recipestages")
    public static List<String> getPackageStages(String packageName) {
        return getTypeStages(Types.PACKAGE, packageName);
    }

    @ZenMethod
    @Method(modid = "mobstages")
    public static String getMobStage(String mobName) {
        for (Stage stage : stageMap.values()) {
            if (stage.getMobStage(mobName) != null) {
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
    @Method(modid = "dimstages")
    public static boolean isStaged(int dimension) {
        return getDimensionStage(dimension) != null;
    }

    @ZenMethod
    public static void checkConflicts() {
        CraftTweakerAPI.logInfo("[Stage Duplicate] Starting duplicate checks....");

        Map<String, List<String>> duplicateRecipes = new HashMap<>();
        Map<String, List<String>> duplicateDimensions = new HashMap<>();
        Map<String, List<String>> duplicateMobs = new HashMap<>();
        Map<String, List<String>> duplicateTiCMaterial = new HashMap<>();
        Map<String, List<String>> duplicateTiCModifier = new HashMap<>();
        Map<String, List<String>> duplicateTiCToolType = new HashMap<>();
        Map<IIngredient, List<String>> duplicateIngredient = new HashMap<>();

        for (Stage currStage : stageMap.values()) {
            for (Stage checkStage : stageMap.values()) {
                if (currStage.getStage().equals(checkStage.getStage())) {
                    continue;
                }
                Helper.getDuplicates(duplicateRecipes, currStage.getStage(), currStage.getStagedTypes(Types.RECIPE_NAME), checkStage.getStagedTypes(Types.RECIPE_NAME));
                Helper.getDuplicates(duplicateDimensions, currStage.getStage(), currStage.getStagedTypes(Types.DIMENSION), checkStage.getStagedTypes(Types.DIMENSION));
                Helper.getDuplicates(duplicateMobs, currStage.getStage(), currStage.getStagedTypes(Types.MOB), checkStage.getStagedTypes(Types.MOB));
                Helper.getDuplicates(duplicateTiCMaterial, currStage.getStage(), currStage.getStagedTypes(Types.TINKER_MATERIAL), checkStage.getStagedTypes(Types.TINKER_MATERIAL));
                Helper.getDuplicates(duplicateTiCModifier, currStage.getStage(), currStage.getStagedTypes(Types.TINKER_MODIFIER), checkStage.getStagedTypes(Types.TINKER_MODIFIER));
                Helper.getDuplicates(duplicateTiCToolType, currStage.getStage(), currStage.getStagedTypes(Types.TINKER_TOOL), checkStage.getStagedTypes(Types.TINKER_TOOL));
                Helper.getIngredientDuplicates(duplicateIngredient, currStage.getStage(), currStage.getStagedIngredients(), checkStage.getStagedIngredients());
            }
        }

        duplicateRecipes.forEach((recipe, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate recipe stage for `%s` for stages %s", recipe, stages)));
        duplicateDimensions.forEach((dimension, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate dimension stage for `%s` for stages %s", dimension, stages)));
        duplicateMobs.forEach((dimension, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate mob stage for `%s` for stages %s", dimension, stages)));
        duplicateTiCMaterial.forEach((dimension, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate TiC Material stage for `%s` for stages %s", dimension, stages)));
        duplicateTiCModifier.forEach((dimension, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate TiC Modifier stage for `%s` for stages %s", dimension, stages)));
        duplicateTiCToolType.forEach((dimension, stages) -> CraftTweakerAPI.logError(String.format("[Stage Duplicate] Found a duplicate TiC ToolType stage for `%s` for stages %s", dimension, stages)));
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
     * Update a passed Map of the staged types to another Map listing which contains that type
     * and a String List of the stages which are for that Staged Type.
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

    private static String getTypeStage(String value) {
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

    private static List<String> getTypeStages(Types type, String value) {
        List<String> stages = new ArrayList<>();
        if (value == null || value.length() < 1) {
            return null;
        }

        for (Stage stage : stageMap.values()) {
            List<StagedType> stagedTypes = stage.getStagedTypes(type);
            for (StagedType stagedType : stagedTypes) {
                if (stagedType.getValue().equalsIgnoreCase(value)) {
                    stages.add(stage.getStage());
                }
            }
        }

        return stages;
    }
}
