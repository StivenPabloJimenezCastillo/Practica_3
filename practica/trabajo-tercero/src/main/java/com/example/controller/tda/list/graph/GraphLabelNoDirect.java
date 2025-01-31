package com.example.controller.tda.list.graph;

import java.util.Random;

import com.example.controller.excepcion.LabelException;
import com.example.models.Veterinaria;

public class GraphLabelNoDirect<E> extends GraphLabelDirect<E> {
    public GraphLabelNoDirect(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices, clazz);
    }


    public void insertEdgeL(E v1, E v2, Float weight) throws Exception {
        if (isLabelsGraph()) {
            add_edge(getVerticeL(v1), getVerticeL(v2), weight);
            add_edge(getVerticeL(v2), getVerticeL(v1), weight);
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
    }
    
}
