package dev.strafbefehl.deluxehubreloaded.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import dev.strafbefehl.deluxehubreloaded.config.Messages;
import org.bukkit.entity.Player;

@CommandAlias("%custom-commands")
public final class CustomCommandLoader extends BaseCommand {

    private final CustomCommandHandler module;

    public CustomCommandLoader(CustomCommandHandler module) {
        this.module = module;
    }

    @Default
    public void onDefault(Player player) {
        if (module.inDisabledWorld(player.getLocation())) return;

        String command = this.getExecCommandLabel().toLowerCase();

        for (CustomCommand customCommand : module.getCommands()) {
            if (customCommand.getAliases().stream().anyMatch(alias -> alias.equals(command))) {
                if (customCommand.getPermission() != null) if (!player.hasPermission(customCommand.getPermission())) {
                    Messages.CUSTOM_COMMAND_NO_PERMISSION.send(player);
                    return;
                }
                module.getPlugin().getActionManager().executeActions(player, customCommand.getActions());
            }
        }

    }
}