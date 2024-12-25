package io.github.silvigarabis.esplitter;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.List;
import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public enum Messages {

    noPerm("没有权限", "no permission"),
    noPlayer("缺少玩家", "no player"),
    noPlayerOnConsole("在控制台中必须指定玩家", "no player on console"),
    unknownCommand("未知的命令格式", "unknown command"),

    invGuiTitle("附魔分离控制台", "inv.gui.title"),
    invGuiStatusModeSplit("分离模式", "inv.gui.status.split"),
    invGuiStatusModeGrind("除魔模式", "inv.gui.status.grind"),
    invGuiStatusModeUnknown("未知模式", "inv.gui.status.unknown"),
    invGuiButtonPgUp("上一页", "inv.gui.button.pgup"),
    invGuiButtonPgDown("下一页", "inv.gui.button.pgdown"),
    invGuiButtonComplete("完成", "inv.gui.button.complete"),
    invGuiButtonCancel("取消", "inv.gui.button.cancel"),
    invGuiErrorClose("插件出现了未知错误，请联系管理员", "inv.gui.error.close"),
    invGuiElementTitle("共 %d 个附魔", "inv.gui.element.title"),

    consumpsionExperienceLevels("需要经验等级 x %d", "consumption.experience.levels"),
    consumpsionExperiencePoints("需要经验点数 x %d", "consumption.experience.points"),
    consumpsionItem("需要 %d 个 %s", "consumption.item"),
    consumpsionReturnItem("返还物品：%s x %d", "consumption.return.item"),
    consumpsionNone("无额外消耗", "consumption.none");

    public static enum MessageKey {
        COMMAND_NO_PERMISSION("command-no-permission"),
        COMMAND_UNKNOWN("command-unknown"),
        COMMAND_RESOLVING("command-resolving"),

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
        GUI_UNEXPECTED_EVENT_ERROR_CLOSE_ALL("gui.unexpected-event-error-close-all"),
        GUI_UNEXPECTED_EVENT_ERROR("gui.unexpected-event-error"),
        GUI_UNEXPECTED_CLOSE("gui.unexpected-error"),

        INVALID_PLUGIN_CONFIG("console-message.invalid-plugin-config", "配置文件错误！尝试在修正后使用/esplitter reload重新加载"),
        PLUGIN_LOADING("console-message.plugin-loading", "插件正在加载"),
        PLUGIN_ENABLED("console-message.plugin-enabled", "插件已加载完毕"),
        PLUGIN_DISABLED("console-message.plugin-disabled", "插件已退出"),
        PLUGIN_ERROR_DOUBLE_LOAD("console-message.plugin-load-twice-error", "错误！插件被启用了两次！"),
        PLUGIN_RELOADING_CONFIG("console-message.plugin-config-reloading", "正在加载配置"),
        PLUGIN_CONFIG_LOADED("console-message.plugin-config-loaded", "消息文本已加载"),
        PLUGIN_CONFIG_LOAD_ERROR("console-message.plugin-config-load-fail", "配置加载出现错误"),
        PLUGIN_RELOADING_MESSAGE_CONFIG("console-message.plugin-message-config-reloading", "正在加载消息文本"),
        PLUGIN_MESSAGE_CONFIG_LOADED("console-message.plugin-message-config-loaded", "消息文本已加载"),
        PLUGIN_MESSAGE_CONFIG_LOAD_MISSING("console-message.plugin-message-config-missing", "缺失 {} 条消息文本： {}"),

        CHAT_PREFIX("chat-prefix", "[ESplitter]"),
        LOGGER_NAME("logger-name", "ESplitter");

        private String messageKey = null;
        public String getMessageKey(){
            return messageKey;
        }

        private String defaultString;
        public String getDefaultString(){
            return defaultString;
        }

        private MessageKey(String messageKey){
            this.messageKey = messageKey;
        }
        private MessageKey(String messageKey, String defaultString){
            this.messageKey = messageKey;
            this.defaultString = defaultString;
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

    private final String text;
    private final String key;

    public String getMessage(Object... places) {
        // not fully implements
        return "[ESplitter] " + getText(places);
    }

    public String getMessagePlayer(Player player, Object... places) {
        // not fully implements
        return "[ESplitter] " + getPlayerText(player, places);
    }

    public String getText(Object... places) {
        return String.format(this.text, (Object[]) places);
    }

    public String getPlayerText(Player player, Object... places) {
        // not fully implements
        return getText(places);
    }

    public String getKey() {
        return key;
    }

    private Messages(String text, String key) {
        this.text = text;
        this.key = key;
    }

    private static String DEFAULT_LOGGER_NAME = "ESplitter";
    private static Map<MessageKey, String> messages = new EnumMap<MessageKey, String>(MessageKey.class);

    public static List<MessageKey> getMissingMessageKeys() {
        List<MessageKey> missingKeys = new ArrayList<>();
        for (MessageKey key : MessageKey.values()) {
            if (!messages.containsKey(key)) {
                missingKeys.add(key);
            }
        }
        return missingKeys;
    }

    public static void cleanMessageConfig() {
        messages.clear();
    }

    public static void loadMessageConfig(ConfigurationSection messageConfig) {
        for (Object messageKeyObject : MessageKey.values()) {
            MessageKey messageKey = (MessageKey) messageKeyObject;
            String key = messageKey.getMessageKey();
            String messageString = messageConfig.getString(key);
            if (messageString == null) {
                continue;
            }
            messages.put(messageKey, messageString);
        }
    }

    public static void loadMessageConfig(File yamlConfigFile) {
        var messageConfig = YamlConfiguration.loadConfiguration(yamlConfigFile);
        loadMessageConfig(messageConfig);
    }

    public static String getMessageString(MessageKey messageKey, boolean translateColorCode) {
        var string = messages.get(messageKey);
        if (string == null || string.length() == 0) {
            string = messageKey.getDefaultString();
        }

        if (string == null || string.length() == 0) {
            string = messageKey.getMessageKey();
        } else if (translateColorCode) {
            string = string.replaceAll("&([0-9a-fmnol])", "§$1");
            string = string.replaceAll("&&", "&");
        }
        return string;
    }

    public static String getMessageString(MessageKey messageKey) {
        return getMessageString(messageKey, true);
    }

    public static String getMessage(MessageKey key, String[] replacements) {
        var messageString = getMessageString(key);
        for (var replacement : replacements) {
            messageString = messageString.replaceFirst("\\{\\}", Matcher.quoteReplacement(replacement));
        }
        return messageString;
    }

    public static String getMessage(MessageKey key) {
        return getMessageString(key);
    }

    public static String getMessage(MessageKey key, String replacement, String... replacements) {
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++) {
            fullReplacements[idx] = replacements[idx - 1];
        }
        return getMessage(key, fullReplacements);
    }

    public static String getMessage(Player player, MessageKey key, String[] replacements) {
        return getMessage(key, replacements);
    }

    public static String getMessage(Player player, MessageKey key) {
        return getMessage(key);
    }

    public static String getMessage(Player player, MessageKey key, String replacement, String... replacements) {
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++) {
            fullReplacements[idx] = replacements[idx - 1];
        }
        return getMessage(player, key, fullReplacements);
    }

    public static void send(CommandSender sender, MessageKey key, String replacement, String... replacements) {
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++) {
            fullReplacements[idx] = replacements[idx - 1];
        }
        send(sender, key, fullReplacements);
    }

    public static void send(CommandSender sender, MessageKey key, String[] replacements) {
        send(sender, getMessage(key, replacements));
    }

    public static void send(CommandSender sender, MessageKey key) {
        send(sender, getMessageString(key));
    }

    public static void send(CommandSender sender, String message) {
        for (var line : message.split("\n")) {
            line = getMessageString(MessageKey.CHAT_PREFIX) + " " + line;
            sender.sendMessage(line);
        }
    }

    public static void consoleLog(java.util.logging.Level level, MessageKey key, String replacement,
            String... replacements) {
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++) {
            fullReplacements[idx] = replacements[idx - 1];
        }
        consoleLog(level, key, fullReplacements);
    }

    public static void consoleLog(java.util.logging.Level level, MessageKey key, String[] replacements) {
        consoleLog(level, getMessage(key, replacements));
    }

    public static void consoleLog(java.util.logging.Level level, MessageKey key) {
        consoleLog(level, getMessageString(key));
    }

    public static void consoleLog(java.util.logging.Level level, String message) {
        String loggerName = getMessageString(MessageKey.LOGGER_NAME);
        if (loggerName == null
                || loggerName.length() == 0
                || loggerName.equals(MessageKey.LOGGER_NAME.getMessageKey())) {
            loggerName = DEFAULT_LOGGER_NAME;
        }
        message = message.replaceAll("[§&]([0-9a-fmnol])", "");
        var logger = Logger.getLogger(loggerName);
        for (var line : message.split("\n")) {
            logger.log(level, line);
        }
    }

    public static void consoleInfo(MessageKey key, String replacement, String... replacements) {
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++) {
            fullReplacements[idx] = replacements[idx - 1];
        }
        consoleInfo(key, fullReplacements);
    }

    public static void consoleInfo(MessageKey key, String[] replacements) {
        consoleInfo(getMessage(key, replacements));
    }

    public static void consoleInfo(MessageKey key) {
        consoleInfo(getMessageString(key));
    }

    public static void consoleInfo(String message) {
        consoleLog(java.util.logging.Level.INFO, message);
    }

    public static void consoleWarn(MessageKey key, String replacement, String... replacements) {
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++) {
            fullReplacements[idx] = replacements[idx - 1];
        }
        consoleWarn(key, fullReplacements);
    }

    public static void consoleWarn(MessageKey key, String[] replacements) {
        consoleWarn(getMessage(key, replacements));
    }

    public static void consoleWarn(MessageKey key) {
        consoleWarn(getMessageString(key));
    }

    public static void consoleWarn(String message) {
        consoleLog(java.util.logging.Level.WARNING, message);
    }

    public static void consoleError(MessageKey key, String replacement, String... replacements) {
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++) {
            fullReplacements[idx] = replacements[idx - 1];
        }
        consoleError(key, fullReplacements);
    }

    public static void consoleError(MessageKey key, String[] replacements) {
        consoleError(getMessage(key, replacements));
    }

    public static void consoleError(MessageKey key) {
        consoleError(getMessageString(key));
    }

    public static void consoleError(String message) {
        consoleLog(java.util.logging.Level.SEVERE, message);
    }

}
