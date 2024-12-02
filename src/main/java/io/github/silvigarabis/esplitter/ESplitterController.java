/*
   Copyright (c) 2023 Silvigarabis
   EnchantmentSplitter is licensed under Mulan PSL v2.
   You can use this software according to the terms and conditions of the Mulan PSL v2. 
   You may obtain a copy of Mulan PSL v2 at:
            http://license.coscl.org.cn/MulanPSL2 
   THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.  
   See the Mulan PSL v2 for more details.  
*/

package io.github.silvigarabis.esplitter;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Unmodifiable;

import static org.bukkit.Material.ENCHANTED_BOOK;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 控制一次会话。
 */
public final class ESplitterController {
    private Player player;
    private ItemStack selectedItem;
    private Map<Enchantment, Integer> currentEnchants;
    private List<Set<Enchantment>> evaluatedEnchantGroupList;
    private ControllerConfig config;

    public ItemStack getSelectedItem() {
        return selectedItem.clone();
    }
    public Map<Enchantment, Integer> getCurrentEnchants() {
        return new LinkedHashMap<>(currentEnchants);
    }
    public List<Set<Enchantment>> getEvaluatedEnchantGroupList() {
        return new LinkedList<>(evaluatedEnchantGroupList.stream().map(LinkedHashSet::new).toList());
    }

    public static class ControllerConfig {
        public int maxEnchantSetSize = 1;
    }

    public ESplitterController(Player player){
        this.config = new ControllerConfig();
        this.player = player;
    }

    public void selectItem(ItemStack item){
        if (item != null) {
            this.selectedItem = item.clone();
        } else {
            this.selectedItem = null;
        }
        this.updateEnchants();
        this.evalEnchantsSet();
    }

    /**
     * 更新已知的附魔列表
     */
    private void updateEnchants() {
        Map<Enchantment, Integer> currentEnchants;
        if (this.selectedItem.getType().equals(ENCHANTED_BOOK)){
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.selectedItem.getItemMeta();
            currentEnchants = new LinkedHashMap<>(meta.getStoredEnchants());
        } else {
            currentEnchants = new LinkedHashMap<>(this.selectedItem.getEnchantments());
        }
        this.currentEnchants = currentEnchants;
    }

    /**
     * Evaluates the current enchantments set.
     */
    private void evalEnchantsSet(){
        int maxEnchantSetSize = this.config.maxEnchantSetSize;
        List<Set<Enchantment>> evaluatedEnchantGroupList = new LinkedList<>();

        // 这里做了一个以 maxEnchantSetSize 为单位的分组
        Set<Enchantment> enchantSet = null;
        for (var ench : this.currentEnchants.keySet()){
            if (enchantSet == null || enchantSet.size() >= maxEnchantSetSize) {
                enchantSet = new LinkedHashSet<>();
                evaluatedEnchantGroupList.add(enchantSet);
            }
            enchantSet.add(ench);
        }

        this.evaluatedEnchantGroupList = evaluatedEnchantGroupList;
    }

    public boolean removeEnchants(int enchantSetIndex){
        var enchantSet = this.evaluatedEnchantGroupList.get(enchantSetIndex);
        if (enchantSet == null) {
            return false;
        } else {
            this.evaluatedEnchantGroupList.set(enchantSetIndex, null);
        }
        for (var ench : enchantSet){
            this.currentEnchants.remove(ench);
        }
        this.updateItem();
        return true;
    }

    /**
     * 在物品上移除已经被移除的附魔
     */
    public ItemStack updateItem() {
        var item = this.selectedItem;
        if (item == null) {
            return null;
        }

        if (item.getType().equals(ENCHANTED_BOOK)){
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

            Set<Enchantment> enchantsToRemove = new HashSet<>(meta.getStoredEnchants().keySet());
            enchantsToRemove.removeAll(this.currentEnchants.keySet());
            for (var ench : enchantsToRemove) {
                meta.removeStoredEnchant(ench);
            }

            item.setItemMeta(meta);
        } else {
            Set<Enchantment> enchantsToRemove = new HashSet<>(item.getEnchantments().keySet());
            enchantsToRemove.removeAll(this.currentEnchants.keySet());
            for (var ench : enchantsToRemove) {
                item.removeEnchantment(ench);
            }
        }
        return item.clone();
    }

    public boolean splitEnchantSet(int enchantSetIndex){
        throw new RuntimeException("method not implements");
    }
    
    public boolean grindEnchantSet(int enchantSetIndex){
        throw new RuntimeException("method not implements");
    }

    // public boolean removeEnchantment(EnchantmentSet enchantSet){
    //     if (selectedItem == null || currentEnchants.size() == 0){
    //         return false;
    //     }
        
    //     var playerInv = player.getInventory();
    //     if (!playerInv.contains(selectedItem)){
    //         return false;
    //     }

    //     var slot = playerInv.first(selectedItem);

    //     for (var ench : enchantSet){
    //         selectedItem.removeEnchantment(ench);
    //         currentEnchants.remove(ench);
    //     }

    //     evalEnchantGroupList.removeIf((e) -> e == enchantSet);

    //     playerInv.setItem(slot, selectedItem);
        
    //     return true;
    // }
}
