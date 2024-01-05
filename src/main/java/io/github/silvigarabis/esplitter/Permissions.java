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

import org.bukkit.command.CommandSender;

public enum Permissions {
    MAIN_COMMAND("command"),
    COMMAND_GUI("gui"),
    COMMAND_GUI_OTHERS("gui.others"),
    COMMAND_DEBUG("debug"),
    COMMAND_HELP("help"),
    COMMAND_RELOAD("reload");

    private String name;
    private String permissionKey;

    public String getName(){
        return name;
    }
    public String getPermissionKey(){
        return permissionKey;
    }

    public boolean check(CommandSender sender){
        return sender.hasPermission(permissionKey);
    }

    private Permissions(String name){
        this.name = name;
        this.permissionKey = "esplitter." + name;
    }
}