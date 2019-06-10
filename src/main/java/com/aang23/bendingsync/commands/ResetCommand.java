package com.aang23.bendingsync.commands;

import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.ReSkillableDataStorage;
import com.aang23.bendingsync.utils.BendingSyncUtils;
import com.crowsofwar.avatar.common.data.Bender;

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
        EntityPlayer forgePlayer = (EntityPlayer) spongePlayer;

        BendingDataStorage bendingStorage = new BendingDataStorage();
        DSSDataStorage dssStorage = new DSSDataStorage();
        ReSkillableDataStorage reskillableStorage = new ReSkillableDataStorage();

        BendingDataStorage.setDataDromBendingStorage(Bender.get(forgePlayer), bendingStorage);
        DSSDataStorage.setDataFromDSSStorage(forgePlayer, dssStorage);
        ReSkillableDataStorage.setDataFromReSkillabletorage(forgePlayer, reskillableStorage);

        BendingSyncUtils.saveDataToDatabaseForPlayer(forgePlayer);

        src.sendMessage(Text.builder().color(TextColors.GREEN)
                .append(Text.of(spongePlayer.getName() + "'s datas has beeen cleared !'")).build());

        return CommandResult.success();
    }
}