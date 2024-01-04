package io.github.silvigarabis.esplitter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.EnumMap;

public class Messages {
    public enum MessageKey {
        COMMAND_NO_PERMISSION("command-no-permission"),
        COMMAND_UNKNOWN("command-unknown"),

        COMMAND_GUI_NO_PERMISSION("command-gui.no-permission"),
        COMMAND_GUI_NO_PERMISSION_FOR_OTHERS("command-gui.no-permission-for-others"),
        COMMAND_GUI_OTHER_PLAYER_NOTFOUND("command-gui.other-player-not-found"),
        COMMAND_GUI_PLAYER_ONLY("command-gui.player-only"),

        COMMAND_DEBUG_NO_PERMISSION("command-debug.no-permission"),
        COMMAND_DEBUG_ENABLED("command-debug.debug-enable"),
        COMMAND_DEBUG_DISABLED("command-debug.debug-disable"),
        COMMAND_DEBUG_STATUS("command-debug.debug-status"),

        COMMAND_RELOAD_NO_PERMISSION("command-reload.no-permission"),
        COMMAND_RELOAD_PROCESSING("command-reload.reload-processing"),
        COMMAND_RELOAD_SUCCESS("command-reload.success"),
        COMMAND_RELOAD_FAIL("command-reload.fail"),

        COMMAND_HELP_NO_PERMISSION("command-help.no-permission"),
        COMMAND_HELP_GENERAL("command-help.general"),
        COMMAND_HELP_ESGUI("command-help.esgui"),

        GUI_TITLE("gui.title"),
        GUI_UNEXPECTED_CLOSE("gui.unexpected-error"),

        INVALID_PLUGIN_CONFIG("invalid-plugin-config"),
        CHAT_PREFIX("chat-prefix");

        private String messageKey;
        public String getMessageKey(){
            return messageKey;
        }
        private MessageKey(String messageKey){
            this.messageKey = messageKey;
        }
        public void send(CommandSender sender){
            Messages.send(sender, this);
        }
        public void send(CommandSender sender, String... replacements){
            Messages.send(sender, this, replacements);
        }
        public String getMessageString(){
            return Messages.getMessageString(this);
        }
    }

    private static Map<MessageKey, String> messages = new EnumMap<MessageKey, String>();
    public static int countMissingMessage(){
        return MessageKey.values().length - messages.size();
    }
    public static void cleanMessageConfig(){
        messages.clear();
    }
    public static void loadMessageConfig(ConfigurationSection messageConfig){
        for (MessageKey messageKey : MessageKey.values()){
            var key = messageKey.getMessageKey();
            var messageString = messageConfig.get(key);
            if (messageString == null){
                continue;
            }
            messages.put(messageKey, messageString);
        }
    }
    public static void loadMessageConfig(File yamlConfigFile){
        var messageConfig = YamlConfiguration.loadConfiguration(yamlConfigFile);
        loadMessageConfig(messageConfig);
    }
    public static String getMessageString(MessageKey messageKey){
        return messages.contains(messageKey) ? messages.get(messageKey) : messageKey.getMessageKey();
    }
    
    public static void send(CommandSender sender, MessageKey key, String replacement, String... replacements){
       String[] fullReplacements = new String[replacements.length + 1];
       fullReplacements[0] = replacement;
       for (int idx = 1; idx <= replacements.length; idx++){
          fullReplacements[idx] = replacements[idx - 1];
       }
       send(sender, key, fullReplacements);
    }
    public static void send(CommandSender sender, MessageKey key, String[] replacements){
        var messageString = getMessageString(key);
        for (var replacement : replacements){
            messageString = messageString.replaceFirst("\\{\\}", replacement);
        }
        send(sender, messageString);
    }
    public static void send(CommandSender sender, MessageKey key){
        send(sender, getMessageString(key));
    }
    public static void send(CommandSender sender, String message){
        message = getMessageString(MessageKey.CHAT_PREFIX) + " " + message;
        message = message.replaceAll("&([0-9a-fmnol])", "§$1");
        message = message.replaceAll("&&", "&");
        sender.sendMessage(message);
    }
    
    public static void sendConsole(MessageKey key, String[] replacements){
        send(Bukkit.getConsoleSender(), key, replacements);
    }
    public static void sendConsole(MessageKey key, String replacement, String... replacements){
        send(Bukkit.getConsoleSender(), replacement, replacements);
    }
    public static void sendConsole(MessageKey key){
        send(Bukkit.getConsoleSender(), key);
    }
    public static void sendConsole(String message){
        send(Bukkit.getConsoleSender(), message);
    }
}