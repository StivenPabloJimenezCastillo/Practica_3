package com.example.controller.algoritmos;

import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.Adycencia;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;

public class Floyd {

    public static double[][] calcularDistancias(GraphLabelNoDirect<?> graph) {
        if (graph == null) {
            return new double[0][0];
        }

        Integer numVertices = graph.nro_vertices();
        double[][] distancias = new double[numVertices][numVertices];

        // Inicializar matriz
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distancias[i][j] = (i == j) ? 0 : Double.POSITIVE_INFINITY;
            }
        }

        // Llenar con pesos existentes
        for (int i = 0; i < numVertices; i++) {
            LinkedList<Adycencia> adyacencias = graph.adycencias(i + 1);
            try {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adycencia ady = adyacencias.get(j);
                    int destino = ady.getDestination() - 1;
                    double peso = ady.getWeight();
                    if (peso >= 0) {
                        distancias[i][destino] = peso;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Aplicar algoritmo
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distancias[i][k] != Double.POSITIVE_INFINITY &&
                        distancias[k][j] != Double.POSITIVE_INFINITY &&
                        distancias[i][j] > distancias[i][k] + distancias[k][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }

        return distancias;
    }

    public static double obtenerCaminoMasCorto(GraphLabelNoDirect<?> graph, Integer inicio, Integer fin) {
        if (graph == null) {
            throw new IllegalStateException("Grafo no inicializado");
        }

        double[][] distancias = calcularDistancias(graph);

        // Verificar índices válidos
        if (inicio < 1 || inicio > distancias.length || fin < 1 || fin > distancias.length) {
            throw new IllegalArgumentException("Vértices inválidos");
        }

        // Ajustar índices a base 0
        return distancias[inicio - 1][fin - 1];
    }
}
