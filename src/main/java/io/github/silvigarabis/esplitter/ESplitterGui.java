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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import org.bukkit.entity.Player;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.event.inventory.InventoryAction;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ESplitterGui {
    
    /////////////////////////
    //  构建gui使用的基础物品
    ////////////////////////
    //外围物品
    public static final ItemStack borderItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE); 
    
    //包围物品
    public static final ItemStack centerItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE); 
    
    //分隔线
    public static final ItemStack lineItem = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    
    public static final ItemStack noticeItem = new ItemStack(Material.OAK_SIGN);

    ///////////////////////////
    //构建gui使用的槽位索引
    //////////////////////////
    //边框物品位置
    public static final int[] borderIndexes = {
        0,                              8,
        9, 10, 11, 12, 13, 14, 15, 16, 17,
        18,                             26,
        27,                             35,
        36,                             44,
        45,                             53
    };
    
    //中心物品位置
    public static final int[] elementIndexes = {
        19,20,21,22,23,24,25,
        28,29,30,31,32,33,34,
        37,38,39,40,41,42,43,
        46,47,48,49,50,51,52
    }; 
    
    //分隔线
    public static final int[] upperIndexes = {
        1,  2,  3,  4,   5,  6,  7
    };
    
    //显示物品的位置
    public static final int selectedItemIndex = 1;
    
    //如果有需要提醒的时候，显示提醒物品的位置
    //public static final int notificationItemIndex = 7;
    
    //显示已经选择的附魔的位置
    //public static final int selectedEnchantmentIndex = 3;
    
    //显示 split 按钮的位置
    //public static final int splitButtonIndex = 4;
    
    //显示 grind 按钮的位置
    //public static final int grindButtonIndex = 5;

    //显示 cancel 按钮的位置
    //public static final int enterCancelIndex = 6;

    //显示 page down
    public static final int pageDownIndex = 53;

    //显示 page up
    public static final int pageUpIndex = 45;
    
    private Map<Integer, ItemStack> itemStacks = new HashMap();
    
    private ESplitterController ctrl;
    
    public ESplitterGui(ESplitterController ctrl){
        this.ctrl = ctrl;
        
        buildBorder();
        buildLine();
        buildMiscButton();
    }
    
    public void buildBorder(){
        for (int idx : borderIndexes)
            itemStacks.put(idx, borderItem);
        
        update();
    }
    
    public void buildLine(){
        for (int idx: upperIndexes)
            itemStacks.put(idx, lineItem);
        
        update();
    }
    
    public void buildMiscButton(){
        itemStacks.put(pageDownIndex, new ItemStack(Material.STONE));
        itemStacks.put(pageUpIndex, new ItemStack(Material.GLASS));
        
        update();
    }
    
    public void clearElements(){
        for (int idx : elementIndexes){
            if (itemStacks.containsKey(idx))
                itemStacks.remove(idx);
        }
        
        update();
    }
    
    public void setPage(int pageIndex){
        clearElements();
        
        if (pages.size() == 0)
            return;
        
        var pageElements = this.pages.get(pageIndex);
        this.curPageIndex = pageIndex;
        
        Iterator<Enchantment> iterator = pageElements.listIterator();
        for (int elemIndex = 0; iterator.hasNext(); elemIndex++){
            var ench = iterator.next();
            
            if (ench == null){
                continue;
            }
            
            var item = new ItemStack(Material.ENCHANTED_BOOK);
            item.addUnsafeEnchantment(ench, this.enchantments.get(ench));
            
            int invIndex = elementIndexes[elemIndex];
            
            itemStacks.put(invIndex, item);
        }
        
        this.curPageIndex = pageIndex;
        update();
    }
    
    public void pageDown(){
        int nextPageIndex = this.curPageIndex+1;
        if (nextPageIndex == this.pages.size())
            nextPageIndex = 0;
            
        this.setPage(nextPageIndex);
    }
    
    public void pageUp(){
        int nextPageIndex = this.curPageIndex-1;
        if (nextPageIndex == -1)
            nextPageIndex = this.pages.size()-1;
            
        this.setPage(nextPageIndex);
    }
    
    private Inventory inventory = null;
    private InventoryView inventoryView = null;
    public boolean update(){
        if (this.inventory == null){
            return false;
        }
        
        this.inventory.clear();
        for (int idx : this.itemStacks.keySet()){
            this.inventory.setItem(idx, this.itemStacks.get(idx));
        }
        
        return true;
        
    }
    public void show(Player player) {
        
        inventory = Bukkit.createInventory(player, 54, "Es");
        inventoryView = player.openInventory(inventory);
        
        EventListener.guiViews.put(inventoryView, this);
        
        this.update();
        
        Logger.debug("open view for player "+player.getName());
    }
    
    public void setSelectedItem(ItemStack item){
        this.itemStacks.put(selectedItemIndex, item != null ? item.clone() : null);
        this.update();
        
    }
    
    private Map<Enchantment, Integer> enchantments;
    
    private int curPageIndex = 0;
    
    private List<List<Enchantment>> pages = new ArrayList();
    
    public void setEnchantmentElements(Map<Enchantment, Integer> enchantments){
        if (enchantments == null || enchantments.size() == 0){
            this.pages.clear();
            this.setPage(0);
            return;
        }
        
        //get elements
        var enchList = new ArrayList(enchantments.keySet());
        int totalPageCount = enchList.size() / elementIndexes.length + 1;
        
        // build pages
        this.curPageIndex = 0;
        this.pages.clear();
        for (int curPage = 0; curPage < totalPageCount; curPage++){
            
            var startIndex = curPage * elementIndexes.length;
            var endIndex = Math.min(startIndex + elementIndexes.length, enchList.size());
            
            List<Enchantment> pageElements = enchList.subList(startIndex, endIndex);
            this.pages.add(pageElements);
        }
        
        //save enchant infos
        this.enchantments = enchantments;
        
        //update page view
        this.setPage(0);
        
    }
    
    public static boolean isInteractButtonAction(InventoryAction action){
        return action == InventoryAction.PICKUP_ALL
          || action == InventoryAction.PICKUP_HALF
          || action == InventoryAction.PICKUP_ONE
          || action == InventoryAction.PICKUP_SOME;
    }
    
    public static boolean isExtractAction(InventoryAction action){
        return action == InventoryAction.PICKUP_ALL
          || action == InventoryAction.PICKUP_HALF
          || action == InventoryAction.PICKUP_ONE
          || action == InventoryAction.PICKUP_SOME
          || action == InventoryAction.DROP_ONE_SLOT
          || action == InventoryAction.DROP_ALL_SLOT
          || action == InventoryAction.MOVE_TO_OTHER_INVENTORY;
    }
    
    public static boolean isPlaceInAction(InventoryAction action){
        return action == InventoryAction.PLACE_ALL
          || action == InventoryAction.PLACE_ONE
          || action == InventoryAction.PLACE_SOME;
    }
    
    public static int getEnchantmentElementIndex(int slotIndex){
        for (int i = 0; i < elementIndexes.length; i++){
            if (slotIndex == elementIndexes[i]){
                return i;
            }
        }
        return -1;
    }
    
    private boolean splitEnchantmentFromElement(int elementIndex){
        Enchantment ench = null;
        boolean isSuccess = false;
        try {
            ench = this.pages.get(this.curPageIndex).get(elementIndex);
        } catch (IndexOutOfBoundsException e){
        }
        
        if (ench != null && this.ctrl.splitEnchantment(ench)){
            isSuccess = true;
            
            // update page view async
            // prevent event data conflict
            Utils.runTask(() -> {
                pages.get(curPageIndex).set(elementIndex, null);
                setPage(curPageIndex);
            });
            
        } else {
            if (ench == null) Logger.debugWarning("空的附魔选择");
            
            Logger.debugWarning("附魔分离条件未满足");
        }
        
        return isSuccess;
    }
    
    protected void onInvClick(InventoryClickEvent event){
        var action = event.getAction();
        boolean allowAction = false;
        
        if (event.getClickedInventory() != this.inventory)
            return;
        
        int slotIndex = event.getSlot();
        
        // 不用else if 是因为操作可以使用的action有重复
        
        if (isInteractButtonAction(action)){
            switch (slotIndex){
                case pageUpIndex:
                    this.pageUp();
                    break;
                case pageDownIndex:
                    this.pageDown();
                    break;
                //case cancelButtonIndex:
            }
        }
        
        if (isExtractAction(action)){
            int clickedElementIndex = getEnchantmentElementIndex(slotIndex);
            
            //这里可能有未知的bug
            if (clickedElementIndex != -1){
                if (this.splitEnchantmentFromElement(clickedElementIndex)){
                    allowAction = true;
                }
            } else if (slotIndex == selectedItemIndex){
                if (this.ctrl.player.getInventory().contains(this.ctrl.selectedItem)){
                    allowAction = true;
                    this.ctrl.player.getInventory().removeItem(this.ctrl.selectedItem);
                    this.ctrl.selectItemAsync(null); //同步处理的话，可能会因为此事件而被覆盖
                }
            }
        }
        
        if (isPlaceInAction(action) && slotIndex == selectedItemIndex){
            
            /*
            这段代码可能有点难以理解
            
            首先，我们在事件中可以修改点击位置的物品
            其次，一些操作会“取出”点击位置的物品
            再然后，可能会放下鼠标光标上的物品
            当碰到这些操作的时候，并且点击在显示“选择物品”的槽位
            执行如下操作：
            记录鼠标光标上的物品
            尝试将物品返回玩家的物品栏
            成功的话，则允许此次事件
            并选择此物品
            */
            
            var newSelection = event.getCursor().clone();
            
            //没办法放回去就先拒绝放入，避免玩家物品丢失
            if (0 == ctrl.player.getInventory().addItem(newSelection.clone()).size()){
                Logger.debug("放入 "+newSelection.getType().toString());
                
                allowAction = true;
                
                //选择物品需要更改物品栏的所有位置，为避免与事件出现冲突，故在此异步更改
                this.ctrl.selectItemAsync(newSelection);
            }
            
        }
        
        if (!allowAction){
            Logger.debug("cancelled");
            event.setCancelled(true);
        }
    }

    protected void onInvClose(InventoryCloseEvent event){
        EventListener.guiViews.remove(this.inventoryView);
        this.inventory = null;
        this.inventoryView = null;
        Logger.debug("close view for player "+event.getPlayer().getName());
    }

    protected void onInvDrag(InventoryDragEvent event){
        if (event.getInventory() == this.inventory)
            event.setCancelled(true);
        
    }

}
