package me.relow.relow;

import me.relow.relow.RelowGui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class RelowController {

    public static void changeSelectItem(event, ItemStack newSelectItem){}
    
    protected static void inventoryClick(InventoryClickEvent enent){
        RelowGui relowGui = RelowGui.getRelowGuiOf((Player) event.getView().getPlayer());
        if (relowGui == null || relowGui.isGuiClosed()){
            event.setCancelled(true);
            return;
        }
        if (event.getClickedInventory() != relowGui.getInventory())
            return;
        
        relowGui.newOperation(event.getSlot());
        
    }
    protected static void inventoryClose(InventoryCloseEvent enent){
        RelowGui relowGui = RelowGui.getRelowGuiOf((Player) event.getView().getPlayer());
        if (relowGui != null && !relowGui.isGuiClosed())
            relowGui.closeGui();
    }
    
    public static boolean isPlayerOpenedRelowGui(Player player){
        RelowGui relowGui = RelowGui.getRelowGuiOf(player);
        if (relowGui == null || !relowGui.isOpened())
            return false;
        return true;
    }
    
}