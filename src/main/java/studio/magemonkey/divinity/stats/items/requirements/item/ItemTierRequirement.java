package studio.magemonkey.divinity.stats.items.requirements.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import studio.magemonkey.codex.config.api.ILangMsg;
import studio.magemonkey.divinity.Divinity;
import studio.magemonkey.divinity.config.Config;
import studio.magemonkey.divinity.modules.LeveledItem;
import studio.magemonkey.divinity.modules.ModuleItem;
import studio.magemonkey.divinity.modules.api.QModuleDrop;
import studio.magemonkey.divinity.stats.items.ItemStats;
import studio.magemonkey.divinity.stats.items.ItemTags;
import studio.magemonkey.divinity.stats.items.requirements.api.ItemRequirement;
import studio.magemonkey.divinity.stats.tiers.Tier;

public class ItemTierRequirement extends ItemRequirement<String> {

    public ItemTierRequirement(@NotNull String name, @NotNull String format) {
        super(
                "tier", name, format,
                ItemTags.PLACEHOLDER_REQ_ITEM_TIER,
                ItemTags.TAG_REQ_ITEM_TIER,
                PersistentDataType.STRING);

        // Legacy keys
        this.keys.add(NamespacedKey.fromString("prorpgitems:req_item_tier"));
        this.keys.add(NamespacedKey.fromString("prorpgitems:qrpg_req_item_tiertier"));
        this.keys.add(NamespacedKey.fromString("quantumrpg:qrpg_req_item_tiertier"));
    }

    @Override
    @NotNull
    public Class<String> getParameterClass() {
        return String.class;
    }

    @Override
    public boolean canApply(@NotNull ItemStack src, @NotNull ItemStack target) {
        String tierId = this.getRaw(src);
        if (tierId == null) return true;

        QModuleDrop<?> module = ItemStats.getModule(target);
        if (module == null) return true;

        String itemId = ItemStats.getId(target);
        if (itemId == null) return true;

        ModuleItem moduleItem = module.getItemById(itemId);
        if (moduleItem == null || !(moduleItem instanceof LeveledItem)) return true;

        Tier tier = ((LeveledItem) moduleItem).getTier();

        return tier.getId().equalsIgnoreCase(tierId);
    }

    @Override
    public ILangMsg getApplyMessage(@NotNull ItemStack src, @NotNull ItemStack target) {
        String tierId = this.getRaw(src);
        if (tierId == null) return null;

        Tier tier = Config.getTier(tierId);
        if (tier == null) return null;

        return Divinity.getInstance().lang().Module_Item_Apply_Error_Tier.replace("%tier%", tier.getName());
    }

    @Override
    @NotNull
    public String formatValue(@NotNull ItemStack item, @NotNull String value) {
        Tier tier = Config.getTier(value);
        if (tier == null) {
            Divinity.getInstance().warn("Invalid Tier requirement provided: '" + value + "' !");
            return "";
        }

        return tier.getName();
    }
}
