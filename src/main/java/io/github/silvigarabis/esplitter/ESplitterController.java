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
    
    protected List<String> notifications = new ArrayList();
    
    protected ItemStack selectedItem;
    
    protected Map<Enchantment, Integer> enchantments;
    
    protected List<EnchantmentSet> enchantSetList;

    private ESplitterGui gui;
    
    public ESplitterController(Player player){
        if (!ESplitterPlugin.isConfigured()){
            player.sendMessage("插件配置错误！");
            return;
        }

        this.gui = new ESplitterGui(this);
        this.player = player;
        
        this.selectItem(player.getInventory().getItemInMainHand());
        
        showGui();
    }
    
    public void showGui(){
        this.gui.show(this.player);
    }
    
    public int getEnchantLevel(Enchantment ench){
        return this.enchantments.get(ench);
    }

    public static boolean isItemAcceptable(ItemStack item){
        //附魔书暂时不支持（软限制）
        return !item.getType().equals(ENCHANTED_BOOK);
    }

    public boolean isItemAcceptable(){
        return itemAccepted;
    }
    
    private boolean itemAccepted = false;

    public void selectItem(ItemStack item){
        this.selectedItem = item;
        this.gui.setSelectedItem(item);
        
        if (item != null && isItemAcceptable(item)){
            itemAccepted = true;
            //之前没想到这里返回的是 com.google.common.collect.ImmutableMap
            //用一个HashMap改一下
            this.enchantments = new HashMap(item.getEnchantments());
        } else {
            itemAccepted = false;
            this.enchantments = new HashMap();
        }

        this.divideEnchantmentSet();
        this.gui.setEnchantmentElements(this.enchantSetList, this.enchantments);
    }

    // 感觉写的不是很好，先就这样吧，以后看看怎么弄

    private void divideEnchantmentSet(){
        this.enchantSetList = new ArrayList<>();

        for (var ench : this.enchantments.keySet()){
            this.enchantSetList.add(new EnchantmentSet(ench));
        }
    }

    public boolean splitEnchantment(EnchantmentSet enchantSet){
        return removeEnchantment(enchantSet);
    }
    
    public boolean grindEnchantment(EnchantmentSet enchantSet){
        return removeEnchantment(enchantSet);
    }

    public boolean removeEnchantment(EnchantmentSet enchantSet){
        if (selectedItem == null || enchantments.size() == 0){
            return false;
        }
        
        var playerInv = player.getInventory();
        if (!playerInv.contains(selectedItem)){
            return false;
        }

        var slot = playerInv.first(selectedItem);

        for (var ench : enchantSet){
            selectedItem.removeEnchantment(ench);
            enchantments.remove(ench);
        }

        enchantSetList.removeIf((e) -> e == enchantSet);

        playerInv.setItem(slot, selectedItem);
        
        gui.setSelectedItem(selectedItem);
        
        return true;
    }
    
    public void selectItemAsync(ItemStack item){
        Utils.runTask(() -> {
            selectItem(item);
            Logger.debug("异步更改了玩家选择的物品");
        });
    }
    
    public static void closeAll(){
    }
}
