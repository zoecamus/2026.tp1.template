package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.*;

public class InMemorySocioRepository implements SocioRepository {

    private final Map<Integer, Socio> almacen = new HashMap<>();

    @Override
    public void guardar(Socio socio) {
        almacen.put(socio.getId(), socio);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return Optional.ofNullable(almacen.get(id));
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public void actualizar(Socio socio) {
        almacen.put(socio.getId(), socio);
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return almacen.values().stream()
                .filter(s -> s.getDni().equals(dni))
                .findFirst();
    }
}