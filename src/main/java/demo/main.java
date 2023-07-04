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
            List<String> username = Connection.seleccionUsuario();
            System.out.println("======================");
            System.out.println("Bienvenido "+ username.get(1));
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
                     case 0:
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
        }
        System.out.println("TPO realizado por: ");
    }
}
