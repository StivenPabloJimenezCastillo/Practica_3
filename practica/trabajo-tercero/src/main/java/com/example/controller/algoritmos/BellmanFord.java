package com.example.controller.algoritmos;

import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.Adycencia;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;

public class BellmanFord {

    public static double[] calcularDistancias(GraphLabelNoDirect<?> graph, Integer inicio) {
        if (graph == null) {
            throw new IllegalStateException("Grafo no inicializado");
        }

        Integer numVertices = graph.nro_vertices();
        double[] distancias = new double[numVertices];

        // Inicializar distancias
        for (int i = 0; i < numVertices; i++) {
            distancias[i] = Double.POSITIVE_INFINITY;
        }
        distancias[inicio - 1] = 0; // El vértice de inicio tiene distancia 0

        // Relajar todas las aristas (V-1) veces
        for (int i = 1; i < numVertices; i++) {
            for (int u = 1; u <= numVertices; u++) {
                LinkedList<Adycencia> adyacencias = graph.adycencias(u);
                try {
                    for (int j = 0; j < adyacencias.getSize(); j++) {
                        Adycencia ady = adyacencias.get(j);
                        Integer v = ady.getDestination() - 1; // Convertir a índice base 0
                        double peso = ady.getWeight();

                        // Relajar la arista
                        if (distancias[u - 1] != Double.POSITIVE_INFINITY &&
                            distancias[v] > distancias[u - 1] + peso) {
                            distancias[v] = distancias[u - 1] + peso;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Verificar si hay ciclos negativos
        for (int u = 1; u <= numVertices; u++) {
            LinkedList<Adycencia> adyacencias = graph.adycencias(u);
            try {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adycencia ady = adyacencias.get(j);
                    Integer v = ady.getDestination() - 1; // Convertir a índice base 0
                    double peso = ady.getWeight();

                    // Si aún se puede relajar, hay un ciclo negativo
                    if (distancias[u - 1] != Double.POSITIVE_INFINITY &&
                        distancias[v] > distancias[u - 1] + peso) {
                        throw new IllegalStateException("El grafo contiene un ciclo negativo");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return distancias;
    }

    public static double obtenerCaminoMasCorto(GraphLabelNoDirect<?> graph, Integer inicio, Integer fin) {
        if (graph == null) {
            throw new IllegalStateException("Grafo no inicializado");
        }

        double[] distancias = calcularDistancias(graph, inicio);

        // Verificar índices válidos
        if (inicio < 1 || inicio > distancias.length || fin < 1 || fin > distancias.length) {
            throw new IllegalArgumentException("Vértices inválidos");
        }

        // Ajustar índices a base 0
        return distancias[fin - 1];
    }
}