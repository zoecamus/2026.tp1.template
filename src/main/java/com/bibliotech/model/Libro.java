package com.bibliotech.model;

public record Libro(
        String isbn,
        String titulo,
        String autor,
        int anio,
        Categoria categoria,
        int paginas,
        boolean disponible
) implements Recurso {

      public Libro conDisponibilidad(boolean nuevaDisponibilidad) {
        return new Libro(isbn, titulo, autor, anio, categoria, paginas, nuevaDisponibilidad);
    }
}