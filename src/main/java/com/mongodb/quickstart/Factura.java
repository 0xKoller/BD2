package com.mongodb.quickstart;

import com.mongodb.client.*;
import it.unimi.dsi.fastutil.Hash;
import org.bson.Document;

import java.util.*;

public class Factura {

    private static String id;

    private static  String id_user;
    private  static double importe;
    private static  Document productos;
    private  static boolean metodo;
    private  static String id_emple;
    private static  Date fecha;

    private boolean estado;



    public Factura() {
        this.id = generarId();
        this.fecha = new Date();
    }

    public Factura(String id_user, double importe, Document productos, boolean metodo, String id_emple) {
        this.id_user = id_user;
        this.importe = importe;
        this.productos = productos;
        this.metodo = metodo;
        this.id_emple = id_emple;
        this.id = generarId();
        this.fecha = new Date();
    }

    public static String getId() {
        return id;
    }

    private static String generarId(){
        Random random = new Random();
        String caracteres = "0123456789";
        int longitud = 10;
        String nuevoId = "";
            StringBuilder sb = new StringBuilder(longitud);
            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(caracteres.length());
                char caracter = caracteres.charAt(indice);
                sb.append(caracter);
            }
            nuevoId = sb.toString();

        return nuevoId;
    }

    public  static String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public  static double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }


    public HashMap<String, Object> getProductos(){
        HashMap<String, Object> map = new HashMap<String, Object>(this.productos);
        return map;
    }



    public void setProductos(HashMap<String, Object> productos) {
        float resultado = 0;
        for (Object value : productos.values()) {
            int floatValue = (int) value;
            resultado += floatValue;
        }
        setImporte(resultado);
        Document doc = new Document(productos);
        this.productos = doc;
    }

    public Document getProductosForFactura(){
        Document productos = new Document(this.productos);
        return productos;
    }

    public static  boolean getMetodo() {
        return metodo;
    }

    public void setMetodo(boolean metodo) {
        this.metodo = metodo;
    }

    public  static String getId_emple() {
        return id_emple;
    }

    public  static void setId_emple(String id_emple) {
        Factura.id_emple = id_emple;
    }

    public  static Date getFecha() {
        return fecha;
    }

    public Document toDocument() {
        Document document = new Document();
        document.append("id_user", id_user)
                .append("importe", importe)
                .append("productos", productos)
                .append("metodo", metodo)
                .append("id_emple", id_emple)
                .append("fecha", fecha);
        return document;
    }
}
