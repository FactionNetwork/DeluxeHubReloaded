package dev.strafbefehl.deluxehubreloaded.utility;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.strafbefehl.deluxehubreloaded.Permissions;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

	private static final Permission UPDATE_PERM = new Permission(Permissions.UPDATE_NOTIFICATION.getPermission(), PermissionDefault.FALSE);
	private static final long CHECK_INTERVAL = 12_000;
	private final JavaPlugin plugin;
	private final String localPluginVersion;
	private final Gson gson = new Gson();

	public UpdateChecker(JavaPlugin plugin) {
		this.plugin = plugin;
		this.localPluginVersion = "v" + plugin.getDescription().getVersion();
	}

	public void checkForUpdate() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
					String raw;
					try {
						final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.github.com/repos/Strafbefehl/DeluxeHubReloaded/releases").openConnection();
						connection.setRequestMethod("GET");
						raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
					} catch (IOException e) {
						cancel();
						return;
					}
					JsonArray ghData = gson.fromJson(raw, JsonArray.class);
					JsonObject latest = ghData.get(0).getAsJsonObject();

					final String version = latest.get("tag_name").getAsString();
					final String URL = latest.get("html_url").getAsString();

					if (localPluginVersion.equals(version)) return;

					if (localPluginVersion.contains("-dev")) {
						plugin.getLogger().warning("This is DEV version of DeluxeHubReloaded plugin.");
						plugin.getLogger().warning("You can download stable version at:");
						plugin.getLogger().warning(URL);
					} else {
						plugin.getLogger().warning("An update for DeluxeHubReloaded (%VERSION%) is available at:".replace("%VERSION%", version));
						plugin.getLogger().warning(URL);
					}

					Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().registerEvents(new Listener() {
						@EventHandler(priority = EventPriority.MONITOR)
						public void onPlayerJoin(final PlayerJoinEvent event) {
							final Player player = event.getPlayer();
							if (!player.hasPermission(UPDATE_PERM)) return;

							new BukkitRunnable() {
								@Override
								public void run() {
									if (localPluginVersion.contains("-dev")) {
										player.sendMessage(TextUtil.color("&7This is DEV version of DeluxeHubReloaded plugin."));
										TextComponent downloadMsg = new TextComponent(TextUtil.color("&a> You can download stable version here! <"));
										downloadMsg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, URL));
										player.spigot().sendMessage(downloadMsg);
									} else {
										player.sendMessage(TextUtil.color("&7An update (" + version + ") for DeluxeHubReloaded is available."));
										TextComponent downloadMessage = new TextComponent(TextUtil.color("&a[Download]"));
										downloadMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, URL));

										TextComponent spacer = new TextComponent(TextUtil.color(" &7| "));

										TextComponent changelogMessage = new TextComponent(TextUtil.color("&6[Changelog]"));
										changelogMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/uQkg8ZeHzK"));

										downloadMessage.addExtra(spacer);
										downloadMessage.addExtra(changelogMessage);

										player.spigot().sendMessage(downloadMessage);
									}
								}
							}.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds
						}
					}, plugin));

					cancel();
				});
			}
		}.runTaskTimer(plugin, 0, CHECK_INTERVAL);
	}
}
