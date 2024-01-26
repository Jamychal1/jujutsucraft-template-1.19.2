package com.bware.core.cursed_energy;

import com.bware.core.cursed_energy.initializer.EntityComponents;
import com.bware.core.cursed_energy.interfaces.CEComponentI;
import com.bware.core.registries.ModAttributes;
import com.bware.core.registries.ModdedEffects;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class CursedEnergyComponent implements CEComponentI, AutoSyncedComponent {
    private static final int REGEN_REQUIRE = 60;
    private static final int MAX_XP_BONUS = 50;
    public static final int CE_FRAMES = 324;

    private int cursed_energy = 0;
    private int regenTime = 0;
    private final PlayerEntity player;

    private float ceFrame = 0;

    public CursedEnergyComponent(PlayerEntity playerIn) {
        player = playerIn;
    }

    @Override
    public int getCE() {
        if (player.hasStatusEffect(ModdedEffects.REVERSE_CT)){
            return (int)(player.getHealth() * 2);
        }
        return cursed_energy;
    }

    @Override
    public int getBaseCEMax() {
        return (int)player.getAttributeValue(ModAttributes.PLAYER_MAX_CE);
    }

    @Override
    public int getCEMax() {
        return Math.max(getBaseCEMax() + getXPBonus(), 0);
    }

    @Override
    public int getXPBonus() {
        if (getBaseCEMax() <= 0) {
            return 0;
        }
        return Math.min(player.experienceLevel/2, MAX_XP_BONUS);
    }

    @Override
    public boolean canAfford(int cost) {
        return player.isCreative() || getCE() >= cost;
    }

    @Override
    public boolean shouldRender() {
        return getCEMax() > 0;
    }

    @Override
    public void useCE(int cost) {
        if (player.isCreative()) {
            return;
        }

        if (player.hasStatusEffect(ModdedEffects.SIX_EYES)){
            cursed_energy = Math.max(0, cursed_energy - cost / 2);
        }

        cursed_energy = Math.max(0, cursed_energy - cost);
        regenTime = Math.min(regenTime, -getRegenRequirement());
    }

    @Override
    public void setCE(int value) {
        cursed_energy = value;
    }

    public void chargeCE(int charge) {
        cursed_energy += charge;
        if (cursed_energy > getCEMax() && !player.hasStatusEffect(ModdedEffects.CE_REGEN)) {
            cursed_energy = getCEMax();
        }
    }

    @Override
    public int getCEFrame() {
        return (int) ceFrame;
    }

    @Override
    public void serverTick() {
        if (cursed_energy < getCEMax() || player.hasStatusEffect(ModdedEffects.CE_REGEN)) {
            regenerate();
        } else if (regenTime != 0) {
            regenTime = 0;
        }

        if (cursed_energy > getCEMax() && !player.hasStatusEffect(ModdedEffects.CE_REGEN)) {
            cursed_energy = getCEMax();
        }
        EntityComponents.CURSED_ENERGY.sync(player);
    }

    @Override
    public void clientTick() {

        ceFrame += framesPerTick();
        ceFrame %= CE_FRAMES;
    }

    private float framesPerTick() {
        return Math.min(getRegenRate() / 16f, 3f);
    }

    private void regenerate(){
        regenTime += getRegenRate();
        if (regenTime >= getRegenRequirement()) {
            chargeCE(1);
            regenTime -= getRegenRequirement();
        }
    }

    private int getRegenRequirement() {
        return REGEN_REQUIRE;
    }

    @Override
    public int getRegenRate() {
        float regenRate = (float)player.getAttributeValue(ModAttributes.PLAYER_CE_REGEN);

        if (player.isCreative()) {
            return REGEN_REQUIRE - regenTime;
        }
        else if (player.hasStatusEffect(ModdedEffects.SUPPRESSED)) {
            return 0;
        }

        /*if (player.hasStatusEffect(ModStatusEffects.DREAMY)) {
            regenRate *= 1.85f * Math.pow(1.3f, player.getStatusEffect(ModStatusEffects.DREAMY).getAmplifier());
        }*/

        return (int)regenRate;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }
}
