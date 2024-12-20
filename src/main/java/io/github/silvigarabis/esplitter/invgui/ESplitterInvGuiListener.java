package io.github.silvigarabis.esplitter.invgui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;

import io.github.silvigarabis.esplitter.ESplitterPlugin;

import java.util.Map;
import java.util.WeakHashMap;

public final class ESplitterInvGuiListener implements Listener {
    
    static final Map<InventoryView, ESplitterInvGui> guiViews = new WeakHashMap();
    
    /* pass click event to gui */
    @EventHandler(ignoreCancelled=true)
    public void inventoryClick(InventoryClickEvent event){
        var inventoryView = event.getView();
        var gui = guiViews.get(inventoryView);
        if (gui == null){
            return;
        }
        try {
            gui.onInvClick(event);
        } catch (Exception ex){
            closeAll();
            throw ex;
        }
    }

    /* pass close event to gui */
    @EventHandler(ignoreCancelled=true)
    public void inventoryClose(InventoryCloseEvent event){
        var inventoryView = event.getView();
        var gui = guiViews.get(inventoryView);
        if (gui == null)
            return;
        
        guiViews.remove(inventoryView);
        try {
            gui.onInvClose(event);
        } catch (Exception ex){
            closeAll();
            throw ex;
        }
    }

    /* pass drag event to gui */
    @EventHandler(ignoreCancelled=true)
    public void inventoryDrap(InventoryDragEvent event){
        var inventoryView = event.getView();
        var gui = guiViews.get(inventoryView);
        if (gui == null)
            return;

        try {
            gui.onInvDrag(event);
        } catch (Exception ex){
            closeAll();
            throw ex;
        }
    }
    
    private static void closeAll(){
        ESplitterPlugin.getPluginInstance().getLogger().severe("处理事件时出现未知错误，强行关闭所有窗口");

        for (Map.Entry<InventoryView, ESplitterInvGui> entry : guiViews.entrySet()){
            try {
                // entry.getValue().closeGui();
                entry.getKey().getPlayer().closeInventory();
                entry.getKey().getPlayer().sendMessage("[ESplitter] 出现未知错误");
            } catch (Throwable ignored){
                
            }
        }

        guiViews.clear();

        ESplitterPlugin.getPluginInstance().getLogger().severe("所有窗口已被关闭");
    }
}
