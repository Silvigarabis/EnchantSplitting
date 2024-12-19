package io.github.silvigarabis.esplitter;

import java.text.MessageFormat;

import org.bukkit.entity.Player;

public class Messages {
    private static Messages msg(String text, String key){
        return new Messages(text, key);
    }

    public static Messages noPerm =
            msg("没有权限", "no permission");
    public static Messages noPlayer =
            msg("缺少玩家", "no player");
    public static Messages noPlayerOnConsole =
            msg("在控制台中必须指定玩家", "no player on console");
    public static Messages unknownCommand =
            msg("未知的命令格式", "unknown command");
    public static Messages invGuiStatusModeSplit =
            msg("分离模式", "inv.gui.status.split");
    public static Messages invGuiStatusModeGrind =
            msg("除魔模式", "inv.gui.status.grind");
    public static Messages invGuiButtonPgUp =
            msg("上一页", "inv.gui.button.pgup");
    public static Messages invGuiButtonPgDown =
            msg("下一页", "inv.gui.button.pgdown");
    public static Messages invGuiButtonCancel =
            msg("取消", "inv.gui.button.cancel");

    private final String text;
    private final String key;

    public String getMessage(Object... places) {
        return MessageFormat.format(this.text, (Object[]) places);
    }

    public String getMessagePlayer(Player player, Object... places) {
        return getMessage(places);
    }
    public String getKey() {
        return key;
    }

    private Messages(String text, String key) {
        this.text = text;
        this.key = key;
    }
}
