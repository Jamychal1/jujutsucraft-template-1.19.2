package com.bware.core.cursed_energy.interfaces;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface CEComponentI extends ServerTickingComponent {
    int getCE();
    int getBaseCEMax();
    int getCEMax();
    int getXPBonus();

    void clientTick();

    int getRegenRate();

    boolean canAfford(int cost);

    boolean shouldRender();

    void useCE(int cost);

    void setCE(int value);



    int getCEFrame();

}