package org.nooll.nulllang.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nooll.nulllang.lang.LangManager;

public final class LangCommand implements CommandExecutor {

    private final LangManager langManager;

    public LangCommand(LangManager langManager) {
        this.langManager = langManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(langManager.text(player, "command.lang.usage")
                    .replace("%langs%", String.join(", ", langManager.getAvailableLangs())));
            return true;
        }

        String lang = normalize(args[0]);

        if (!langManager.exists(lang)) {
            player.sendMessage(langManager.text(player, "command.lang.not-found")
                    .replace("%lang%", args[0]));
            return true;
        }

        langManager.setLang(player, lang);

        player.sendMessage(langManager.text(player, "command.lang.changed")
                .replace("%lang%", lang));

        return true;
    }

    private String normalize(String input) {
        input = input.toLowerCase();

        return switch (input) {
            case "russian", "русский", "ru" -> "ru";
            case "english", "английский", "en" -> "en";
            default -> input;
        };
    }
}