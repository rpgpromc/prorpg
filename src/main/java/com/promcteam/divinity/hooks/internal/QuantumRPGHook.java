package com.promcteam.divinity.hooks.internal;

import com.promcteam.codex.utils.StringUT;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.promcteam.divinity.QuantumRPG;
import com.promcteam.divinity.data.api.RPGUser;
import com.promcteam.divinity.data.api.UserProfile;
import com.promcteam.divinity.hooks.HookClass;
import com.promcteam.divinity.hooks.HookLevel;
import com.promcteam.divinity.modules.list.classes.ClassManager;
import com.promcteam.divinity.modules.list.classes.api.UserClassData;

public class QuantumRPGHook implements HookLevel, HookClass {

    private QuantumRPG plugin;

    public QuantumRPGHook(@NotNull QuantumRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getClass(@NotNull Player player) {
        RPGUser user = plugin.getUserManager().getOrLoadUser(player);
        if (user == null) return "";

        UserProfile   prof  = user.getActiveProfile();
        UserClassData cData = prof.getClassData();
        if (cData == null) return "";

        return StringUT.colorOff(cData.getPlayerClass().getName());
    }

    @Override
    public int getLevel(@NotNull Player player) {
        RPGUser user = plugin.getUserManager().getOrLoadUser(player);
        if (user == null) return 0;

        UserProfile   prof  = user.getActiveProfile();
        UserClassData cData = prof.getClassData();
        return cData != null ? cData.getLevel() : 0;
    }

    @Override
    public void giveExp(@NotNull Player player, int amount) {
        RPGUser user = plugin.getUserManager().getOrLoadUser(player);
        if (user == null) return;

        UserProfile   prof  = user.getActiveProfile();
        UserClassData cData = prof.getClassData();
        if (cData == null) { return; }
        cData.addExp(amount);
    }

    @Override
    public void takeMana(@NotNull Player player, double amount, boolean ofMax) {
        ClassManager classManager = plugin.getModuleCache().getClassManager();
        if (classManager == null) return;

        classManager.consumeMana(player, amount, ofMax);
    }

}