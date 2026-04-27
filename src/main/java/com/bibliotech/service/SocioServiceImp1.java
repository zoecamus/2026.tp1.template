package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.exception.DNIDuplicadoException;
import com.bibliotech.exception.EmailInvalidoException;
import com.bibliotech.model.Socio;
import com.bibliotech.model.TipoSocio;
import com.bibliotech.repository.SocioRepository;
import java.util.List;
import java.util.Optional;

public class SocioServiceImp1 implements SocioService {

    private final SocioRepository socioRepository;

    public SocioServiceImp1(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public void registrarSocio(int id, String nombre, String apellido,
                               String dni, String email, TipoSocio tipo)
            throws BibliotecaException {

        if (id <= 0)
            throw new BibliotecaException("El ID del socio debe ser mayor a 0.");
        if (nombre == null || nombre.isBlank())
            throw new BibliotecaException("El nombre no puede estar vacio.");
        if (apellido == null || apellido.isBlank())
            throw new BibliotecaException("El apellido no puede estar vacio.");
        if (!email.contains("@"))
            throw new EmailInvalidoException(email);
        if (socioRepository.buscarPorDni(dni).isPresent())
            throw new DNIDuplicadoException(dni);

        socioRepository.guardar(new Socio(id, nombre, apellido, dni, email, tipo));
        System.out.println("Socio registrado: " + nombre + " " + apellido);
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