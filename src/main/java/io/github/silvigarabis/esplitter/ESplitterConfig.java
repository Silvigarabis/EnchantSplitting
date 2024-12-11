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

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;

public class ESplitterConfig {
    protected static final Configuration DEFAULT_CONFIGURATIONS = new MemoryConfiguration();
    public static final Configuration CONFIGURATIONS = null;

    static {
        DEFAULT_CONFIGURATIONS.set("config-version", 1);
    }

    private String key;
    public String getKey() {
        return key;
    }

    private ESplitterConfig(String key) {
        this.key = key;
    }
    public static final String SPLIT_NEED_EMPTY_BOOK = "split-enchant.need-empty-book";
    public static final String CONFIG_VERSION = "config-version";

    private ESplitterConfig(){}
}
