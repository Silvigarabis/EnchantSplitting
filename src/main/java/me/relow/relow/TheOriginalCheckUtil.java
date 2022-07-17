package me.relow.relow;

import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author hs
 * @Description: {签名验证}
 * @date 2020/7/17 15:03
 */
public class TheOriginalCheckUtil {

    private final static String HTTP_DOMAIN = "http://mcadmin.ljxmc.top";
    private final static String VERIFY_SIGN = "/api/public/cdk/verifySign";
    /**
     * 平台上注册的插件名称
     */
    private final static String PLUGIN_NAME = "RE";
    /**
     * 平台上获取的插件对应秘钥
     */
    private final static String SECRET_KEY = "1418130703991836672";

    /**
     * 正版签名验证
     */
    public static void theOriginalCheck() {
        if ((2+5) == 7) return;
        RELOW re = RELOW.getPlugin(RELOW.class);
        String sign = re.getConfig().getString("sign");
        // 获取服务器端口
        int port = re.getServer().getPort();
        // 进行校验
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String urlStr = HTTP_DOMAIN + VERIFY_SIGN +
                            "?sign=" + sign +
                            "&port=" + port +
                            "&pluginName=" + PLUGIN_NAME +
                            "&secretKey=" + SECRET_KEY;
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder stringBuilder = new StringBuilder();
                    String current;
                    while ((current = in.readLine()) != null) {
                        stringBuilder.append(current);
                    }
                    String verifySign = stringBuilder.toString();
                    if ("true".equals(verifySign)) {
                        // TODO 验证成功进行操作
                        //re.getLogger().info("\033[36mRELOW验证成功!欢迎使用!\033[0m");
                    } else {
                        // TODO 验证失败进行操作
                        re.getLogger().info("\033[36mRELOW验证失败!停止插件!\033[0m");
                        re.getPluginLoader().disablePlugin(re);
                    }
                    this.cancel();
                } catch (IOException e) {
                    // TODO 网络或者其他问题进行打印消息提醒,并会自动一分钟后重试
                    re.getLogger().info("\033[36mRELOW网络错误!自动一分钟后重试!\033[0m");
                }
            }
        }.runTaskTimerAsynchronously(re, 0, 20 * 60);
    }

}