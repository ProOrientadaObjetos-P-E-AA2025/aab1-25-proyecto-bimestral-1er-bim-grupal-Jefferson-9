package modelo;

import java.util.ArrayList;

public class Factura {
    private String clienteNombre;
    private String clienteCedula;
    private String clienteTelefono;
    private String clienteDireccion;
    private ArrayList<ItemVenta> items;
    private double subtotal;
    private double iva;
    private double total;

    public Factura(String nombre, String cedula, String telefono, String direccion) {
        this.clienteNombre = nombre;
        this.clienteCedula = cedula;
        this.clienteTelefono = telefono;
        this.clienteDireccion = direccion;
        this.items = new ArrayList<>();
        this.subtotal = 0;
        this.iva = 0;
        this.total = 0;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        ItemVenta item = new ItemVenta(producto, cantidad);
        items.add(item);
    }

    public void calcTotales() {
        subtotal = 0;
        iva = 0;
        total = 0;

        for (ItemVenta item : items) {
            double itemSubtotal = item.getSubtotalPromocional();
            subtotal += item.getSubtotalPromocional();

            String categoria = item.getProducto().getCategoria().toLowerCase();

            switch (categoria) {
                case "vivienda":
                    iva += itemSubtotal * 0.12;
                    break;
                case "educacion":
                case "alimentacion":
                case "salud":
                    iva += 0;
                    break;
                case "vestimenta":
                    iva += itemSubtotal * 0.08;
                    break;
                default:
                    iva += itemSubtotal * 0.15;
                    break;
            }
        }

        total = subtotal + iva;
    }

    public void mostrarImpuestosPorcategoria() {
        String categorias[] = { "Vivienda", "Educacion", "Alimentacion", "Vestimenta", "Salud" };
        double deducibles[] = new double[categorias.length];

        for (ItemVenta item : items) {
            String categoria = item.getProducto().getCategoria().toLowerCase();
            double subTotalItem = item.getSubtotalPromocional();

            double ivaItem = 0;
            switch (categoria) {
                case "vivienda":
                    ivaItem += subTotalItem * 0.12;
                    break;
                case "educacion":
                case "alimentacion":
                case "salud":
                    break;
                case "vestimenta":
                    ivaItem += subTotalItem * 0.08;
                    break;
                default:
                    ivaItem += subTotalItem * 0.15;
                    break;
            }

            for (int i = 0; i < categorias.length; i++) {
                if (categoria.equalsIgnoreCase(categorias[i])) {
                    deducibles[i] += ivaItem;
                    break;
                }
            }
        }

        System.out.println("\n=== Impuestos a la renta deducibles por categoría ===");
        for (int i = 0; i < categorias.length; i++) {
            if (deducibles[i] > 0) {
                System.out.printf("%-15s: $%.2f\n", categorias[i], deducibles[i]);
            }
        }
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getClienteCedula() {
        return clienteCedula;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public String getClienteDireccion() {
        return clienteDireccion;
    }

    public ArrayList<ItemVenta> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIva() {
        return iva;
    }

    public double getTotal() {
        return total;
    }

    public int getTotalProductos() {
        int totalProductos = 0;
        for (ItemVenta item : items) {
            totalProductos += item.getCantidad();
        }
        return totalProductos;
    }

}