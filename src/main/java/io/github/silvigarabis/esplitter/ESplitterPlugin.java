/*
   Copyright (c) 2024 Silvigarabis
   EnchantmentSplitter is licensed under Mulan PSL v2.
   You can use this software according to the terms and conditions of the Mulan PSL v2. 
   You may obtain a copy of Mulan PSL v2 at:
            http://license.coscl.org.cn/MulanPSL2 
   THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.  
   See the Mulan PSL v2 for more details.  
*/

package io.github.silvigarabis.esplitter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.logging.Logger;
import java.util.List;
import java.io.File;

public final class ESplitterPlugin extends JavaPlugin {
    
    private boolean debugMode = false;
    private ConfigurationSection config;
    
    protected void setDebugMode(boolean enable){
        debugMode = enable;
    }
    
    public boolean isDebugMode(){
        return debugMode;
    }
    
    public static ESplitterPlugin getPlugin(){
        return plugin;
    }
    private static ESplitterPlugin plugin = null;
    
    public ConfigurationSection getPluginConfig(){
        return config;
    }
    public static boolean isConfigured(){
        return ESplitterConfig.isConfigured();
    }

    private Logger logger;

    @Override
    public void onEnable(){
        this.logger = this.getLogger();

        Messages.consoleInfo(Messages.MessageKey.PLUGIN_LOADING);
        
        if (plugin == null){
            plugin = this;
        } else {
            Messages.consoleError(Messages.MessageKey.PLUGIN_ERROR_DOUBLE_LOAD);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getCommand("esplitter").setExecutor(new MainCommandExecutor());
        
        this.reloadConfig();

        Messages.consoleInfo(Messages.MessageKey.PLUGIN_ENABLED);
        logger.info("ESplitter 插件，一个让玩家可以分离装备上的附魔的插件");
        logger.info("源代码： https://github.com/Imeaces/EnchantmentSplitter");
        logger.info("你可以在在GitHub上提出建议，或者反馈错误： https://github.com/Imeaces/EnchantmentSplitter/issues");
    }
    
    @Override
    public void onDisable(){
        if (plugin == this){
            plugin = null;
        }
        ESplitterConfig.cleanConfig();
        Messages.cleanMessageConfig();
        Messages.consoleInfo(Messages.MessageKey.PLUGIN_DISABLED);
    }
    
    @Override
    public void reloadConfig(){
        Messages.consoleInfo(Messages.MessageKey.PLUGIN_RELOADING_CONFIG);

        saveDefaultConfig();
        super.reloadConfig();
        ESplitterConfig.cleanConfig();
        ESplitterConfig.loadConfig(getConfig());
        
        if (ESplitterConfig.isConfigured()){
            Messages.consoleInfo(Messages.MessageKey.PLUGIN_CONFIG_LOADED);
        } else {
            Messages.consoleWarn(Messages.MessageKey.PLUGIN_CONFIG_LOAD_ERROR);
        }
        
        Messages.consoleInfo(Messages.MessageKey.PLUGIN_RELOADING_MESSAGE_CONFIG);
        saveResource("message.yml", false);
        var messageConfigFile = new File(getDataFolder().getPath(), "message.yml");
        //Messages.cleanMessageConfig();
        Messages.loadMessageConfig(messageConfigFile);
        List<Messages.MessageKey> missingMessageKeys = Messages.getMissingMessageKeys();
        if (missingMessageKeys.size() == 0){
            Messages.consoleInfo(Messages.MessageKey.PLUGIN_MESSAGE_CONFIG_LOADED);
        } else {
            //大致上就是把缺失的key打印出来
            Messages.consoleWarn(
                Messages.MessageKey.PLUGIN_MESSAGE_CONFIG_LOAD_MISSING,
                Integer.toString(missingMessageKeys.size()),
                missingMessageKeys
                  .stream()
                  .map(key -> key.getMessageKey())
                  .toString()
            );
        }
    }
}
