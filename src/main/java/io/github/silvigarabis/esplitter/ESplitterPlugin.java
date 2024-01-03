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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.logging.Logger;
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
        return plugin != null && plugin.getPlugin().config != null;
    }

    private Logger logger;

    @Override
    public void onEnable(){
        this.logger = this.getLogger();

        logger.info("ESplitter 正在加载。");
        
        if (plugin == null){
            plugin = this;
        } else {
            logger.severe("检测到另一个插件实例正在运行！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        this.getCommand("esplitter").setExecutor(new MainCommandExecutor());
        
        logger.info("ESplitter 已加载。");
        logger.info("ESplitter 插件，一个让玩家可以分离装备上的附魔的插件");
        logger.info("源代码： https://github.com/Imeaces/EnchantmentSplitter");
        logger.info("你可以在在GitHub上提出建议，或者反馈错误： https://github.com/Imeaces/EnchantmentSplitter/issues");
    }
    
    @Override
    public void onDisable(){
        if (plugin == this){
            plugin = null;
        }
        logger.info("插件已禁用");
    }
    
    @Override
    public void reloadConfig(){
        saveDefaultConfig();

        ConfigurationSection config;
        try {
            super.reloadConfig();
            config = getConfig();
        } catch (InvalidConfigurationException ex){
            logger.severe("尝试加载配置文件时出现错误");
            ex.printStackTrace();
            config = null;
        }
        
        this.config = null;
        if (ESplitterConfig.verifyConfig(config)){
            this.config = config;
            logger.info("配置文件加载成功！");
        } else {
            logger.warning("插件配置有误，请检查配置文件是否正确。");
            logger.warning("在确认无误后，可以使用/esplitter reload重新加载");
        }
    }
}
