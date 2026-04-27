package com.bibliotech.exception;

public class RecursoNoDisponibleException extends BibliotecaException {
    public RecursoNoDisponibleException(String isbn) {
        super("El recurso con ISBN '" + isbn + "' no está disponible para préstamo.");
    }
}