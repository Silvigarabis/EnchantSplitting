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
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class MainCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
        ESplitterPlugin.getPlugin().getLogger().info("正在处理指令……");
        
        if (!Permissions.MAIN_COMMAND.check(sender)){
            sender.sendMessage("没有使用权限");
            return true;
        }
        
        if (args.length == 0){
            if ("esgui".equals(label))
                openGuiCmd(sender, label, args);
            else
                helpCmd(sender, label, args);
            return true;
        }
        
        if ("esgui".equals(label) && args.length == 1){
            openGuiCmd(sender, label, args);
            return true;
        }

        switch (args[0]){
            case "gui":
                guiCmd(sender, label, args);
                break;
            case "reload":
                reloadCmd(sender, label, args);
                break;
            case "debug":
                debugCmd(sender, label, args);
                break;
            case "help":
                helpCmd(sender, label, args);
                break;
            default:
                sender.sendMessage("未知的命令格式");
                helpCmd(sender, label, args);
                break;
        }
        
        return true;
    }
    
    private static Player getPlayer(String playerName){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getName().equalsIgnoreCase(playerName))
                return player;
        }
        return null;
    }

    private void guiCmd(CommandSender sender, String label, String[] args){
        if (!Permissions.COMMAND_GUI.check(sender)){
            sender.sendMessage("没有权限打开GUI");
            return;
        }

        if (args.length == 2){
            var player = getPlayer(args[1]);
            if (!sender.equals(player) && !Permissions.COMMAND_GUI_OTHERS.check(sender)){
                sender.sendMessage("没有权限为其他玩家打开GUI");
                return;
            }
            if (player != null){
                openGui(player);
            } else {
                sender.sendMessage("错误！未找到玩家");
            }
        } else {
            if (sender instanceof Player){
                openGui((Player)sender);
            } else {
                sender.sendMessage("仅玩家可用");
            }
        }
    }
    
    private void debugCmd(CommandSender sender, String label, String[] args){
        if (!Permissions.COMMAND_DEBUG.check(sender)){
            sender.sendMessage("没有使用权限");
            return;
        }
        if (args.length == 2){
            boolean mode = "true".equals(args[1]);
            ESplitterPlugin.getPlugin().setDebugMode(mode);
            sender.sendMessage("Debug 模式被设置为 " + mode);
        } else {
            sender.sendMessage("Debug 模式:" + ESplitterPlugin.getPlugin().isDebugMode());
        }
    }

    private void reloadCmd(CommandSender sender, String label, String[] args){
        if (!Permissions.COMMAND_RELOAD.check(sender)){
            sender.sendMessage("没有使用权限");
            return;
        }
        sender.sendMessage("not implemented");
    }
    
    private void helpCmd(CommandSender sender, String label, String[] args){
        sender.sendMessage("用法：/" + label + " [gui|help|reload|debug]");
    }
    
    private void openGuiCmd(CommandSender sender, String label, String[] args){
        String[] fullArgs = new String[args.length + 1];
        fullArgs[0] = "gui";
        for (int idx = 1; idx <= args.length; idx ++){
            fullArgs[idx] = args[idx - 1];
        }
        guiCmd(sender, label, fullArgs);
    }
    
    private void openGui(Player player){
        new ESplitterController(player);
    }
}
