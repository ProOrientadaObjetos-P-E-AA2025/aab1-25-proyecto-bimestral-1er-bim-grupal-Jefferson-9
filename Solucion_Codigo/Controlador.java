package controlador;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Archivos;
import modelo.Factura;
import modelo.ItemVenta;
import modelo.Producto;

public class Controlador {
    Scanner sc;
    Archivos archivo;
    ArrayList<Producto> inventario;

    public Controlador() {
        sc = new Scanner(System.in);
        archivo = new Archivos();
        inventario = archivo.cargarLista();
    }

    public void ejecutarOpcion(int opcion) {
        try {

            switch (opcion) {
                case 1:
                    System.out.println("\n=== INGRESO DE PRODUCTOS AL INVENTARIO ===");
                    String nombre = "";
                    while (true) {
                        System.out.print("Nombre: ");
                        nombre = sc.nextLine().trim();
                        if (!nombre.isEmpty()) {
                            break;
                        } else {
                            System.out.println("El nombre no puede estar vacío.");
                        }
                    }

                    double precio = -1;
                    while (true) {
                        System.out.print("Precio: ");
                        if (sc.hasNextDouble()) {
                            precio = sc.nextDouble();
                            sc.nextLine();
                            if (precio > 0) {
                                break;
                            } else {
                                System.out.println("El precio debe ser mayor que cero.");
                            }
                        } else {
                            System.out.println("Ingrese un valor numérico.");
                            sc.nextLine();
                        }
                    }

                    int cantidad = -1;
                    while (true) {
                        System.out.print("Cantidad: ");
                        if (sc.hasNextInt()) {
                            cantidad = sc.nextInt();
                            sc.nextLine();
                            if (cantidad > 0) {
                                break;
                            } else {
                                System.out.println("La cantidad debe ser mayor que cero.");
                            }
                        } else {
                            System.out.println("Ingrese un número entero");
                            sc.nextLine();
                        }
                    }

                    String caducidad = "";
                    while (true) {
                        System.out.print("Caducidad (AAAA/MM/DD) o (indefinido): ");
                        caducidad = sc.nextLine();
                        if (caducidad.isEmpty() || caducidad.equalsIgnoreCase("indefinido")) {
                            caducidad = "indefinido";
                            break;
                        } else {
                            try {
                                LocalDate.parse(caducidad.replace("/", "-"));
                                break;
                            } catch (DateTimeParseException e) {
                                System.out.println("Formato inválido, use AAA/MM/DD o escriba 'indefinido'.");
                                continue;
                            }
                        }
                    }

                    String categoriasValidas[] = { "vivienda", "educacion", "alimentacion", "vestimenta", "salud" };
                    String categoria = "";
                    while (true) {
                        System.out.print("Categoría (vivienda, educacion, alimentacion, vestimenta, salud): ");
                        categoria = sc.nextLine().trim();
                        boolean valida = false;
                        for (String cat : categoriasValidas) {
                            if (cat.equalsIgnoreCase(categoria)) {
                                categoria = cat;
                                valida = true;
                                break;
                            }
                        }
                        if (valida) {
                            break;
                        } else {
                            System.out.println("Categoría no válida, intente otra vez.");
                        }
                    }

                    Producto articulo = new Producto(nombre, precio, cantidad, caducidad, categoria);
                    inventario.add(articulo);
                    archivo.guardarLista(inventario);
                    System.out.println("Producto agregado correctamente.");
                    break;

                case 2:
                    System.out.printf("\n%73s\n", "=== AGREGAR STOCK ===");
                    mostrarInventario(inventario);
                    System.out.print("Ingrese el nombre del producto que desea agregar stock: ");
                    String nombreBuscar = sc.nextLine();

                    boolean encontrado = false;
                    for (Producto p : inventario) {
                        if (p.getNombre().equalsIgnoreCase(nombreBuscar)) {
                            int agregar = -1;

                            while (true) {
                                System.out.print("Ingrese la cantidad a agregar: ");
                                if (sc.hasNextInt()) {
                                    agregar = sc.nextInt();
                                    sc.nextLine();
                                    if (agregar > 0) {
                                        break;
                                    } else {
                                        System.out.println("La cantidad debe ser mayor que cero.");
                                    }
                                } else {
                                    System.out.println("Ingrese solo números.");
                                    sc.nextLine();
                                }
                            }

                            p.setStock(p.getStock() + agregar);
                            System.out.println("Stock actualizado, nuevo stock: " + p.getStock());
                            encontrado = true;
                            break;
                        }

                    }

                    if (!encontrado) {
                        System.out.println("El producto no fue encontrado en el inventario.");
                        break;
                    }

                    archivo.guardarLista(inventario);
                    break;

                case 3:
                    Controlador.eliminarProducto(inventario, sc);
                    archivo.guardarLista(inventario);
                    break;

                case 4:
                    System.out.println(
                            "\nLos productos que esten en promoción, tienen el 20% de descuento al comprar");
                    mostrarInventario(inventario);
                    break;

                case 5:
                    System.out.printf("\n%62s\n", "=== VENTA ===");
                    if (inventario.isEmpty()) {
                        System.out.println("No hay productos en el inventario.");
                        break;
                    }
                    Archivos archivos = new Archivos();
                    ArrayList<ItemVenta> itemsVenta = new ArrayList<>();
                    String respuesta = "n";
                    Factura factura = null;

                    do {
                        mostrarInventarioIndice(inventario);

                        int indice = -1;
                        while (true) {
                            System.out.print("Ingrese el índice del producto que desea comprar: ");
                            if (sc.hasNextInt()) {
                                indice = sc.nextInt();
                                sc.nextLine();

                                if (indice - 1 >= 0 && indice - 1 < inventario.size()) {
                                    break;
                                } else {
                                    System.out.println("Índice fuera de rango");
                                }

                            } else {
                                System.out.println("Debe ingresar un número válido");
                                sc.nextLine();
                            }

                        }

                        Producto seleccionado = inventario.get(indice - 1);

                        int cantCompra = -1;
                        while (true) {
                            System.out.print("Ingrese la cantidad que desea comprar: ");
                            if (sc.hasNextInt()) {
                                cantCompra = sc.nextInt();
                                sc.nextLine();

                                if (cantCompra > 0 && cantCompra <= seleccionado.getStock()) {
                                    break;
                                } else if (cantCompra <= 0) {
                                    System.out.println("La cantidad debe ser meyor que 0");
                                } else {
                                    System.out.println(
                                            "No hay suficiente stock. Stock actual: " + seleccionado.getStock());
                                }

                            } else {
                                System.out.println("Debe ingresar un número válido");
                                sc.nextLine();
                            }

                        }

                        // Agrega al detalle venta
                        itemsVenta.add(new ItemVenta(seleccionado, cantCompra));

                        // Preguntar si quiere agg otro producto
                        System.out.print("¿Desea agregar otro producto? (s/n): ");
                        respuesta = sc.nextLine();

                    } while (respuesta.equalsIgnoreCase("s"));

                    System.out.print("¿Desea factura?(s/n): ");
                    String deseafact = sc.nextLine();

                    if (deseafact.equalsIgnoreCase("s")) {
                        System.out.print("Ingrese el nombre del cliente: ");
                        String nombreCliente = sc.nextLine();

                        System.out.print("Ingrese el número de cédula del cliente: ");
                        String cedulaCliente = sc.nextLine();

                        System.out.print("Ingrese el teléfono del cliente: ");
                        String telefonoCliente = sc.nextLine();

                        System.out.print("Ingrese la dirección del cliente: ");
                        String direccionCliente = sc.nextLine();

                        factura = new Factura(nombreCliente, cedulaCliente, telefonoCliente, direccionCliente);

                        for (ItemVenta item : itemsVenta) {
                            factura.agregarProducto(item.getProducto(), item.getCantidad());
                        }

                        factura.calcTotales();
                        archivos.guardarFactura(factura);

                        System.out.print("\n¿Desea ver la factura?(s/n):");
                        String verFact = sc.nextLine();

                        if (verFact.equalsIgnoreCase("s")) {
                            archivos.mostrarUltFactura();
                            factura.mostrarImpuestosPorcategoria();
                        }
                    } else {
                        notaVenta(itemsVenta);
                    }

                    for (ItemVenta item : itemsVenta) {
                        Producto prod = item.getProducto();
                        int nuevoStock = prod.getStock() - item.getCantidad();
                        prod.setStock(nuevoStock);
                    }

                    archivos.guardarLista(inventario);

                    if (deseafact.equalsIgnoreCase("s") && factura != null) {
                        archivo.guardarEstadistica(factura);
                    }
                    System.out.println("Venta realizada con éxito.");
                    break;

                case 6:
                    System.out.printf("\n%60s\n", "=== MOSTRAR TODAS LAS FACTURAS ===");
                    archivo.mostrarFacturas();
                    break;

                case 7:
                    System.out.printf("\n%55s\n", "=== MOSTRAR ESTADÍSTICAS ===");
                    archivo.mostrarEstadisticas();
                    break;

                case 8:
                    System.out.println("\n=== BORRAR ESTADÍSTICAS ===");
                    System.out.print("¿Seguro(a) que desea borrar todas las estadísticas? (s/n): ");
                    String confirmar = sc.nextLine();

                    if (confirmar.equalsIgnoreCase("s")) {
                        archivo.borrarEstadisticas();
                    } else {
                        System.out.println("Operación cancelada");
                    }
                    break;

                case 9:
                    System.out.println("¡Hasta Pronto!");
                    break;

                default:
                    System.out.println("Opción inválida, intente otra vez.");

            }

        } catch (Exception e) {
            System.err.println("Error al procesar: " + e.getMessage());
        }
    }

    public static void mostrarInventario(ArrayList<Producto> inventario) {
        System.out.printf("\n%77s\n", "=== INVENTARIO COMPLETO ===");
        System.out.println(
                "+-----------------------------------------------------------------------------------------------------------------------------+");
        if (inventario.isEmpty()) {
            System.out.println("No hay productos en el inventario.");
        } else {
            for (Producto p : inventario) {
                System.out.println(p);
            }
        }
        System.out.println(
                "+-----------------------------------------------------------------------------------------------------------------------------+");
    }

    public static void eliminarProducto(ArrayList<Producto> inventario, Scanner sc) {
        if (inventario.isEmpty()) {
            System.out.println("No hay productos en el inventario");
            return;
        }
        System.out.println("=== ELIMINAR PRODUCTO ===");
        mostrarInventarioIndice(inventario);

        System.out.print("Ingrese el número del producto que desea eliminar: ");
        int indice = sc.nextInt();
        sc.nextLine();

        if (indice < 1 || indice > inventario.size()) {
            System.out.println("Índice inválido");
        } else {
            Producto eliminado = inventario.remove(indice - 1);
            System.out.println("Producto eliminado: " + eliminado.getNombre());
        }
    }

    public static void mostrarInventarioIndice(ArrayList<Producto> inventario) {
        System.out.printf("\n%70s\n", "=== INVENTARIO DISPONIBLE ===");
        System.out.println(
                "+------------------------------------------------------------------------------------------------------------------+");
        if (inventario.isEmpty()) {
            System.out.println("No hay productos en el inventario");
            return;
        }

        for (int i = 0; i < inventario.size(); i++) {
            Producto p = inventario.get(i);
            System.out.printf("|%-3d| %-25s | Precio: $%-8.2f | Stock: %-4d | Vence: %-10s | Categoría: %-15s |\n",
                    i + 1,
                    p.getNombre(),
                    p.getPrecio(),
                    p.getStock(),
                    p.getCaducidad(),
                    p.getCategoria());
        }
        System.out.println(
                "+------------------------------------------------------------------------------------------------------------------+");
    }

    public static void notaVenta(ArrayList<ItemVenta> itemsVenta) {
        System.out.printf("\n%55s\n", "=== NOTA DE VENTA ===");
        System.out.println("\n+----------------------------------------------------------------------------------+");
        double total = 0;

        for (ItemVenta item : itemsVenta) {
            Producto prod = item.getProducto();
            double subtotal = prod.getPrecio() * item.getCantidad();
            System.out.printf("| Producto: %-30s | Cantidad: %-3d | Subtotal: $%10.2f |\n", prod.getNombre(),
                    item.getCantidad(), subtotal);

            total += subtotal;
        }

        System.out.println("+----------------------------------------------------------------------------------+");
        System.out.printf("|%66s $%13.2f |\n", "| Total a pagar:", total);
        System.out.println("+----------------------------------------------------------------------------------+\n");
    }
}