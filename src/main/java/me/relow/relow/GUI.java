package me.relow.relow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GUI {
    public static void GUI1(Player player, ItemStack itemStack){
        TheOriginalCheckUtil.theOriginalCheck();
        //新建GUI
        Inventory REgui = Bukkit.createInventory(player, 27, "附魔分离器");
        ItemStack border1 = new ItemStack(Material.STAINED_GLASS_PANE);//边界1，外围
        ItemStack border2 = new ItemStack(Material.BOOK);
        ItemStack border3 = new ItemStack(Material.SIGN);
        ItemStack border4 = new ItemStack(Material.BOOK);

        ItemMeta meta4 = border4.getItemMeta();
        meta4.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a请手持需要分离的物品打开GUI!"));
        border4.setItemMeta(meta4);
        ItemMeta meta3 = border3.getItemMeta();
        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&',"&f右侧分析的附魔中，左键点击&a分离附魔 &f| 右键点击&b洗去附魔"));
        meta3.setLore(lores);
        border3.setItemMeta(meta3);

        REgui.setItem(0,border1);
        REgui.setItem(2,border1);
        REgui.setItem(9,border1);
        REgui.setItem(11,border1);
        REgui.setItem(18,border1);
        REgui.setItem(19,border1);
        REgui.setItem(20,border1);

        REgui.setItem(1,border3);

        for(int i = 3;i < 9;i++){
            REgui.setItem(i,border2);
        }

        for(int i = 12;i < 18;i++){
            REgui.setItem(i,border2);
        }

        for(int i = 21;i < 27;i++){
            REgui.setItem(i,border2);
        }

        if(itemStack != null && !itemStack.getType().equals(Material.AIR) && !itemStack.getType().equals(Material.ENCHANTED_BOOK)){
            REgui.setItem(10,itemStack);
            Map enchatment_map = itemStack.getItemMeta().getEnchants();
            if (!enchatment_map.isEmpty()) {
                Set<Enchantment> enchantmentSet = enchatment_map.keySet();
                ItemStack enBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) enBook.getItemMeta();
                int i = 3;
                int cnt = 0;
                for (Enchantment key : enchantmentSet) {

                    if(!Config.getNoRemove().contains(key.getName())){

                        storageMeta.addStoredEnchant(key, (int) enchatment_map.get(key), true);
                        enBook.setItemMeta(storageMeta);

                        //判断权限
                        int limitNum = judgePermission(player);
                        //player.sendMessage("您所在的权限组允许分析的附魔最大数量为:" + limitNum + "");
                        if(cnt >= limitNum){
                            break;
                        }
                        REgui.setItem(i, enBook);
                        cnt++;
                        i++;
                        if (i > 8 && i < 10) {
                            i += 3;
                        }
                        if (i > 17 && i < 20) {
                            i += 3;
                        }
                        if (i > 27){
                            player.sendMessage("再多的附魔请分离前面的一部分之后再来哦~");
                            break;
                        }
                        storageMeta.removeStoredEnchant(key);
                    }

                }
            }
        }else {
            REgui.setItem(10,border4);
        }



        player.openInventory(REgui);

    }

    public static int judgePermission(Player player){
        //返回其所在权限组能够看到的数量
        for(int i = 0; i < Config.getPermissionsList().size(); i++){
            if(player.hasPermission(Config.getPermissionsList().get(i).getName())){
                return Config.getPermissionsList().get(i).getNum();
            }
        }
        return Config.getDefaultNum();
    }
}
