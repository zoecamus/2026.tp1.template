package com.bibliotech.model;

public record Ebook(
        String isbn,
        String titulo,
        String autor,
        int anio,
        Categoria categoria,
        String formatoArchivo,  // PDF, EPUB, MOBI
        double tamanioMB
) implements Recurso {
    // Los ebooks siempre están disponibles (no tienen ejemplar físico único)
    public boolean disponible() {
        return true;
    }
}
