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

import io.github.silvigarabis.esplitter.data.ESplitterEvaluatedEnchantSet;

import static org.bukkit.Material.BARRIER;
import static org.bukkit.Material.FILLED_MAP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 使用一个大箱子的GUI作为程序UI
 */
public class ESplitterInvGui {

    // @formatter:off
    ///////////////////////////
    //构建gui使用的槽位索引
    //////////////////////////
    //边框物品位置
    public static final int[] slotsBorder1 = {
        0,                              8,
        9,  10, 11, 12, 13, 14, 15, 16, 17,
        18,                             26,
        27,                             35,
        36,                             44,
        45,                             53,
    };

    //中心物品背景
    public static final int[] slotsBorder2 = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
            46, 47, 48, 49, 50, 51, 52,
    };

    //分隔线

    public static final int[] slotsBorder3 = {
            1,  2,  3,  4,  5,  6,  7,
                47,     49,     51,
    };

    // 选项位置
    public static final int[] slotsElements = {
                20,     22,     24,
                29,     31,     33,
                38,     40,     42,
    };
    // @formatter:on

    //显示物品的位置
    public static final int slotItemSelected = 1;

    public static final int slotItemResult = 48;

    public static final int slotItemButtonCancel = 50;

    //如果有需要提醒的时候，显示提醒物品的位置
    public static final int slotItemStatusMode = 7;

    //显示 page down
    public static final int slotItemButtonPgDown = 53;

    //显示 page up
    public static final int slotItemButtonPgUp = 45;

    @SuppressWarnings("unchecked")
    private static final BiFunction<ESplitterInvGui, Integer, ItemStack>[] slotItemUpdaters = new BiFunction[54];
    @SuppressWarnings("unchecked")
    private static final Function<Player, ItemStack>[] slotItemCreators = new Function[54];
    private static final ItemStack[] slotItemsStatic = new ItemStack[54];

    private static final void createLocalizedButton(int slot, Messages msg, Material material) {
        final ItemStack item0 = new ItemStack(material);
        var meta0 = item0.getItemMeta();
        meta0.setDisplayName(msg.getMessage());
        item0.setItemMeta(meta0);

        slotItemsStatic[slot] = item0.clone();

        slotItemCreators[slot] = (player) -> {
            var metaDyn = meta0.clone();
            var itemDyn = item0.clone();
            metaDyn.setDisplayName(msg.getMessagePlayer(player));
            itemDyn.setItemMeta(metaDyn);
            return itemDyn;
        };
    }

    static {
        /////////////////////////
        //  构建gui使用的基础物品
        ////////////////////////

        {
            // 包围框物品
            final ItemStack itemBorder1 = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            final ItemStack itemBorder2 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            final ItemStack itemBorder3 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            var meta0 = itemBorder1.getItemMeta();
            meta0.setDisplayName("");
            itemBorder1.setItemMeta(meta0);
            itemBorder2.setItemMeta(meta0);
            itemBorder3.setItemMeta(meta0);

            for (int slot : slotsBorder1) {
                slotItemsStatic[slot] = itemBorder1;
            }
            // for (int slot : slotsBorder2) {
            //     slotItemsStatic[slot] = itemBorder2;
            // }
            for (int slot : slotsBorder3) {
                slotItemsStatic[slot] = itemBorder3;
            }
        }

        // 按钮
        createLocalizedButton(slotItemButtonCancel, Messages.invGuiButtonCancel, Material.BARRIER);
        createLocalizedButton(slotItemButtonPgDown, Messages.invGuiButtonPgDown, Material.STONE);
        createLocalizedButton(slotItemButtonPgUp, Messages.invGuiButtonPgUp, Material.STONE);
    }

    public static enum OperationMode {
        SPLIT,
        GRIND
    }

    private ItemStack[] slotItems = Arrays.stream(slotItemsStatic).map(item -> item.clone()).toArray(ItemStack[]::new);
    private Player player;
    private ESplitterController controller;
    private OperationMode operationMode = OperationMode.SPLIT;
    private List<List<ESplitterEvaluatedEnchantSet>> pages = new ArrayList<>();

    private void callSlotCreator(int slot) {
        var creator = slotItemCreators[slot];
        if (creator != null) {
            slotItems[slot] = creator.apply(player);
        }
    }

    private void callSlotCreators() {
        for (int i = 0; i < slotItemCreators.length; i++) {
            callSlotCreator(i);
        }
    }

    private void callSlotUpdater(int slot) {
        var updater = slotItemUpdaters[slot];
        var oldItem = slotItems[slot];
        if (updater == null) {
            return;
        }
        var newItem = updater.apply(this, slot);
        if (newItem != oldItem) {
            slotItems[slot] = newItem;
        }
    }

    private void callSlotUpdaters() {
        for (int i = 0; i < slotItemUpdaters.length; i++) {
            callSlotUpdater(i);
        }
    }

    public ESplitterInvGui(ESplitterController controller) {
        this.controller = controller;
        this.player = controller.getPlayer();
        callSlotCreators();
        callSlotUpdaters();
        updatePages();

        switchOperationMode(operationMode);
    }

    public void clearElements() {
        for (int idx : slotsElements) {
            if (slotItems.containsKey(idx))
                slotItems.remove(idx);
        }

        update();
    }

    public void setPage(int pageIndex) {
        clearElements();

        if (pages.size() == 0) {
            return;
        }
        var pageElements = this.pages.get(pageIndex);
        this.curPageIndex = pageIndex;

        Iterator<EnchantmentSet> iterator = pageElements.listIterator();
        for (int elemIndex = 0; iterator.hasNext(); elemIndex++) {
            var enchantSet = iterator.next();

            if (enchantSet == null) {
                continue;
            }

            var item = new ItemStack(Material.ENCHANTED_BOOK);
            for (var ench : enchantSet) {
                item.addUnsafeEnchantment(ench, this.enchantments.get(ench));
            }

            int invIndex = slotsElements[elemIndex];

            slotItems.put(invIndex, item);
        }

        this.curPageIndex = pageIndex;
        update();
    }

    public void pageDown() {
        int nextPageIndex = this.curPageIndex + 1;
        if (nextPageIndex == this.pages.size())
            nextPageIndex = 0;

        this.setPage(nextPageIndex);
    }

    public void pageUp() {
        int nextPageIndex = this.curPageIndex - 1;
        if (nextPageIndex == -1)
            nextPageIndex = this.pages.size() - 1;

        this.setPage(nextPageIndex);
    }

    private Inventory inventory = null;
    private InventoryView inventoryView = null;

    public boolean update() {
        if (this.inventory == null) {
            return false;
        }

        this.inventory.clear();
        for (int idx : this.slotItems.keySet()) {
            this.inventory.setItem(idx, this.slotItems.get(idx));
        }

        return true;

    }

    public void show(Player player) {

        inventory = Bukkit.createInventory(player, 54, "Es");
        inventoryView = player.openInventory(inventory);

        ESplitterListener.guiViews.put(inventoryView, this);

        this.update();

        Logger.debug("open view for player " + player.getName());
    }

    public void closeGui() {
        if (this.inventoryView != null) {
            this.controller.player.closeInventory();
        }

        this.inventory = null;
        this.inventoryView = null;
    }

    public void setSelectedItem(ItemStack item) {
        this.slotItems.put(slotItemSelected, item != null ? item.clone() : null);
        this.update();

    }

    private Map<Enchantment, Integer> enchantments;

    private int curPageIndex = 0;

    private List<List<EnchantmentSet>> pages = new ArrayList();

    public void setEnchantmentElements(List<EnchantmentSet> enchantSetList, Map<Enchantment, Integer> enchantments) {

        //复制一份，不然被改了就麻烦了
        enchantSetList = new ArrayList<>(enchantSetList);

        //get elements
        var enchList = new ArrayList(enchantments.keySet());

        // count page
        int totalPageCount = enchantSetList.size() / slotsElements.length + 1;

        // build pages
        this.curPageIndex = 0;
        this.pages.clear();
        for (int curPage = 0; curPage < totalPageCount; curPage++) {

            var startIndex = curPage * slotsElements.length;
            var endIndex = Math.min(startIndex + slotsElements.length, enchantSetList.size());

            List<EnchantmentSet> pageElements = enchantSetList.subList(startIndex, endIndex);
            this.pages.add(pageElements);
        }

        //save enchant infos
        this.enchantments = enchantments;

        //update page view
        this.setPage(0);

    }

    public static boolean isInteractButtonAction(InventoryAction action) {
        return action == InventoryAction.PICKUP_ALL
                || action == InventoryAction.PICKUP_HALF
                || action == InventoryAction.PICKUP_ONE
                || action == InventoryAction.PICKUP_SOME;
    }

    public static boolean isExtractAction(InventoryAction action) {
        return action == InventoryAction.PICKUP_ALL
                || action == InventoryAction.PICKUP_HALF
                || action == InventoryAction.PICKUP_ONE
                || action == InventoryAction.PICKUP_SOME
                || action == InventoryAction.DROP_ONE_SLOT
                || action == InventoryAction.DROP_ALL_SLOT
                || action == InventoryAction.MOVE_TO_OTHER_INVENTORY;
    }

    public static boolean isPlaceInAction(InventoryAction action) {
        return action == InventoryAction.PLACE_ALL
                || action == InventoryAction.PLACE_ONE
                || action == InventoryAction.PLACE_SOME;
    }

    public static int getEnchantmentElementIndex(int slotIndex) {
        for (int i = 0; i < slotsElements.length; i++) {
            if (slotIndex == slotsElements[i]) {
                return i;
            }
        }
        return -1;
    }

    public static enum OperationMode {
        SPLIT, GRIND
    }

    private OperationMode operationMode = OperationMode.SPLIT;

    private void switchOperationMode() {
        if (operationMode == OperationMode.SPLIT) {
            operationMode = OperationMode.GRIND;
        } else if (operationMode == OperationMode.GRIND) {
            operationMode = OperationMode.SPLIT;
        }

        switchOperationMode(operationMode);
    }

    private void switchOperationMode(OperationMode mode) {
        var item = noticeItem.clone();

        var meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        if (operationMode == OperationMode.SPLIT) {
            lore.add("当前模式: 分离模式");
            lore.add("点击附魔项目以分离");
        } else if (operationMode == OperationMode.GRIND) {
            lore.add("当前模式: 去魔模式");
            lore.add("点击附魔项目以移除");
        } else {
            //以目前的逻辑来说不会有，但是我加一个也没关系吧
            lore.add("错误: 未知的模式，你做了什么？");
        }

        meta.setLore(lore);

        item.setItemMeta(meta);

        slotItems.put(slotItemStatusMode, item);

        update();
    }

    /**
     * 玩家尝试取出附魔项目时调用此方法，根据模式的不同，有着不一样的逻辑。
     */
    private boolean touchElement(int elementIndex) {
        EnchantmentSet enchantSet = null;
        boolean canPickup = false;
        boolean removeElement = false;

        try {
            enchantSet = this.pages.get(this.curPageIndex).get(elementIndex);
        } catch (IndexOutOfBoundsException e) {
        }

        if (enchantSet != null && operationMode == OperationMode.SPLIT) {
            //有时候我会想，是特性重要还是代码可读性重要
            //比如这段地方我使用了一个“在此之外”的操作来完成了
            //玩家可以直接拿出来附魔书的特性

            boolean result = this.controller.splitEnchantSet(enchantSet);
            canPickup = result;
            removeElement = result;
        } else if (enchantSet != null && operationMode == OperationMode.GRIND) {
            if (this.controller.grindEnchantSet(enchantSet)) {
                removeElement = true;
            }

            //永远也不应该成功取出，因为这是去魔模式
            canPickup = false;
        }

        if (removeElement) {
            Utils.runTask(() -> {
                pages.get(curPageIndex).set(elementIndex, null);
                setPage(curPageIndex);
            });
        }

        return canPickup;
    }

    protected void onInvClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != this.inventory)
            return;

        boolean allowEventAction = false;
        boolean hasAction = false;

        var action = event.getAction();
        int slotIndex = event.getSlot();

        //一些普通的点按按钮
        if (isInteractButtonAction(action)) {
            switch (slotIndex) {
                case slotItemButtonPgUp:
                    this.pageUp();
                    hasAction = true;
                    break;
                case slotItemButtonPgDown:
                    this.pageDown();
                    hasAction = true;
                    break;
                case slotItemStatusMode:
                    this.switchOperationMode();
                    hasAction = true;
                    break;
                default:
                    break;
            }

        }

        //尝试取出某个项目
        if (!hasAction && isExtractAction(action)) {
            int clickedElementIndex = getEnchantmentElementIndex(slotIndex);

            //取出某个附魔书项目
            if (clickedElementIndex != -1) {
                if (this.touchElement(clickedElementIndex)) {
                    allowEventAction = true;
                }

                hasAction = true;

                //取出物品项目
            } else if (slotIndex == slotItemSelected) {

                //判断玩家的物品栏是否实际存在已选中的物品
                //如果不存在，那就不可以“取出”（直接移动到物品栏或拿取到鼠标（见isExtractAction()） 

                if (this.controller.player.getInventory().contains(this.controller.selectedItem)) {
                    allowEventAction = true;
                    this.controller.player.getInventory().removeItem(this.controller.selectedItem);
                    this.controller.selectItemAsync(null); //同步处理的话，可能会因为此事件而被覆盖
                }

                hasAction = true;
            }
        }

        //正在尝试放入物品
        if (!hasAction && isPlaceInAction(action) && slotIndex == slotItemSelected) {
            hasAction = true;

            //获取玩家想放进去的物品
            var newSelection = event.getCursor().clone();

            //把物品放回玩家的物品栏
            //这里的值是尝试将物品放入玩家的物品栏时无法直接放入的物品的数量
            //TODO: 修复这里可能导致刷物的问题，如果选择的物品的数量大于1的话

            int giveBackResult = controller.player.getInventory().addItem(newSelection.clone()).size();

            if (giveBackResult == 0 /* 所有物品正常放入物品栏，可以继续 */ ) {
                Logger.debug("放入 " + newSelection.getType().toString());
                allowEventAction = true;

                //选择物品需要更改物品栏的所有位置，为避免与事件出现冲突，故在此异步更改
                this.controller.selectItemAsync(newSelection);
            }
        }

        if (!allowEventAction) {
            Logger.debug("cancelled");
            event.setCancelled(true);
        }

    }

    protected void onInvClose(InventoryCloseEvent event) {
        this.closeGui();
        Logger.debug("close view for player " + event.getPlayer().getName());
    }

    protected void onInvDrag(InventoryDragEvent event) {
        //暂时来说不会处理拖动物品，而是直接取消
        if (event.getInventory() == this.inventory)
            event.setCancelled(true);
    }

}
