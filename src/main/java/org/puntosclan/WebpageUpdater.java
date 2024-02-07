package org.puntosclan;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class WebpageUpdater {
    private static final String YAML_FILE_PATH = "/jugadores.yml";
    private static final String HTML_FILE_PATH = "pagina.html";
    private static final String PLANTILLA_HTML = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Player Stats</title>\n" +
            "    <style>\n" +
            "        table {\n" +
            "            width: 100%;\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "\n" +
            "        th, td {\n" +
            "            padding: 8px;\n" +
            "            text-align: left;\n" +
            "            border-bottom: 1px solid #ddd;\n" +
            "        }\n" +
            "\n" +
            "        th {\n" +
            "            background-color: #f2f2f2;\n" +
            "        }\n" +
            "\n" +
            "        tr:hover {background-color: #f5f5f5;}\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Player Stats</h1>\n" +
            "    <table>\n" +
            "        <thead>\n" +
            "            <tr>\n" +
            "                <th>Overall Rank</th>\n" +
            "                <th>No Debuff</th>\n" +
            "                <th>Username</th>\n" +
            "                <th>Debuff</th>\n" +
            "                <th>BuildUHC</th>\n" +
            "                <th>Gapple</th>\n" +
            "                <th>TJ Kills</th>\n" +
            "                <th>Soup</th>\n" +
            "                <th>Deaths</th>\n" +
            "                <th>Combo</th>\n" +
            "                <th>Sumo</th>\n" +
            "                <th>Rating</th>\n" +
            "                <th>Search</th>\n" +
            "                <th>View</th>\n" +
            "            </tr>\n" +
            "        </thead>\n" +
            "        <tbody>\n" +
            "            {{CONTENIDO}}\n" +
            "        </tbody>\n" +
            "    </table>\n" +
            "</body>\n" +
            "</html>";

    public void updateWebpageFromYaml() {
        StringBuilder contenidoHTML = new StringBuilder();

        try (InputStream inputStream = getClass().getResourceAsStream(YAML_FILE_PATH)) {
            if (inputStream != null) {
                Yaml yaml = new Yaml();
                Map<String, Map<String, Integer>> jugadores = yaml.load(inputStream);

                if (jugadores != null) {
                    for (Map.Entry<String, Map<String, Integer>> entry : jugadores.entrySet()) {
                        String playerName = entry.getKey();
                        Map<String, Integer> puntos = entry.getValue();

                        if (puntos != null) {
                            contenidoHTML.append("<tr>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Overall Rank", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("No Debuff", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(playerName).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Debuff", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("BuildUHC", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Gapple", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("TJ Kills", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Soup", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Deaths", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Combo", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Sumo", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Rating", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("Search", 0)).append("</td>");
                            contenidoHTML.append("<td>").append(puntos.getOrDefault("View", 0)).append("</td>");
                            contenidoHTML.append("</tr>");
                        } else {
                            System.err.println("El jugador " + playerName + " no tiene puntos registrados.");
                        }
                    }
                } else {
                    System.err.println("No se pudieron cargar los datos de los jugadores desde el archivo YAML.");
                }
            } else {
                System.err.println("No se pudo encontrar el archivo jugadores.yml en el classpath.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el archivo YAML.");
        }

        // Reemplazar el marcador de posición en la plantilla HTML con el contenido generado
        String contenidoActualizado = PLANTILLA_HTML.replace("{{CONTENIDO}}", contenidoHTML.toString());

        // Escribir el contenido actualizado en el archivo HTML
        escribirEnPaginaWeb(contenidoActualizado);
    }


    private void escribirEnPaginaWeb(String contenido) {
        try (FileWriter writer = new FileWriter(HTML_FILE_PATH)) {
            writer.write(contenido);
            System.out.println("Contenido actualizado en la página web correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al escribir en la página web.");
        }
    }

    // Método para agregar puntos a un jugador
    public void agregarPuntosAJugador(String playerName, String juego, int puntos) {
        try {
            // Cargar el archivo YAML
            Yaml yaml = new Yaml();
            InputStream inputStream = getClass().getResourceAsStream(YAML_FILE_PATH);
            Map<String, Map<String, Integer>> jugadores = yaml.load(inputStream);

            // Verificar si el jugador ya existe en el mapa de jugadores
            if (jugadores.containsKey(playerName)) {
                // Obtener el mapa de puntos del jugador
                Map<String, Integer> puntosJugador = jugadores.get(playerName);
                // Agregar los puntos para el juego especificado
                puntosJugador.put(juego, puntos);

                // Guardar los cambios en el archivo YAML
                try (Writer writer = new FileWriter(YAML_FILE_PATH)) {
                    yaml.dump(jugadores, writer);
                }

                // Actualizar la página web después de guardar los cambios
                updateWebpageFromYaml();
            } else {
                System.err.println("El jugador " + playerName + " no existe en el archivo YAML.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al agregar puntos al jugador.");
        }
    }

    public static void main(String[] args) {
        // Para usar como aplicación independiente
        WebpageUpdater webpageUpdater = new WebpageUpdater();

        // Observar cambios en el archivo YAML y actualizar la página web
        try {
            Path yamlFilePath = Paths.get(WebpageUpdater.class.getResource(YAML_FILE_PATH).toURI());
            WatchService watchService = FileSystems.getDefault().newWatchService();
            yamlFilePath.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.context().toString().equals(yamlFilePath.getFileName().toString())) {
                        // Solo necesitamos actualizar la página web si se modifica el archivo YAML externamente,
                        // pero en este caso, la actualización se realizará internamente en el método agregarPuntosAJugador.
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            System.err.println("Error al observar cambios en el archivo YAML.");
        }
    }
}
