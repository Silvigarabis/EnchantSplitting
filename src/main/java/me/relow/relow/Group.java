package me.relow.relow;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    
    private static String basePermissionKey = "relow.group";

    private static Logger logger;

    private static Map<String, Group> groupMap = new HashMap<>();
    //额外维护一个列表用于记录组序
    private static List<String> groupIndexes = new ArrayList<>();
    
    private static Group defaultGroup;
    
    private String name;
    
    private int splitExperienceCost;
    private int splitMoneyCost;
    private int splitPointsCost;
    private int grindExperienceCost;
    private int grindMoneyCost;
    private int grindPointsCost;
    private int splitSuccessRate;
    private int grindSuccessRate;
    private int visibleEnchantmentsCount;
    
    public static load(JavaPlugin plugin, ConfigurationSection config){
        this.logger = plugin.getLogger();
        
        removeAllGroup();
        
        for (String groupName : config.getKeys(false)) {
            newGroup(groupName, groupConfig);
        }
    }
    
    public static void newGroup(String groupName, ConfigurationSection groupConfig){
        if (getGroup(name) != null){
            logger.error("权限组"+name+"已经存在!");
            return;
        }
        Group newGroup = new Group(groupName, groupConfig);
        GroupMap.put(name, newGroup);
        GroupIndexes.add(name);
        
        logger.info("已加载权限组"+name);
    }
    
    public static void newGroup(String name, int splitExperienceCost, int splitMoneyCost, int splitPointsCost, int grindExperienceCost, int grindMoneyCost, int grindPointsCost, int splitSuccessRate, int grindSuccessRate, int visibleEnchantmentsCount){
        if (getGroup(name) != null){
            logger.error("权限组"+name+"已经存在!");
            return;
        }
        Group newGroup = new Group(splitExperienceCost, splitMoneyCost, splitPointsCost, grindExperienceCost, grindMoneyCost, grindPointsCost, splitSuccessRate, grindSuccessRate, visibleEnchantmentsCount);
        
        GroupMap.put(name, newGroup);
        GroupIndexes.add(name);
        
        logger.info("已加载权限组"+name);
    }
    
    public static void removeGroup(String name){
        if (getGroup(name) == null)
            return;
        GroupIndexes.remove(name);
        GroupMap.remove(name);
    }
    
    public static void removeAllGroups(){
        GroupIndexes.clear();
        GroupMap.clear();
    }

    public static Group setDefaultGroup(ConfigurationSection groupConfig){
        defaultGroup = new Group(groupConfig);
    };

    public static Group setDefaultGroup(int splitExperienceCost, int splitMoneyCost, int splitPointsCost, int grindExperienceCost, int grindMoneyCost, int grindPointsCost, int splitSuccessRate, int grindSuccessRate, int visibleEnchantmentsCount){
        defaultGroup = new Group(splitExperienceCost, splitMoneyCost, splitPointsCost, grindExperienceCost, grindMoneyCost, grindPointsCost, splitSuccessRate, grindSuccessRate, visibleEnchantmentsCount);
    }
    
    public Group(ConfigurationSection groupConfig){
        return new Group(groupConfig.getInt("splitExperienceCost"), groupConfig.getInt("splitMoneyCost"), groupConfig.getInt("splitPointsCost"), groupConfig.getInt("grindExperienceCost"), groupConfig.getInt("grindMoneyCost"), groupConfig.getInt("grindPointsCost"), groupConfig.getInt("splitSuccessRate"), groupConfig.getInt("grindSuccessRate"), groupConfig.getInt("visibleEnchantmentsCount"));
    };
    
    public Group(String name, int splitExperienceCost, int splitMoneyCost, int splitPointsCost, int grindExperienceCost, int grindMoneyCost, int grindPointsCost, int splitSuccessRate, int grindSuccessRate, int visibleEnchantmentsCount){
    
        this.name = name;
        
        this.splitExperienceCost = Math.max(0, splitExperienceCost);
        this.splitMoneyCost = Math.max(0, splitMoneyCost);
        this.splitPointsCost = Math.max(0, splitPointsCost);
        
        this.grindExperienceCost = Math.max(0, grindExperienceCost);
        this.grindMoneyCost = Math.max(0, grindMoneyCost);
        this.grindPointsCost = Math.max(0, grindPointsCost);
        
        this.splitSuccessRate = Math.min(100, Math.max(0, splitSuccessRate));
        this.grindSuccessRate = Math.min(100, Math.max(0, grindSuccessRate));
        
        this.visibleEnchantmentsCount = Math.max(1,visibleEnchantmentsCount);
        
        return this;
        
    }
    
    public static Group getGroup(String name){
        Group group = groupMap.get(name);
        if (group == null)
            logger.error("无法找到组+name");
        return group;
    }
    
    public static Group getPrimaryGroup(Player player){
        for (String groupName : groupIndexes){
            if (player.hasPermission(basePermissionKey+"."+groupName)){
                return getGroup(groupName);
            }
        }
        return defaultGroup;
    }

    public static int getSplitExperienceCostOf(Player player){
        return getPrimaryGroupOf(player).getSplitExperienceCostOf();
    }
    public static int getSplitMoneyCostOf(Player player){
        return getPrimaryGroupOf(player).getSplitMoneyCostOf();
    }
    public static int getSplitPointsCostOf(Player player){
        return getPrimaryGroupOf(player).getSplitPointsCostOf();
    }
    public static int getGrindExperienceCostOf(Player player){
        return getPrimaryGroupOf(player).getGrindExperienceCostOf();
    }
    public static int getGrindMoneyCostOf(Player player){
        return getPrimaryGroupOf(player).getGrindMoneyCostOf();
    }
    public static int getGrindPointsCostOf(Player player){
        return getPrimaryGroupOf(player).getGrindPointsCostOf();
    }
    public static int getSplitSuccessRateOf(Player player){
        return getPrimaryGroupOf(player).getSplitSuccessRateOf();
    }
    public static int getGrindSuccessRateOf(Player player){
        return getPrimaryGroupOf(player).getGrindSuccessRateOf();
    }
    public static int getVisibleEnchantmentsCountOf(Player player){
        return getPrimaryGroupOf(player).getVisibleEnchantmentsCountOf();
    }
    
    public static Map<String, Group> getGroupMap(){
        return groupMap;
    }
    
    public String getName(){
        return this.name;
    }
    public int getSplitExperienceCost(){
        return this.splitExperienceCost;
    }
    public int getSplitMoneyCost(){
        return this.splitMoneyCost;
    }
    public int getSplitPointsCost(){
        return this.splitPointsCost;
    }
    public int getGrindExperienceCost(){
        return this.grindExperienceCost;
    }
    public int getGrindMoneyCost(){
        return this.grindMoneyCost;
    }
    public int getGrindPointsCost(){
        return this.grindPointsCost;
    }
    public int getSplitSuccessRate(){
        return this.splitSuccessRate;
    }
    public int getGrindSuccessRate(){
        return this.grindSuccessRate;
    }
    public int getVisibleEnchantmentsCount(){
        return this.visibleEnchantmentsCount;
    }
    
}


/*

# 此设置限制了一次能够分析出来的附魔的数量
visibleEnchantments: 3

# 分离附魔等级消耗数
splitExperienceCost: 5
# 分离附魔金币消耗数
splitMoneyCost: 500
# 分离附魔点卷消耗数
splitPointsCost: 5

# 洗去附魔等级消耗数
grindExperienceCost: 1
# 洗去附魔金币消耗数
grindMoneyCost: 100
# 分离附魔点卷消耗数
grindPointsCost: 1

# 分离附魔成功率（百分率，100为一定成功）
splitSuccessRate: 100
# 洗去附魔成功率（百分率，100为一定成功）
grindSuccessRate: 100

*/