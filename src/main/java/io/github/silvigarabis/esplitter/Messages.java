package io.github.silvigarabis.esplitter;

import java.text.MessageFormat;

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

    private final String text;
    private final String key;

    public String getMessage(Object... places) {
        return MessageFormat.format(this.text, (Object[]) places);
    }

    public String getKey() {
        return key;
    }

    private Messages(String text, String key) {
        this.text = text;
        this.key = key;
    }
}
