package io.github.silvigarabis.esplitter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permissions {
    OP_OTHER_PLAYER("otherplayer"),
    COMMAND("command");
    
    public static final String PREFIX = "esplitter.";
    private String permission;
    public String getPermission() {
        return permission;
    }

    public boolean checkPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }
    public boolean checkPermission(Player player) {
        return player.hasPermission(permission);
    }
    private Permissions(String permission) {
        this.permission = PREFIX + permission;
    }
}
