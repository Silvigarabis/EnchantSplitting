package me.relow.relow.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.command.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Messager {
    private static Logger logger;
    private static String prefix;
    private static ConfigurationSection messageConfig;
    
    public static String translateMessageKey(String messageKey){
        return messageConfig.getString(messageKey, messageKey);
    }
    
    public static String getMessage(String messageKey, String[] replaceHolders){
        String message = translateMessageKey(messageKey);
        
        for (let i=0; i<replaceHolders.length; i++){
            message = message.replaceFirst("\\{\\}", replaceHolders[i]);
        }
        
        message = config.getString("prefix", prefix);
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    public static void sendMessage(CommandSender receivr, String messageKey, String[] replaceHolders){
        String message = getMessage(messageKey, replaceHolders);
        receivr.sendMessage(message);
    }
    
    public static void load(JavaPlugin plugin, ConfigurationSection config){
        logger = plugin.getLogger();
        prefix = plugin.getName();
        messageConfig = config;
    }
    
}
