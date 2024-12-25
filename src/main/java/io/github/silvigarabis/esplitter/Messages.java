package io.github.silvigarabis.esplitter;

import java.util.EnumMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public enum Messages {

    commandArgMissingPlayer("缺少玩家", "no player"),
    commandArgMissingPlayerOnConsole("在控制台中必须指定玩家", "no player on console"),
    commandErrorNoPerm("没有权限", "no permission"),
    commandErrorUnknown("未知的命令格式", "unknown command"),

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

    private final String text;
    private final String key;

    public String getMessage(Object... places) {
        // not fully implements
        return "[ESplitter] " + getText(places);
    }

    public String getPlayerMessage(Player player, Object... places) {
        // not fully implements
        return "[ESplitter] " + getPlayerText(player, places);
    }

    public String getConsoleMessage(Object... places) {
        return "[ESplitter] " + getConsoleText(places);
    }

    public String getText(Object... places) {
        // copy raw text
        String text = this.text;

        // transform color code
        text = text.replaceAll("&([0-9a-fmnol])", "§$1");
        text = text.replaceAll("&&", "&");

        // replace placeholders
        text = String.format(text, (Object[]) places);

        return text;
    }

    public String getPlayerText(Player player, Object... places) {
        // not fully implements
        return getText(places);
    }

    public String getConsoleText(Object... places) {
        // return text with color codes removed.
        return getText(places).replaceAll("[§&]([0-9a-fmnol])", "");
    }

    public String getKey() {
        return key;
    }

    private Messages(String text, String key) {
        this.text = text;
        this.key = key;
    }

    private static final Map<Messages, String> messages = new EnumMap<Messages, String>(Messages.class);

    static final List<Messages> findMessagesMissing(){
        List<Messages> missingKeys = new ArrayList<>(Arrays.asList(Messages.values()));
        missingKeys.removeAll(messages.keySet());
        return missingKeys;
    }

    static final void cleanMessagesConfig() {
        messages.clear();
    }

    static final void loadMessagesConfig(ConfigurationSection messageConfig) {
        for (Messages message : Messages.values()){
            String msgKey = message.getKey();
            String msgText = messageConfig.getString(msgKey);
            if (msgText == null) {
                continue;
            }
            messages.put(message, msgText);
        }
    }
}
