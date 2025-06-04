package vista;

import controlador.Controlador;
import java.util.Scanner;
public class Vista {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Controlador controlador = new Controlador();
        int opcion;
        do {
            System.out.printf("\n%25s\n", "=== MENÚ ===");
            System.out.println("1. Agregar producto nuevo");
            System.out.println("2. Agregar stock a producto existente");
            System.out.println("3. Eliminar un producto del inventario");
            System.out.println("4. Mostrar productos");
            System.out.println("5. Registrar venta");
            System.out.println("6. Mostrar todas las facturas guardadas");
            System.out.println("7. Ver estadísticas");
            System.out.println("8. Borrar estadísticas");
            System.out.println("9. Salir");

            while (true) {
                System.out.print("Elija una opción: ");
                if (sc.hasNextInt()) {
                    opcion = sc.nextInt();
                    sc.nextLine();
                    break;
                } else {
                    System.out.println("Debe ingresar un número del 1 al 9.");
                    sc.nextLine();
                }
            }

            controlador.ejecutarOpcion(opcion);
        } while (opcion != 9);

        sc.close();
    }
}
