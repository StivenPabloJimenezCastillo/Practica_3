package com.example.controller.dao.services;

import com.example.controller.dao.VeterinariaDao ;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.models.Veterinaria;

public class VeterinariaServices {
    private VeterinariaDao  obj;
    public LinkedList listAll() {
        return obj.getListAll();
    }

    public VeterinariaServices() {
        obj = new VeterinariaDao ();
    }
    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

   
    public Veterinaria getVeterinaria() {
        return obj.getVeterinaria();
    }

    public void setVeterinaria(Veterinaria veterinaria) {
        obj.setVeterinaria(veterinaria);
    }
    
    public Veterinaria get(Integer id) throws Exception {
        return obj.get(id);
    }

    public String dibujarMapa() {
        return obj.dibujarMapa();
    }

    public void createGraph() {
        obj.createGraph();
    }

    public void guardarGrafoEnJson() {
        obj.guardarGrafoEnJson();
    }

    public void cargarGrafo() {
        obj.cargarGrafo();
    }

    public double[][] Floyd() {
        return obj.Floyd();
    }

    public LinkedList<Integer> bfs(int inicio) {
        return obj.bfs(inicio);
    }

    public double obtenerCaminoMasCorto(int inicio, int fin) {
        return obj.obtenerCaminoMasCortoFloyd(inicio, fin);
    }

    public double[] BellmanFord(Integer inicio) {
        return obj.BellmanFord(inicio);
    }

    public double obtenerCaminoMasCortoBellmanFord(Integer inicio, Integer fin) {
        return obj.obtenerCaminoMasCortoBellmanFord(inicio, fin);
    }

    public double medirTiempoFloyd(){
        return obj.medirTiempoFloyd();
    }

    public double medirTiempoBellmanFord() {
        return obj.medirTiempoBellmanFord();
    }

    public void generarGrafo(Integer tamaño) {
        obj.generarGrafo(tamaño);
    }

    public String dibujarMatriz() {
        return obj.dibujarMatriz();
    }

}
