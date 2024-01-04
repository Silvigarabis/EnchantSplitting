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