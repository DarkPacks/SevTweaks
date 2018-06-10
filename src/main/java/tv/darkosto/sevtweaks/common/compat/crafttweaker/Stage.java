package tv.darkosto.sevtweaks.common.compat.crafttweaker;

import com.blamejared.recipestages.handlers.Recipes;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import net.darkhax.itemstages.compat.crt.ItemStagesCrT;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tv.darkosto.sevtweaks.common.util.Helper;

import java.util.*;
import java.util.stream.Collectors;

@ZenRegister
@ModOnly("itemstages")
@ZenClass("mods.sevtweaks.stager.Stage")
public class Stage {
    private String stage;
    private Map<IIngredient, StagedIngredient> stagedIngredients = new HashMap<>();
    private List<StagedType> stagedTypes = new ArrayList<>();

    public Stage(String stage) {
        this.stage = stage;
    }

    @ZenMethod
    public String getStage() {
        return stage;
    }

    @ZenMethod
    public Map<IIngredient, StagedIngredient> getStagedIngredients() {
        return stagedIngredients;
    }

    @ZenMethod
    public List<StagedType> getStagedTypes() {
        return stagedTypes;
    }

    public List<StagedType> getStagedTypes(Types type) {
        return stagedTypes.stream().filter(t -> t.getType() == type).collect(Collectors.toList());
    }

    @ZenMethod
    public String getIngredientStage(IIngredient testIngredient) {
        for (Map.Entry<IIngredient, StagedIngredient> stagedIngredient : stagedIngredients.entrySet()) {
            IIngredient ingredient = stagedIngredient.getValue().getIngredient();

            if (testIngredient instanceof MCOreDictEntry) {
                if (ingredient instanceof MCOreDictEntry) {
                    if (((MCOreDictEntry) testIngredient).getName().equals(((MCOreDictEntry) ingredient).getName())) {
                        return this.getStage();
                    }
                }
            } else if (ingredient instanceof MCOreDictEntry) {
                if (ingredient.contains(testIngredient)) {
                    return this.getStage();
                }
            } else if (ingredient.contains(testIngredient)) {
                return this.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public String getContainerStage(String container) {
        List<StagedType> stagedContainers = getStagedTypes(Types.CONTAINER);
        for (StagedType stagedContainer : stagedContainers) {
            if (stagedContainer.getValue().equalsIgnoreCase(container)) {
                return this.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public String getRecipeNameStage(String container) {
        List<StagedType> stagedRecipes = getStagedTypes(Types.RECIPE_NAME);
        for (StagedType stagedRecipe : stagedRecipes ) {
            if (stagedRecipe.getValue().equalsIgnoreCase(container)) {
                return this.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public String getDimensionStage(int dimension) {
        List<StagedType> stagedDimensions = getStagedTypes(Types.DIMENSION);
        for (StagedType stagedDimension : stagedDimensions ) {
            if (stagedDimension.getValue().equalsIgnoreCase(Integer.toString(dimension))) {
                return this.getStage();
            }
        }

        return null;
    }

    @ZenMethod
    public boolean isStaged(IIngredient ingredient) {
        return this.getIngredientStage(ingredient) != null;
    }

    @ZenMethod
    public boolean isStaged(String name) {
        if (this.getContainerStage(name) != null) {
            return true;
        }

        if (Helper.validateRecipeName(name) == null) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Recipe name `%s` is not valid! Example: minecraft:boat", this.getStage(), name));

            return false;
        }

        return this.getRecipeNameStage(name) != null;
    }

    @ZenMethod
    public boolean isStaged(int dimension) {
        return this.getDimensionStage(dimension) != null;
    }

    /**
     * Set the stage on the IItemStack given.
     */
    @ZenMethod
    @SuppressWarnings("UnusedReturnValue")
    public Stage addIngredient(IIngredient ingredient, @Optional(valueBoolean = true) boolean recipeStage) {
        if (ingredient == null) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Ingredient can not be null!", this.getStage()));

            return this;
        }
        if (stagedIngredients.containsKey(ingredient)) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Failed to add the ingredient `%s` due to already being added.", this.getStage(), ingredient.toString()));

            return this;
        }
        stagedIngredients.put(ingredient, new StagedIngredient(ingredient, recipeStage));

        return this;
    }

    @ZenMethod
    public Stage addIngredients(IIngredient[] ingredients, @Optional(valueBoolean = true) boolean recipeStage) {
        for (IIngredient ingredient : ingredients) {
            addIngredient(ingredient, recipeStage);
        }

        return this;
    }

    @ZenMethod
    public Stage addDimension(int dimension) {
        stageType(Types.DIMENSION, Integer.toString(dimension));

        return this;
    }

    @ZenMethod
    public Stage addContainer(String container) {
        stageType(Types.CONTAINER, container);

        return this;
    }

    @ZenMethod
    public Stage addPackage(String container) {
        stageType(Types.PACKAGE, container);

        return this;
    }

    @ZenMethod
    public Stage addRecipeName(String recipeName) {
        if (Helper.validateRecipeName(recipeName) == null) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Recipe name `%s` is not valid! Example: minecraft:boat", this.getStage(), recipeName));

            return this;
        }

        stageType(Types.RECIPE_NAME, recipeName);

        return this;
    }

    public void stageType(Types type, String value) {
        // TODO: Check if it was already added to this Stage.
        stagedTypes.add(new StagedType(value, type));
    }

    @ZenMethod
    @SuppressWarnings("UnusedReturnValue")
    public Stage build() {
        for (Map.Entry<IIngredient, StagedIngredient> stagedIngredient : stagedIngredients.entrySet()) {
            StagedIngredient ingredient = stagedIngredient.getValue();
            ItemStagesCrT.addItemStage(getStage(), ingredient.getIngredient());
            if (ingredient.shouldStageRecipe()) {
                Recipes.setRecipeStage(getStage(), ingredient.getIngredient());
            }
        }

        for (StagedType stagedType : stagedTypes) {
            switch (stagedType.getType()) {
                case RECIPE_NAME:
                    Recipes.setRecipeStage(getStage(), stagedType.getValue());
                    break;
                case DIMENSION:
                    break;
            }
        }

        return this;
    }
}

