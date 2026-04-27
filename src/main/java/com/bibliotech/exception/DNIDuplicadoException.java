package com.bibliotech.exception;

public class DNIDuplicadoException extends BibliotecaException {
    public DNIDuplicadoException(String dni) {
        super("Ya existe un socio registrado con el DNI '" + dni + "'.");
    }
}