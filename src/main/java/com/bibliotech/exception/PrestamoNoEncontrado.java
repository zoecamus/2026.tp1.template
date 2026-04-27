package com.bibliotech.exception;

public class PrestamoNoEncontrado extends BibliotecaException {
    public PrestamoNoEncontrado(int prestamoId) {
        super("No se encontró ningún préstamo activo con ID " + prestamoId + ".");
    }
}