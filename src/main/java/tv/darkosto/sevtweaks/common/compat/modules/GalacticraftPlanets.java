package tv.darkosto.sevtweaks.common.compat.modules;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import tv.darkosto.sevtweaks.SevTweaks;
import tv.darkosto.sevtweaks.common.compat.ICompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalacticraftPlanets extends ICompat {
    private Map<ItemStack, ItemStack> smeltingMap = new HashMap<>();
    private List<ItemStack> removals = new ArrayList<>();

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
        // As GC Planets registers it's recipes late and we don't want some. We'll have to do it ourselves...
        Item marsBlocks = Item.REGISTRY.getObject(new ResourceLocation("galacticraftplanets", "mars"));
        if (marsBlocks != null) {
            removals.add(new ItemStack(marsBlocks, 1)); // Mars: Copper Ore
            removals.add(new ItemStack(marsBlocks, 1, 1)); // Mars: Tin Ore
            removals.add(new ItemStack(marsBlocks, 1, 3)); // Mars: Iron Ore
        }

        Item venusBlocks = Item.REGISTRY.getObject(new ResourceLocation("galacticraftplanets", "venus"));
        if (venusBlocks != null) {
            removals.add(new ItemStack(venusBlocks, 1, 6)); // Venus: Aluminum Ore
            removals.add(new ItemStack(venusBlocks, 1, 7)); // Venus: Copper Ore
            removals.add(new ItemStack(venusBlocks, 1, 8)); // Venus: Galena Ore
            removals.add(new ItemStack(venusBlocks, 1, 9)); // Venus: Quartz Ore
            removals.add(new ItemStack(venusBlocks, 1, 11)); // Venus: Tin Ore
        }

        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            // TODO: Can this be done better? I.E. Performance wise?
            for (ItemStack removal : removals) {
                if (entry.getKey().isItemEqual(removal)) {
                    SevTweaks.logger.info(String.format("Removing GCPlanets %s Smelting Recipe.", entry.getKey().getDisplayName()));
                    smeltingMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {
            FurnaceRecipes.instance().getSmeltingList().remove(entry.getKey(), entry.getValue());
        }
    }
}
