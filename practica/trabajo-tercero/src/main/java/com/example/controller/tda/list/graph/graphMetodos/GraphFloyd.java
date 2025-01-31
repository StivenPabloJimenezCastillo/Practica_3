package com.example.controller.tda.list.graph.graphMetodos;
public class GraphFloyd {
    private Float[][] matrizDistancias;
    private Integer numVertices;

    public GraphFloyd(Integer numVertices) {
        this.numVertices = numVertices;
        this.matrizDistancias = new Float[numVertices + 1][numVertices + 1];
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (i == j) {
                    matrizDistancias[i][j] = 0.0f;
                } else {
                    matrizDistancias[i][j] = Float.MAX_VALUE;
                }
            }
        }
    }

    public void agregarArista(Integer v1, Integer v2, Float peso) {
        matrizDistancias[v1][v2] = peso;
    }

    public void calcularCaminoMasCorto() {
        for (int k = 1; k <= numVertices; k++) {
            for (int i = 1; i <= numVertices; i++) {
                for (int j = 1; j <= numVertices; j++) {
                    if (matrizDistancias[i][k] != Integer.MAX_VALUE && matrizDistancias[k][j] != Float.MAX_VALUE) {
                        Float distanciaNueva = matrizDistancias[i][k] + matrizDistancias[k][j];
                        if (distanciaNueva < matrizDistancias[i][j]) {
                            matrizDistancias[i][j] = distanciaNueva;
                        }
                    }
                }
            }
        }
    }

    public Float getCaminoMasCorto(Integer v1, Integer v2) {
        return matrizDistancias[v1][v2];
    }

}