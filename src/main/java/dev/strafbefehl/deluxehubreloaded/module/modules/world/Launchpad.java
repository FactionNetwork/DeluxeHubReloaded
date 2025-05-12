package dev.strafbefehl.deluxehubreloaded.module.modules.world;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.config.ConfigType;
import dev.strafbefehl.deluxehubreloaded.cooldown.CooldownType;
import dev.strafbefehl.deluxehubreloaded.module.Module;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class Launchpad extends Module {

	private double launch;
	private double launchY;
	private List<String> actions;
	private Material topBlock;
	private Material bottomBlock;

	public Launchpad(DeluxeHubPlugin plugin) {
		super(plugin, ModuleType.LAUNCHPAD);
	}

	@Override
	public void onEnable() {
		FileConfiguration config = getConfig(ConfigType.SETTINGS);
		launch = config.getDouble("launchpad.launch_power", 1.3);
		launchY = config.getDouble("launchpad.launch_power_y", 1.2);
		actions = config.getStringList("launchpad.actions");

		try {
			topBlock = Material.valueOf(config.getString("launchpad.top_block").toUpperCase());
		} catch (IllegalArgumentException e) {
			// Fallback to a default material if invalid
			topBlock = Material.STONE_PRESSURE_PLATE;
			getPlugin().getLogger().warning("Invalid top block material in config. Defaulting to STONE_PRESSURE_PLATE");
		}

		try {
			bottomBlock = Material.valueOf(config.getString("launchpad.bottom_block").toUpperCase());
		} catch (IllegalArgumentException e) {
			// Fallback to a default material if invalid
			bottomBlock = Material.REDSTONE_BLOCK;
			getPlugin().getLogger().warning("Invalid bottom block material in config. Defaulting to REDSTONE_BLOCK");
		}

		if (launch > 4.0) launch = 4.0;
		if (launchY > 4.0) launchY = 4.0;
	}

	@Override
	public void onDisable() {
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		if (inDisabledWorld(location)) return;

		// Check for launchpad block
		if (location.getBlock().getType() == topBlock && location.subtract(0, 1, 0).getBlock().getType() == bottomBlock) {

			// Check for cooldown
			if (tryCooldown(player.getUniqueId(), CooldownType.LAUNCHPAD, 1)) {
				player.setVelocity(location.getDirection().multiply(launch).setY(launchY));
				executeActions(player, actions);
			}
		}
	}

}
