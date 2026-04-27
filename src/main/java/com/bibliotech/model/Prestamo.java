package com.bibliotech.model;

import java.time.LocalDate;

public record Prestamo(
        int id,
        int socioId,
        String isbn,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucionEsperada,
        LocalDate fechaDevolucionReal  // null si aún no fue devuelto
) {
    public boolean estaActivo() {
        return fechaDevolucionReal == null;
    }

    public long diasRetraso() {
        if (fechaDevolucionReal == null) return 0;
        long retraso = java.time.temporal.ChronoUnit.DAYS.between(
                fechaDevolucionEsperada, fechaDevolucionReal
        );
        return Math.max(0, retraso);
    }

    // Devuelve una copia del préstamo con la fecha de devolución registrada
    public Prestamo conDevolucion(LocalDate fecha) {
        return new Prestamo(id, socioId, isbn, fechaPrestamo, fechaDevolucionEsperada, fecha);
    }
}