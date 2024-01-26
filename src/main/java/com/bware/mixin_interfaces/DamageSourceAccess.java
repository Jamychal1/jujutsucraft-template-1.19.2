package com.bware.mixin_interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;

public interface DamageSourceAccess {

    void setElectric();
    boolean isElectric();
    void setAffliction();
    boolean isAffliction();





    static boolean isElectric(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.isElectric();
    }
    static boolean isAffliction(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.isAffliction();
    }

    static DamageSource create(String name, boolean electric, boolean affliction) {
        DamageSource source = new DamageSource(name);
        if (source instanceof DamageSourceAccess access) {
            if (electric) access.setElectric();
            if (affliction) access.setAffliction();
        }
        return source;
    }

    static DamageSource magic(Entity attacker) {
        return new EntityDamageSource("jujutsu.direct_magic", attacker).setBypassesArmor().setUsesMagic();
    }

    default boolean isTransethereal() {
        return this instanceof DamageSource source && source.getAttacker() instanceof PlayerEntity player && player.isCreative();
    }


    DamageSource ELECTRIC = create("jujutsu.electric", true, false).setUnblockable();
    DamageSource SUREHIT = create("jujutsu.surehit", true, false).setUnblockable();
    DamageSource CURSEPOWER = new DamageSource("jujutsu.cursedpower").setOutOfWorld().setBypassesArmor().setBypassesProtection().setUnblockable();
    DamageSource REVERSETECHNIQUE = create("jujutsu.rct", false, true).setUsesMagic().setBypassesArmor();
    DamageSource CURSED_FLAME = create("jujutsu.cursed_flame", false, true).setBypassesArmor().setFire();
}
