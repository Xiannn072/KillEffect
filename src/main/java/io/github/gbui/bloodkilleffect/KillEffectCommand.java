package io.github.gbui.bloodkilleffect;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import java.util.ArrayList;
import java.util.List;

public class KillEffectCommand extends CommandBase {
    @Override
    public String getCommandName() { return "killeffect"; }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/killeffect <toggle|effect|random|tier|players|mykills|pvp|list|config>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(sender)));
            return;
        }

        BloodKillEffectMod mod = BloodKillEffectMod.instance;
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "toggle":
                mod.setEnabled(!BloodKillEffectMod.enabled);
                sender.addChatMessage(new ChatComponentText(
                    "KillEffect " + (BloodKillEffectMod.enabled ? "enabled" : "disabled")));
                break;

            case "effect":
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText("Usage: /killeffect effect <name>"));
                    return;
                }
                String effectName = args[1];
                KillEffect effect = EffectRegistry.get(effectName);
                if (effect == null) {
                    sender.addChatMessage(new ChatComponentText("Unknown effect: " + effectName));
                    return;
                }
                mod.setSelectedEffect(effectName);
                mod.setRandomMode(false);
                sender.addChatMessage(new ChatComponentText("Effect set to: " + effectName));
                break;

            case "random":
                mod.setRandomMode(true);
                sender.addChatMessage(new ChatComponentText("Random mode enabled"));
                break;

            case "players":
                mod.setPlayersOnly(!BloodKillEffectMod.playersOnly);
                sender.addChatMessage(new ChatComponentText(
                    "Players only: " + (BloodKillEffectMod.playersOnly ? "ON" : "OFF")));
                break;

            case "mykills":
                mod.setKilledByPlayerOnly(!BloodKillEffectMod.killedByPlayerOnly);
                sender.addChatMessage(new ChatComponentText(
                    "My kills only: " + (BloodKillEffectMod.killedByPlayerOnly ? "ON" : "OFF")));
                break;

            case "pvp":
                mod.setPvpOnly(!BloodKillEffectMod.pvpOnly);
                sender.addChatMessage(new ChatComponentText(
                    "PvP only: " + (BloodKillEffectMod.pvpOnly ? "ON" : "OFF")));
                break;

            case "tier":
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText("Usage: /killeffect tier <name>"));
                    return;
                }
                PerformanceTier tier = PerformanceTier.fromString(args[1]);
                mod.setPerformanceTier(tier);
                sender.addChatMessage(new ChatComponentText(
                    "Performance tier set to: " + tier.getDisplayName()));
                break;

            case "list":
                sender.addChatMessage(new ChatComponentText("=== Effects ==="));
                for (KillEffect e : EffectRegistry.getAll()) {
                    sender.addChatMessage(new ChatComponentText("  - " + e.getName()));
                }
                sender.addChatMessage(new ChatComponentText("=== Tiers ==="));
                for (PerformanceTier t : PerformanceTier.values()) {
                    sender.addChatMessage(new ChatComponentText(
                        "  - " + t.getDisplayName() + " (scale: " + t.getParticleScale() + "x)"));
                }
                break;

            case "config":
                sender.addChatMessage(new ChatComponentText("Open GUI: Mods > KillEffect > Config"));
                break;

            default:
                sender.addChatMessage(new ChatComponentText("Unknown subcommand: " + subCommand));
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args,
            net.minecraft.util.BlockPos pos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("toggle");
            completions.add("effect");
            completions.add("random");
            completions.add("tier");
            completions.add("players");
            completions.add("mykills");
            completions.add("pvp");
            completions.add("list");
            completions.add("config");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("effect")) {
                completions.addAll(EffectRegistry.getAllNames());
            } else if (args[0].equalsIgnoreCase("tier")) {
                for (PerformanceTier t : PerformanceTier.values()) {
                    completions.add(t.name().toLowerCase());
                }
            }
        }
        return completions;
    }
}
