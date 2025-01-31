package com.example.controller.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.controller.algoritmos.BellmanFord;
import com.example.controller.algoritmos.Floyd;
import com.example.controller.dao.implement.AdapterDao;
import com.example.controller.dao.services.VeterinariaServices;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.Adycencia;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.controller.tda.queque.Queque;
import com.example.models.Veterinaria;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class VeterinariaDao extends AdapterDao<Veterinaria> {
    private Veterinaria veterinaria;
    private LinkedList listAll;
    private GraphLabelNoDirect<Veterinaria> graph;

    public VeterinariaDao() {
        super(Veterinaria.class);
        cargarGrafo();
    }

    public Veterinaria getVeterinaria() {
        if (veterinaria == null) {
            veterinaria = new Veterinaria();
        }
        return this.veterinaria;
    }

    public void setVeterinaria(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
    }

    public LinkedList getListAll() {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1;
        veterinaria.setId(id);
        this.persist(this.veterinaria);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getVeterinaria(), getVeterinaria().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public String dibujarMapa() {
        String mapa = "var osmUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png'," + "\n";
        mapa += "       osmAttrib = '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors',"
                + "\n";
        mapa += "       osm = L.tileLayer(osmUrl, {maxZoom: 15, attribution: osmAttrib});" + "\n";
        mapa += "var map = L.map('map').setView([-3.996716943365505, -79.20174975448631], 15);" + "\n" + "\n";
        mapa += "L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {" + "\n";
        mapa += "   attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'"
                + "\n";
        mapa += "}).addTo(map);" + "\n";
        try {
            for (Integer i = 0; i < listAll().getSize(); i++) {
                Veterinaria v = (Veterinaria) listAll().get(i);
                mapa += "L.marker([" + v.getLtd() + " , " + v.getLng() + "]).addTo(map)" + "\n";
                mapa += ".bindPopup('" + v.getNombre() + "')" + "\n";
                mapa += ".openPopup();" + "\n";
            }
            FileWriter file = new FileWriter(
                    "resources" + File.separatorChar + "maps" + File.separatorChar + "mapa.js");
            file.write(mapa);
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapa;
    }

    public void createGraph() {
        try {
            LinkedList<Veterinaria> lv = listAll();
            if (!lv.isEmpty()) {
                graph = new GraphLabelNoDirect<>(lv.getSize(), Veterinaria.class);
                Veterinaria[] m = lv.toArray();

                for (Integer i = 0; i < lv.getSize(); i++) {
                    graph.labelsVerticeL((i + 1), m[i]);
                }
                generarAdyacenciasAleatorias();
            }
            if (graph != null) {
                dibujarMapa();
                graph.drawGraph();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double calcularDistancia(Double lat1, Double lon1, Double lat2, Double lon2) {
        final Integer R = 6371; // Radio de la Tierra en kilómetros
        double latDist = Math.toRadians(lat2 - lat1);
        double lonDist = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDist / 2) * Math.sin(lonDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Retorna la distancia en kilómetros
    }

    // Guardar el grafo en un archivo JSON
    public void guardarGrafoEnJson() {
        if (graph == null) {
            createGraph();
        }

        JsonArray jsonGrafo = new JsonArray();
        Gson gson = new Gson();
        File file = new File("media" + File.separatorChar + "grafo.json");

        try {
            // Leer el archivo JSON existente si existe
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                jsonGrafo = gson.fromJson(reader, JsonArray.class);
                reader.close();
            }
            Map<Integer, JsonObject> existingNodesMap = new HashMap<>();
            for (Integer i = 0; i < jsonGrafo.size(); i++) {
                JsonObject nodo = jsonGrafo.get(i).getAsJsonObject();
                Integer id = nodo.get("id").getAsInt();
                existingNodesMap.put(id, nodo);
            }
            Integer numVertices = graph.nro_vertices();

            for (Integer i = 1; i <= numVertices; i++) {
                Veterinaria vet = graph.getLabelL(i);

                if (existingNodesMap.containsKey(vet.getId())) {
                    continue;
                }

                JsonObject nodo = new JsonObject();
                nodo.addProperty("id", vet.getId());
                nodo.addProperty("nombre", vet.getNombre());

                JsonArray adyacenciasArray = new JsonArray();
                LinkedList<Adycencia> adyacencias = graph.adycencias(i);

                if (adyacencias.getSize() < 3) {
                    generarAdyacenciasParaNuevoNodo(vet.getId(), numVertices);
                    adyacencias = graph.adycencias(i); 
                }

                for (Integer j = 0; j < adyacencias.getSize(); j++) {
                    Adycencia ady = adyacencias.get(j);
                    adyacenciasArray.add(ady.getDestination());
                }

                nodo.add("adjacencias", adyacenciasArray);
                jsonGrafo.add(nodo);
            }

            FileWriter writer = new FileWriter(file);
            gson.toJson(jsonGrafo, writer);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarGrafo() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("media" + File.separatorChar + "grafo.json");
            JsonArray jsonGrafo = gson.fromJson(reader, JsonArray.class);
            reader.close();

            LinkedList<Veterinaria> lv = listAll();
            graph = new GraphLabelNoDirect<>(lv.getSize(), Veterinaria.class);
            Veterinaria[] m = lv.toArray();

            // Mapa para asociar ID de Veterinaria con número de vértice
            Map<Integer, Integer> idToVertex = new HashMap<>();
            for (int i = 0; i < lv.getSize(); i++) {
                Veterinaria vet = m[i];
                idToVertex.put(vet.getId(), i + 1); // Vértices son 1-based
                graph.labelsVerticeL(i + 1, vet);
            }

            // Reconstruir aristas
            for (int i = 0; i < jsonGrafo.size(); i++) {
                JsonObject nodo = jsonGrafo.get(i).getAsJsonObject();
                int vetIdOrigen = nodo.get("id").getAsInt();
                int origen = idToVertex.get(vetIdOrigen); // Obtener número de vértice

                JsonArray adyacencias = nodo.getAsJsonArray("adjacencias");
                for (int j = 0; j < adyacencias.size(); j++) {
                    int vetIdDestino = adyacencias.get(j).getAsInt();
                    int destino = idToVertex.get(vetIdDestino);

                    Veterinaria vetOrigen = graph.getLabelL(origen);
                    Veterinaria vetDestino = graph.getLabelL(destino);

                    // Calcular peso
                    double peso = calcularDistancia(
                            vetOrigen.getLtd(),
                            vetOrigen.getLng(),
                            vetDestino.getLtd(),
                            vetDestino.getLng());

                    try {
                        graph.insertEdgeL(vetOrigen, vetDestino, (float) peso);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            dibujarMapa();
            graph.drawGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generarAdyacenciasAleatorias() {
        if (graph == null)
            return;

        Random random = new Random();
        Integer numVertices = graph.nro_vertices();

        for (Integer i = 1; i <= numVertices; i++) {
            Integer adyacenciasCreadas = 0;
            while (adyacenciasCreadas < 3) {
                Integer destino = random.nextInt(numVertices) + 1;
                try {
                    if (destino == i || graph.is_edge(i, destino))
                        continue;
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                Veterinaria origen = graph.getLabelL(i);
                Veterinaria destinoVet = graph.getLabelL(destino);
                Double peso = calcularDistancia(
                        origen.getLtd(), origen.getLng(),
                        destinoVet.getLtd(), destinoVet.getLng());

                try {
                    graph.add_edge(i, destino, peso.floatValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adyacenciasCreadas++;
            }
        }
    }

    private void generarAdyacenciasParaNuevoNodo(Integer idNuevoNodo, Integer numVertices) {
        Random random = new Random();
        Integer adyacenciasCreadas = 0;

        while (adyacenciasCreadas < 3) {
            Integer destino = random.nextInt(numVertices) + 1;
            if (destino == idNuevoNodo) {
                continue; // Evitar autoreferencia
            }

            try {
                if (!graph.is_edge(idNuevoNodo, destino)) {
                    Veterinaria origen = graph.getLabelL(idNuevoNodo);
                    Veterinaria destinoVet = graph.getLabelL(destino);

                    Double peso = calcularDistancia(
                            origen.getLtd(), origen.getLng(),
                            destinoVet.getLtd(), destinoVet.getLng());

                    graph.add_edge(idNuevoNodo, destino, peso.floatValue());
                    adyacenciasCreadas++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public LinkedList<Integer> bfs(Integer inicio) {
        LinkedList<Integer> recorrido = new LinkedList<>();
        if (graph == null) {
            createGraph();
            if (graph == null)
                return recorrido; // Grafo no inicializado
        }

        try {
            if (inicio < 1 || inicio > graph.nro_vertices())
                return recorrido;

            boolean[] visitados = new boolean[graph.nro_vertices() + 1];
            Queque<Integer> cola = new Queque<>(graph.nro_vertices());
            cola.queque(inicio);
            visitados[inicio] = true;

            while (cola.getSize() > 0) {
                Integer actual = cola.dequeque();
                recorrido.add(actual);

                LinkedList<Adycencia> adyacencias = graph.adycencias(actual);
                for (Integer i = 0; i < adyacencias.getSize(); i++) {
                    Integer destino = adyacencias.get(i).getDestination();
                    if (!visitados[destino]) {
                        visitados[destino] = true;
                        cola.queque(destino);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recorrido;
    }

    public double[][] Floyd() {
        if (graph == null) {
            cargarGrafo();
            if (graph == null)
                return new double[0][0];
        }
        return Floyd.calcularDistancias(graph);
    }

    public double obtenerCaminoMasCortoFloyd(Integer inicio, Integer fin) {
        if (graph == null) {
            cargarGrafo();
            if (graph == null)
                throw new IllegalStateException("Grafo no inicializado");
        }
        return Floyd.obtenerCaminoMasCorto(graph, inicio, fin);
    }

    public double[] BellmanFord(Integer inicio) {
        if (graph == null) {
            cargarGrafo();
            if (graph == null)
                throw new IllegalStateException("Grafo no inicializado");
        }
        return BellmanFord.calcularDistancias(graph, inicio);
    }

    public double obtenerCaminoMasCortoBellmanFord(Integer inicio, Integer fin) {
        if (graph == null) {
            cargarGrafo();
            if (graph == null)
                throw new IllegalStateException("Grafo no inicializado");
        }
        return BellmanFord.obtenerCaminoMasCorto(graph, inicio, fin);
    }

    public String dibujarMatriz() {
        StringBuilder htmlContent = new StringBuilder();
        
        // Obtener matriz de Floyd
        double[][] distancias = Floyd();
        LinkedList<Veterinaria> veterinarias = listAll();
    
        // Estructura HTML
        htmlContent.append("<!DOCTYPE html>\n")
                  .append("<html lang='es'>\n")
                  .append("<head>\n")
                  .append("    <title>Matriz de Distancias y Grafo</title>\n")
                  .append("    <script src='vis.js'></script>\n")
                  .append("    <style>\n")
                  .append("        .container { margin: 20px; }\n")
                  .append("        .distance-matrix { border-collapse: collapse; width: 100%; }\n")
                  .append("        .distance-matrix th, .distance-matrix td { border: 1px solid #ddd; padding: 8px; text-align: center; }\n")
                  .append("        .distance-matrix th { background-color: #f2f2f2; }\n")
                  .append("        h1 { color: #333; }\n")
                  .append("        #mynetwork { width: 600px; height: 400px; border: 1px solid lightgray; }\n")
                  .append("    </style>\n")
                  .append("</head>\n")
                  .append("<body>\n")
                  .append("    <div class='header'>\n")
                  .append("        <p>Muestra el grafo</p>\n")
                  .append("    </div>\n")
                  .append("    <div id='mynetwork'></div>\n")
                  .append("    <script type='text/javascript' src='graph.js'></script>\n")
                  .append("    <div class='container'>\n")
                  .append("        <h1>Matriz de Distancias</h1>\n")
                  .append("        <table class='distance-matrix'>\n");
    
        try {
            // Generar cabecera de la tabla
            htmlContent.append("<tr><th></th>");
            for (int i = 0; i < veterinarias.getSize(); i++) {
                htmlContent.append("<th>").append(veterinarias.get(i).getNombre()).append("</th>");
            }
            htmlContent.append("</tr>\n");
    
            // Generar filas de la matriz
            for (int i = 0; i < distancias.length; i++) {
                htmlContent.append("<tr>")
                          .append("<td><b>").append(veterinarias.get(i).getNombre()).append("</b></td>");
                
                for (int j = 0; j < distancias[i].length; j++) {
                    String distancia = String.format("%.3f", distancias[i][j]);
                    htmlContent.append("<td>").append(distancia).append("</td>");
                }
                htmlContent.append("</tr>\n");
            }
            
            htmlContent.append("        </table>\n")
                      .append("    </div>\n")
                      .append("</body>\n")
                      .append("</html>");
    
            // Guardar archivo
            File file = new File("resources" + File.separatorChar + "graph" + File.separatorChar + "grafo.html");
            FileWriter writer = new FileWriter(file);
            writer.write(htmlContent.toString());
            writer.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return htmlContent.toString();
    }
    


    public void generarGrafo(int tamaño) {
        try {
            // Limpiar grafo existente
            graph = new GraphLabelNoDirect<>(tamaño, Veterinaria.class);

            // Generar datos de prueba
            for (int i = 1; i <= tamaño; i++) {
                Veterinaria vet = new Veterinaria();
                vet.setId(i);
                vet.setNombre("Veterinaria " + i);
                vet.setLtd(Math.random() * 100); // Latitud aleatoria
                vet.setLng(Math.random() * 100); // Longitud aleatoria
                graph.labelsVerticeL(i, vet);
            }

            // Generar adyacencias aleatorias
            for (int i = 1; i <= tamaño; i++) {
                for (int j = 1; j <= tamaño; j++) {
                    if (i != j && Math.random() < 0.3) { // 30% de probabilidad de conexión
                        double peso = calcularDistancia(
                            graph.getLabelL(i).getLtd(), graph.getLabelL(i).getLng(),
                            graph.getLabelL(j).getLtd(), graph.getLabelL(j).getLng()
                        );
                        graph.add_edge(i, j, (float) peso);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para medir tiempo de Floyd
    public double medirTiempoFloyd() {
        long inicio = System.nanoTime();
        Floyd(); // Ejecutar el algoritmo
        long fin = System.nanoTime();
        return (fin - inicio) / 1_000_000.0; // Convertir a milisegundos
    }

    // Método para medir tiempo de Bellman-Ford
    public double medirTiempoBellmanFord() {
        long inicio = System.nanoTime();
        BellmanFord(1); // Ejecutar el algoritmo (asumiendo inicio=1)
        long fin = System.nanoTime();
        return (fin - inicio) / 1_000_000.0; // Convertir a milisegundos
    }
}
