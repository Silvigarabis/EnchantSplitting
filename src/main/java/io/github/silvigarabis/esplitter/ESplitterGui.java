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
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

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

/**
 * 使用一个大箱子的GUI作为程序UI
 */
public class ESplitterGui {
    
    /////////////////////////
    //  构建gui使用的基础物品
    ////////////////////////
    //外围物品
    public static final ItemStack borderItem = createTextItem(Material.BLACK_STAINED_GLASS_PANE, "");
    
    //包围物品
    public static final ItemStack centerItem = createTextItem(Material.GRAY_STAINED_GLASS_PANE, "");
    
    //分隔线
    public static final ItemStack lineItem = createTextItem(Material.WHITE_STAINED_GLASS_PANE, "");
    
    public static final ItemStack noticeItem = createTextItem(Material.OAK_SIGN, "");
    
    public static final ItemStack pageUpItem = createTextItem(Material.GLASS, "");
    public static final ItemStack pageDownItem = createTextItem(Material.STONE, "");

    public static final ItemStack itemAcceptableStatusItem = createTextItem(Material.GREEN_STAINED_GLASS_PANE, "");

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
           20,   22,   24,   
                             
           38,   40,   42
                            
    };
    /*
    public static final int[] elementIndexes = {
        19,20,21,22,23,24,25,
        28,29,30,31,32,33,34,
        37,38,39,40,41,42,43,
        46,47,48,49,50,51,52
    };
    */
    
    //分隔线
    public static final int[] upperIndexes = {
        1,  2,  3,  4,   5,  6,  7
    };
    
    //显示物品的位置
    public static final int selectedItemIndex = 1;
    
    public static final int itemAcceptableStatusIndex = 2;

    //如果有需要提醒的时候，显示提醒物品的位置
    public static final int notificationItemIndex = 7;

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
        itemStacks.put(itemAcceptableStatusIndex, itemAcceptableStatusItem);
        itemStacks.put(pageDownIndex, pageDownItem);
        itemStacks.put(pageUpIndex, pageUpItem);
        itemStacks.put(notificationItemIndex, noticeItem);
        
        updateOperationModeStatus();
        updateItemAcceptableStatus();
        updatePageStatus();
        update();
    }

    private void updateOperationModeStatus(){
        ItemStack noticeItem;
        if (operationMode == OperationMode.SPLIT){
            noticeItem = createTextItem(
                Material.ENCHANTING_TABLE,
                "当前模式: 分离模式",
                "点击附魔项目以分离"
            );
        } else if (operationMode == OperationMode.GRIND){
            noticeItem = createTextItem(
                Material.GRINDSTONE,
                "当前模式: 去魔模式",
                "点击附魔项目以移除"
            );
        } else {
            throw new RuntimeException("未知的模式");
        }

        itemStacks.put(notificationItemIndex, noticeItem);
    }
    private void updateItemAcceptableStatus(){
        ItemStack item;
        
        if (this.ctrl.selectedItem == null){
            item = createTextItem(
                Material.YELLOW_STAINED_GLASS_PANE,
                "在左侧放入物品以分析附魔"
            );
        } else if (this.ctrl.isItemAcceptable()){
            item = createTextItem(
                Material.GREEN_STAINED_GLASS_PANE,
                "已完成附魔分析",
                "检测到以下" + this.ctrl.enchantSetList.size() + "组附魔"
            );
        } else {
            item = createTextItem(
                Material.RED_STAINED_GLASS_PANE,
                "无法为此物品进行附魔分析"
            );
        }
        itemStacks.put(itemAcceptableStatusIndex, item);
    }
    private void updatePageStatus(){
        ItemStack pageUpItem;
        ItemStack pageDownItem;
        int lastPageNumberText = this.curPageIndex;
        int curPageNumberText = this.curPageIndex + 1;
        int nextPageNumberText = this.curPageIndex + 2;
        int startPageNumberText = 1;
        int endPageNumberText = this.pages.size();

        String curPageText = "当前处于第" + curPageNumberText + "页，共" + endPageNumberText + "页";
        String lastPageText = "上一页，第" + lastPageNumberText + "页";
        String nextPageText = "下一页，第" + nextPageNumberText + "页";
        String backToFirstPageText = "返回第" + startPageNumberText + "页";
        String backToLastPageText = "返回第" + endPageNumberText + "页";

        if (this.pages.size() <= 1){
            pageUpItem = createTextItem(
                ESplitterGui.pageUpItem,
                "无上一页",
                curPageText
            );
            pageDownItem = createTextItem(
                ESplitterGui.pageDownItem,
                "无下一页",
                curPageText
            );
        } else if (this.curPageIndex == 0){
            pageUpItem = createTextItem(
                ESplitterGui.pageUpItem,
                backToLastPageText,
                curPageText
            );
            pageDownItem = createTextItem(
                ESplitterGui.pageDownItem,
                nextPageText,
                curPageText
            );
        } else if (this.curPageIndex == this.pages.size() - 1){
            pageUpItem = createTextItem(
                ESplitterGui.pageUpItem,
                lastPageText,
                curPageText
            );
            pageDownItem = createTextItem(
                ESplitterGui.pageDownItem,
                backToFirstPageText,
                curPageText
            );
        } else {
            pageUpItem = createTextItem(
                ESplitterGui.pageUpItem,
                lastPageText,
                curPageText
            );
            pageDownItem = createTextItem(
                ESplitterGui.pageDownItem,
                nextPageText,
                curPageText
            );
        }
        itemStacks.put(pageDownIndex, pageDownItem);
        itemStacks.put(pageUpIndex, pageUpItem);
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
        
        Iterator<EnchantmentSet> iterator = pageElements.listIterator();
        for (int elemIndex = 0; iterator.hasNext(); elemIndex++){
            var enchantSet = iterator.next();
            
            if (enchantSet == null){
                continue;
            }

            var item = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) item.getItemMeta();
            for (var ench : enchantSet){
                itemMeta.addStoredEnchant(ench, this.enchantments.get(ench), true);
            }
            item.setItemMeta(itemMeta);

            int invIndex = elementIndexes[elemIndex];
            
            itemStacks.put(invIndex, item);
        }
        
        this.curPageIndex = pageIndex;
        buildMiscButton();
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
    public void closeGui(){
        if (this.inventoryView != null){
            this.ctrl.player.closeInventory();
        }
        
        this.inventory = null;
        this.inventoryView = null;
    }
    
    public void setSelectedItem(ItemStack item){
        this.itemStacks.put(selectedItemIndex, item != null ? item.clone() : null);
        buildMiscButton();
        this.update();
    }
    
    private Map<Enchantment, Integer> enchantments;
    
    private int curPageIndex = 0;
    
    private List<List<EnchantmentSet>> pages = new ArrayList();
    
    public void setEnchantmentElements(List<EnchantmentSet> enchantSetList, Map<Enchantment, Integer> enchantments){
        
        //复制一份，不然被改了就麻烦了
        enchantSetList = new ArrayList<>(enchantSetList);

        //get elements
        var enchList = new ArrayList(enchantments.keySet());

        // count page
        int totalPageCount = enchantSetList.size() / elementIndexes.length + 1;
        
        // build pages
        this.curPageIndex = 0;
        this.pages.clear();
        for (int curPage = 0; curPage < totalPageCount; curPage++){
            
            var startIndex = curPage * elementIndexes.length;
            var endIndex = Math.min(startIndex + elementIndexes.length, enchantSetList.size());
            
            List<EnchantmentSet> pageElements = enchantSetList.subList(startIndex, endIndex);
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
         // || action == InventoryAction.SWAP_WITH_CURSOR;
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
    
    public static enum OperationMode {
        SPLIT, GRIND
    }

    private OperationMode operationMode = OperationMode.SPLIT;

    private void switchOperationMode(){
        if (operationMode == OperationMode.SPLIT){
            switchOperationMode(OperationMode.GRIND);
        } else if (operationMode == OperationMode.GRIND){
            switchOperationMode(OperationMode.SPLIT);
        }
    }

    private void switchOperationMode(OperationMode mode){
        operationMode = mode;
        buildMiscButton();
    }

    /**
     * 玩家尝试取出附魔项目时调用此方法，根据模式的不同，有着不一样的逻辑。
     */
    private boolean touchElement(int elementIndex){
        EnchantmentSet enchantSet = null;
        boolean canPickup = false;
        boolean removeElement = false;

        try {
            enchantSet = this.pages.get(this.curPageIndex).get(elementIndex);
        } catch (IndexOutOfBoundsException e){
        }
        
        if (enchantSet != null && operationMode == OperationMode.SPLIT){
            //有时候我会想，是特性重要还是代码可读性重要
            //比如这段地方我使用了一个“在此之外”的操作来完成了
            //玩家可以直接拿出来附魔书的特性

            boolean result = this.ctrl.splitEnchantment(enchantSet);
            canPickup = result;
            removeElement = result;
        } else if (enchantSet != null && operationMode == OperationMode.GRIND){
            if (this.ctrl.grindEnchantment(enchantSet)){
                removeElement = true;
            }
            
            //永远也不应该成功取出，因为这是去魔模式
            canPickup = false;
        }

        if (removeElement){
            Utils.runTask(() -> {
                pages.get(curPageIndex).set(elementIndex, null);
                setPage(curPageIndex);
            });
        }

        return canPickup;
    }
    
    protected void onInvClick(InventoryClickEvent event){
        if (event.getClickedInventory() != this.inventory)
            return;

        boolean allowEventAction = false;
        boolean hasAction = false;
        
        var action = event.getAction();
        int slotIndex = event.getSlot();

        //一些普通的点按按钮
        if (isInteractButtonAction(action)){
            switch (slotIndex){
                case pageUpIndex:
                    this.pageUp();
                    hasAction = true;
                    break;
                case pageDownIndex:
                    this.pageDown();
                    hasAction = true;
                    break;
                case notificationItemIndex:
                    this.switchOperationMode();
                    hasAction = true;
                    break;
                default:
                    break;
            }

        }

        //尝试取出某个项目
        if (!hasAction && isExtractAction(action)){
            int clickedElementIndex = getEnchantmentElementIndex(slotIndex);
            
            //取出某个附魔书项目
            if (clickedElementIndex != -1){
                if (this.touchElement(clickedElementIndex)){
                    allowEventAction = true;
                }

                hasAction = true;

            //取出物品项目
            } else if (slotIndex == selectedItemIndex){
                
                //判断玩家的物品栏是否实际存在已选中的物品
                //如果不存在，那就不可以“取出”（直接移动到物品栏或拿取到鼠标（见isExtractAction()） 

                if (this.ctrl.player.getInventory().contains(this.ctrl.selectedItem)){
                    allowEventAction = true;
                    this.ctrl.player.getInventory().removeItem(this.ctrl.selectedItem);
                    this.ctrl.selectItemAsync(null); //同步处理的话，可能会因为此事件而被覆盖
                }

                hasAction = true;
            }
        }
        
        //正在尝试放入物品
        if (!hasAction && isPlaceInAction(action) && slotIndex == selectedItemIndex){
            hasAction = true;

            //获取玩家想放进去的物品
            var newSelection = event.getCursor().clone();
            
            //把物品放回玩家的物品栏
            //这里的值是尝试将物品放入玩家的物品栏时无法直接放入的物品的数量
            //TODO: 修复这里可能导致刷物的问题，如果选择的物品的数量大于1的话
            
            int giveBackResult = ctrl.player.getInventory().addItem(newSelection.clone()).size();
            
            if (giveBackResult == 0 /* 所有物品正常放入物品栏，可以继续 */ ){
                Logger.debug("放入 "+newSelection.getType().toString());
                allowEventAction = true;
                
                //选择物品需要更改物品栏的所有位置，为避免与事件出现冲突，故在此异步更改
                this.ctrl.selectItemAsync(newSelection);
            }
        }
        
        if (!allowEventAction){
            Logger.debug("cancelled");
            event.setCancelled(true);
        }

    }
    
    protected void onInvClose(InventoryCloseEvent event){
        this.closeGui();
        Logger.debug("close view for player "+event.getPlayer().getName());
    }

    protected void onInvDrag(InventoryDragEvent event){
        //暂时来说不会处理拖动物品，而是直接取消
        if (event.getInventory() == this.inventory)
            event.setCancelled(true);
    }

    public static ItemStack createTextItem(ItemStack item, String... texts){
        List<String> textList = new ArrayList<>();
        for (var text : texts){
            textList.add(text);
        }
        return createTextItem(item, textList);
    }
    public static ItemStack createTextItem(ItemStack item, List<String> textList){
        String titleText = null;
        if (textList.size() > 0){
            titleText = textList.get(0);
        }
        if (titleText != null){
            titleText = "§r§f" + titleText;
        }
        List<String> contentList = null;
        if (textList.size() > 1){
            contentList = new ArrayList<>();
            for (var text : textList.subList(1, textList.size())){
                contentList.add("§r§f" + text);
            }
        }
        item = item.clone();
        var meta = item.getItemMeta();
        meta.setDisplayName(titleText);
        meta.setLore(contentList);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack createTextItem(Material material, String... texts){
        var item = new ItemStack(material);
        return createTextItem(item, texts);
    }
    public static ItemStack createTextItem(Material material, List<String> textList){
        var item = new ItemStack(material);
        return createTextItem(item, textList);
    }
}
