package com.mongodb.quickstart;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Factura {

    private String id;

    private String id_user;
    private int doc;
    private double importe;
    private List<String> productos;
    private String metodo;
    private String id_emple;
    private Date fecha;

    private boolean estado;

    public Factura() {
    }

    public Factura(String id_user, int doc, double importe, List<String> productos, String metodo, String id_emple, Date fecha) {
        this.id_user = id_user;
        this.doc = doc;
        this.importe = importe;
        this.productos = productos;
        this.metodo = metodo;
        this.id_emple = id_emple;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = generarId();
    }

    private static String generarId(){
        Random random = new Random();
        String caracteres = "0123456789";
        int longitud = 10;
        String nuevoId = "";
        boolean idRepetido = true;
        while (idRepetido) {
            StringBuilder sb = new StringBuilder(longitud);
            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(caracteres.length());
                char caracter = caracteres.charAt(indice);
                sb.append(caracter);
            }
            nuevoId = sb.toString();
        }
        return nuevoId;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getDoc() {
        return doc;
    }

    public void setDoc(int doc) {
        this.doc = doc;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public List<String> getProductos() {
        return productos;
    }

    public void setProductos(List<String> productos) {
        this.productos = productos;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getId_emple() {
        return id_emple;
    }

    public void setId_emple(String id_emple) {
        this.id_emple = id_emple;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
