package com.example.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.controller.dao.services.VeterinariaServices;
import com.example.controller.tda.list.LinkedList;
import com.google.gson.Gson;

@Path("veterinaria")
public class VeterinariaApi {
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        HashMap map = new HashMap<>();
        VeterinariaServices ps = new VeterinariaServices();
        map.put("msg", "OK");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }

        return Response.ok(map).build();
    }

    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam("id") Integer id) {
        HashMap map = new HashMap<>();
        VeterinariaServices ps = new VeterinariaServices();
        try {
            ps.setVeterinaria(ps.get(id));
        } catch (Exception e) {
            // TODO: handle exception
        }
        map.put("msg", "OK");
        map.put("data", ps.getVeterinaria());
        if (ps.getVeterinaria().getId() == null) {
            map.put("data", "No existe la veterinaria con ese identificador");
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }
        return Response.ok(map).build();
    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {
        HashMap res = new HashMap<>();
        Gson g = new Gson();
        String a = g.toJson(map);
        System.out.println("******* " + a);
        try {
            VeterinariaServices ps = new VeterinariaServices();
            ps.getVeterinaria().setNombre(map.get(("nombre")).toString());
            ps.getVeterinaria().setTelefono(map.get(("fono")).toString());
            ps.getVeterinaria().setHorario(map.get(("horario")).toString());
            ps.getVeterinaria().setLtd(Double.parseDouble(map.get("ltd").toString()));
            ps.getVeterinaria().setLng(Double.parseDouble(map.get("lng").toString()));
            ps.save();
            res.put("msg", "OK");
            res.put("data", "Veterinaria registrada correctamente");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Error en sav data " + e.toString());
            res.put("msg", "Error");
            // res.put("msg", "ERROR");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
            // TODO: handle exception
        }

    }

    @Path("/guardar/grafo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarGrafo() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            VeterinariaServices ps = new VeterinariaServices();

            // Verificar si hay menos de 4 veterinarias
            if (ps.listAll().getSize() < 4) {
                res.put("msg", "Error");
                res.put("data", "No hay suficientes Veterinarias (se requieren al menos 4) para crear el grafo");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            } else {
                ps.guardarGrafoEnJson();
                ps.cargarGrafo();
                ps.dibujarMatriz();
                res.put("msg", "OK");
                res.put("data", "Grafo guardado correctamente en JSON");
                return Response.ok(res).build();
            }

        } catch (Exception e) {
            // Manejo de errores y excepciones
            res.put("msg", "Error");
            res.put("data", "Ocurrió un error al procesar la solicitud: " + e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap map) {
        HashMap res = new HashMap<>();

        try {
            VeterinariaServices ps = new VeterinariaServices();
            ps.setVeterinaria(ps.get(Integer.parseInt(map.get("id").toString())));
            ps.getVeterinaria().setNombre(map.get(("nombre")).toString());
            ps.getVeterinaria().setTelefono(map.get(("fono")).toString());
            ps.getVeterinaria().setHorario(map.get(("horario")).toString());
            ps.getVeterinaria().setLtd(Double.parseDouble(map.get("ltd").toString()));
            ps.getVeterinaria().setLng(Double.parseDouble(map.get("lng").toString()));

            ps.update();
            res.put("msg", "OK");
            res.put("data", "Persona registrada correctamente");
            return Response.ok(res).build();

        } catch (Exception e) {
            System.out.println("Error en sav data " + e.toString());
            res.put("msg", "Error");
            // res.put("msg", "ERROR");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
            // TODO: handle exception
        }

    }

    @GET
    @Path("/camino-mas-corto/{inicio}/{fin}/{metodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response caminoMasCorto(
            @PathParam("inicio") Integer inicio,
            @PathParam("fin") Integer fin,
            @PathParam("metodo") String metodo) {

        HashMap<String, Object> mapa = new HashMap<>();
        VeterinariaServices vs = new VeterinariaServices();
        double distanciaMinima = Double.POSITIVE_INFINITY;

        try {
            if (metodo.equalsIgnoreCase("floyd")) {
                distanciaMinima = vs.obtenerCaminoMasCorto(inicio, fin);
            } else if (metodo.equalsIgnoreCase("bellman-ford")) {
                distanciaMinima = vs.obtenerCaminoMasCortoBellmanFord(inicio, fin);
            } else {
                mapa.put("msg", "Error");
                mapa.put("data", "Método no válido. Use 'floyd' o 'bellman-ford'.");
                return Response.status(Status.BAD_REQUEST).entity(mapa).build();
            }

            mapa.put("msg", "OK");
            mapa.put("metodo", metodo);
            mapa.put("data", distanciaMinima);
        } catch (Exception e) {
            mapa.put("msg", "Error");
            mapa.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(mapa).build();
        }

        return Response.ok(mapa).build();
    }

    @GET
    @Path("/floyd")
    @Produces(MediaType.APPLICATION_JSON)
    public Response floyd() {
        HashMap<String, Object> mapa = new HashMap<>();
        VeterinariaServices vs = new VeterinariaServices();

        try {
            double[][] distancias = vs.Floyd();
            mapa.put("msg", "OK");
            mapa.put("data", distancias);
        } catch (Exception e) {
            mapa.put("msg", "Error");
            mapa.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(mapa).build();
        }

        return Response.ok(mapa).build();
    }

    @GET
    @Path("/comparar-tiempos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response compararTiempos() {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> tablaTiempos = new HashMap<>();
    
        try {
            VeterinariaServices vs = new VeterinariaServices();
    
            // Medir tiempos para 10, 20 y 30 datos
            for (int tamaño : new int[]{10, 20, 30}) {
                vs.generarGrafo(tamaño); // Generar grafo con el tamaño actual
    
                // Medir Floyd
                double tiempoFloyd = vs.medirTiempoFloyd();
                tablaTiempos.put("Floyd (" + tamaño + " datos)", tiempoFloyd);
    
                // Medir Bellman-Ford
                double tiempoBellmanFord = vs.medirTiempoBellmanFord();
                tablaTiempos.put("Bellman-Ford (" + tamaño + " datos)", tiempoBellmanFord);
            }
    
            response.put("msg", "OK");
            response.put("data", tablaTiempos);
            return Response.ok(response).build();
    
        } catch (Exception e) {
            response.put("msg", "Error");
            response.put("data", "Error al medir tiempos: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
