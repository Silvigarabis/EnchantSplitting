package io.github.silvigarabis.esplitter.invgui;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

    public static final int[] slotsBorder1 = ESplitterInvGuiConstants.slotsBorder1;
    public static final int[] slotsBorder2 = ESplitterInvGuiConstants.slotsBorder2;
    public static final int[] slotsBorder3 = ESplitterInvGuiConstants.slotsBorder3;
    public static final int[] slotsElements = ESplitterInvGuiConstants.slotsElements;

    //显示物品的位置
    public static final int slotItemSelected = 1;      // L1,C2

    public static final int slotItemResult = 48;       // L6,C4

    public static final int slotItemButtonCancel = 50; // L6,C6

    //如果有需要提醒的时候，显示提醒物品的位置
    public static final int slotItemStatusMode = 7;    // L1,C8

    //显示 page down
    public static final int slotItemButtonPgDown = 53; // L6,C9

    //显示 page up
    public static final int slotItemButtonPgUp = 45;   // L6,C1

    // @formatter:on

    @SuppressWarnings("unchecked")
    private static final BiFunction<ESplitterInvGui, Integer, ItemStack>[] slotItemUpdaters = new BiFunction[54];
    @SuppressWarnings("unchecked")
    static final Function<Player, ItemStack>[] slotItemCreators = new Function[54];
    static final ItemStack[] slotItemsStatic = new ItemStack[54];

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
        ESplitterInvGuiItemBuilder.createLocalizedButton(slotItemButtonCancel, Messages.invGuiButtonCancel, Material.BARRIER);
        ESplitterInvGuiItemBuilder.createLocalizedButton(slotItemButtonPgDown, Messages.invGuiButtonPgDown, Material.STONE);
        ESplitterInvGuiItemBuilder.createLocalizedButton(slotItemButtonPgUp, Messages.invGuiButtonPgUp, Material.STONE);

        // 元素
        for (int elementIndex = 0; elementIndex < slotsElements.length; elementIndex++) {
            int slot = slotsElements[elementIndex];
            final int elementIndexFinalized = elementIndex;
            slotItemUpdaters[slot] = (gui, slotThatUpdating) -> {
                return ESplitterInvGuiItemBuilder.updateSlotElement(gui, elementIndexFinalized);
            };
        }
    }

    private ItemStack[] slotItems = Arrays.stream(slotItemsStatic).map(item -> item.clone()).toArray(ItemStack[]::new);
    private Player player;
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
