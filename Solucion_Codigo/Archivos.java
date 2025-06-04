package modelo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Archivos {
    private String archivo = "data/inventario.dat";
    private String archivoFacturas = "data/facturas.txt";
    private String archivoEstadisticas = "data/estadisticas.txt";

    public void crearCarpetasinoExiste() {
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            if (carpeta.mkdirs()) {
                System.out.println("Carpeta 'data' creada correctamente.");
            } else {
                System.out.println("Error al crear la carpeta 'data'.");
            }
        }
    }

    // Se guarda la lista completa en el archivo
    public void guardarLista(ArrayList<Producto> lista) {
        crearCarpetasinoExiste();
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(archivo))) {
            salida.writeObject(lista);
            System.out.println("Inventario guardado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar inventario: " + e.getMessage());
        }
    }

    // Se lee la lista completa guardada en el archivo
    public ArrayList<Producto> cargarLista() {
        File f = new File(archivo);
        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ArrayList<Producto>) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al guardar inventario: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarFactura(Factura factura) {
        crearCarpetasinoExiste();
        try (BufferedWriter escribir = new BufferedWriter(new FileWriter(archivoFacturas, true))) {
            StringBuilder linea = new StringBuilder();

            // Detalles del cliente
            linea.append(factura.getClienteNombre()).append(",");
            linea.append(factura.getClienteCedula()).append(",");
            linea.append(factura.getClienteTelefono()).append(",");
            linea.append(factura.getClienteDireccion()).append(";");

            // Los productos ItemVenta
            ArrayList<ItemVenta> items = factura.getItems();
            for (int i = 0; i < items.size(); i++) {
                ItemVenta item = items.get(i);
                Producto p = item.getProducto();
                int cantidad = item.getCantidad();
                double precioUnitario = p.getPrecio();
                boolean descuento = item.tuvoDescuento();
                Double subTotalFinal = item.getSubtotalPromocional();

                linea.append(p.getNombre()).append("-")
                        .append(cantidad).append("-")
                        .append(precioUnitario).append("-")
                        .append(descuento ? "S" : "N").append("-")
                        .append(subTotalFinal);

                if (i < items.size() - 1) {
                    linea.append(",");
                }
            }

            linea.append(";")
                    .append(factura.getSubtotal()).append(";")
                    .append(factura.getIva()).append(";")
                    .append(factura.getTotal()).append(";");

            escribir.write(linea.toString());
            escribir.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar la factura: " + e.getMessage());
        }
    }

    public void mostrarFacturas() {
        try (BufferedReader leer = new BufferedReader(new FileReader(archivoFacturas));) {
            String linea;
            int contFacturas = 1;

            while ((linea = leer.readLine()) != null) {
                String partes[] = linea.split(";");
                String clienteDatos[] = partes[0].split(",");
                String productosDatos[] = partes[1].split(",");
                double subtotal = Double.parseDouble(partes[2]);
                double iva = Double.parseDouble(partes[3]);
                double total = Double.parseDouble(partes[4]);

                String nombre = clienteDatos[0];
                String cedula = clienteDatos[1];
                String telefono = clienteDatos[2];
                String direccion = clienteDatos[3];

                System.out.printf("\n%56s\n", "======== FACTURA ========");
                System.out.println(
                        "+--------------------------------------------------------------------------------------+");
                System.out.println(
                        "|                                    SuperMaxi                                         |");
                System.out.printf("| Dirección: %-42s Factura N°: %-18d |\n", "Calle Sucre e Imbabura", contFacturas);
                System.out.println(
                        "| Ruc: 1900237501001                                                                   |");
                System.out.printf("| Teléfono: %-75s|\n", "09745634784");
                System.out.println(
                        "+--------------------------------------------------------------------------------------+");
                System.out.printf("| Cliente: %-76s|\n", nombre);
                System.out.printf("| Cédula: %-77s|\n", cedula);
                System.out.printf("| Teléfono: %-75s|\n", telefono);
                System.out.printf("| Dirección: %-74s|\n", direccion);
                System.out.printf("| Fecha: %-78s|\n", java.time.LocalDate.now());
                System.out.println(
                        "+--------------------------------------------------------------------------------------+");
                System.out.println(
                        "| Artículo                  | Cantidad | Precio UniT. | Descuento |    Subtotal Final  |");
                System.out.println(
                        "+---------------------------+----------+--------------+-----------+--------------------+");

                for (String producto : productosDatos) {
                    String[] datos = producto.split("-");
                    String nombreProd = datos[0];
                    int cantidad = Integer.parseInt(datos[1]);
                    double precio = Double.parseDouble(datos[2]);
                    String descuento = datos[3].equals("S") ? "Sí" : "No";
                    double subtotalFinal = Double.parseDouble(datos[4]);

                    System.out.printf("| %-25.24s | %8d | $%11.2f | %9s | $%17.2f |\n",
                            nombreProd, cantidad, precio, descuento, subtotalFinal);
                }

                System.out.println(
                        "+-----------------------------------------------------------------+--------------------+");
                System.out.printf("| %63s | $%17.2f |\n", "Subtotal:", subtotal);
                System.out.printf("| %63s | $%17.2f |\n", "Iva:", iva);
                System.out.printf("| %63s | $%17.2f |\n", "Total:", total);
                System.out.println(
                        "+-----------------------------------------------------------------+--------------------+");
                contFacturas++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer la factura: " + e.getMessage());
        }
    }

    public void mostrarUltFactura() {
        try (BufferedReader leer = new BufferedReader(new FileReader(archivoFacturas));) {
            String linea;
            String ultimaLinea = null;
            int cont = 0;

            while ((linea = leer.readLine()) != null) {
                ultimaLinea = linea;
                cont++;
            }
            if (ultimaLinea != null) {
                String[] partes = ultimaLinea.split(";");
                String[] clienteDatos = partes[0].split(",");
                String[] productosDatos = partes[1].split(",");
                double subtotal = Double.parseDouble(partes[2]);
                double iva = Double.parseDouble(partes[3]);
                double total = Double.parseDouble(partes[4]);

                String nombre = clienteDatos[0];
                String cedula = clienteDatos[1];
                String telefono = clienteDatos[2];
                String direccion = clienteDatos[3];

                System.out.printf("\n%56s\n", "======== FACTURA ========");
                System.out.println(
                        "+--------------------------------------------------------------------------------------+");
                System.out.println(
                        "|                                    SuperMaxi                                         |");
                System.out.printf("| Dirección: %-42s Factura N°: %-18d |\n", "Calle Sucre e Imbabura", cont);
                System.out.println(
                        "| Ruc: 1900237501001                                                                   |");
                System.out.printf("| Teléfono: %-75s|\n", "09745634784");
                System.out.println(
                        "+--------------------------------------------------------------------------------------+");
                System.out.printf("| Cliente: %-76s|\n", nombre);
                System.out.printf("| Cédula: %-77s|\n", cedula);
                System.out.printf("| Teléfono: %-75s|\n", telefono);
                System.out.printf("| Dirección: %-74s|\n", direccion);
                System.out.printf("| Fecha: %-78s|\n", java.time.LocalDate.now());
                System.out.println(
                        "+--------------------------------------------------------------------------------------+");
                System.out.println(
                        "| Artículo                  | Cantidad | Precio UniT. | Descuento |    Subtotal Final  |");
                System.out.println(
                        "+---------------------------+----------+--------------+-----------+--------------------+");

                for (String producto : productosDatos) {
                    String[] datos = producto.split("-");
                    String nombreProd = datos[0];
                    int cantidad = Integer.parseInt(datos[1]);
                    double precio = Double.parseDouble(datos[2]);
                    String descuento = datos[3].equals("S") ? "Sí" : "No";
                    double subtotalFinal = Double.parseDouble(datos[4]);

                    System.out.printf("| %-25.24s | %8d | $%11.2f | %9s | $%17.2f |\n",
                            nombreProd, cantidad, precio, descuento, subtotalFinal);
                }

                System.out.println(
                        "+-----------------------------------------------------------------+--------------------+");
                System.out.printf("| %63s | $%17.2f |\n", "Subtotal:", subtotal);
                System.out.printf("| %63s | $%17.2f |\n", "Iva:", iva);
                System.out.printf("| %63s | $%17.2f |\n", "Total:", total);
                System.out.println(
                        "+-----------------------------------------------------------------+--------------------+");
            }
        } catch (IOException e) {
            System.out.println("Error al leer la factura: " + e.getMessage());
        }
    }

    public void guardarEstadistica(Factura factura) {
        crearCarpetasinoExiste();
        try (BufferedWriter escribir = new BufferedWriter(new FileWriter(archivoEstadisticas, true))) {
            for (ItemVenta item : factura.getItems()) {
                Producto p = item.getProducto();
                String nombre = p.getNombre();
                String categoria = p.getCategoria();
                int cantidad = item.getCantidad();
                double subtotal = item.getSubtotal();

                escribir.write(nombre + ";" + categoria + ";" + cantidad + ";" + subtotal);
                escribir.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    public void mostrarEstadisticas() {
        File f = new File(archivoEstadisticas);
        if (!f.exists()) {
            System.out.println("No hay estadísticas registradas");
            return;
        }

        ArrayList<String> nombresProduc = new ArrayList<>();
        ArrayList<Integer> cantidadProduc = new ArrayList<>();
        ArrayList<Double> totalesProduc = new ArrayList<>();

        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<Integer> cantidadescategorias = new ArrayList<>();
        ArrayList<Double> totalesCategorias = new ArrayList<>();

        double totalGeneral = 0;
        int totalProducVendidos = 0;

        try (BufferedReader leer = new BufferedReader(new FileReader(archivoEstadisticas))) {
            String linea;
            while ((linea = leer.readLine()) != null) {
                String partes[] = linea.split(";");
                String nombre = partes[0];
                String categoria = partes[1];
                int cantidad = Integer.parseInt(partes[2]);
                double subtotal = Double.parseDouble(partes[3]);

                totalGeneral += subtotal;
                totalProducVendidos += cantidad;

                int indexProducto = nombresProduc.indexOf(nombre);
                if (indexProducto != -1) {
                    cantidadProduc.set(indexProducto, cantidadProduc.get(indexProducto) + cantidad);
                    totalesProduc.set(indexProducto, totalesProduc.get(indexProducto) + subtotal);
                } else {
                    nombresProduc.add(nombre);
                    cantidadProduc.add(cantidad);
                    totalesProduc.add(subtotal);
                }

                int indexCategoria = categorias.indexOf(categoria);
                if (indexCategoria != -1) {
                    cantidadescategorias.set(indexCategoria, cantidadescategorias.get(indexCategoria) + cantidad);
                    totalesCategorias.set(indexCategoria, totalesCategorias.get(indexCategoria) + subtotal);
                } else {
                    categorias.add(categoria);
                    cantidadescategorias.add(cantidad);
                    totalesCategorias.add(subtotal);
                }
            }

            System.out.println("\n=== ESTADÍSTICAS DE VENTAS ===");
            System.out.printf("Total general vendido: $%.2f\n", totalGeneral);
            System.out.printf("Cantidad total de productos vendidos: %d\n", totalProducVendidos);
            System.out.println("============================================");

            System.out.println("\n--- Productos Vendidos ---");
            for (int i = 0; i < nombresProduc.size(); i++) {
                System.out.printf("Producto: %-25s | Cantidad: %3d | Total: $%.2f\n",
                        nombresProduc.get(i), cantidadProduc.get(i), totalesProduc.get(i));
            }

            System.out.println("\n--- Ventas por Categoría ---");
            for (int i = 0; i < categorias.size(); i++) {
                System.out.printf("Categoría: %-15s | Productos vendidos: %3d | Total: $%.2f\n",
                        categorias.get(i), cantidadescategorias.get(i), totalesCategorias.get(i));
            }

        } catch (IOException e) {
            System.err.println("Error al leer las estadísticas: " + e.getMessage());
        }
    }

    public void borrarEstadisticas() {
        File f = new File(archivoEstadisticas);
        if (f.exists()) {
            try (PrintWriter borrar = new PrintWriter(f)) {
                borrar.print("");
                System.out.println("Estadísticas borradas");
            } catch (IOException e) {
                System.out.println("Error al borrar las estadísticas: " + e.getMessage());
            }
        } else {
            System.out.println("No hay estadísticas para borrar");
        }
    }
}
