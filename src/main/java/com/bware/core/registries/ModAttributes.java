package com.bware.core.registries;

import com.bware.JujutsuCraft;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModAttributes {
    public static final EntityAttribute PLAYER_MAX_CE = register("player.max_ce",
            (new ClampedEntityAttribute("attribute.sorcery.name.player.max_ce", 0.0D, 0.0D, 1024.0D)).setTracked(true));
    public static final EntityAttribute PLAYER_CE_REGEN = register("player.ce_regen",
            (new ClampedEntityAttribute("attribute.sorcery.name.player.ce_regen", 8.0D, 0.0D, 1024.0D)).setTracked(true));
    public static final EntityAttribute PLAYER_EVASION = register("player.evasion",
            (new ClampedEntityAttribute("attribute.sorcery.name.player.evasion", 0.5D, 0.0D, 1024.0D)).setTracked(true));

    private static EntityAttribute register(String id, EntityAttribute attribute) {
        return Registry.register(Registry.ATTRIBUTE, new Identifier(JujutsuCraft.MOD_ID, id), attribute);
    }

    public static void registerAttributes() {
        System.out.println("Registering entity attributes for " + JujutsuCraft.MOD_ID);
    }
}
