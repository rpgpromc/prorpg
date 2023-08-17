package su.nightexpress.quantumrpg.modules.list.itemgenerator.editor.trimmings;

import mc.promcteam.engine.manager.api.menu.Slot;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;
import su.nightexpress.quantumrpg.modules.list.itemgenerator.editor.AbstractEditorGUI;
import su.nightexpress.quantumrpg.modules.list.itemgenerator.editor.EditorGUI;

public class TrimmingPatternsGUI extends AbstractEditorGUI {
    private final TrimmingListGUI.TrimmingEntry entry;

    public TrimmingPatternsGUI(Player player, ItemGeneratorReference itemGenerator, TrimmingListGUI.TrimmingEntry entry) {
        super(player, 6, "[&d" + itemGenerator.getId() + "&r] editor/" + EditorGUI.ItemType.ARMOR_TRIMINGS.getTitle(), itemGenerator);
        this.entry = entry;
    }

    @Override
    public void setContents() {
        int i = 0;
        for (TrimPattern pattern : Registry.TRIM_PATTERN) {
            i++;
            if (i % this.inventory.getSize() == 53) {
                this.setSlot(i, getNextButton());
                i++;
            } else if (i % 9 == 8) {i++;}
            if (i % this.inventory.getSize() == 45) {
                this.setSlot(i, getPrevButton());
                i++;
            } else if (i % 9 == 0) {i++;}
            String name = pattern.getKey().getKey();
            setSlot(i, new Slot(createItem(TrimmingGUI.fromPattern(pattern),
                    "&e" + name.substring(0, 1).toUpperCase() + name.substring(1))) {
                @Override
                public void onLeftClick() {
                    itemGenerator.getConfig().remove(TrimmingListGUI.getPath(entry.getArmorTrim()));
                    entry.setArmorTrim(new ArmorTrim(entry.getArmorTrim().getMaterial(), pattern));
                    itemGenerator.getConfig().set(TrimmingListGUI.getPath(entry.getArmorTrim()), entry.getWeight());
                    saveAndReopen();
                    close();
                }
            });
        }
        this.setSlot(this.getPages() * this.inventory.getSize() - 9, getPrevButton());
        this.setSlot(this.getPages() * this.inventory.getSize() - 1, getNextButton());
    }
}
