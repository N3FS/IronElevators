package me.ScarleTomato.IronElevators;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class IronElevators extends JavaPlugin {

    int MIN_ELEVATION = 3, MAX_ELEVATION = 14;
    Material ELEVATOR_MATERIAL = Material.IRON_BLOCK;
    Sound ELEVATOR_WHOOSH = Sound.ENTITY_IRON_GOLEM_ATTACK;

    @Override
    public void onEnable() {
        loadConfig();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void saveDefaultConfig() {
        Path path = getDataFolder().toPath().resolve("config.yml");
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path.getParent());
            } catch (IOException ex) {
                getLogger().warning("Can't create base directory: " + ex.getLocalizedMessage());
            }
            try {
                Files.createFile(path);
                YamlConfiguration config = YamlConfiguration.loadConfiguration(path.toFile());
                config.set("minElevation", MIN_ELEVATION);
                config.set("maxElevation", MAX_ELEVATION);
                config.set("elevatorMaterial", ELEVATOR_MATERIAL.name());
                config.set("elevatorWhoosh", ELEVATOR_WHOOSH.name());
                config.save(path.toFile());
            } catch (IOException ex) {
                getLogger().warning("Can't create config file: " + ex.getLocalizedMessage());
            }
        }
    }

    private void loadConfig() {
        this.saveDefaultConfig();
        this.reloadConfig();

        try {
            this.MIN_ELEVATION = this.getConfig().getInt("minElevation");
            this.MAX_ELEVATION = this.getConfig().getInt("maxElevation");
            this.ELEVATOR_MATERIAL = Material.valueOf(this.getConfig().getString("elevatorMaterial"));
            this.ELEVATOR_WHOOSH = Sound.valueOf(this.getConfig().getString("elevatorWhoosh"));
        } catch (Exception ex) {
            getLogger().warning("Failed to load config: " + ex.getLocalizedMessage());
        }
    }
}
