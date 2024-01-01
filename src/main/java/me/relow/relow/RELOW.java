package me.relow.relow;

import me.relow.relow.RelowLogger;
import me.relow.relow.command.help;
import me.relow.relow.command.openRE;
import me.relow.relow.command.reload;
import me.relow.relow.event.click;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import java.io.File;

public final class RELOW extends JavaPlugin {
    private Logger logger;
    
    private static File pluginFile;
    public static File getPluginFile(){
        return pluginFile;
    }
    private static Economy econ = null;

    @Override
    public void onEnable() {
        RelowLogger.init(this);
        this.logger = RelowLogger.getLogger();
        
        if(getServer().getPluginManager().getPlugin("PlayerPoints") == null){
            logger.info("未找到点卷插件！");
        }else {
            if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
                logger.info("已找到PlayerPoints插件！");
            }
        }

        // Plugin startup logic
        if (!setupEconomy()) {
            logger.info("未找到经济插件！");
        }else {
            logger.info("已找到Vault！");
        }

        logger.info("\033[36m= = = = = = = = = = = = = = = = = = = = = = \033[0m");
        logger.info("RE-LOW");
        logger.info("作者:StrawberryYu || QQ:2332742172");
        logger.info("此版本为修改版本，你可以在 https://github.com/Imeaces/EnchantmentSplitter/tree/archive/fixed-origin-relow 找到修改后的源代码");
        logger.info("主要是移除了付费验证以及让日志输出不要那么奇怪");
        logger.info("其他没有修复任何东西，因为我看不懂代码，选择重制插件");
        logger.info("\033[36m= = = = = = = = = = = = = = = = = = = = = = \033[0m");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        pluginFile = this.getDataFolder();
        Config.loadConfig();

        getServer().getPluginManager().registerEvents(new click(),this);
        getCommand("rel-open").setExecutor(new openRE());
        getCommand("rel-reload").setExecutor(new reload());
        getCommand("rel").setExecutor(new help());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
