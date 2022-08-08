package net.hackedbygod.byteworld;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PlayerEvents {
    public static final Logger LOGGER = LoggerFactory.getLogger(Common.ModID);

    public static void onPlayerDeath(ServerPlayerEntity player, DamageSource damageSource, float damageAmount) {
        List<Text> deathMessage = damageSource.getDeathMessage(player).withoutStyle();
        Vec3d location = player.getPos();
        StringBuilder playerDeathMessage = new StringBuilder();
        for (Text temp : deathMessage) {
            playerDeathMessage.append(temp.getString());
        }
        playerDeathMessage.append(" at ");
        playerDeathMessage.append(Math.round(location.x));
        playerDeathMessage.append(", ");
        playerDeathMessage.append(Math.round(location.y));
        playerDeathMessage.append(", ");
        playerDeathMessage.append(Math.round(location.z));
        sendDeathMessageToDiscord(playerDeathMessage.toString());
    }

    private static void sendDeathMessageToDiscord(String playerDeathMessage) {

        String discordHookURL = DiscordEventsMod.CONFIG.getDiscordWebHookUrl();
        String bot_name = DiscordEventsMod.CONFIG.getDiscordBotName();

        HttpURLConnection connection = null;
        String msg_string = "{\"username\":\"" + bot_name + "\",\"content\":\"" + playerDeathMessage + "\"}";

        String messagePayload = "payload_json=" + URLEncoder.encode(msg_string);
        try {
            URL url = new URL(discordHookURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 ");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send http post to discord
            try(OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = messagePayload.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            //Get the server response.
            //TODO: We should probably provide some way to pass this back to the console for end user debugging purposes.
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            bufferedReader.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }
}