package com.example.controller.tda.list.graph;

import com.example.controller.excepcion.OverFlowException;

public class GraphNoDirect extends GraphDirect {
    public GraphNoDirect(Integer nro_vertices) {
        super(nro_vertices);
    }

    public void add_edge(Integer v1, Integer v2, Float weight) throws Exception {
        if (v1.intValue() <= nro_vertices() && v2.intValue() <= nro_vertices()) {
            if (!is_edge(v1, v2)) {
                //nro_edges++;
                setNro_edges(nro_edges() + 1);
                Adycencia aux = new Adycencia();
                aux.setWeight(weight);
                aux.setDestination(v2);
                getListaAdycencias()[v1].add(aux);

                Adycencia auxD = new Adycencia();
                auxD.setWeight(weight);
                auxD.setDestination(v1);
                getListaAdycencias()[v2].add(auxD);
            }
        } else {
            throw new OverFlowException("Vertices fuera de rango");
        }
        super.add_edge(v1, v2, weight);
    }
}
