package me.relow.relow;

import me.relow.relow.RelowGui;
import me.relow.relow.RelowController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class InventoryGuiListener extends RelowController implements Listener {
    
    boolean disabled = false;
    
    public void setDisabled(boolean toDisable){
        this.disabled = toDisable;
    }
    
    public void setDisabled(){
        this.setDisabled(true);
    }
    
    @EventHander
    public void inventoryClickCondition(InventoryClickEvent event){
        if (disabled) return;
        if (RelowController.isPlayerOpenedRelowGui((Player) event.getView().getPlayer()))
            super.inventoryClick(event);
    }
    /*
    @EventHander
    public void inventoryOpenCondition(InventoryOpenEvent event){
        if (RelowController.isRelowGuiView(event.getView()))
            super.inventoryOpen(event);
    }
    */
    @EventHander
    public void inventoryCloseCondition(InventoryCloseEvent event){
        if (disabled) return;
        if (RelowController.isPlayerOpenedRelowGui((Player) event.getView().getPlayer()))
            super.inventoryClose(event);
    }

    @EventHander
    public void inventoryDrapCondition(InventoryDragEvent event){
        if (disabled) return;
        if (RelowController.isPlayerOpenedRelowGui((Player) event.getView().getPlayer()))
            event.setCancelled(true);
    }
    /*
    @EventHandler
    public void inventoryOpened(InventoryClickEvent event){

        RELOW re = RELOW.getPlugin(RELOW.class);
        if (event.getView().getTitle().equalsIgnoreCase("附魔分离器")){
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
    */
}