package org.nooll.nulllang.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface NullLangAPI {

    String getLang(UUID uuid);

    String getLang(OfflinePlayer player);

    void setLang(UUID uuid, String lang);

    void setLang(Player player, String lang);

    String text(Player player, String key);

    String text(UUID uuid, String key);

    List<String> list(Player player, String key);

    List<String> list(UUID uuid, String key);

    boolean exists(String lang);
}