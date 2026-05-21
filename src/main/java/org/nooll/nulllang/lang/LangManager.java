package org.nooll.nulllang.lang;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.nooll.nulllang.NullLangPlugin;
import org.nooll.nulllang.api.NullLangAPI;
import org.nooll.nulllang.event.LangChangeEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class LangManager implements NullLangAPI {

    private final NullLangPlugin plugin;

    private final Map<String, FileConfiguration> langs = new HashMap<>();
    private final Map<UUID, String> playerLangs = new HashMap<>();

    private File playersFile;
    private FileConfiguration playersConfig;

    private String defaultLang;

    public LangManager(NullLangPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.defaultLang = plugin.getConfig().getString("default-lang", "en");

        loadLangs();
        loadPlayers();
    }

    private void loadLangs() {
        langs.clear();

        File folder = new File(plugin.getDataFolder(), "lang");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files == null) {
            return;
        }

        for (File file : files) {
            String langId = file.getName().replace(".yml", "").toLowerCase();
            langs.put(langId, YamlConfiguration.loadConfiguration(file));
        }
    }

    private void loadPlayers() {
        playersFile = new File(plugin.getDataFolder(), "players.yml");

        if (!playersFile.exists()) {
            try {
                playersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        playersConfig = YamlConfiguration.loadConfiguration(playersFile);

        if (!playersConfig.isConfigurationSection("players")) {
            return;
        }

        for (String key : playersConfig.getConfigurationSection("players").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String lang = playersConfig.getString("players." + key, defaultLang);
                playerLangs.put(uuid, lang);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public void savePlayers() {
        for (Map.Entry<UUID, String> entry : playerLangs.entrySet()) {
            playersConfig.set("players." + entry.getKey(), entry.getValue());
        }

        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLang(UUID uuid) {
        return playerLangs.getOrDefault(uuid, defaultLang);
    }

    @Override
    public String getLang(OfflinePlayer player) {
        return getLang(player.getUniqueId());
    }

    @Override
    public void setLang(UUID uuid, String lang) {
        if (!exists(lang)) {
            return;
        }

        playerLangs.put(uuid, lang.toLowerCase());
        savePlayers();
    }

    @Override
    public void setLang(Player player, String lang) {
        if (!exists(lang)) {
            return;
        }

        String oldLang = getLang(player.getUniqueId());
        String newLang = lang.toLowerCase();

        playerLangs.put(player.getUniqueId(), newLang);
        savePlayers();

        Bukkit.getPluginManager().callEvent(
                new LangChangeEvent(player, oldLang, newLang)
        );
    }

    @Override
    public String text(Player player, String key) {
        return text(player.getUniqueId(), key);
    }

    @Override
    public String text(UUID uuid, String key) {
        String lang = getLang(uuid);
        FileConfiguration config = langs.getOrDefault(lang, langs.get(defaultLang));

        if (config == null) {
            return color("&cMissing lang file: " + lang);
        }

        String value = config.getString(key);

        if (value == null && !lang.equals(defaultLang)) {
            FileConfiguration fallback = langs.get(defaultLang);
            if (fallback != null) {
                value = fallback.getString(key);
            }
        }

        if (value == null) {
            return color("&cMissing key: " + key);
        }

        return color(value);
    }
    public void setDefaultIfAbsent(UUID uuid) {
        if (!playerLangs.containsKey(uuid)) {
            playerLangs.put(uuid, defaultLang);
            savePlayers();
        }
    }
    @Override
    public List<String> list(Player player, String key) {
        return list(player.getUniqueId(), key);
    }

    @Override
    public List<String> list(UUID uuid, String key) {
        String lang = getLang(uuid);
        FileConfiguration config = langs.getOrDefault(lang, langs.get(defaultLang));

        if (config == null) {
            return List.of(color("&cMissing lang file: " + lang));
        }

        List<String> list = config.getStringList(key);

        if (!config.contains(key) && !lang.equals(defaultLang)) {
            FileConfiguration fallback = langs.get(defaultLang);
            if (fallback != null) {
                config = fallback;
            }
        }

        if (!config.contains(key)) {
            return List.of(color("&cMissing key: " + key));
        }

        return list.stream().map(this::color).toList();
    }

    @Override
    public boolean exists(String lang) {
        return langs.containsKey(lang.toLowerCase());
    }

    public Set<String> getAvailableLangs() {
        return langs.keySet();
    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}