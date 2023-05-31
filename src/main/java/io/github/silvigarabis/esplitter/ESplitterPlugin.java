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

//import io.github.silvigarabis.enchantsplitting.command.MainCommand;
//import io.github.silvigarabis.enchantsplitting.listener.InventoryGuiListener;
//import io.github.silvigarabis.enchantsplitting.utils.Messager;

//import net.milkbowl.vault.economy.Economy;

//import org.black_ixx.playerpoints.PlayerPoints;
//import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
//import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.logging.Logger;
import java.io.File;

public final class ESplitterPlugin extends JavaPlugin {
    
    private boolean debugMode = false;
    
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
    
    private Logger logger;
    /*
    private File groupConfigFile = new File(getDataFolder(), "group.yml");
    private File messageConfigFile = new File(getDataFolder(), "message.yml");
    
    private ConfigurationSection groupConfig;
    private ConfigurationSection messageConfig;
    
    private Economy economy = null;
    
    private boolean hasPlayerPoints;
    private boolean hasEconomy;
    
    public boolean getHasEconomy() {
        return hasEconomy;
    }

    public boolean getHasPlayerPoints() {
        return hasPlayerPoints;
    }
*/
    @Override
    public void onEnable() {
        plugin = this;
        
        this.logger = this.getLogger();
        
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        this.getCommand("esplitter").setExecutor(new MainCommandExecutor());
        
        /*
        logger.info("====================");
        logger.info("R E L O W");
        logger.info("源代码: https://github.com/Silvigarabis/relow");
        logger.info("====================");
        
        loadSoftDepends();
        saveDefaultConfig();
        reloadConfig();
        
        if (!loadConfigClass()) return;
        
        Group.load(this);
        Messager.load(this);
        
        
        
        logger.info("插件已加载");
        */
        logger.info("Esplitter 插件 已加载。");
    }
    
    @Override
    public void onDisable() {
        plugin = null;
        /*
        Group.removeAllGroups();
        */
        logger.info("插件已禁用");
        
    }
    /*
    public boolean loadConfigClass(){
        if (!Config.load(this)){
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }
    
    @Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
        if (!messageConfigFile.exists()) {
            saveResource("message.yml", false);
        }
        if (!groupConfigFile.exists()) {
            saveResource("group.yml", false);
        }
    }
    
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.groupConfig = YamlConfiguration.loadConfiguration(groupConfigFile);
        this.messageConfig = YamlConfiguration.loadConfiguration(messageConfigFile);
    }

    public Economy getEconomy(){
        return economy;
    }
    
    private void loadSoftDepends(){
    
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")){
            this.hasPlayerPoints = true;
            logger.info("已找到PlayerPoints");
        } else {
            this.hasPlayerPoints = false;
        }

        this.hasEconomy = setupEconomy();
        if (this.hasEconomy)
            logger.info("已找到Vault");
        else
            logger.info("未找到Vault");
        
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.economy = rsp.getProvider();
        return this.economy != null;
    }
    
    public boolean executeReload(CommandSender sender){
        logger.info("重新加载中");
        reloadConfig();
        if (!loadConfigClass()) return false;
        Group.load(this);
        Messager.load(this);
        sender.sendMessage("已重新加载！");
        return true;
    }
    
    public ConfigurationSection getGroupConfig(){
        return this.groupConfig;
    }
    
    public ConfigurationSection getMessageConfig(){
        return this.messageConfig;
    }
*/
}
