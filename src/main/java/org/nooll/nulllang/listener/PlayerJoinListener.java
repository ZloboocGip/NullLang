package org.nooll.nulllang.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.nooll.nulllang.lang.LangManager;

public final class PlayerJoinListener implements Listener {

    private final LangManager langManager;

    public PlayerJoinListener(LangManager langManager) {
        this.langManager = langManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        langManager.getLang(event.getPlayer());
    }
}