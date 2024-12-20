package io.github.silvigarabis.esplitter;

import java.text.MessageFormat;

import org.bukkit.entity.Player;

public class Messages {
    private static Messages msg(String text, String key){
        return new Messages(text, key);
    }

    // @formatter:off
    public static final Messages noPerm =
        msg("没有权限", "no permission");
    public static final Messages noPlayer =
        msg("缺少玩家", "no player");
    public static final Messages noPlayerOnConsole =
        msg("在控制台中必须指定玩家", "no player on console");
    public static final Messages unknownCommand =
        msg("未知的命令格式", "unknown command");

    public static final Messages invGuiTitle =
        msg("附魔分离控制台", "inv.gui.title");
    public static final Messages invGuiStatusModeSplit =
        msg("分离模式", "inv.gui.status.split");
    public static final Messages invGuiStatusModeGrind =
        msg("除魔模式", "inv.gui.status.grind");
    public static final Messages invGuiStatusModeUnknown =
        msg("未知模式", "inv.gui.status.unknown");
    public static final Messages invGuiButtonPgUp =
        msg("上一页", "inv.gui.button.pgup");
    public static final Messages invGuiButtonPgDown =
        msg("下一页", "inv.gui.button.pgdown");
    public static final Messages invGuiButtonComplete =
        msg("完成", "inv.gui.button.complete");
    public static final Messages invGuiButtonCancel =
        msg("取消", "inv.gui.button.cancel");
    public static final Messages invGuiErrorClose =
        msg("插件出现了未知错误，请联系管理员", "inv.gui.error.close");
    public static final Messages invGuiElementTitle =
        msg("共 %d 个附魔", "inv.gui.element.title");

    public static final Messages consumpsionExperienceLevels =
        msg("需要经验等级 x %d", "consumption.experience.levels");
    public static final Messages consumpsionExperiencePoints =
        msg("需要经验点数 x %d", "consumption.experience.points");
    public static final Messages consumpsionItem =
        msg("需要 %d 个 %s", "consumption.item");
    public static final Messages consumpsionReturnItem =
        msg("返还物品：%s x %d", "consumption.return.item");
    public static final Messages consumpsionNone =
        msg("无额外消耗", "consumption.none");

    // @formatter:on

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
        return MessageFormat.format(this.text, (Object[]) places);
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
}
