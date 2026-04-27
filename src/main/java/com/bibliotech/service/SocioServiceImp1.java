// src/main/java/com/bibliotech/service/SocioServiceImpl.java
package com.bibliotech.service;

import com.bibliotech.exception.DNIDuplicadoException;
import com.bibliotech.exception.EmailInvalidoException;
import com.bibliotech.model.Socio;
import com.bibliotech.model.TipoSocio;
import com.bibliotech.repository.SocioRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class SocioServiceImp1 implements SocioService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private final SocioRepository socioRepository;

    public SocioServiceImp1(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public void registrarSocio(int id, String nombre, String apellido,
                               String dni, String email, TipoSocio tipo)
            throws DNIDuplicadoException, EmailInvalidoException {

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailInvalidoException(email);
        }

        if (socioRepository.buscarPorDni(dni).isPresent()) {
            throw new DNIDuplicadoException(dni);
        }

        Socio socio = new Socio(id, nombre, apellido, dni, email, tipo);
        socioRepository.guardar(socio);
        System.out.println("✔ Socio registrado: " + nombre + " " + apellido);
    }

    @Override
    public Optional<Socio> buscarPorId(int id) {
        return socioRepository.buscarPorId(id);
    }

    @Override
    public List<Socio> listarTodos() {
        return socioRepository.buscarTodos();
    }
}