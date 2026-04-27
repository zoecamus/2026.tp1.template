package com.bibliotech.repository;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRecursoRepository implements RecursoRepository {

    private final Map<String, Recurso> almacen = new HashMap<>();

    @Override
    public void guardar(Recurso recurso) {
        almacen.put(recurso.isbn(), recurso);
    }

    @Override
    public Optional<Recurso> buscarPorId(String isbn) {
        return Optional.ofNullable(almacen.get(isbn));
    }

    @Override
    public List<Recurso> buscarTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public void actualizar(Recurso recurso) {
        almacen.put(recurso.isbn(), recurso);
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        return almacen.values().stream()
                .filter(r -> r.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {
        return almacen.values().stream()
                .filter(r -> r.autor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return almacen.values().stream()
                .filter(r -> r.categoria() == categoria)
                .collect(Collectors.toList());
    }
}