package tv.darkosto.sevtweaks.common.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;

public class StagedIngredient {
    private IIngredient ingredient;
    private boolean stageRecipe;

    public StagedIngredient(IIngredient ingredients, boolean stageRecipe) {
        this.ingredient = ingredients;
        this.stageRecipe = stageRecipe;
    }

    public IIngredient getIngredient() {
        return ingredient;
    }

    public boolean shouldStageRecipe() {
        return stageRecipe;
    }
}
