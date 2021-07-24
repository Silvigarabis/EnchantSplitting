package me.relow.relow.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class help implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            player.sendMessage("=========RELOW分离附魔=========");
            player.sendMessage("作者:StrawberryYu | QQ:2332742172");
            player.sendMessage("手持附魔物品输入 /rel-open 打开界面");
            if(player.isOp()){
                player.sendMessage("/rel-reload 重载插件");
            }
            player.sendMessage("=========RELOW分离附魔=========");
        }else {
            System.out.println("=========RELOW分离附魔=========");
            System.out.println("作者:StrawberryYu | QQ:2332742172");
            System.out.println("手持附魔物品输入 /rel-open 打开界面");
            System.out.println("/rel-reload 重载插件");
            System.out.println("=========RELOW分离附魔=========");
        }
        return false;
    }
}
