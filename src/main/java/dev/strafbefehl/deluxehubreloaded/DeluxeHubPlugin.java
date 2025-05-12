package dev.strafbefehl.deluxehubreloaded;

import cl.bgmp.minecraft.util.commands.exceptions.*;
import dev.strafbefehl.deluxehubreloaded.action.ActionManager;
import dev.strafbefehl.deluxehubreloaded.base.BuildMode;
import dev.strafbefehl.deluxehubreloaded.command.CommandManager;
import dev.strafbefehl.deluxehubreloaded.config.ConfigManager;
import dev.strafbefehl.deluxehubreloaded.config.ConfigType;
import dev.strafbefehl.deluxehubreloaded.config.Messages;
import dev.strafbefehl.deluxehubreloaded.config.Version;
import dev.strafbefehl.deluxehubreloaded.cooldown.CooldownManager;
import dev.strafbefehl.deluxehubreloaded.hook.HooksManager;
import dev.strafbefehl.deluxehubreloaded.inventory.InventoryManager;
import dev.strafbefehl.deluxehubreloaded.module.ModuleManager;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import dev.strafbefehl.deluxehubreloaded.module.modules.hologram.HologramManager;
import dev.strafbefehl.deluxehubreloaded.utility.NamespacedKeys;
import dev.strafbefehl.deluxehubreloaded.utility.UpdateChecker;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class DeluxeHubPlugin extends JavaPlugin {

	private static final int BSTATS_ID = 23061;

	private ConfigManager configManager;
	private ActionManager actionManager;
	private HooksManager hooksManager;
	private CommandManager commandManager;
	private CooldownManager cooldownManager;
	private ModuleManager moduleManager;
	private InventoryManager inventoryManager;
	private Version currentVersion;

	public void onEnable() {
		long start = System.currentTimeMillis();
		getLogger().log(Level.INFO, "Based on original code from DeluxeHub");
		getLogger().log(Level.INFO, "Modified, and maintained by Strafbefehl, 2025");

		// Check server version
		if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") ||
				Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16") ||
				Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18") ||
				Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
			getLogger().severe("============= UNSUPPORTED SERVER VERSION =============");
			getLogger().severe("DeluxeHubReloaded requires at least Spigot 1.21 to run.");
			getLogger().severe("Please update your server to a newer version.");
			getLogger().severe("The plugin will now disable.");
			getLogger().severe("============= UNSUPPORTED SERVER VERSION =============");
			getPluginLoader().disablePlugin(this);
			return;
		}

		// Check if using Spigot
		try {
			Class.forName("org.spigotmc.SpigotConfig");
		} catch (ClassNotFoundException ex) {
			getLogger().severe("============= SPIGOT NOT DETECTED =============");
			getLogger().severe("DeluxeHubReloaded requires Spigot to run, you can download");
			getLogger().severe("Spigot here: https://www.spigotmc.org/wiki/spigot-installation/.");
			getLogger().severe("The plugin will now disable.");
			getLogger().severe("============= SPIGOT NOT DETECTED =============");
			getPluginLoader().disablePlugin(this);
			return;
		}

		// Enable bStats metrics
		new MetricsLite(this, BSTATS_ID);

		NamespacedKeys.registerKeys();

		// Check plugin hooks
		hooksManager = new HooksManager(this);

		// Load config files
		configManager = new ConfigManager();
		configManager.loadFiles(this);

		// If there were any configuration errors we should not continue
		if (!getServer().getPluginManager().isPluginEnabled(this)) return;

		// Command manager
		commandManager = new CommandManager(this);
		commandManager.reload();

		// Cooldown manager
		cooldownManager = new CooldownManager();

		// Inventory (GUI) manager
		inventoryManager = new InventoryManager();
		if (!hooksManager.isHookEnabled("HEAD_DATABASE")) inventoryManager.onEnable(this);

		// Core plugin modules
		moduleManager = new ModuleManager();
		moduleManager.loadModules(this);

		// Action system
		actionManager = new ActionManager(this);

		//BuildMode manager
		BuildMode.getInstance();

		// Load update checker (if enabled)
		if (getConfigManager().getFile(ConfigType.SETTINGS).getConfig().getBoolean("check-updates"))
			new UpdateChecker(this).checkForUpdate();

		// Register BungeeCord channels
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		getLogger().log(Level.INFO, "");
		getLogger().log(Level.INFO, "Successfully loaded in " + (System.currentTimeMillis() - start) + "ms");

		currentVersion = Version.parse(getDescription().getVersion());

		// Initialize and load configurations
		configManager = new ConfigManager();
		configManager.loadFiles(this);
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		moduleManager.unloadModules();
		inventoryManager.onDisable();
		//configManager.saveFiles();
	}

	public void reload() {
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);

		configManager.reloadFiles();

		BuildMode.getInstance().runScheduler();

		inventoryManager.onDisable();
		inventoryManager.onEnable(this);

		getCommandManager().reload();

		moduleManager.loadModules(this);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.Command cmd, @NotNull String commandLabel, String[] args) {
		try {
			getCommandManager().execute(cmd.getName(), args, sender);
		} catch (CommandPermissionsException e) {
			Messages.NO_PERMISSION.send(sender);
		} catch (MissingNestedCommandException e) {
			sender.sendMessage(ChatColor.RED + e.getUsage());
		} catch (CommandUsageException e) {
			sender.sendMessage(ChatColor.RED + "Usage: " + e.getUsage());
		} catch (WrappedCommandException e) {
			if (e.getCause() instanceof NumberFormatException) {
				sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
			} else {
				sender.sendMessage(ChatColor.RED + "An internal error has occurred. See console.");
				e.printStackTrace();
			}
		} catch (CommandException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
		}
		return true;
	}

	public HologramManager getHologramManager() {
		return (HologramManager) moduleManager.getModule(ModuleType.HOLOGRAMS);
	}

	public HooksManager getHookManager() {
		return hooksManager;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public CooldownManager getCooldownManager() {
		return cooldownManager;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public Version getCurrentVersion() {
		return currentVersion;
	}

	public ActionManager getActionManager() {
		return actionManager;
	}
}
