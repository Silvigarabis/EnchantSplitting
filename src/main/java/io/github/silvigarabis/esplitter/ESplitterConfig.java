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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.util.Objects;

/**
 * 用于验证配置是否正确，以及存储正确的配置的类。
 * 暂时没有起到任何作用，因为我不会java
 */
public class ESplitterConfig {
    public static final String SPLIT_NEED_EMPTY_BOOK = "split-enchant.need-empty-book";
    public static final String CONFIG_VERSION = "config-version";

    private static ConfigurationSection config;
    
    public static ConfigurationSection getConfig(){
        return config;
    }

    private static boolean verifyConfig(ConfigurationSection config){
        return config != null
        && config.isBoolean(SPLIT_NEED_EMPTY_BOOK)
        && config.isInt(CONFIG_VERSION);
    }
    
    public static boolean isConfigured(){
        return config != null && ESplitterPlugin.getPlugin() != null;
    }

    public static boolean loadConfig(File configFile){
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(configFile);

        if (yamlConfig == null){
            return false;
        }

        return loadConfig(yamlConfig);
    }

    public static boolean loadConfig(ConfigurationSection unverifiedConfig){
        if (verifyConfig(unverifiedConfig)){
            ESplitterConfig.config = unverifiedConfig;
        }
        return ESplitterConfig.config != null
            && ESplitterConfig.config.equals(unverifiedConfig);
    }

    public static void cleanConfig(){
        config = null;
    }

    private ESplitterConfig(){
        throw new RuntimeException("no constructor");
    }
}
