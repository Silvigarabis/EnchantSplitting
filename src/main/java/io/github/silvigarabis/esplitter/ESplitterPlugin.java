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

    @Override
    public void onEnable() {
        plugin = this;
        
        this.logger = this.getLogger();
        
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        this.getCommand("esplitter").setExecutor(new MainCommandExecutor());
        
        logger.info("Esplitter 插件 已加载。");
    }
    
    @Override
    public void onDisable() {
        plugin = null;
        logger.info("插件已禁用");
        
    }
}
