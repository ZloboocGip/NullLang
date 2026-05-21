package org.nooll.nulllang;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.nooll.nulllang.api.NullLangAPI;
import org.nooll.nulllang.command.LangCommand;
import org.nooll.nulllang.lang.LangManager;
import org.nooll.nulllang.listener.PlayerJoinListener;
import java.util.Objects;

public final class NullLangPlugin extends JavaPlugin {

    private LangManager langManager;
    private NullLangAPI api;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResource("lang/ru.yml", false);
        saveResource("lang/en.yml", false);

        this.langManager = new LangManager(this);
        this.langManager.load();

        this.api = langManager;

        Bukkit.getServicesManager().register(
                NullLangAPI.class,
                api,
                this,
                ServicePriority.Normal
        );

        Objects.requireNonNull(getCommand("lang"), "Command 'lang' is not defined in plugin.yml")
                .setExecutor(new LangCommand(langManager));
        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(langManager),
                this
        );
    }

    @Override
    public void onDisable() {
        if (langManager != null) {
            langManager.savePlayers();
        }

        Bukkit.getServicesManager().unregister(NullLangAPI.class, api);
    }

    public NullLangAPI getApi() {
        return api;
    }

    public LangManager getLangManager() {
        return langManager;
    }
}