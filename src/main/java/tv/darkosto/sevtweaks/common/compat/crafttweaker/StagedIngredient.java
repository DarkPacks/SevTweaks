package tv.darkosto.sevtweaks.common.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.sevtweaks.stager.StagedIngredient")
public class StagedIngredient {
    private IIngredient ingredient;
    private boolean stageRecipe;

    StagedIngredient(IIngredient ingredients, boolean stageRecipe) {
        this.ingredient = ingredients;
        this.stageRecipe = stageRecipe;
    }

    public IIngredient getIngredient() {
        return ingredient;
    }

    boolean shouldStageRecipe() {
        return stageRecipe;
    }
}
