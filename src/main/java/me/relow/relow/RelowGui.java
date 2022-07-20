package me.relow.relow;

import me.relow.relow.utils.Messager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RelowGui {
    private static Map<Player, Inventory> relowGuiMap = new HashMap<>();;
    
    //边框物品位置
    private static final int[] borderIndexes = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
    
    //int[] centerIndexes = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43}; 
    
    //中心物品位置（特定顺序）
    private static final int[] centerIndexes = {31,30,32,29,33,28,34,40,39,41,38,42,37,43,22,21,23,20,24,19,25,13,12,14,11,15,10,16};
    
    //int[] upperIndexes = {19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43}; 
    
    //下部物品位置（特定顺序）
    private static final int[] upperIndexes = {31,30,32,29,33,28,34,40,39,41,38,42,37,43,22,21,23,20,24,19,25}; 
    
    //分隔线物品位置
    private static final int[] lineIndexes = {19,20,21,22,23,24};
    
    //显示物品的位置
    private static final int displayItemIndex = 13;
    
    //如果有需要提醒的时候，显示提醒物品的位置
    private static final int displayNoticeIndex = 16;
    
    //外围物品
    private static ItemStack borderItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE); 
    
    //包围物品
    private static ItemStack centerItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE); 
    
    //分隔线
    private static ItemStack lineItem = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    
    //箱子ui
    private Inventory gui;
    
    //正在查看的ui页面
    private InventoryView guiView;
    
    private Player guiOwner;
    
    private boolean guiOpened = false;
    private boolean guiClosed = false;
    
    private ItemStack noticeItem = new ItemStack(Material.SIGN);
    private ItemStack selectItem;
    
    public RelowGui(Player player){
    
        guiOwner = player;
        
        //新建GUI
        gui = Bukkit.createInventory(guiOwner, 54, Messager.getMesage("guiTitle"));
        
        selectItem = player.getItemInHand();
        
        if (selectItem == null || selectItem.getType().equals(Material.AIR) || selectItem.getType().equals(Material.ENCHANTED_BOOK)){
            selectItem = new ItemStack(Material.BOOK);
        }
        
        //------------
        //绘制基础图像
        //------------
        
        //将物品放到对应位置
        for (int i=0; i<borderIndexes.length ; i++){
            gui.setItem(borderIndexes[i],borderItem);
        }
        for (int i=0; i<centerIndexes.length ; i++){
            gui.setItem(borderIndexes[i],centerItem);
        }
        for (int i=0; i<lineIndexes.length ; i++){
            gui.setItem(lineIndexes[i],lineItem);
        }
        gui.setItem(displayItemIndex, selectItem);
        if (noticeItem != null)
            gui.setItem(displayNoticeIndex, noticeItem);
        
    }
    
    public Inventory getInventory(){
        return gui;
    }
    
    public Player getGuiOwner(){
        return guiOwner;
    }
    
    public void setSelectItem(Itemstack item){
        selectItem = item;
    }
    public void setNotice(String string){}
    
    public void openGui(){
        if (guiOpened) return;


        if (guiClosed) return;
        
        guiView = guiOwner.openInventory(gui);
        
        if (isCurrentGuiView(guiOwner.getOpenInventory()))
            guiOpened = true;
    }
    
    public void closeGui(){
    
        if (guiClosed) return;
        
        guiClosed = true;
        
        if (guiOpened)
            guiView.close();
        
        removeRelowGui(this);
        
    }
    
    public boolean isGuiOpened(){
        if (guiClosed)
            return false;
        else if (guiOpened)
            return isCurrentGuiView(guiOwner.getOpenInventory());
        else
            return false;
    }
    
    public boolean isGuiClosed(){
        if (guiClosed)
            return true;
        else if (guiOpened)
            return !isCurrentGuiView(guiOwner.getOpenInventory());
        else
            return false;
    }
    
    public boolean isCurrentGuiView(InventoryView view)(
        return view == guiView;
    }
    
    public static RelowGui getRelowGuiOf(Player player){
        return relowGuiMap.get(player);
    }
    public static RelowGui getRelowGuiFromView(InventoryView view){
        for (RelowGui relowGui : relowGuiMap.values()){
            if (relowGui.isCurrentGuiView(view))
                return relowGui;
        }
        return null;
    }
    
    public static relowGui newGui(Player player){
        RelowGui relowGui = relowGuiMap.get(player);
        if (relowGui != null && !relowGui.isClosed()){
            relowGui.closeGui();
        }
        relowGui = new RelowGui(player);
        relowGuiMap.put(player, relowGui);
        return relowGui;
    }
    
    //返回的值表示打开GUI是否成功
    public static boolean openGui(Player player){
        RelowGui relowGui = newGui(player);
        relowGui.openGui();
        return relowGui.isGuiOpened();
    }
    
    public static void removeRelowGui(RelowGui relowGui){
        Player guiOwner = relowGui.getGuiOwner();
        Inventory inListGui = relowGuiMap.get(guiOwner);
        
        
        if (inListGui == relowGui.getInventory()){
            relowGuiMap.remove(guiOwner);
        }

        if (!relowGui.isClosed())
            relowGui.closeGui();

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

    /*
        Map enchantMap = selectItem.getItemMeta().getEnchants();
        if (!enchantMap.isEmpty()) {
            Set<Enchantment> enchantsSet = enchantsMap.keySet();
            ItemStack enBook = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) enBook.getItemMeta();
            int i = 3;
            int cnt = 0;
            for (Enchantment key : enchantmentSet) {
                if (Config.getNoRemove().contains(key.getName())){
                    continue;
                storageMeta.addStoredEnchant(key, (int) enchantsMap.get(key), true);
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
        }


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


        player.openInventory(REgui);

    }
    */
