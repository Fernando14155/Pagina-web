package org.puntosclan;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class PuntosClan extends JavaPlugin implements Listener {
    private File configFile;

    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(), "jugadores.yml");

        // Crear el archivo de configuración si no existe
        if (!configFile.exists()) {
            saveResource("jugadores.yml", false);
        }



        // Registrar eventos
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Plugin de Skywars habilitado.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin de Skywars deshabilitado.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName(); // Obtener el nombre del jugador que se está uniendo

        getLogger().info(playerName + " joined the game");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
            return false;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("skywars")) {
            if (args.length >= 3 && args[0].equalsIgnoreCase("puntos") && args[1].equalsIgnoreCase("win")) {
                String playerName = args[2];
                Player targetPlayer = Bukkit.getPlayer(playerName);
                if (targetPlayer != null) {
                    addPointsToPlayer(targetPlayer, "skywars", 5);
                    return true;
                } else {
                    player.sendMessage("El jugador " + playerName + " no está en línea.");
                    return false;
                }
            } else {
                player.sendMessage("Uso incorrecto del comando. Usa /skywars puntos win <jugador>.");
                return false;
            }
        }
        return false;
    }

    private void addPointsToPlayer(Player player, String game, int points) {
        Map<String, Map<String, Integer>> playerData = loadPlayerData();
        if (playerData != null) {
            String playerName = player.getName();
            Map<String, Integer> pointsData = playerData.get(playerName);
            if (pointsData == null) {
                pointsData = new HashMap<>();
                playerData.put(playerName, pointsData);
            }
            int currentPoints = pointsData.getOrDefault(game, 0);
            pointsData.put(game, currentPoints + points);

            savePlayerData(playerData);
            player.sendMessage("Se han agregado " + points + " puntos de " + game + " al jugador " + playerName);
        } else {
            player.sendMessage("Error al cargar los datos del archivo jugadores.yml");
        }
    }

    private Map<String, Map<String, Integer>> loadPlayerData() {
        Yaml yaml = new Yaml();
        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
            return yaml.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void savePlayerData(Map<String, Map<String, Integer>> playerData) {
        Yaml yaml = new Yaml();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            yaml.dump(playerData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
