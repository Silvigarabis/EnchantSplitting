package me.relow.relow;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class RelogLogger {
  private static Logger logger;
  public static void init(JavaPlugin plugin){
    logger = plugin.getLogger();
  }
  public static Logger getLogger(){
    return logger;
  }
}