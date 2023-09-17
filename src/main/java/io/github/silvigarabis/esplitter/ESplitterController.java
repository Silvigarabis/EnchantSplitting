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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import static org.bukkit.Material.ENCHANTED_BOOK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.WeakHashMap;

public final class ESplitterController {
    
    //private WeakHashMap<Player, ESplitterController> controllers = new WeakMap();
    
    protected Player player;
    
    private List<String> notifications = new ArrayList();
    
    protected ItemStack selectedItem;
    
    private Map<Enchantment, Integer> enchantments;
    
    protected ESplitterGui gui;
    
    protected ESplitterController(Player player){
        this.gui = new ESplitterGui(this);
        this.player = player;
        
        this.selectItem(player.getInventory().getItemInMainHand());
        
        showGui();
    }
    
    public void showGui(){
        this.gui.show(this.player);
    }
    
    public void selectItem(ItemStack item){
        this.selectedItem = item;
        this.gui.setSelectedItem(item);
        
        if (item == null){
            return;
        }
        
        //附魔书暂时不支持
        if (item.getType().equals(ENCHANTED_BOOK)){
            this.enchantments = new HashMap();
        } else
            //之前没想到这里返回的是 com.google.common.collect.ImmutableMap
            //用一个HashMap改一下
            this.enchantments = new HashMap(item.getEnchantments());
        }
        
        this.gui.setEnchantmentElements(this.enchantments);
    }
    
    public boolean splitEnchantment(Enchantment ench){
        return removeEnchantment(ench);
    }
    
    public boolean grindEnchantment(Enchantment ench){
        return removeEnchantment(ench);
    }
    
    public boolean removeEnchantment(Enchantment ench){
        if (selectedItem == null || enchantments.size() == 0){
            return false;
        }
        
        var playerInv = player.getInventory();
        if (!playerInv.contains(selectedItem)){
            return false;
        }
        
        var slot = playerInv.first(selectedItem);
        
        selectedItem.removeEnchantment(ench);
        enchantments.remove(ench);
        
        playerInv.setItem(slot, selectedItem);
        
        gui.setSelectedItem(selectedItem);
        
        return true;
    }
    
    protected void selectItemAsync(ItemStack item){
        Utils.runTask(() -> {
            selectItem(item);
            Logger.debug("异步更改了玩家选择的物品");
        });
    }
}
