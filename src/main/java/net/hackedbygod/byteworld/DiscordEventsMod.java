package net.hackedbygod.byteworld;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordEventsMod implements DedicatedServerModInitializer {
    // Create a logger instance to use to write text to the console and the log file.
    public static final Logger LOGGER = LoggerFactory.getLogger(Common.ModID);
    public static Config CONFIG = Config.LoadConfig();

    @Override
    public void onInitializeServer() {
        if(CONFIG.isLoaded) {
            LOGGER.info("ByteWorld Discord Events Mod loading : v" + Common.Version);

            //Register for Allow Death event and call in to our class PlayerEvents.
            ServerPlayerEvents.ALLOW_DEATH.register((player, damageSource, damageAmount) -> {
                PlayerEvents.onPlayerDeath(player, damageSource, damageAmount);
                return true;
            });
        }
    }
}
