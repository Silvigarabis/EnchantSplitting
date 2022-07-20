package me.relow.relow;

import me.relow.relow.Group;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Config {
    private static Logger logger;
    
    private static ConfigurationSection config;
    
    private static List<String> noRemoveEnchants;
    private static String noRemoveLore;
    
    private static boolean splitCostExperience;
    private static boolean splitCostMoney;
    private static boolean splitCostPoints;
    private static boolean grindCostExperience;
    private static boolean grindCostMoney;
    private static boolean grindCostPoints;
    
    public static boolean load(JavaPlugin plugin){
        logger = plugin.getLogger();
        config = plugin.getConfig();
        
        noRemoveEnchants = config.getStringList("noRemoveEnchant", new ArrayList<String>());
        noRemoveLore = config.getString("noRemoveLore", "");
        
        splitCostExperience = config.getBoolean("splitCostExperience", false);
        splitCostMoney = config.getBoolean("splitCostExperience", false);
        splitCostPoints = config.getBoolean("splitCostExperience", false);
        grindCostExperience = config.getBoolean("grindCostExperience", false);
        grindCostMoney = config.getBoolean("grindCostExperience", false);
        grindCostPoints = config.getBoolean("grindCostExperience", false);
        
        if (splitCostMoney || grindCostMoney && !plugin.hasEconomy()){
            logger.warning("未找到可用的经济插件");
            logger.warning("将会禁用金币消耗");
            splitCostMoney = false;
            grindCostMoney = false;
        }
        if (splitCostPoints || grindCostPoints && !plugin.hasPlayerPoints()){
            logger.warning("未找到PlayerPoints插件");
            logger.warning("将会禁用点券消耗");
            splitCostPoints = false;
            grindCostPoints = false;
        }
        
        Group.setDefaultGroup(config);
        logger.info("已加载默认组！");
        
    }
    
    public static List<String> getNoRemoveEnchants(){
        return noRemoveEnchants;
    }
    public static String getNoRemoveLore(){
        return noRemoveLore;
    }
    public static boolean isSplitCostExperience(){
        return splitCostExperience;
    }
    public static boolean isSplitCostMoney(){
        return splitCostMoney;
    }
    public static boolean isSplitCostPoints(){
        return splitCostPoints;
    }
    public static boolean isGrindCostExperience(){
        return grindCostExperience;
    }
    public static boolean isGrindCostMoney(){
        return grindCostMoney;
    }
    public static boolean isGrindCostPoints(){
        return grindCostPoints;
    }

}
