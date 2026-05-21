package org.nooll.nulllang.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class LangChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String oldLang;
    private final String newLang;

    public LangChangeEvent(Player player, String oldLang, String newLang) {
        this.player = player;
        this.oldLang = oldLang;
        this.newLang = newLang;
    }

    public Player getPlayer() {
        return player;
    }

    public String getOldLang() {
        return oldLang;
    }

    public String getNewLang() {
        return newLang;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}