package com.example.controller.tda.list.graph;

import java.util.Random;

import com.example.controller.tda.list.LinkedList;
import com.example.models.Veterinaria;


public abstract class Graph {
    public abstract Integer nro_vertices();
    public abstract Integer nro_edges();
    public abstract Boolean is_edge(Integer v1, Integer v2) throws Exception;
    public abstract Float weight_edge(Integer v1, Integer v2) throws Exception;
    public abstract void add_edge(Integer v1, Integer v2) throws Exception;
    public abstract void add_edge(Integer v1, Integer v2, Float weight) throws Exception;
    public abstract LinkedList<Adycencia> adycencias(Integer vi);

    @Override
    public String toString(){
        String grafo = "";
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo += "V" + i + "\n";
                LinkedList<Adycencia> lista = adycencias(i);
                if(!lista.isEmpty()){
                    Adycencia[] ady = lista.toArray();
                    for(int j = 0; j < ady.length; j++){
                        Adycencia a = ady[j];
                        grafo += "ady " + "V" + a.getDestination() + " weight: " + a.getWeight() + "\n";
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return grafo;
    }

}
