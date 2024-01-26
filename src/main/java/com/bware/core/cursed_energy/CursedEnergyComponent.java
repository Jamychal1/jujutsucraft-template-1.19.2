package com.bware.core.cursed_energy;

import com.bware.initializers.cca.EntityComponents;
import com.bware.core.cursed_energy.interfaces.CEComponentI;
import com.bware.initializers.event.ModAttributes;
import com.bware.initializers.registries.ModdedEffects;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class CursedEnergyComponent implements CEComponentI, AutoSyncedComponent {
    private static final int MAX_XP_BONUS = 50;
    private static final int REGEN_REQUIRE = 60;
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
        while (player.hasStatusEffect(ModdedEffects.REVERSE_CT)) {
            return (int)(player.getHealth() + 10);
        }
        return cursed_energy;
    }

    @Override
    public int getBaseCEMax() {
        return (int)player.getAttributeValue(ModAttributes.PLAYER_MAX_CE);
    }

    @Override
    public void serverTick() {
        if (player.hasStatusEffect(ModdedEffects.REVERSE_CT)) {
            return;
        }

        if (cursed_energy < getCEMax()) {
            regenerate();
        } else if (regenTime != 0) {
            regenTime = 0;
        }

        if (cursed_energy > getCEMax()) {
            cursed_energy = getCEMax();
        }

        EntityComponents.CURSED_ENERGY.sync(player);
    }

    private void regenerate() {
        regenTime += getRegenRate();
        if (regenTime >= getRegenRequirement()) {
            chargeMana(1);
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

        if (player.hasStatusEffect(ModdedEffects.AWAKENING)) {
            regenRate *= 1.85f * Math.pow(1.3f, player.getStatusEffect(ModdedEffects.AWAKENING).getAmplifier());
        }

        return (int)regenRate;
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
        return getCEMax() > 0 && !player.hasStatusEffect(ModdedEffects.CE_DAMAGE);
    }

    @Override
    public void useCE(int cost) {
        if (player.isCreative()) {
            return;
        }

        if (player.hasStatusEffect(ModdedEffects.SIX_EYES)){
            cursed_energy = Math.max(0, cursed_energy - (cost / 2));
        }

        cursed_energy = Math.max(0, cursed_energy - cost);
        regenTime = Math.min(regenTime, -getRegenRequirement());
    }

    @Override
    public void setCE(int value) {
        cursed_energy = value;
    }

    public void chargeMana(int charge) {
        cursed_energy += charge;
        if (cursed_energy > getCEMax()) {
            cursed_energy = getCEMax();
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        cursed_energy = tag.getInt("cursed_energy");
        regenTime = tag.getInt("regen_time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("cursed_energy", cursed_energy);
        tag.putInt("regen_time", regenTime);
    }

    private float framesPerTick() {

        return Math.min(getRegenRate() / 16f, 3f);
    }

    @Override
    public int getCEFrame() {
        return (int)ceFrame;
    }

    @Override
    public void clientTick() {

        ceFrame += framesPerTick();
        ceFrame %= CE_FRAMES;
    }
}
