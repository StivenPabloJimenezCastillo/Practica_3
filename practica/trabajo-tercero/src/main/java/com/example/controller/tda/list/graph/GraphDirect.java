package com.example.controller.tda.list.graph;

import com.example.controller.excepcion.OverFlowException;
import com.example.controller.tda.list.LinkedList;

public class GraphDirect extends Graph {
  private Integer nro_vertices;
  private Integer nro_edges;
  private LinkedList<Adycencia> listaAdycencias[];


  public GraphDirect(Integer nro_vertices) {
    this.nro_vertices = nro_vertices;
    this.nro_edges = 0;
    listaAdycencias = new LinkedList[nro_vertices + 1];
    for (int i = 1; i <= nro_vertices; i++) {
      listaAdycencias[i] = new LinkedList<>();
    }
  }

  //@Override
  public Integer nro_edges() {
    // TODO Auto-generated method stub
    return this.nro_edges;
  }

 // @Override
  public Integer nro_vertices() {
    // TODO Auto-generated method stub
    return this.nro_vertices;
  }

//  @Override
  public Boolean is_edge(Integer v1, Integer v2) throws Exception {
    Boolean band = false;
    if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
      LinkedList<Adycencia> lista = listaAdycencias[v1];
      if (!lista.isEmpty()) {
        Adycencia[] matrix = lista.toArray();
        for (int i = 0; i < matrix.length; i++) {
          Adycencia aux = matrix[i];
          if (aux.getDestination().intValue() == v2.intValue()) {
            band = true;
            break;
          }
        }
      }

    } else {
      throw new OverFlowException("Vertices fuera de rango");
    }
    return band;
  }

 // @Override
  public Float weight_edge(Integer v1, Integer v2) throws Exception {
    // TODO Auto-generated method stub
    Float weight = Float.NaN;
    if (is_edge(v1, v2)) {
      LinkedList<Adycencia> lista = listaAdycencias[v1];
      if (!lista.isEmpty()) {
        Adycencia[] matrix = lista.toArray();
        for (int i = 0; i < matrix.length; i++) {
          Adycencia aux = matrix[i];
          if (aux.getDestination().intValue() == v2.intValue()) {
            weight = aux.getWeight();
            break;
          }
        }
      }
    }
    return weight;
  }

  //@Override
  public void add_edge(Integer v1, Integer v2, Float weight) throws Exception{
    // TODO Auto-generated method stub
    if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
      if (!is_edge(v1, v2)) { 
        nro_edges++;
        Adycencia aux = new Adycencia();
        aux.setWeight(weight);
        aux.setDestination(v2);
        listaAdycencias[v1].add(aux);
      }
    } else {
      throw new OverFlowException("Vertices fuera de rango");
    }

  }

  //@Override
  public void add_edge(Integer v1, Integer v2) throws Exception {
      // TODO Auto-generated method stub
      this.add_edge(v1, v2, Float.NaN);
  }

  //@Override
  public LinkedList<Adycencia> adycencias(Integer vi) {
      return listaAdycencias[vi];
  }

  public LinkedList<Adycencia>[] getListaAdycencias() {
    return this.listaAdycencias;
  }

  public void setListaAdycencias(LinkedList<Adycencia>[] listaAdycencias) {
    this.listaAdycencias = listaAdycencias;
  }



  public void setNro_edges(Integer nro_edges) {
    this.nro_edges = nro_edges;
  }
}
