package com.promcteam.divinity.modules.list.gems;

import com.promcteam.codex.config.api.JYML;
import com.promcteam.codex.hooks.external.citizens.CitizensHK;
import com.promcteam.codex.utils.StringUT;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.promcteam.divinity.QuantumRPG;
import com.promcteam.divinity.modules.EModule;
import com.promcteam.divinity.modules.SocketItem;
import com.promcteam.divinity.modules.api.socketing.ModuleSocket;
import com.promcteam.divinity.modules.list.gems.GemManager.Gem;
import com.promcteam.divinity.modules.list.gems.merchant.MerchantTrait;
import com.promcteam.divinity.stats.bonus.BonusMap;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GemManager extends ModuleSocket<Gem> {

    public GemManager(@NotNull QuantumRPG plugin) {
        super(plugin, Gem.class);
    }

    @Override
    @NotNull
    public String getId() {
        return EModule.GEMS;
    }

    @Override
    @NotNull
    public String version() {
        return "1.76";
    }

    @Override
    public void setup() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    protected void onPostSetup() {
        super.onPostSetup();

        CitizensHK citizensHook = plugin.getCitizens();
        if (citizensHook != null) {
            TraitInfo trait = TraitInfo.create(MerchantTrait.class);
            citizensHook.registerTrait(plugin, trait);
        }
    }

    // -------------------------------------------------------------------- //
    // CLASSES

    public class Gem extends SocketItem {

        private TreeMap<Integer, BonusMap> bonusMap;

        public Gem(@NotNull QuantumRPG plugin, @NotNull JYML cfg) {
            super(plugin, cfg, GemManager.this);

            this.bonusMap = new TreeMap<>();
            for (String sLvl : cfg.getSection("bonuses-by-level")) {
                int lvl = StringUT.getInteger(sLvl, -1);
                if (lvl < 1) {
                    continue;
                }
                String   path = "bonuses-by-level." + sLvl + ".";
                BonusMap bMap = new BonusMap();
                bMap.loadStats(cfg, path + "item-stats");
                bMap.loadDamages(cfg, path + "damage-types");
                bMap.loadDefenses(cfg, path + "defense-types");

                this.bonusMap.put(lvl, bMap);
            }
        }

        @Nullable
        public BonusMap getBonusMap(int lvl) {
            Map.Entry<Integer, BonusMap> e = this.bonusMap.floorEntry(lvl);
            if (e == null) return null;

            return e.getValue();
        }

        @Override
        @NotNull
        protected ItemStack build(int lvl, int uses, int suc) {
            ItemStack item = super.build(lvl, uses, suc);

            ItemMeta meta = item.getItemMeta();
            if (meta == null) return item;

            BonusMap bMap = this.getBonusMap(lvl);
            if (bMap == null) return item;

            if (meta.hasDisplayName()) {
                String name = bMap.replacePlaceholders(meta.getDisplayName());
                meta.setDisplayName(name);
            }

            List<String> lore = meta.getLore();
            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    lore.set(i, bMap.replacePlaceholders(lore.get(i)));
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);

            return item;
        }
    }
}