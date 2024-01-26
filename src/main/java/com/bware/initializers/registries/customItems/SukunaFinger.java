package com.bware.initializers.registries.customItems;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SukunaFinger extends Item {
    public SukunaFinger(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.jujutsucraft.sukuna_finger.tooltip.shift").formatted(Formatting.DARK_RED));
        } else {
            tooltip.add(Text.translatable("item.jujutsucraft.sukuna_finger.tooltip").formatted(Formatting.DARK_RED));
        }
    }
}
