package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.Prestamo;

import java.util.List;

public interface PrestamoService {
    Prestamo realizarPrestamo(String isbn, int socioId) throws BibliotecaException;
    Prestamo registrarDevolucion(int prestamoId) throws BibliotecaException;
    List<Prestamo> listarActivos();
    List<Prestamo> listarHistorialSocio(int socioId);
}