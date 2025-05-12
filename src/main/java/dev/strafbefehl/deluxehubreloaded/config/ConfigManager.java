package dev.strafbefehl.deluxehubreloaded.config;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

	private final Map<ConfigType, ConfigHandler> configurations;
	private ConfigMigrator migrator;

	public ConfigManager() {
		configurations = new HashMap<>();
	}

	public void loadFiles(DeluxeHubPlugin plugin) {
		// Initialize the config migrator
		migrator = new ConfigMigrator(plugin);

		// Register all config files
		registerFile(ConfigType.SETTINGS, new ConfigHandler(plugin, "config"));
		registerFile(ConfigType.MESSAGES, new ConfigHandler(plugin, "messages"));
		registerFile(ConfigType.DATA, new ConfigHandler(plugin, "data"));
		registerFile(ConfigType.COMMANDS, new ConfigHandler(plugin, "commands"));

		// Load all configs
		configurations.values().forEach(ConfigHandler::saveDefaultConfig);

		// Perform migrations if needed
		migrateConfigs();

		// Set the Messages configuration
		Messages.setConfiguration(getFile(ConfigType.MESSAGES).getConfig());
	}

	/**
	 * Migrates all configurations that need updating
	 */
	private void migrateConfigs() {
		for (Map.Entry<ConfigType, ConfigHandler> entry : configurations.entrySet()) {
			ConfigHandler handler = entry.getValue();
			if (handler.needsMigration()) {
				if (migrator.migrateConfig(entry.getKey(), handler)) {
					handler.setMigrated();
				}
			}
		}
	}

	public ConfigHandler getFile(ConfigType type) {
		return configurations.get(type);
	}

	public void reloadFiles() {
		configurations.values().forEach(ConfigHandler::reload);
		// Perform migrations after reload in case plugin has been updated
		migrateConfigs();
		Messages.setConfiguration(getFile(ConfigType.MESSAGES).getConfig());
	}

	public void saveFiles() {
		configurations.values().forEach(ConfigHandler::save);
	}

	public void registerFile(ConfigType type, ConfigHandler config) {
		configurations.put(type, config);
	}

	public FileConfiguration getFileConfiguration(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}
}