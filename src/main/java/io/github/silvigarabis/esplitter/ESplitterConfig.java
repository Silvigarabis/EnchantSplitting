package io.github.silvigarabis.esplitter;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;

public class ESplitterConfig {
    protected static final Configuration DEFAULT_CONFIGURATIONS = new MemoryConfiguration();

    static {
        DEFAULT_CONFIGURATIONS.set("config-version", (int) 1);
    }

    public static final String SPLIT_NEED_EMPTY_BOOK = "split-enchant.need-empty-book";
    public static final String CONFIG_VERSION = "config-version";

}
