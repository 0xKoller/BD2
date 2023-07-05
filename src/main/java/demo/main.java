package demo;

import com.mongodb.quickstart.Connection;
import java.util.List;
import org.jedis.connectionJedis;
import java.util.Scanner;


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
            UserSession.loginUser();
            String idUser = username.get(0);
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
//                       El usuario seleccione el carrito para ver los elementos
                         connectionJedis.printCartItems(idUser);
                         break;
                     case 2:
                        System.out.print("Ingrese el ID del carrito: ");
                        String cartId = scanner.nextLine();

                        scanner.nextLine();
                        System.out.print("Ingrese el ID del artículo: ");
                        String itemId = scanner.nextLine();

                        System.out.print("Ingrese la cantidad: ");
                        int cantidad = scanner.nextInt();
                        connectionJedis.addItemToCart(cartId,idUser, itemId, cantidad);
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
                         System.out.print("Ingrese el ID del carrito para Eliminar un item: ");
                         String cartIdDel = scanner.nextLine();

                         System.out.print("Ingrese el ID del carrito para Eliminar: ");
                         String itemIdDel = scanner.nextLine();

                         connectionJedis.removeItemCart(cartIdDel,itemIdDel);
                         System.out.print("El item fue borrado con exito ");
                         connectionJedis.printCartItems(idUser);
                         break;
                     case 5:
                         System.out.print("Ingrese el ID del carrito para Eliminar un item: ");
                         String cartIdDelete = scanner.nextLine();

                         connectionJedis.deleteCart(cartIdDelete);
                         System.out.print("Carrito borrado con exito ");
                         break;
                     case 6:

                         break;
                     case 7:
                         Connection.verProductos();
                         break;
                     case 0:
                         UserSession.logoutUser();
                         break;
                 }

             }
         }else{
             while (opt != 0){
                 System.out.println("1.- Ver prodcutos");
                 System.out.println("2.- Modificar producto");
                 System.out.println("3.- Crear usuario");
                 System.out.println("4.- Agregar producto al catalogo");
                 System.out.println("0.- SALIR");
                 System.out.print("Ingrese una opcion: ");
                 opt = scanner.nextInt();
                 switch (opt) {
                     case 1:
                         Connection.verProductos();
                         break;
                     case 2:
                         Connection.actualizarProducto();
                         break;
                     case 3:
                         Connection.crearUsuario();
                         break;
                     case 4:
                         Connection.agregarProducto();
                         break;
                     case 0:
                         break;
                 }
             }
        }
        System.out.println("TPO realizado por: ");
    }
}
