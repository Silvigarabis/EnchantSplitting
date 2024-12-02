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

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ESplitterPlugin extends JavaPlugin {
    
    public static ESplitterPlugin getPluginInstance(){
        if (pluginInstance == null) {
            throw new RuntimeException("plugin instance not found");
        }
        return pluginInstance;
    }
    private static ESplitterPlugin pluginInstance = null;
    
    private Logger logger;

    @Override
    public void onEnable() {
        this.logger = this.getLogger();

        logger.info("ESplitter 正在加载。");
        
        if (pluginInstance == null){
            pluginInstance = this;
        } else {
            logger.severe("插件状态异常");
            throw new IllegalStateException("plugin is not null");
        }

        registerEvents(this);
        registerCommands(this);
        
        logger.info("ESplitter 已加载。");
        logger.info("ESplitter 插件，一个让玩家可以分离装备上的附魔的插件");
        logger.info("源代码： https://github.com/Imeaces/EnchantmentSplitter");
        logger.info("你可以在在GitHub上提出建议，或者反馈错误： https://github.com/Imeaces/EnchantmentSplitter/issues");
    }

    private static void registerEvents(ESplitterPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(new ESplitterListener(), plugin);
    }

    private static void registerCommands(ESplitterPlugin plugin){
        plugin.getCommand("esplitter").setExecutor(new MainCommandExecutor());
    }
    
    
    @Override
    public void onDisable() {
        if (this.equals(pluginInstance)){
            pluginInstance = null;
        }
        logger.info("插件已禁用");
    }
}
