package dev.strafbefehl.deluxehubreloaded.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.Permissions;
import dev.strafbefehl.deluxehubreloaded.config.Messages;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import dev.strafbefehl.deluxehubreloaded.module.modules.world.LobbySpawn;
import dev.strafbefehl.deluxehubreloaded.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand {

	private final DeluxeHubPlugin plugin;

	public SetLobbyCommand(DeluxeHubPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(
			aliases = {"setlobby"},
			desc = "Set the lobby location"
	)
	public void setlobby(final CommandContext args, final CommandSender sender) throws CommandException {

		if (!sender.hasPermission(Permissions.COMMAND_SET_LOBBY.getPermission())) {
			Messages.NO_PERMISSION.send(sender);
			return;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("Console cannot set the spawn location.");
			return;
		}

		Player player = (Player) sender;
		if (plugin.getModuleManager().getDisabledWorlds().contains(player.getWorld().getName())) {
			sender.sendMessage(TextUtil.color("&cYou cannot set the lobby location in a disabled world."));
			return;
		}

		LobbySpawn lobbyModule = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY));
		lobbyModule.setLocation(player.getLocation());
		Messages.SET_LOBBY.send(sender);

	}

}
