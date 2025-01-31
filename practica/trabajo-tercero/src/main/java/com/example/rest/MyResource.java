package com.example.rest;

import java.util.Arrays;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.controller.dao.services.VeterinariaServices;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.models.Veterinaria;

@Path("myresource")
public class MyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt() {        
        HashMap mapa = new HashMap<>();        
        GraphLabelNoDirect graph = new GraphLabelNoDirect<>(5, String.class);
        try{
            VeterinariaServices vs = new VeterinariaServices();
            LinkedList<Veterinaria> lv = vs.listAll();
            if (!lv.isEmpty()) {
                graph = new GraphLabelNoDirect<>(lv.getSize(), Veterinaria.class);
                Veterinaria[] m = lv.toArray();
                for (int i = 0; i < lv.getSize(); i++) {
                    graph.labelsVerticeL((i+1), m[i]);
                }
            }
            graph.drawGraph();
            vs.cargarGrafo();
            vs.dibujarMapa();
            vs.dibujarMatriz();
            System.err.println(vs.obtenerCaminoMasCorto(1, 5));
            System.err.println(Arrays.deepToString(vs.Floyd()));
            System.err.println(graph.toString());

        } catch(Exception e){
            mapa.put("msg", "Ok");
            mapa.put("data", e.toString());
            return Response.status(Status.BAD_REQUEST).entity(mapa).build();
        }
        System.out.println(graph.toString());
        
        mapa.put("msg", "OK");        
        mapa.put("data", graph.toString());
        return Response.ok(mapa).build();
    }
}
