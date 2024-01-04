package io.github.silvigarabis.esplitter;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.YamlConfiguration;

import java.util.Map;
import java.util.EnumMap;

public class Messages {
    public enum MessageKey {
        COMMAND_NO_PERMISSION_COMMAND("command-no-permission.command"),
        COMMAND_NO_PERMISSION_GUI("command-no-permission.gui"),
        COMMAND_NO_PERMISSION_GUI_OTHERS("command-no-permission.gui.others"),
        COMMAND_NO_PERMISSION_RELOAD("command-no-permission.reload"),
        COMMAND_NO_PERMISSION_DEBUG("command-no-permission.debug");
        
        private String messageKey;
        public String getMessageKey(){
            return messageKey;
        }
        private MessageKeys(String messageKey){
            this.messageKey = messageKey;
        }
        public void send(CommandSender sender){
            Messages.send(sender, this);
        }
        public void send(CommandSender sender, String... replacements){
            Messages.send(sender, this, replacements);
        }
    }

    private static Map<String, String> messages = new EnumMap<>();
    public void cleanMessageConfig(){
        messages.clear();
    }
    public void loadMessageConfig(ConfigurationSection messageConfig){
        for (var messageKey : MessageKey.values()){
            var key = messageKey.getMessageKey();
            var messageString = messageConfig.get(key);
            if (messageString == null){
                messageString = "";
            }
            messages.put(messageKey, messageString);
        }
    }
    public static String getMessageString(MessageKey messageKey){
        return messages.contains(messageKey) ? messages.get(messageKey) : "";
    }
    public static void send(CommandSender sender, MessageKeys key, String[] replacements){
        var messageString = getMessageString(key);
        for (var replacement : replacements){
            messageString = messageString.replaceFirst("\\{\\}", replacement);
        }
        sender.sendMessage(messageString);
    }
    public static void send(CommandSender sender, MessageKeys key){
        sender.sendMessage(getMessageString(key));
    }
}