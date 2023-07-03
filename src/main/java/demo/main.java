package demo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import org.jedis.connectionJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

import java.util.Scanner;
// import com.datastax.driver.core.Cluster;
// import com.datastax.driver.core.Host;
// import com.datastax.driver.core.Metadata;
// import com.datastax.driver.core.Session;

import static java.lang.System.out;

public class main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//      String connectionString = "mongodb://localhost:27017";
//        MongoClient mongoClient = MongoClients.create(connectionString);

//        Establezco conexion con Mongo
//        MongoDatabase database = mongoClient.getDatabase("tpo");

//        Obtener colleccion de mongo
//                MongoCollection<Document> collection = database.getCollection("usuarios");
//        FindIterable<Document> documents = collection.find();
//        MongoCursor<Document> cursor = documents.iterator();
//        while (cursor.hasNext()) {
//            Document document = cursor.next();
//            System.out.println(document.toJson());
//        }
        System.out.println("TPO BD II");
        System.out.println("-----------------");
        System.out.println("MENU");
        System.out.println("-----------------");
        System.out.println("1.- Usuario");
        System.out.println("2.- Admin");
        System.out.print("Ingrese una opcion: ");
        Scanner scanner = new Scanner(System.in);
        int opt = scanner.nextInt();

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
         if (opt == 1){



             while (opt != 0){

                 System.out.println("1.- Ver carrito");
                 System.out.println("2.- Agregar al carrito");
                 System.out.println("3.- Actualizar al carrito");
                 System.out.println("4.- Eliminar producto del carrito");
                 System.out.println("5.- Eliminar al carrito");
                 System.out.println("6.- Facturar carrito");
                 System.out.println("7.- Volver un paso atras en un carrito");
                 System.out.println("0.- SALIR");
                 System.out.print("Ingrese una opcion: ");
                 int opt2 = scanner.nextInt();
                 while (opt2 != -1) {
                     if (opt2 == 1){
                         System.out.print("Ingrese el ID del carrito para ver: ");
                         String cartIdBuscar = scanner.next();

                         // Obtener la cantidad de elementos en el carrito
                         connectionJedis.printCartItems(cartIdBuscar);
                         opt2 = 0;
                     } else if (opt2 == 2) {
                         System.out.print("Ingrese el ID del carrito: ");
                         String cartId = scanner.next();

                         System.out.print("Ingrese el ID del cliente: ");
                         String clienteId = scanner.next();

                         System.out.print("Ingrese el ID del artículo: ");
                         String itemId = scanner.next();

                         System.out.print("Ingrese la cantidad: ");
                         int cantidad = scanner.nextInt();

                         scanner.nextLine();


                         // Agregar elementos al carrito
                         connectionJedis.addItemToCart(cartId,clienteId, itemId, cantidad);
                         opt2 = 0;

                     } else if(opt2 == 3){
                         // Actualizar el carrito
                         System.out.print("Ingrese el ID del carrito para actualizar: ");
                         String cartIdUpdate = scanner.next();
                         System.out.print("Ingrese el ID del item para actualizar: ");
                         String itemIdUpdate = scanner.next();
                         System.out.print("Ingrese la nueva cantidad: ");
                         int cantidadNueva = scanner.nextInt();
                         connectionJedis.updateCartItemQuantity(cartIdUpdate,itemIdUpdate,cantidadNueva);
                         opt2 = 0;
                     } else if (opt2 == 4){
                         System.out.print("Ingrese el ID del carrito para eliminar un producto: ");
                         String cartIdDel = scanner.next();
                         System.out.print("Ingrese el ID del item para eliminar: ");
                         String itemIdDel = scanner.next();
                         connectionJedis.removeItemCart(cartIdDel,itemIdDel);
                         opt2 = 0;
                     } else if (opt2 == 5){
                         System.out.print("Ingrese el ID del carrito a eliminar : ");
                         String cartIdDelete = scanner.next();
                         connectionJedis.deleteCart(cartIdDelete);
                         opt2 = 0;
                     } else if (opt2 == 7){
                         System.out.print("Ingrese el ID del carrito para volver un paso atras: ");
                         String cartIdUndo = scanner.next();
                         connectionJedis.undo(cartIdUndo);
                         connectionJedis.printCartItems(cartIdUndo);
                         opt2 = 0;

                     } else if (opt2 == 0){
                         opt = 0;
                     }
                 }
             }
         }else{
             while (opt != 0){


                 System.out.println("0.- SALIR");
             }

         }

        System.out.println("2.- Mostrar datos de Redis");
        System.out.println("3.- Mostrar datos de Cassandra");
        System.out.println("4.- Mostrar datos de ObjectDB");
        System.out.println("5.- Modificacion de productos del carrito de compras");
        System.out.println("6.- Recuperar estado anterior de carrito");

//        System.out.println("FIN de Mongo");
//        System.out.println("INICIO de REDIS");
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
//        // Agregar elementos al carrito
//        connectionJedis.addItemToCart(cartId,clienteId, itemId, cantidad);
//
//        System.out.print("Ingrese el ID del carrito para ver: ");
//        String cartIdBuscar = scanner.nextLine();
//
//        // Obtener la cantidad de elementos en el carrito
//        connectionJedis.printCartItems(cartIdBuscar);
//
//        // Actualizar el carrito
//        System.out.print("Ingrese el ID del carrito para actualizar: ");
//        String cartIdUpdate = scanner.nextLine();
//        System.out.print("Ingrese el ID del item para actualizar: ");
//        String itemIdUpdate = scanner.nextLine();
//        System.out.print("Ingrese la nueva cantidad: ");
//        int cantidadNueva = scanner.nextInt();
//        connectionJedis.updateCartItemQuantity(cartIdUpdate,itemIdUpdate,cantidadNueva);
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
