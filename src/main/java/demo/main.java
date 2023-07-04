package demo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.quickstart.Connection;
import org.bson.Document;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.jedis.connectionJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

import java.util.Scanner;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

import static java.lang.System.out;

public class main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String connectionString = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);

//      Establezco conexion con Mongo
        MongoDatabase database = mongoClient.getDatabase("tpo");

//      Obtener colleccion de mongo
        MongoCollection<Document> collection = database.getCollection("usuarios");
        Scanner scanner = new Scanner(System.in);
        int opt = -1;
        while (opt != 1 && opt != 2){

            System.out.println("TPO BD II");
            System.out.println("-----------------");
            System.out.println("MENU");
            System.out.println("-----------------");
            System.out.println("1.- Usuario");
            System.out.println("2.- Admin");
            System.out.print("Ingrese una opcion: ");
            opt = scanner.nextInt();
        }
        if (opt == 1) {
            int user = -1;
            // Obtener todos los documentos de la colección
            MongoCursor<Document> cursor = collection.find().iterator();

            // Array para almacenar los IDs de los usuarios
            Map<String, String> usuarios = new HashMap<>();
            // Iterar sobre los documentos
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String id = document.getString("id");
                String nombre = document.getString("name");

                // Imprimir el nombre y el ID
                System.out.println("Nombre: " + nombre + " | ID: " + id);

                // Agregar el ID al array
                usuarios.put(id,nombre);
            }
            String userInput;
            // Bucle para validar la entrada del usuario
            while (true) {
                System.out.print("Ingrese un ID válido: ");
                userInput = scanner.nextLine();
                if (usuarios.containsKey(userInput)) {
                    break; // Salir del bucle si la entrada es válida
                } else {
                    System.out.println("ID inválido. Inténtelo nuevamente.");
                }
            }
            String username = usuarios.get(userInput);
            System.out.println("======================");
            System.out.println("Bienvenido "+ username);
             while (opt != 0){
                 System.out.println("1.- Ver carritos");
                 System.out.println("2.- Agregar al carrito");
                 System.out.println("3.- Actualizar al carrito");
                 System.out.println("4.- Eliminar producto del carrito");
                 System.out.println("5.- Eliminar el carrito");
                 System.out.println("6.- Facturar carrito");
                 System.out.println("7.- Ver prodcutos");
                 System.out.println("0.- SALIR");
                 System.out.print("Ingrese una opcion: ");
                 opt = scanner.nextInt();
                 switch (opt) {
                     case 1:
//                         El usuario seleccione el carrito para ver los elementos
                         System.out.print("Ingrese el ID del carrito para ver sus elementos: ");
                         String cartIdBuscar = scanner.nextLine();
                         connectionJedis.printCartItems(cartIdBuscar);
                         break;
                     case 2:
                        System.out.print("Ingrese el ID del carrito: ");
                        String cartId = scanner.nextLine();
                        System.out.print("Ingrese el ID del cliente: ");
                        String clienteId = scanner.nextLine();
                        System.out.print("Ingrese el ID del artículo: ");
                        String itemId = scanner.nextLine();
                        System.out.print("Ingrese la cantidad: ");
                        int cantidad = scanner.nextInt();
                        connectionJedis.addItemToCart(cartId,clienteId, itemId, cantidad);
                        break;
                     case 3:
                        System.out.print("Ingrese el ID del carrito para actualizar: ");
                        String cartIdUpdate = scanner.nextLine();
                        System.out.print("Ingrese el ID del item para actualizar: ");
                        String itemIdUpdate = scanner.nextLine();
                        System.out.print("Ingrese la nueva cantidad: ");
                        int cantidadNueva = scanner.nextInt();
                        connectionJedis.updateCartItemQuantity(cartIdUpdate,itemIdUpdate,cantidadNueva);
                        break;
                     case 4:
                         System.out.println("4.- Eliminar producto del carrito");
                         break;
                     case 5:

                         System.out.println("5.- Eliminar el carrito");
                         break;
                     case 6:

                         System.out.println("6.- Facturar carrito");
                         break;
                     case 7:

                         Connection.verProductos();
                         break;
                     default:
                         System.out.println("Opción inválida");
                         break;
                 }
             }
         }else{
             while (opt != 0){


                 System.out.println("0.- SALIR");
                 opt = scanner.nextInt();
             }

//
//
//
//        System.out.print("Ingrese el ID del carrito: ");
//        String cartId = scanner.nextLine();
//
//        System.out.print("Ingrese el ID del cliente: ");
//        String clienteId = scanner.nextLine();
//
//        System.out.print("Ingrese el ID del artículo: ");
//        String itemId = scanner.nextLine();
//
//        System.out.print("Ingrese la cantidad: ");
//        int cantidad = scanner.nextInt();
//
//        scanner.nextLine();
//
//
//
//
//        // Obtener la cantidad de elementos en el carrito
//



        }
        System.out.println("TPO realizado por: ");
    }

    public static void crearUsuario(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre");
        String name = scanner.nextLine();
        System.out.print("Direccion");
        String address = scanner.nextLine();
        System.out.print("Documento");
        int doc = scanner.nextInt();
    }

}
