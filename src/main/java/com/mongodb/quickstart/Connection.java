package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "tpo";
    private static final String COLLECTION_NAME = "productos";

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

            // Agregar un nuevo producto
            //agregarProducto("Producto 1", "Descripción del producto 1", 9.99);

            // Ver todos los productos
            verProductos();
        }
    }

    public static void agregarProducto(String nombre, String descripcion, double precio) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document producto = new Document()
                    .append("nombre", nombre)
                    .append("descripcion", descripcion)
                    .append("precio", precio);

            collection.insertOne(producto);
            System.out.println("Producto agregado correctamente.");
        }
    }

    public static void verProductos() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                System.out.println(document.toJson());
            }
        }
    }
}