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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Map;
import java.util.HashMap;

public final class EventListener implements Listener {
    
    protected static Map<InventoryView, ESplitterGui> guiViews = new HashMap();
    
    @EventHandler(ignoreCancelled=true)
    public void inventoryClick(InventoryClickEvent event){
        var action = event.getAction();
        var ctype = event.getClick();
        var isTopInv = event.getClickedInventory() == event.getView().getTopInventory();
        var slot = event.getSlot();
        
        Logger.debug("inventoryClick: "
            + "isTopInv: "+isTopInv +", "
            + "ctype: "+ctype.toString() +", "
            + "action: "+action.toString() +", "
            + "slot: "+slot +", "
        );
        
        var inventoryView = event.getView();
        
        if (!guiViews.containsKey(inventoryView)){
            return;
        }
        
        var gui = guiViews.get(inventoryView);
        try {
            gui.onInvClick(event);
        } catch (Exception ex){
            closeAll();
            throw ex;
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void inventoryClose(InventoryCloseEvent event){
    
        Logger.debug("inventoryClose: "
        );

        var inventoryView = event.getView();
        
        if (!guiViews.containsKey(inventoryView)){
            return;
        }

        var gui = guiViews.get(inventoryView);
        guiViews.remove(inventoryView);
        try {
            gui.onInvClose(event);
        } catch (Exception ex){
            closeAll();
            throw ex;
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void inventoryDrap(InventoryDragEvent event){
        
        Logger.debug("inventoryDrap: "
        );
        
        var inventoryView = event.getView();
        
        if (!guiViews.containsKey(inventoryView)){
            return;
        }

        var gui = guiViews.get(inventoryView);
        try {
            gui.onInvDrag(event);
        } catch (Exception ex){
            closeAll();
            throw ex;
        }
    }
    
    private static void closeAll(){
        ESplitterPlugin.getPlugin().getLogger().severe("处理事件时出现未知错误，强行关闭所有窗口");

        for (Map.Entry<InventoryView, ESplitterGui> entry : guiViews.entrySet()){
            try {
                entry.getValue().closeGui();
                entry.getKey().getPlayer().closeInventory();
                entry.getKey().getPlayer().sendMessage("[ESplitter] 出现未知错误");
            } catch (Throwable ignored){
                
            }
        }

        guiViews.clear();

        ESplitterPlugin.getPlugin().getLogger().severe("所有窗口已被关闭");
    }
}
