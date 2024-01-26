package com.bware.core.registries;

import com.bware.core.cursed_energy.CursedEnergyComponent;
import com.bware.core.cursed_energy.initializer.EntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ModStatusEffect extends StatusEffect {

    public static final DamageSource SUREHIT = new DamageSource("cleave").setUsesMagic().setBypassesArmor().setBypassesProtection().setUnblockable();


    public ModStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (this == ModdedEffects.DOMAIN_HIT) {

            if (!entity.isPlayer()) {
                entity.timeUntilRegen = 8;
                entity.damage(SUREHIT, 1.0f);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        if (this == ModdedEffects.DOMAIN_HIT) {
            i = duration % 15;
            if (i == 0) {
                return true;
            }
        }

        return this == ModdedEffects.DOMAIN_HIT;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        if (this == ModdedEffects.DOMAIN_HIT && entity.isPlayer()) {
            CursedEnergyComponent component = EntityComponents.CURSED_ENERGY.get(entity);

            if (component.getCE() > component.getCEMax()) {
                entity.damage(SUREHIT, 0.3f * ((float)component.getCE() - component.getCEMax()));
            }
        }

    }
}
