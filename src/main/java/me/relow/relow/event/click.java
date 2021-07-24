package me.relow.relow.event;

import me.relow.relow.*;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class click implements Listener {

    private static String prefix;
    private static String close;
    private static String money1Not;
    private static String money1Yes;
    private static String level1Yes;
    private static String level1Not;
    private static String points1Yes;
    private static String points1Not;

    private static String money2Not;
    private static String money2Yes;
    private static String level2Yes;
    private static String level2Not;
    private static String points2Yes;
    private static String points2Not;

    private static String fenliFail;
    private static String washFail;
    private static String notAllow;

    public void messageWork(Player player){
        String temp;

        prefix = ChatColor.translateAlternateColorCodes('&', Config.getPrefix());

        close = ChatColor.translateAlternateColorCodes('&', Config.getClose());

        points1Yes = ChatColor.translateAlternateColorCodes('&',Config.getPoints1Yes());
        temp = points1Yes.replace("%points1%",String.valueOf(Config.getPoints_amount()));
        points1Yes = temp;

        points2Yes = ChatColor.translateAlternateColorCodes('&',Config.getPoints2Yes());
        temp = points2Yes.replace("%points2%",String.valueOf(Config.getPoints_amount2()));
        points2Yes = temp;

        points1Not = ChatColor.translateAlternateColorCodes('&',Config.getPoints1Not());
        temp = points1Not.replace("%points2%",String.valueOf(Config.getPoints_amount()));
        points1Not = temp;

        points2Not = ChatColor.translateAlternateColorCodes('&',Config.getPoints2Not());
        temp = points2Not.replace("%points2%",String.valueOf(Config.getPoints_amount2()));
        points2Not = temp;

        money1Not = ChatColor.translateAlternateColorCodes('&', Config.getMoney1Not());
        temp = money1Not.replace("%money1%",String.valueOf(Config.getMoney_amount()));
        money1Not = temp;

        money1Yes = ChatColor.translateAlternateColorCodes('&', Config.getMoney1Yes());
        temp = money1Yes.replace("%money1%",String.valueOf(Config.getMoney_amount()));
        money1Yes = temp;

        level1Yes = ChatColor.translateAlternateColorCodes('&', Config.getLevel1Yes());
        temp = level1Yes.replace("%level1%",String.valueOf(Config.getLevel_amount()));
        level1Yes = temp;

        level1Not = ChatColor.translateAlternateColorCodes('&', Config.getLevel1Not());
        temp = level1Not.replace("%level1%",String.valueOf(Config.getLevel_amount()));
        level1Not = temp;

        money2Not = ChatColor.translateAlternateColorCodes('&', Config.getMoney2Not());
        temp = money2Not.replace("%money2%",String.valueOf(Config.getMoney_amount2()));
        money2Not = temp;

        money2Yes = ChatColor.translateAlternateColorCodes('&', Config.getMoney2Yes());
        temp = money2Yes.replace("%money2%",String.valueOf(Config.getMoney_amount2()));
        money2Yes = temp;

        level2Yes = ChatColor.translateAlternateColorCodes('&', Config.getLevel2Yes());
        temp = level2Yes.replace("%level2%",String.valueOf(Config.getLevel_amount2()));
        level2Yes = temp;

        level2Not = ChatColor.translateAlternateColorCodes('&', Config.getLevel2Not());
        temp = level2Not.replace("%level2%",String.valueOf(Config.getLevel_amount2()));
        level2Not = temp;

        fenliFail = ChatColor.translateAlternateColorCodes('&', Config.getFenliFail());
        washFail = ChatColor.translateAlternateColorCodes('&', Config.getWashFail());
        notAllow = ChatColor.translateAlternateColorCodes('&', Config.getNotAllow());
    }

    @EventHandler
    public void openGUI(InventoryClickEvent event){

        RELOW re = RELOW.getPlugin(RELOW.class);
        if (event.getView().getTitle().equalsIgnoreCase("附魔分离器")){
            TheOriginalCheckUtil.theOriginalCheck();
            Player player = (Player) event.getWhoClicked();
            messageWork(player);
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if(clicked != null && !clicked.getType().equals(Material.AIR)){
                if(clicked.getType().equals(Material.ENCHANTED_BOOK)){
                    if(judgeFull(player)){
                        player.sendMessage(prefix + "背包已满!请清理后再来!");
                        return;
                    }
                    if(event.getClick().equals(ClickType.LEFT)){
                        //分离附魔
                        //首先判断金币 等级
                        //再判断概率

                        if(Config.isPoints()){
                            //判断点券
                            PlayerPointsAPI ppAPI;
                            PlayerPoints points = (PlayerPoints) getServer().getPluginManager().getPlugin("PlayerPoints");
                            ppAPI = new PlayerPointsAPI(points);

                            permissions playerPer = judgePer(player);
                            if(playerPer == null){
                                if(ppAPI.look(player.getUniqueId()) < Config.getPoints_amount()){
                                    player.sendMessage(prefix + points1Not);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }else {
                                    player.sendMessage(prefix + points1Yes);
                                    ppAPI.take(player.getUniqueId(),Config.getPoints_amount());
                                }
                            }else {
                                if(ppAPI.look(player.getUniqueId()) < playerPer.getPoints1()){
                                    String temp = Config.getPoints1Not().replace("%points1%",String.valueOf(playerPer.getPoints1()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }else {
                                    String temp = Config.getPoints1Yes().replace("%points1%",String.valueOf(playerPer.getPoints1()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    ppAPI.take(player.getUniqueId(),playerPer.getPoints1());
                                }
                            }
                        }

                        if (Config.isLevel()) {//判断等级
                            //先判断玩家权限组
                            permissions playerPer = judgePer(player);
                            if(playerPer == null){
                                if (Judge.JudgeLevel(player, Config.getLevel_amount())) {
                                    player.sendMessage(prefix + level1Yes);
                                    player.setLevel(player.getLevel() - Config.getLevel_amount());
                                } else {
                                    player.sendMessage(prefix + level1Not);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }
                            }else {
                                if (Judge.JudgeLevel(player, playerPer.getLevel1())) {
                                    String temp = Config.getLevel1Yes().replace("%level1%",String.valueOf(playerPer.getLevel1()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.setLevel(player.getLevel() - playerPer.getLevel1());
                                } else {
                                    String temp = Config.getLevel1Not().replace("%level1%",String.valueOf(playerPer.getLevel1()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }
                            }
                        }

                        if (Config.isMoney()) {//判断金币
                            permissions playerPer = judgePer(player);
                            if(playerPer == null){
                                if (Judge.JudgeMoney(player, Config.getMoney_amount())){
                                    player.sendMessage(prefix + money1Yes);
                                }else {
                                    player.sendMessage(prefix + money1Not);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }
                            }else {
                                if (Judge.JudgeMoney(player, playerPer.getMoney1())){
                                    String temp = Config.getMoney1Yes().replace("%money1%",String.valueOf(playerPer.getMoney1()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);

                                }else {
                                    String temp = Config.getMoney1Not().replace("%money1%",String.valueOf(playerPer.getMoney1()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }
                            }
                        }

                        Random random = new Random();
                        int chance = random.nextInt(100);
                        permissions playerPer = judgePer(player);
                        if(playerPer == null){
                            if (chance > Config.getR_success_rate()) {
                                player.sendMessage(prefix + fenliFail);
                                player.closeInventory();
                                player.sendMessage(prefix + close);
                                return;
                            }
                        }else {
                            if (chance > playerPer.getRsuccess()) {
                                player.sendMessage(prefix + fenliFail);
                                player.closeInventory();
                                player.sendMessage(prefix + close);
                                return;
                            }
                        }


                        ItemStack itemStack = event.getClickedInventory().getItem(10);
                        func(itemStack,clicked,player);
                        player.getInventory().addItem(clicked);
                        //player.sendMessage(player.getItemInHand().toString());
                        player.getItemInHand().setAmount(0);
                        player.closeInventory();
                        player.sendMessage(prefix + close);
                    }else if (event.getClick().equals(ClickType.RIGHT)){
                        //洗去附魔
                        //首先判断金币 等级
                        //再判断概率

                        if(Config.isPoints2()){
                            //判断点券
                            PlayerPointsAPI ppAPI;
                            PlayerPoints points = (PlayerPoints) getServer().getPluginManager().getPlugin("PlayerPoints");
                            ppAPI = new PlayerPointsAPI(points);

                            permissions playerPer = judgePer(player);
                            if(playerPer == null){
                                if(ppAPI.look(player.getUniqueId()) < Config.getPoints_amount2()){
                                    player.sendMessage(prefix + points2Not);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }else {
                                    player.sendMessage(prefix + points2Yes);
                                    ppAPI.take(player.getUniqueId(),Config.getPoints_amount2());
                                }
                            }else {
                                if(ppAPI.look(player.getUniqueId()) < playerPer.getPoints2()){
                                    String temp = Config.getPoints2Not().replace("%points2%",String.valueOf(playerPer.getPoints2()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }else {
                                    String temp = Config.getPoints2Yes().replace("%points2%",String.valueOf(playerPer.getPoints2()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    ppAPI.take(player.getUniqueId(),playerPer.getPoints2());
                                }
                            }
                        }

                        if (Config.isLevel2()) {//判断等级
                            permissions playerPer = judgePer(player);
                            if(playerPer == null){
                                if (Judge.JudgeLevel(player, Config.getLevel_amount())) {
                                    player.sendMessage(prefix + level2Yes);
                                    player.setLevel(player.getLevel() - Config.getLevel_amount2());
                                } else {
                                    player.sendMessage(prefix + level2Not);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }
                            }else {
                                if (Judge.JudgeLevel(player, playerPer.getLevel2())) {
                                    String temp = Config.getLevel2Yes().replace("%level2%",String.valueOf(playerPer.getLevel2()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.setLevel(player.getLevel() - playerPer.getLevel2());
                                } else {
                                    String temp = Config.getLevel2Not().replace("%level2%",String.valueOf(playerPer.getLevel2()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }
                            }

                        }

                        if (Config.isMoney2()) {//判断金币
                            permissions playerPer = judgePer(player);
                            if(playerPer == null){
                                if (!Judge.JudgeMoney(player, Config.getMoney_amount2())) {
                                    player.sendMessage(prefix + money2Not);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }else {
                                    player.sendMessage(prefix + money2Yes);
                                }
                            }else {
                                if (!Judge.JudgeMoney(player, playerPer.getMoney2())) {
                                    String temp = Config.getMoney2Not().replace("%money2%",String.valueOf(playerPer.getMoney2()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                    player.closeInventory();
                                    player.sendMessage(prefix + close);
                                    return;
                                }else {
                                    String temp = Config.getMoney2Yes().replace("%money2%",String.valueOf(playerPer.getMoney2()));
                                    String temp2 = ChatColor.translateAlternateColorCodes('&',temp);
                                    player.sendMessage(prefix + temp2);
                                }
                            }

                        }

                        Random random = new Random();
                        permissions playerPer = judgePer(player);
                        int chance = random.nextInt(100);
                        if(playerPer == null){
                            if (chance > Config.getR_success_rate()) {
                                player.sendMessage(prefix + washFail);
                                player.closeInventory();
                                player.sendMessage(prefix + close);
                                return;
                            }
                        }else {
                            if (chance > playerPer.getRsuccess()) {
                                player.sendMessage(prefix + washFail);
                                player.closeInventory();
                                player.sendMessage(prefix + close);
                                return;
                            }
                        }

                        ItemStack itemStack = event.getClickedInventory().getItem(10);
                        func(itemStack,clicked,player);
                        //player.sendMessage(player.getItemInHand().toString());
                        player.getItemInHand().setAmount(0);
                        player.closeInventory();
                        player.sendMessage(prefix + close);
                    }
                }
            }
        }
    }

    public void func(ItemStack itemStack, ItemStack enchnbook, Player player) {
        if (itemStack != null && enchnbook != null) {
            EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) enchnbook.getItemMeta();
            Map enchatment_map = storageMeta.getStoredEnchants();
            Set<Enchantment> enchantmentSet = enchatment_map.keySet();
            ItemMeta itemMeta = itemStack.getItemMeta();

            for (Enchantment key : enchantmentSet) {
                if (itemMeta.removeEnchant(key)) {
                    itemStack.setItemMeta(itemMeta);
                    player.getInventory().addItem(itemStack);
                }
            }
        }
    }

    public boolean judgeFull(Player player){

        if(player.getInventory().firstEmpty() == -1){
            return true;//背包满了
        }
        return false;
    }

    public permissions judgePer(Player player){
        //返回其所在权限组能够看到的数量
        for(int i = 0; i < Config.getPermissionsList().size(); i++){
            if(player.hasPermission(Config.getPermissionsList().get(i).getName())){
                return Config.getPermissionsList().get(i);
            }
        }
        return null;
    }
}
