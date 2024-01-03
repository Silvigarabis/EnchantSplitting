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

public class ESplitterConfig {
    public static final String SPLIT_NEED_EMPTY_BOOK = "split-enchant.need-empty-book";
    public static final String CONFIG_VERSION = "config-version";

    public static boolean verifyConfig(ConfigurationSection config){
        return config != null;
    }

    private ESplitterConfig(){}
}
