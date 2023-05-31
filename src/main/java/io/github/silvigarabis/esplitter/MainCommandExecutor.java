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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class MainCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
        Utils.getLogger().info("正在处理指令……");
        
        if (!sender.hasPermission("esplitter.command")){
            sender.sendMessage("没有使用权限");
            return true;
        }
        
        if (args.length == 0 || args[0].equals("gui")){
            if (sender instanceof Player){
                Player player = (Player)sender;
                new ESplitterController(player);
            } else {
                sender.sendMessage("仅玩家可用");
            }
        } else if (args[0].equals("debug")){
            if (!sender.hasPermission("esplitter.debug")){
                sender.sendMessage("没有使用权限");
                return true;
            }
            if (args.length >= 2){
                boolean mode = args[1].equals("true");
                ESplitterPlugin.getPlugin().setDebugMode(mode);
                sender.sendMessage("Debug 模式被设置为 "+mode);
            } else {
                sender.sendMessage("Debug 模式:"+ESplitterPlugin.getPlugin().isDebugMode());
            }
        } else if (args[0].equals("reload")){
            sender.sendMessage("not implemented");
            
        } else if (args[0].equals("help")){
            sender.sendMessage("用法：/"+label+" [gui|help|reload|debug]");
            
        } else {
            sender.sendMessage("未知的命令格式");
            return false;
            
        }
        
        return true;
    }

}
