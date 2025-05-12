package dev.strafbefehl.deluxehubreloaded.module.modules.chat;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.Permissions;
import dev.strafbefehl.deluxehubreloaded.config.ConfigType;
import dev.strafbefehl.deluxehubreloaded.config.Messages;
import dev.strafbefehl.deluxehubreloaded.module.Module;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class ChatCommandBlock extends Module {

	private List<String> blockedCommands;

	public ChatCommandBlock(DeluxeHubPlugin plugin) {
		super(plugin, ModuleType.COMMAND_BLOCK);
	}

	@Override
	public void onEnable() {
		blockedCommands = getConfig(ConfigType.SETTINGS).getStringList("command_block.blocked_commands");
	}

	@Override
	public void onDisable() {
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		if (inDisabledWorld(player.getLocation()) || player.hasPermission(Permissions.BLOCKED_COMMANDS_BYPASS.getPermission()))
			return;

		if (blockedCommands.contains(event.getMessage().toLowerCase())) {
			event.setCancelled(true);
			Messages.COMMAND_BLOCKED.send(player);
		}
	}
}
