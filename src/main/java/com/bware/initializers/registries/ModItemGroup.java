package com.bware.initializers.registries;

import com.bware.JujutsuCraft;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup JUJUTSU = FabricItemGroupBuilder.build(new Identifier(JujutsuCraft.MOD_ID, "jujutsu"),
            () -> new ItemStack(ModItems.SIX_EYES));
}
