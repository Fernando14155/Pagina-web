package org.puntosclan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class SkywarsListener implements Listener {
    private PlayerPoints playerPoints;
    private List<Player> activePlayers; // Lista de jugadores activos en el juego

    public SkywarsListener(PlayerPoints playerPoints) {
        this.playerPoints = playerPoints;
        this.activePlayers = new ArrayList<>();
        initializeActivePlayers(); // Inicializa la lista de jugadores activos
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        activePlayers.remove(player); // Remover al jugador que murió de la lista de jugadores activos
        if (isLastPlayerStanding()) {
            playerPoints.addPoints(player, 3); // Otorgar puntos al último jugador en pie
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        activePlayers.remove(player); // Remover al jugador que sale del juego de la lista de jugadores activos
    }

    private boolean isLastPlayerStanding() {
        return activePlayers.size() == 1; // Si queda solo un jugador activo, entonces es el último en pie
    }

    // Método para inicializar la lista de jugadores activos
    private void initializeActivePlayers() {
        activePlayers.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            activePlayers.add(player);
        }
    }
}
