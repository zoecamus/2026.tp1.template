package com.bibliotech.service;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;
import com.bibliotech.repository.RecursoRepository;

import java.util.List;
import java.util.Optional;

public class RecursoServiceImp1 implements RecursoService {

    private final RecursoRepository recursoRepository;

    public RecursoServiceImp1(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @Override
    public void registrarRecurso(Recurso recurso) {
        recursoRepository.guardar(recurso);
        System.out.println("✔ Recurso registrado: " + recurso.titulo());
    }

    @Override
    public Optional<Recurso> buscarPorIsbn(String isbn) {
        return recursoRepository.buscarPorId(isbn);
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        return recursoRepository.buscarPorTitulo(titulo);
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {
        return recursoRepository.buscarPorAutor(autor);
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return recursoRepository.buscarPorCategoria(categoria);
    }

    @Override
    public List<Recurso> listarTodos() {
        return recursoRepository.buscarTodos();
    }
}