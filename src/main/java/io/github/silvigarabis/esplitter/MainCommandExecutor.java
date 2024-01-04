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
import io.github.silvigarabis.esplitter.Messages.MessageKeys;

import java.util.logging.Logger;

public class MainCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ESplitterPlugin.getPlugin().getLogger().info("正在处理指令……");
        if (!ESplitterPlugin.isConfigured()){
            Messages.send(sender, MessageKeys.INVALID_PLUGIN_CONFIG);
            ESplitterPlugin.getPlugin().getLogger().warning("插件的配置文件没有正确配置");
            ESplitterPlugin.getPlugin().getLogger().warning("这可能是因为其中含有语法错误，或者缺失了必要的配置");
            ESplitterPlugin.getPlugin().getLogger().warning("尝试修正配置，并在这之后使用 /esplitter reload 重新载入配置文件");
        }
    
        if (!Permissions.MAIN_COMMAND.check(sender)){
            Messages.send(sender, MessageKeys.COMMAND_NO_PERMISSION);
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
                Messages.send(sender, MessageKeys.COMMAND_UNKNOWN);
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
        if (!ESplitterPlugin.isConfigured()){
            Messages.send(sender, MessageKeys.INVALID_PLUGIN_CONFIG);
            return;
        }
        if (!Permissions.COMMAND_GUI.check(sender)){
            Messages.send(sender, MessageKeys.COMMAND_GUI_NO_PERMISSION);
            return;
        }

        if (args.length == 2){
            var player = getPlayer(args[1]);
            if (!sender.equals(player) && !Permissions.COMMAND_GUI_OTHERS.check(sender)){
                Messages.send(sender, MessageKeys.COMMAND_GUI_NO_PERMISSION_FOR_OTHERS);
                return;
            }
            if (player != null){
                openGui(player);
            } else {
                Messages.send(sender, MessageKeys.COMMAND_GUI_OTHER_PLAYER_NOTFOUND, args[1]);
            }
        } else {
            if (sender instanceof Player){
                openGui((Player)sender);
            } else {
                Messages.send(sender, MessageKeys.COMMAND_GUI_PLAYER_ONLY);
            }
        }
    }
    
    private void debugCmd(CommandSender sender, String label, String[] args){
        if (!Permissions.COMMAND_DEBUG.check(sender)){
            Messages.send(sender, MessageKeys.COMMAND_DEBUG_NO_PERMISSION);
            return;
        }
        if (args.length == 2){
            boolean mode = "true".equals(args[1]);
            ESplitterPlugin.getPlugin().setDebugMode(mode);
            Messages.send(sender,
                MessageKeys.COMMAND_DEBUG_STATUS,
                "" + ESplitterPlugin.getPlugin().isDebugMode()
            );
        } else {
            Messages.send(sender,
                MessageKeys.COMMAND_DEBUG_STATUS,
                "" + ESplitterPlugin.getPlugin().isDebugMode()
            );
        }
    }

    private void reloadCmd(CommandSender sender, String label, String[] args){
        if (!Permissions.COMMAND_RELOAD.check(sender)){
            Messages.send(sender, MessageKeys.COMMAND_RELOAD_NO_PERMISSION);
            return;
        }
        
        Messages.send(sender, MessageKeys.COMMAND_RELOAD_PROCESSING);

        ESplitterPlugin.getPlugin().reloadConfig();
        
        if (ESplitterPlugin.isConfigured()){
            Messages.send(sender, MessageKeys.COMMAND_RELOAD_SUCCESS);
        } else {
            Messages.send(sender, MessageKeys.COMMAND_RELOAD_FAIL);
        }
    }
    
    private void helpCmd(CommandSender sender, String label, String[] args){
        if ("esgui".equals(label)){
            Messages.send(sender, MessageKeys.COMMAND_HELP_ESGUI, label);
        } else {
            Messages.send(sender, MessageKeys.COMMAND_HELP_GENERAL, label);
        }
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
