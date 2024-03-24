package com.promcteam.divinity.hooks.external.mythicmobs;

import com.promcteam.codex.hooks.NHook;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import com.promcteam.divinity.QuantumRPG;
import com.promcteam.divinity.hooks.HookMobLevel;

public abstract class AbstractMythicMobsHK extends NHook<QuantumRPG> implements HookMobLevel {

    public AbstractMythicMobsHK(@NotNull QuantumRPG plugin) {
        super(plugin);
    }

    public abstract boolean isMythicMob(@NotNull Entity entity);

    @NotNull
    public abstract String getMythicNameByEntity(@NotNull Entity entity);

    public abstract double getMobLevel(@NotNull Entity entity);

    public abstract void setSkillDamage(@NotNull Entity entity, double amount);

    public abstract AbstractMythicEntity getMythicInstance(@NotNull Entity entity);

    public abstract int getMythicVersion();
}