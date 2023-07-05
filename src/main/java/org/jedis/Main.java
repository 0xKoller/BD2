package org.jedis;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el ID del carrito: ");
        String cartId = scanner.nextLine();

        System.out.print("Ingrese el ID del cliente: ");
        String clienteId = scanner.nextLine();

        System.out.print("Ingrese el ID del artículo: ");
        String itemId = scanner.nextLine();

        System.out.print("Ingrese la cantidad: ");
        int cantidad = scanner.nextInt();

        scanner.nextLine();


        // Agregar elementos al carrito
        connectionJedis.addItemToCart(cartId,clienteId, itemId, cantidad);

        System.out.print("Ingrese el ID del carrito para ver: ");
        String cartIdBuscar = scanner.nextLine();

        // Obtener la cantidad de elementos en el carrito
//        connectionJedis.printCartItems();

        // Actualizar el carrito
        System.out.print("Ingrese el ID del carrito para actualizar: ");
        String cartIdUpdate = scanner.nextLine();
        System.out.print("Ingrese el ID del item para actualizar: ");
        String itemIdUpdate = scanner.nextLine();
        System.out.print("Ingrese la nueva cantidad: ");
        int cantidadNueva = scanner.nextInt();
        connectionJedis.updateCartItemQuantity(cartIdUpdate,itemIdUpdate,cantidadNueva);

    }
}
