package com.bware.mixin;

import com.bware.core.registries.ModAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    @Shadow
    public abstract Arm getMainArm();

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource damageSource);

    @Shadow
    public abstract float getAttackCooldownProgress(float baseTime);

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow
    public abstract PlayerInventory getInventory();

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    public abstract void increaseStat(Stat<?> stat, int amount);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.setReturnValue((info.getReturnValue())
                .add(ModAttributes.PLAYER_CE_REGEN, 8d)
                .add(ModAttributes.PLAYER_MAX_CE, 10d)
                .add(ModAttributes.PLAYER_EVASION, 7d));
    }
}

