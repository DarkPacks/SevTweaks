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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ZenRegister
@ModOnly("itemstages")
@ZenClass("mods.sevtweaks.stager.Stage")
public class Stage {
    private String stage;
    private Map<IIngredient, StagedIngredient> stagedIngredients = new HashMap<>();
    private List<String> stagedContainers = new ArrayList<>();
    private List<String> stagedRecipes = new ArrayList<>();

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
    public List<String> getStagedContainers() {
        return stagedContainers;
    }

    @ZenMethod
    public List<String> getStagedRecipes() {
        return stagedRecipes;
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
        if (stagedContainers.contains(container.toLowerCase())) {
            return this.getStage();
        }

        return null;
    }

    @ZenMethod
    public String getRecipeNameStage(String container) {
        if (stagedRecipes.contains(container.toLowerCase())) {
            return this.getStage();
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

    /**
     * Set the stage on the IItemStack given.
     */
    @ZenMethod
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
            if (ingredient == null) {
                CraftTweakerAPI.logError(String.format("[Stage %s] Ingredient can not be null!", this.getStage()));

                continue;
            }
            if (stagedIngredients.containsKey(ingredient)) {
                CraftTweakerAPI.logError(String.format("[Stage %s] Failed to add the ingredient `%s` due to already being added.", this.getStage(), ingredient.toString()));

                continue;
            }
            stagedIngredients.put(ingredient, new StagedIngredient(ingredient, recipeStage));
        }

        return this;
    }

    @ZenMethod
    public Stage addContainer(String container) {
        if (container == null || container.length() < 1) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Container can not be null or empty!", this.getStage()));

            return this;
        }
        if (stagedContainers.contains(container)) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Failed to add the container `%s` due to already being added.", this.getStage(), container));

            return this;
        }
        stagedContainers.add(container.toLowerCase());

        return this;
    }

    @ZenMethod
    public Stage addRecipeName(String recipeName) {
        if (recipeName == null || recipeName.length() < 1) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Recipe name can not be null or empty!", this.getStage()));

            return this;
        }
        String checkedRecipeName = Helper.validateRecipeName(recipeName);
        if (checkedRecipeName == null) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Recipe name `%s` is not valid! Example: minecraft:boat", this.getStage(), recipeName));

            return this;
        }
        if (stagedRecipes.contains(checkedRecipeName)) {
            CraftTweakerAPI.logError(String.format("[Stage %s] Failed to add the recipe name `%s` due to already being added.", this.getStage(), checkedRecipeName));

            return this;
        }
        stagedRecipes.add(checkedRecipeName.toLowerCase());

        CraftTweakerAPI.logInfo(stagedRecipes.toString());

        return this;
    }

    @ZenMethod
    public Stage build() {
        for (Map.Entry<IIngredient, StagedIngredient> stagedIngredient : stagedIngredients.entrySet()) {
            StagedIngredient ingredient = stagedIngredient.getValue();
            ItemStagesCrT.addItemStage(getStage(), ingredient.getIngredient());
            if (ingredient.shouldStageRecipe()) {
                Recipes.setRecipeStage(getStage(), ingredient.getIngredient());
            }
        }
        for (String stagedRecipe : stagedRecipes) {
            Recipes.setRecipeStage(getStage(), stagedRecipe);
        }

        return this;
    }
}

