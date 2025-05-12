package dev.strafbefehl.deluxehubreloaded.module.modules.player;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.base.BuildMode;
import dev.strafbefehl.deluxehubreloaded.module.Module;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerOffHandSwap extends Module {

	public PlayerOffHandSwap(DeluxeHubPlugin plugin) {
		super(plugin, ModuleType.PLAYER_OFFHAND_LISTENER);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@EventHandler
	public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
		if (BuildMode.getInstance().isPresent(event.getPlayer().getUniqueId())) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (BuildMode.getInstance().isPresent(event.getWhoClicked().getUniqueId())) return;
		if (event.getRawSlot() != event.getSlot() && event.getCursor() != null && event.getSlot() == 40) {
			event.setCancelled(true);
		}
	}
}
