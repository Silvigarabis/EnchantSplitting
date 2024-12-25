package io.github.silvigarabis.esplitter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permissions {
    OP_OTHER_PLAYER("otherplayer"),
    MAIN_COMMAND("command"),
    COMMAND_GUI("gui"),
    COMMAND_GUI_OTHERS("gui.others"),
    COMMAND_DEBUG("debug"),
    COMMAND_HELP("help"),
    COMMAND_RELOAD("reload"),
    COMMAND("command");

    public static final String PREFIX = "esplitter.";

    private String permissionKey;
    public String getPermissionKey() {
        return permissionKey;
    }

    private String name;
    public String getName(){
        return name;
    }

    public boolean checkPermission(CommandSender sender) {
        return sender.hasPermission(permissionKey);
    }
    public boolean checkPermission(Player player) {
        return player.hasPermission(permissionKey);
    }

    private Permissions(String permission) {
        this.name = permission;
        this.permissionKey = PREFIX + permission;
    }
}
