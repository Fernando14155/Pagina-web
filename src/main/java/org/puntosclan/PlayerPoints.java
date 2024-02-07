package org.puntosclan;

import java.util.HashMap;
import org.bukkit.entity.Player;

public class PlayerPoints {
    private HashMap<String, Integer> skywarsPoints = new HashMap<>();
    private HashMap<String, Integer> eggwarsPoints = new HashMap<>();
    private HashMap<String, Integer> uhhrunPoints = new HashMap<>();
    private HashMap<String, Integer> partyGamesPoints = new HashMap<>();
    private HashMap<String, Integer> skywarsWinPoints = new HashMap<>();
    private HashMap<String, Integer> skywarsKillPoints = new HashMap<>();

    private HashMap<String, Integer> pointsMap = new HashMap<>(); // HashMap para almacenar los puntos de cada jugador

    public int getSkywarsPoints(Player player) {
        String playerName = player.getName();
        return skywarsPoints.getOrDefault(playerName, 0);
    }

    public void updateWebpageAfterPointsChange() {
        WebpageUpdater webpageUpdater = new WebpageUpdater();
        webpageUpdater.updateWebpageFromYaml();
    }
    public int getEggwarsPoints(Player player) {
        String playerName = player.getName();
        return eggwarsPoints.getOrDefault(playerName, 0);
    }

    public int getUHHRUNPoints(Player player) {
        String playerName = player.getName();
        return uhhrunPoints.getOrDefault(playerName, 0);
    }

    public int getPartyGamesPoints(Player player) {
        String playerName = player.getName();
        return partyGamesPoints.getOrDefault(playerName, 0);
    }
    // Método para agregar puntos a un jugador

    public void addPoints(Player player, int pointsToAdd) {
        String playerName = player.getName();
        int currentPoints = pointsMap.getOrDefault(playerName, 0);
        pointsMap.put(playerName, currentPoints + pointsToAdd);
        updateWebpageAfterPointsChange(); // Llama al método para actualizar la página web
    }

    // Método para restar puntos a un jugador
    public void removePoints(Player player, int pointsToRemove) {
        String playerName = player.getName();
        int currentPoints = pointsMap.getOrDefault(playerName, 0);
        int newPoints = Math.max(0, currentPoints - pointsToRemove); // Asegurarse de que los puntos no sean negativos
        pointsMap.put(playerName, newPoints);
        updateWebpageAfterPointsChange(); // Llama al método para actualizar la página web
    }

    // En la clase PlayerPoints
    public void addSkywarsWinPoints(Player player, int pointsToAdd) {
        String playerName = player.getName();
        int currentPoints = skywarsWinPoints.getOrDefault(playerName, 0);
        skywarsWinPoints.put(playerName, currentPoints + pointsToAdd);
    }

    public void addSkywarsKillPoints(Player player, int pointsToAdd) {
        String playerName = player.getName();
        int currentPoints = skywarsKillPoints.getOrDefault(playerName, 0);
        skywarsKillPoints.put(playerName, currentPoints + pointsToAdd);
    }

    public void addEggwarsPoints(Player player, int pointsToAdd) {
        String playerName = player.getName();
        int currentPoints = eggwarsPoints.getOrDefault(playerName, 0);
        eggwarsPoints.put(playerName, currentPoints + pointsToAdd);
    }

    // Método para obtener los puntos de un jugador
    public int getPoints(Player player) {
        String playerName = player.getName();
        return pointsMap.getOrDefault(playerName, 0);
    }
}
