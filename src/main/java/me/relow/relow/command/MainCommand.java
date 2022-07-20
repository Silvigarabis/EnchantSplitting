package me.relow.relow.command;

import me.relow.relow.RelowGui;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    
    boolean disabled = false;
    
    private JavaPlugin plugin;
    private Logger logger;
    
    public void setDisabled(boolean toDisable){
        this.disabled = toDisable;
    }
    
    public void setDisabled(){
        this.setDisabled(true);
    }

    public void RelowCommandExecutor(JavaPlugin plugin){
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        return;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
        if (disabled) return disabled;
        
        String option = "help";
        boolean isPlayer = (sender instanceof Player);
        
        if (args.length > 0)
          option = args[0]
        else 
          option = "relow";
        
        if (option == "relow"){
            if (isPlayer && sender.hasPermission("relow.relow"))
                return RelowGui.openGui((Player) sender);
            else if (isPlayer && !sender.hasPermission("relow.relow"))
                sender.sendMessage("您没有使用Relow的权限");
            else
                sender.sendMessage("只有玩家可以打开relowGUI");
        } else if (option == "reload"){
            if (sender.hasPermission("relow.reload"))
                return plugin.executeReload(sender);
            else
                sender.sendMessage("您没有权限这么做");
        } else if (sender.hasPermission("relow.help")){
            sender.sendMessage(
                "=========RELOW分离附魔=========",
                "作者:StrawberryYu | QQ:2332742172",
                "手持附魔物品输入 /rel-open 打开界面");
            if (sender.hasPermission("relow.reload"))
                sender.sendMessage("/rel-reload 重载插件");
            return true;
        }
        return false;
    }

}
