package com.bware.initializers.event;

import com.bware.JujutsuCraft;
import com.bware.core.screens.ChangerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlerTypes {
    public static final ScreenHandlerType<ChangerScreenHandler> CHANGER = register("smokestack", ChangerScreenHandler::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registry.SCREEN_HANDLER, new Identifier(JujutsuCraft.MOD_ID, id), new ScreenHandlerType<>(factory));
    }

    public static void registerTypes() {
        System.out.println("Registering screen handler types for " + JujutsuCraft.MOD_ID);
    }
}

