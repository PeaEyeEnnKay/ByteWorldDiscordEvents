package net.hackedbygod.byteworld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Config {
    private String DiscordBotName = "";
    private String DiscordWebHookUrl = "";
    boolean isLoaded = false;
    static String DefaultDiscordWebHookUrl = "https://discordapp.com/api/webhooks/YourDiscordWebHookURLGoesHere";
    static String DefaultBotName = "DefaultBotName";

    public void setDiscordBotName(String DiscordBotName) {
        this.DiscordBotName = DiscordBotName;
    }

    public String getDiscordBotName() {
        return this.DiscordBotName;
    }

    public void setDiscordWebHookUrl(String DiscordWebHookUrl) {
        this.DiscordWebHookUrl = DiscordWebHookUrl;
    }

    public String getDiscordWebHookUrl() {
        return this.DiscordWebHookUrl;
    }

    public static Config LoadConfig() {
        Gson gson = new Gson();
        Config config = null;
        File configurationFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), Common.ModID + ".json");
        if (!configurationFile.exists()) {
            config = writeConfig(configurationFile);
        }
        try {
            if (configurationFile.exists()) {
                BufferedReader bReader = new BufferedReader(new FileReader(configurationFile));

                config = gson.fromJson(bReader, Config.class);
                if (!config.DiscordWebHookUrl.equals(DefaultDiscordWebHookUrl)) {
                    config.isLoaded = true;
                } else {
                    PlayerEvents.LOGGER.warn("Configuration for " + Common.ModID + " has not been edited. Discord messages will not work!");
                    PlayerEvents.LOGGER.warn("Edit \"" + configurationFile.getAbsoluteFile() + "\" and restart the server.");
                }
            }
        } catch (FileNotFoundException e) {
            PlayerEvents.LOGGER.error("Could not load " + Common.ModID + " config. Discord messages will not work!", e);
            PlayerEvents.LOGGER.warn("Check \"" + configurationFile.getAbsoluteFile() + "\" exists.");
        }

        return config;
    }

    private static Config writeConfig(File configurationFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Config newConfig = new Config();
        newConfig.DiscordWebHookUrl = DefaultDiscordWebHookUrl;
        newConfig.DiscordBotName = DefaultBotName;
        String configJSON = gson.toJson(newConfig);
        try (FileWriter fileWriter = new FileWriter(configurationFile.getAbsoluteFile())) {
            fileWriter.write(configJSON);
            PlayerEvents.LOGGER.info("Writing default " + Common.ModID + " configuration. Edit \"" + configurationFile.getAbsoluteFile() + "\"");
        } catch (IOException e) {
            PlayerEvents.LOGGER.error("Couldn't save " + Common.ModID + " config.", e);
        }
        return newConfig;
    }
}
