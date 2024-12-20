package io.github.silvigarabis.esplitter.invgui;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import io.github.silvigarabis.esplitter.ESplitterController;
import io.github.silvigarabis.esplitter.Messages;
import io.github.silvigarabis.esplitter.data.ESplitterEvaluatedEnchantSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 使用一个大箱子的GUI作为程序UI
 */
public class ESplitterInvGui {

    // formartter:off

    static final int[] slotsBorder1 = ESplitterInvGuiConstants.slotsBorder1;
    static final int[] slotsBorder2 = ESplitterInvGuiConstants.slotsBorder2;
    static final int[] slotsBorder3 = ESplitterInvGuiConstants.slotsBorder3;
    static final int[] slotsElements = ESplitterInvGuiConstants.slotsElements;

    //显示物品的位置
    static final int slotItemSelected = 1;          // L1,C2

    static final int slotItemResult = 48;           // L6,C4

    static final int slotItemButtonCancel = 51;     // L6,C6
    static final int slotItemButtonComplete = 49;   // L6,C5

    //如果有需要提醒的时候，显示提醒物品的位置
    static final int slotItemStatusMode = 7;        // L1,C8

    //显示 page down
    static final int slotItemButtonPgDown = 53;     // L6,C9

    //显示 page up
    static final int slotItemButtonPgUp = 45;       // L6,C1

    // @formatter:on

    @SuppressWarnings("unchecked")
    static final BiFunction<ESplitterInvGui, Integer, ItemStack>[] slotItemUpdaters = new BiFunction[54];
    @SuppressWarnings("unchecked")
    static final Function<Player, ItemStack>[] slotItemCreators = new Function[54];
    static final ItemStack[] slotItemsStatic = new ItemStack[54];

    static {
        /////////////////////////
        //  构建gui使用的基础物品
        ////////////////////////
        // 边框
        GuiItemBuilder.initForBorderItem();

        // 按钮
        GuiItemBuilder.createLocalizedButton(slotItemButtonCancel, Messages.invGuiButtonCancel, Material.BARRIER);
        GuiItemBuilder.createLocalizedButton(slotItemButtonPgDown, Messages.invGuiButtonPgDown, Material.STONE);
        GuiItemBuilder.createLocalizedButton(slotItemButtonPgUp, Messages.invGuiButtonPgUp, Material.STONE);
        GuiItemBuilder.createLocalizedButton(slotItemButtonComplete, Messages.invGuiButtonComplete, Material.EXPERIENCE_BOTTLE);

        // 元素
        GuiItemBuilder.ininForElementItem();

        // 状态
        slotItemUpdaters[slotItemStatusMode] = (gui, slot) -> {
            switch (gui.operationMode) {
                case SPLIT:
                    return GuiItemBuilder.createItemLocalized(
                            Material.WRITABLE_BOOK,
                            Messages.invGuiStatusModeSplit,
                            gui.player);
                case GRIND:
                    return GuiItemBuilder.createItemLocalized(
                            Material.GRINDSTONE,
                            Messages.invGuiStatusModeGrind,
                            gui.player);
                default:
                    return GuiItemBuilder.createItemLocalized(
                            Material.GRAY_DYE,
                            Messages.invGuiStatusModeUnknown,
                            gui.player);
            }

        };
    }

    private ItemStack[] slotItems = Arrays.stream(slotItemsStatic).map(item -> item.clone()).toArray(ItemStack[]::new);
    private Player player;
    public Player getPlayer() {
        return player;
    }

    ESplitterController controller;
    private OperationMode operationMode = OperationMode.SPLIT;
    List<ESplitterEvaluatedEnchantSet> cachedEvaluatedEnchantSetList = null;
    int currentShowingPageIndex = 0;
    List<List<ESplitterEvaluatedEnchantSet>> pagedElementList = new LinkedList<>();

    private void _callSlotCreator(int slot) {
        var creator = slotItemCreators[slot];
        if (creator != null) {
            slotItems[slot] = creator.apply(player);
        }
    }

    private void _callSlotCreators(int[] slots) {
        for (int i = 0; i < slots.length; i++) {
            _callSlotCreator(i);
        }
    }

    private void _callSlotCreators() {
        for (int i = 0; i < slotItemCreators.length; i++) {
            _callSlotCreator(i);
        }
    }

    private void _callSlotUpdater(int slot) {
        var updater = slotItemUpdaters[slot];
        if (updater == null) {
            return;
        }
        var oldItem = slotItems[slot];
        var newItem = updater.apply(this, slot);
        if (newItem != oldItem) {
            slotItems[slot] = newItem;
        }
    }

    private void _callSlotUpdaters(int[] slots) {
        for (int i = 0; i < slots.length; i++) {
            _callSlotUpdater(i);
        }
    }

    private void _callSlotUpdaters() {
        for (int i = 0; i < slotItemUpdaters.length; i++) {
            _callSlotUpdater(i);
        }
    }

    private InventoryView viewGui = null;
    private Inventory viewInventory = null;

    public ESplitterInvGui(ESplitterController controller) {
        this.controller = controller;
        this.player = controller.getPlayer();

        generatePages();

        _callSlotCreators();
        _callSlotUpdaters();
        _callSlotUpdaters();
    }

    private void generatePages() {
        cachedEvaluatedEnchantSetList = this.controller.getEvaluatedEnchantGroupList();
        List<List<ESplitterEvaluatedEnchantSet>> pagedList = new ArrayList<>();
        int totalSize = cachedEvaluatedEnchantSetList.size();
        for (int i = 0; i < totalSize; i += slotsElements.length) {
            // 直接将 subList 的结果添加到分页列表
            int end = Math.min(i + slotsElements.length, totalSize);
            pagedList.add(cachedEvaluatedEnchantSetList.subList(i, end));
        }
        pagedElementList = pagedList;
    }

    public void show() {
        viewInventory = Bukkit.createInventory(player, 54, Messages.invGuiTitle.getPlayerText(player));
        viewGui = player.openInventory(viewInventory);

        ESplitterInvGuiListener.guiViews.put(viewGui, this);
    }

    public void onInvClick(InventoryClickEvent event) {
        throw new NotImplementedException();
    }

    public void onInvClose(InventoryCloseEvent event) {
        throw new NotImplementedException();
    }

    public void onInvDrag(InventoryDragEvent event) {
        throw new NotImplementedException();
    }
}
