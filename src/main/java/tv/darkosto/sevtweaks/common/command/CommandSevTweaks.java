package tv.darkosto.sevtweaks.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import tv.darkosto.sevtweaks.SevTweaks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSevTweaks extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "sevtweaks";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/sevtweaks <action> [arguments...]";
    }

    private static final String[] COMMANDS = new String[]{
            "gs",
            "gamestage"
    };

    private static final String[] GSCOMMANDS = new String[]{
            "init",
            "start",
            "sync"
    };

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos blockPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, COMMANDS);
        } else {
            String identifier = args[0].toLowerCase();
            if (identifier.equals("gs") || identifier.equals("gamestages")) {
                return getListOfStringsMatchingLastWord(args, GSCOMMANDS);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponentString("§cNot enough arguments."));
            return;
        }
        String identifier = args[0];
        if (args.length < 2) {
            sender.sendMessage(new TextComponentTranslation("command.sevtweaks.invalid.option", "Testing"));

            return;
        }
        String subIdentifier = args[1];
        switch (identifier.toLowerCase()) {
            case "gs":
            case "gamestage":
                switch (subIdentifier.toLowerCase()) {
                    case "init":
                    case "start":
                        if (SevTweaks.scoreboard.init(server)) {
                            sender.sendMessage(new TextComponentTranslation("command.sevtweaks.gsscore.success"));
                            return;
                        }
                        sender.sendMessage(new TextComponentTranslation("command.sevtweaks.gsscore.failed"));
                        return;
                    case "sync":
                        SevTweaks.scoreboard.syncPlayers(server);
                        sender.sendMessage(new TextComponentTranslation("command.sevtweaks.gssync.success"));
                        return;
                }
            default:
                sender.sendMessage(new TextComponentString("§c" + identifier + " is not valid option! Valid options: " + Arrays.toString(COMMANDS)));
                break;
        }
    }
}
