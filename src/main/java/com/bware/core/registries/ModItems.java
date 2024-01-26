package com.bware.core.registries;

import com.bware.JujutsuCraft;
import com.bware.core.registries.CustomItems.SukunaFinger;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item GRADE_UP = registerItem("grade_up",
            new Item(new FabricItemSettings().group(ModItemGroup.JUJUTSU)));
    public static final Item SIX_EYES = registerItem("six_eyes",
            new Item(new FabricItemSettings().group(ModItemGroup.JUJUTSU).maxCount(1).rarity(Rarity.EPIC)));
    public static final Item REVERSE_CT = registerItem("reverse_ct",
            new Item(new FabricItemSettings().group(ModItemGroup.JUJUTSU).maxCount(1).rarity(Rarity.EPIC)));
    public static final Item SUKUNA_FINGER = registerItem("sukuna_finger",
            new SukunaFinger(new FabricItemSettings().group(ModItemGroup.JUJUTSU).maxCount(20).rarity(Rarity.EPIC)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.REGISTRIES.ITEM, new Identifier(JujutsuCraft.MOD_ID, name), item);
    }


    public static void registerModItems() {
        JujutsuCraft.LOGGER.debug("Registering Mod Items for " + JujutsuCraft.MOD_ID);

    }
}

