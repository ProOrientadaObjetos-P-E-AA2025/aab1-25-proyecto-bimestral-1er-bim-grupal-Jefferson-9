package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Producto implements Serializable {
    private String nombre;
    private double precio;
    private int stock;
    private String caducidad;
    private String categoria;

    public Producto(String nombre, double precio, int stock, String caducidad, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.caducidad = caducidad;
        this.categoria = categoria.trim().toLowerCase();
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void reducirStock(int cantidad) {
        this.stock = stock - cantidad;
    }

    public void setStock(int stockNuevo) {
        this.stock = stockNuevo;
    }

    public boolean caducidad() {
        try {
            if (caducidad.equalsIgnoreCase("indefinido")) {
                return false;
            }
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate fechaCaducidad = LocalDate.parse(caducidad, formato);
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
            return diasRestantes <= 10;
        } catch (Exception e) {
            System.err.println("Error al calcular la caducidad: " + e.getMessage());
            return false;
        }
    }

    public boolean tienePromocion() {
        return caducidad() || stock > 50;
    }

    @Override
    public String toString() {
        String promocion = tienePromocion() ? "Sí" : "No";

        return String.format(
                "| %-25s | Precio: $%-8.2f | Stock: %-5d | Vence: %-10s | Categoría: %-12s | Promoción: %-3s |",
                nombre, precio, stock, caducidad, categoria, promocion);
    }
}
