package dev.strafbefehl.deluxehubreloaded.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.Permissions;
import dev.strafbefehl.deluxehubreloaded.config.Messages;
import dev.strafbefehl.deluxehubreloaded.module.ModuleType;
import dev.strafbefehl.deluxehubreloaded.module.modules.chat.ChatLock;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class LockchatCommand {

	private final DeluxeHubPlugin plugin;

	public LockchatCommand(DeluxeHubPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(
			aliases = {"lockchat"},
			desc = "Locks global chat"
	)
	public void lockchat(final CommandContext args, final CommandSender sender) throws CommandException {

		if (!sender.hasPermission(Permissions.COMMAND_LOCKCHAT.getPermission())) {
			Messages.NO_PERMISSION.send(sender);
			return;
		}

		ChatLock chatLockModule = (ChatLock) plugin.getModuleManager().getModule(ModuleType.CHAT_LOCK);

		if (chatLockModule.isChatLocked()) {
			Bukkit.getOnlinePlayers().forEach(player -> Messages.CHAT_UNLOCKED_BROADCAST.send(player, "%player%", sender.getName()));
			chatLockModule.setChatLocked(false);
		} else {
			Bukkit.getOnlinePlayers().forEach(player -> Messages.CHAT_LOCKED_BROADCAST.send(player, "%player%", sender.getName()));
			chatLockModule.setChatLocked(true);
		}
	}
}
