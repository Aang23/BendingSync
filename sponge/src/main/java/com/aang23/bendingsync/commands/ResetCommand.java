package com.aang23.bendingsync.commands;

import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.utils.BendingSyncUtils;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import net.minecraft.entity.player.EntityPlayer;

public class ResetCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player spongePlayer = args.<Player>getOne("player").get();

        BendingDataStorage bendingStorage = new BendingDataStorage();
        DSSDataStorage dssStorage = new DSSDataStorage();

        bendingStorage.restoreToPlayer(spongePlayer);
        dssStorage.restoreToPlayer(spongePlayer);

        BendingSyncUtils.saveDataToDatabaseForPlayer(spongePlayer);

        src.sendMessage(Text.builder().color(TextColors.GREEN)
                .append(Text.of(spongePlayer.getName() + "'s datas has beeen cleared !'")).build());

        return CommandResult.success();
    }
}