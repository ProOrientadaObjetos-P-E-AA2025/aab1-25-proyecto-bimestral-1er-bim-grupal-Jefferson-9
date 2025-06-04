package modelo;

public class ItemVenta {
    private Producto producto;
    private int cantidad;

    public ItemVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public double getSubtotalPromocional() {
        double precioUnitario = producto.getPrecio();

        if (producto.caducidad() || producto.getStock() > 50) {
            precioUnitario *= 0.8; // Descuento del 20% si esta por caducar
        }

        return precioUnitario * cantidad;
    }

    public boolean tuvoDescuento() {
        return producto.caducidad() || producto.getStock() > 50;
    }
}
