# NullLang

Simple language API for Bukkit/Spigot/Paper plugins.

## Features

- Per-player language
- YAML language files
- Simple API
- Language change event

## Installation

1. Put `NullLang.jar` into `/plugins`
2. Start the server
3. Configure files in `plugins/NullLang/`

## Command

```
/lang <language>
```

Examples:

```
/lang en
/lang ru
```

## Developer API

Add to `plugin.yml`:

```
softdepend:
  - NullLang
```

Get API:

```
NullLangAPI api = Bukkit.getServicesManager()
        .load(NullLangAPI.class);

if (api == null) {
    return;
}
```

Get text:

```java
player.sendMessage(
        api.text(player, "messages.welcome")
);
```

Change language:

```
api.setLang(player, "ru");
```

Listen for language changes:

```
@EventHandler
public void onLangChange(LangChangeEvent event) {
    Player player = event.getPlayer();

    String oldLang = event.getOldLang();
    String newLang = event.getNewLang();
}
```

## License

MIT
