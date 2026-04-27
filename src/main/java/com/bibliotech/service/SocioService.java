package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.Socio;
import com.bibliotech.model.TipoSocio;

import java.util.List;
import java.util.Optional;

public interface SocioService {
    void registrarSocio(int id, String nombre, String apellido,
                        String dni, String email, TipoSocio tipo) throws BibliotecaException;
    Optional<Socio> buscarPorId(int id);
    List<Socio> listarTodos();
}