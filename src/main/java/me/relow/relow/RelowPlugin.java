package me.relow.relow;

import me.relow.relow.command.MainCommand;
import me.relow.relow.listener.InventoryGuiListener;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.logging.Logger;
import java.io.File;

public final class RelowPlugin extends JavaPlugin {

    private Logger logger = Logger.getLogger("Relow");
    
    private File groupConfigFile = new File(getDataFolder(), "group.yml");
    private File messageConfigFile = new File(getDataFolder(), "message.yml");
    
    private FileConfiguration config;
    private FileConfiguration groupConfig;
    private FileConfiguration messageConfig;
    
    private Economy economy = null;
    
    private boolean hasPlayerPoints;
    private boolean hasEconomy;
    
    private InventoryGuiListener relowInventoryListener;
    private MainCommand relowCommandExecutor;
    
    @Override
    public void onEnable() {
        
        this.logger = this.getLogger();
        
        logger.info("====================");
        logger.info("R E L O W");
        logger.info("源代码: https://github.com/Silvigarabis/relow");
        logger.info("====================");
        
        loadSoftDepends();
        saveDefaultConfig();
        reloadConfig();
        
        Config.load(this);
        Group.load(this);
        Messager.load(this);
        
        this.relowInventoryListener = new InventoryGuiListener(this);
        Bukkit.getPluginManager().registerEvents(this.relowInventoryListener, this);
        
        relowCommandExecutor = new MainCommand(this);
        getCommand("relow").setExecutor(relowCommandExecutor);
        
        logger.info("插件已加载");
        
    }
    
    @Override
    public void onDisable() {
    
        GroupManager.removeAllGroups();
        
        relowInventoryListener.setDisabled();
        
        relowCommandExecutor.setDisabled();
        
        logger.info("插件已禁用");
        
    }

    @Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
        if (!messageConfigFile.exists()) {
            saveResource("group.yml", false);
        }
        if (!groupConfigFile.exists()) {
            saveResource("group.yml", false);
        }
    }
    
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.config = getConfig();
        this.groupConfig = YamlConfiguration.loadConfiguration(groupConfigFile);
        this.messageConfig = YamlConfiguration.loadConfiguration(messageConfigFile);
        Group.reload();
        Message.reload();
    }

    public Economy getEconomy(){
        return economy;
    }
    
    private void loadSoftDepends(){
    
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")){
            this.hasPlayerPoints = true;
            logger.info("已找到PlayerPoints");
        } else {
            this.hasPlayerPoints = false;
        }

        this.hasEconomy = setupEconomy();
        if (this.hasEconomy)
            logger.info("已找到Vault");
        else
            logger.info("未找到Vault");
        
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.economy = rsp.getProvider();
        return this.economy != null;
    }
    
    public boolean executeReload(CommandSender sender){
        reloadConfig();
        return true;
    }
    
    public FileConfiguration getConfig(){
        return this.config;
    }

    public FileConfiguration getGroupConfig(){
        return this.groupConfig;
    }
    
    public FileConfiguration getMessageConfig(){
        return this.messageConfig;
    }

}
