package com.bibliotech.exception;

public class RecursoNoEncontradoException extends BibliotecaException {
    public RecursoNoEncontradoException(String isbn) {
        super("No se encontró ningún recurso con ISBN '" + isbn + "'.");
    }
}