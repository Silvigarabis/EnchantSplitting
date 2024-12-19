package io.github.silvigarabis.esplitter.invgui;

import static org.bukkit.Material.ENCHANTED_BOOK;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.silvigarabis.esplitter.Messages;
import io.github.silvigarabis.esplitter.data.ESplitterEvaluatedEnchantSet;

public class ESplitterInvGuiItemBuilder {

    static final ItemStack updateSlotElement(ESplitterInvGui gui, int pagedElemenetIndex) {
        var pageList = gui.pagedElementList.get(gui.currentShowingPageIndex);
        int elementIndex = gui.currentShowingPageIndex * ESplitterInvGui.slotsElements.length + pagedElemenetIndex;
        var enchantSet = gui.cachedEvaluatedEnchantSetList.get(elementIndex);
    
        var allEnchants = gui.controller.getCurrentEnchants();
    
        var itemElement = new ItemStack(ENCHANTED_BOOK);
        var itemElementMeta = (EnchantmentStorageMeta) itemElement.getItemMeta();
    
        // 设置显示标题 (title)
        itemElementMeta.setDisplayName(Messages.invGuiElementTitle.getText(enchantSet.enchantSet.size()));
    
        // 添加附魔 (enchants)
        for (Enchantment ench : enchantSet.enchantSet) {
            int enchLevel = allEnchants.get(ench);
            itemElementMeta.addStoredEnchant(ench, enchLevel, true);
        }
    
        itemElement.setItemMeta(itemElementMeta);
    
        return itemElement;
    }

    static final void createLocalizedButton(int slot, Messages msg, Material material) {
        final ItemStack item0 = new ItemStack(material);
        var meta0 = item0.getItemMeta();
        meta0.setDisplayName(msg.getText());
        item0.setItemMeta(meta0);
    
        ESplitterInvGui.slotItemsStatic[slot] = item0.clone();
    
        ESplitterInvGui.slotItemCreators[slot] = (player) -> {
            var metaDyn = meta0.clone();
            var itemDyn = item0.clone();
            metaDyn.setDisplayName(msg.getTextPlayer(player));
            itemDyn.setItemMeta(metaDyn);
            return itemDyn;
        };
    }
}
