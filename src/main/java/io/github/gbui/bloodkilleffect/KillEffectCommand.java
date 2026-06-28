package io.github.gbui.bloodkilleffect;

import io.github.gbui.bloodkilleffect.config.ConfigManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class KillEffectCommand extends CommandBase {
    @Override
    public String getCommandName() { return "killeffect"; }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/killeffect <toggle|effect|random|tier|list|config>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(sender)));
            return;
        }

        ConfigManager cfg = ConfigManager.get();
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "toggle":
                cfg.setEnabled(!cfg.isEnabled());
                sender.addChatMessage(new ChatComponentText(
                    "KillEffect " + (cfg.isEnabled() ? "enabled" : "disabled")));
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
                cfg.setSelectedEffect(effectName);
                cfg.setRandomMode(false);
                sender.addChatMessage(new ChatComponentText("Effect set to: " + effect.getName()));
                break;

            case "random":
                cfg.setRandomMode(true);
                sender.addChatMessage(new ChatComponentText("Random mode enabled"));
                break;

            case "tier":
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText("Usage: /killeffect tier <name>"));
                    return;
                }
                PerformanceTier tier = PerformanceTier.fromString(args[1]);
                cfg.setPerformanceTier(tier);
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
                        "  - " + t.name() + " (scale: " + t.getParticleScale() + "x)"));
                }
                break;

            case "config":
                BloodKillEffectMod.proxy.openConfigGui();
                break;

            default:
                sender.addChatMessage(new ChatComponentText("Unknown subcommand: " + subCommand));
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args,
                "toggle", "effect", "random", "tier", "list", "config");
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("effect")) {
                return getListOfStringsMatchingLastWord(args,
                    EffectRegistry.getAllNames().toArray(new String[0]));
            }
            if (args[0].equalsIgnoreCase("tier")) {
                return getListOfStringsMatchingLastWord(args,
                    "potato", "balanced", "high", "ultra");
            }
        }
        return Collections.emptyList();
    }
}
