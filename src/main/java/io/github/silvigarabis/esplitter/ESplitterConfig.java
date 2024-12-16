package io.github.silvigarabis.esplitter;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;

public class ESplitterConfig {
    protected static final Configuration DEFAULT_CONFIGURATIONS = new MemoryConfiguration();
    public static final Configuration CONFIGURATIONS = null;

    static {
        DEFAULT_CONFIGURATIONS.set("config-version", 1);
    }

    private String key;
    public String getKey() {
        return key;
    }

    private ESplitterConfig(String key) {
        this.key = key;
    }
    public static final String SPLIT_NEED_EMPTY_BOOK = "split-enchant.need-empty-book";
    public static final String CONFIG_VERSION = "config-version";

    private ESplitterConfig(){}
}
