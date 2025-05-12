package dev.strafbefehl.deluxehubreloaded.module.modules.hotbar;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.base.BuildMode;
import dev.strafbefehl.deluxehubreloaded.config.ConfigType;
import dev.strafbefehl.deluxehubreloaded.module.Module;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import dev.strafbefehl.deluxehubreloaded.module.modules.hotbar.items.CustomItem;
import dev.strafbefehl.deluxehubreloaded.utility.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HotbarManager extends Module {

	private List<HotbarItem> hotbarItems;
	private int _joinSlot;

	public HotbarManager(DeluxeHubPlugin plugin) {
		super(plugin, ModuleType.HOTBAR_ITEMS);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		changeToJoinSlot(event.getPlayer());
	}

	@Override
	public void onEnable() {
		hotbarItems = new ArrayList<>();
		FileConfiguration config = getConfig(ConfigType.SETTINGS);

		if (config.getBoolean("hotbar.joinslot")) {
			_joinSlot = config.getInt("hotbar.slot_number");
		}

		if (config.getBoolean("custom_join_items.enabled")) {

			for (String entry : Objects.requireNonNull(config.getConfigurationSection("custom_join_items.items")).getKeys(false)) {
				ItemStack item = ItemStackBuilder.getItemStack(config.getConfigurationSection("custom_join_items.items." + entry)).build();
				CustomItem customItem = new CustomItem(this, item, config.getInt("custom_join_items.items." + entry + ".slot"), entry);

				if (config.contains("custom_join_items.items." + entry + ".permission")) {
					customItem.setPermission(config.getString("custom_join_items.items." + entry + ".permission"));
				}

				customItem.setConfigurationSection(config.getConfigurationSection("custom_join_items.items." + entry));
				customItem.setAllowMovement(config.getBoolean("custom_join_items.disable_inventory_movement"));
				registerHotbarItem(customItem);
			}

		}

		giveItems();
	}

	@Override
	public void onDisable() {
		removeItems();
	}

	public List<HotbarItem> getHotbarItems() {
		return hotbarItems;
	}

	public void registerHotbarItem(HotbarItem hotbarItem) {
		getPlugin().getServer().getPluginManager().registerEvents(hotbarItem, getPlugin());
		hotbarItems.add(hotbarItem);
	}

	public void changeToJoinSlot(Player player) {
		player.getInventory().setHeldItemSlot(_joinSlot);
	}

	public void giveItems(Player player) {
		hotbarItems.stream()
				.filter(p -> !inDisabledWorld(player.getLocation()) && !BuildMode.getInstance().isPresent(player.getUniqueId()))
				.forEach(hotbarItem -> hotbarItem.giveItem(player));
	}

	private void giveItems() {
		Bukkit.getOnlinePlayers().stream()
				.filter(player -> !inDisabledWorld(player.getLocation())
						&& !BuildMode.getInstance().isPresent(player.getUniqueId()))
				.forEach(player -> hotbarItems.forEach(hotbarItem -> hotbarItem.giveItem(player)));
	}

	private void removeItems() {
		Bukkit.getOnlinePlayers().stream()
				.filter(player -> !inDisabledWorld(player.getLocation()))
				.forEach(player -> hotbarItems.forEach(hotbarItem -> hotbarItem.removeItem(player)));
	}

}
