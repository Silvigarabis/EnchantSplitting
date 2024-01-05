/*
   Copyright (c) 2024 Silvigarabis
   EnchantmentSplitter is licensed under Mulan PSL v2.
   You can use this software according to the terms and conditions of the Mulan PSL v2. 
   You may obtain a copy of Mulan PSL v2 at:
            http://license.coscl.org.cn/MulanPSL2 
   THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.  
   See the Mulan PSL v2 for more details.  
*/

package io.github.silvigarabis.esplitter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.EnumMap;

import java.util.logging.Logger;

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
        CHAT_PREFIX("chat-prefix"),
        LOGGER_NAME("logger-name");

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

    private static String DEFAULT_LOGGER_NAME = "ESplitter";
    private static Map<MessageKey, String> messages = new EnumMap<MessageKey, String>(MessageKey.class);
    public static int countMissingMessage(){
        return MessageKey.values().length - messages.size();
    }
    public static void cleanMessageConfig(){
        messages.clear();
    }
    public static void loadMessageConfig(ConfigurationSection messageConfig){
        for (Object messageKeyObject : MessageKey.values()){
            MessageKey messageKey = (MessageKey) messageKeyObject;
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
        var string = messages.get(messageKey);
        if (string == null && string.length() == 0){
            return messageKey.getMessageKey();
        } else {
            return string;
        }
    }
    
    public static String getMessage(MessageKey key, String[] replacements){
        var messageString = getMessageString(key);
        for (var replacement : replacements){
            messageString = messageString.replaceFirst("\\{\\}", replacement);
        }
        return messageString;
    }
    public static String getMessage(MessageKey key){
        return getMessageString(key);
    }
    public static String getMessage(MessageKey key, String replacement, String... replacements){
       String[] fullReplacements = new String[replacements.length + 1];
       fullReplacements[0] = replacement;
       for (int idx = 1; idx <= replacements.length; idx++){
          fullReplacements[idx] = replacements[idx - 1];
       }
       return getMessage(key, fullReplacements);
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
        send(sender, getMessage(key, replacements));
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
    
    public static void consoleLog(java.util.logging.Level level, MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleLog(level, key, fullReplacements);
    }
    public static void consoleLog(java.util.logging.Level level, MessageKey key, String[] replacements){
        consoleLog(level, getMessage(key, replacements));
    }
    public static void consoleLog(java.util.logging.Level level, MessageKey key){
        consoleLog(level, getMessageString(key));
    }
    public static void consoleLog(java.util.logging.Level level, String message){
        String loggerName = getMessageString(MessageKey.LOGGER_NAME);
        if (loggerName == null
          || loggerName.length() == 0
          || loggerName.equals(MessageKey.LOGGER_NAME.getMessageKey())){
            loggerName = DEFAULT_LOGGER_NAME;
        }
        
        message = message.replaceAll("[§&]([0-9a-fmnol])", "");
        
        Logger.getLogger(loggerName).log(level, message);
    }

    public static void consoleInfo(MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleInfo(key, fullReplacements);
    }
    public static void consoleLog(MessageKey key, String[] replacements){
        consoleInfo(getMessage(key, replacements));
    }
    public static void consoleLog(MessageKey key){
        consoleInfo(getMessageString(key));
    }
    public static void consoleLog(String message){
        consoleLog(java.util.logging.Level.INFO, message);
    }

    public static void consoleWarn(MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleWarn(key, fullReplacements);
    }
    public static void consoleWarn(MessageKey key, String[] replacements){
        consoleWarn(getMessage(key, replacements));
    }
    public static void consoleWarn(MessageKey key){
        consoleWarn(getMessageString(key));
    }
    public static void consoleWarn(String message){
        consoleLog(java.util.logging.Level.WARNING, message);
    }

    public static void consoleError(MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleError(key, fullReplacements);
    }
    public static void consoleError(MessageKey key, String[] replacements){
        consoleError(getMessage(key, replacements));
    }
    public static void consoleError(MessageKey key){
        consoleError(getMessageString(key));
    }
    public static void consoleError(String message){
        consoleLog(java.util.logging.Level.SEVERE, message);
    }

}