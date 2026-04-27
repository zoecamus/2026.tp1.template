package com.bibliotech.exception;

public class LimitePrestamosException extends BibliotecaException {
    public LimitePrestamosException(int socioId, int limite) {
        super("El socio " + socioId + " ya alcanzó su límite de " + limite + " préstamos activos.");
    }
}