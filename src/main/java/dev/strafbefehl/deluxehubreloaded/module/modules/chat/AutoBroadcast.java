package dev.strafbefehl.deluxehubreloaded.module.modules.chat;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.config.ConfigType;
import dev.strafbefehl.deluxehubreloaded.module.Module;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import dev.strafbefehl.deluxehubreloaded.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoBroadcast extends Module implements Runnable {

	private Map<Integer, List<String>> broadcasts;
	private int broadcastTask = 0;
	private int count = 0;
	private int size = 0;
	private int requiredPlayers = 0;

	private Sound sound = null;
	private double volume;
	private double pitch;

	public AutoBroadcast(DeluxeHubPlugin plugin) {
		super(plugin, ModuleType.ANNOUNCEMENTS);
	}

	@Override
	public void onEnable() {
		FileConfiguration config = getConfig(ConfigType.SETTINGS);

		broadcasts = new HashMap<>();
		int count = 0;
		for (String key : config.getConfigurationSection("announcements.announcements").getKeys(false)) {
			broadcasts.put(count, config.getStringList("announcements.announcements." + key));
			count++;
		}

		if (config.getBoolean("announcements.sound.enabled")) {
			try {
				sound = Registry.SOUNDS.get(NamespacedKey.minecraft(config.getString("announcements.sound.value")));
			}catch(Exception ex){
				Bukkit.getLogger().warning("[DeluxeHub] Invalid sound name: " + config.getString("announcements.sound.value")+". Defaulting to block.note_block.pling.");
				sound = Sound.BLOCK_NOTE_BLOCK_PLING;
			}
			volume = config.getDouble("announcements.sound.volume");
			pitch = config.getDouble("announcements.sound.pitch");
		}

		requiredPlayers = config.getInt("announcements.required_players", 0);

		size = broadcasts.size();
		if (size > 0)
			broadcastTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), this, 60L, config.getInt("announcements.delay") * 20L);
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTask(broadcastTask);
	}

	@Override
	public void run() {
		if (count == size) count = 0;

		if (count < size && Bukkit.getOnlinePlayers().size() >= requiredPlayers) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (inDisabledWorld(player.getLocation())) continue;

				broadcasts.get(count).forEach(message -> {
					if (message.contains("<center>") && message.contains("</center>"))
						message = TextUtil.getCenteredMessage(message);
					player.sendMessage(TextUtil.color(message));
				});

				if (sound != null) player.playSound(player.getLocation(), sound, (float) volume, (float) pitch);
			}
			count++;
		}

	}
}
