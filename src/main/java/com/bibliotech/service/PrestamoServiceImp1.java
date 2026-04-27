package com.bibliotech.service;

import com.bibliotech.exception.*;
import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrestamoServiceImp1 implements PrestamoService {

    private static final int DIAS_PRESTAMO = 14;
    private final AtomicInteger contadorId = new AtomicInteger(1);

    private final RecursoRepository recursoRepository;
    private final SocioRepository socioRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoServiceImp1(RecursoRepository recursoRepository,
                               SocioRepository socioRepository,
                               PrestamoRepository prestamoRepository) {
        this.recursoRepository = recursoRepository;
        this.socioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Prestamo realizarPrestamo(String isbn, int socioId) throws BibliotecaException {

        // Punto 8 — ISBN no vacío
        if (isbn == null || isbn.isBlank())
            throw new BibliotecaException("El ISBN no puede estar vacio.");

        // Punto 2 — Existencia del recurso
        Recurso recurso = recursoRepository.buscarPorId(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException(isbn));

        // Punto 1 — Disponibilidad
        if (recurso instanceof Libro libro && !libro.disponible())
            throw new RecursoNoDisponibleException(isbn);

        // Punto 3 — Existencia del socio
        Socio socio = socioRepository.buscarPorId(socioId)
                .orElseThrow(() -> new SocioNoEncontradoException(socioId));

        // Punto 4 — Límite de préstamos
        if (socio.alcanzaLimite())
            throw new LimitePrestamosException(socioId, socio.getTipo().getLimitePrestamos());

        // Punto 9 — No duplicar préstamo activo
        if (socio.getIsbnPrestados().contains(isbn))
            throw new BibliotecaException("El socio " + socioId + " ya tiene prestado el recurso '" + isbn + "'.");

        // Marcar libro como no disponible
        if (recurso instanceof Libro libro)
            recursoRepository.actualizar(libro.conDisponibilidad(false));

        Prestamo prestamo = new Prestamo(
                contadorId.getAndIncrement(),
                socioId,
                isbn,
                LocalDate.now(),
                LocalDate.now().plusDays(DIAS_PRESTAMO),
                null
        );

        prestamoRepository.guardar(prestamo);
        socio.agregarPrestamo(isbn);

        System.out.println("Prestamo #" + prestamo.id() + " registrado. Devolver antes del: " + prestamo.fechaDevolucionEsperada());
        return prestamo;
    }

    @Override
    public Prestamo registrarDevolucion(int prestamoId) throws BibliotecaException {

        // Punto 5 — Devolución válida (activo)
        Prestamo prestamo = prestamoRepository.buscarPorId(prestamoId)
                .filter(Prestamo::estaActivo)
                .orElseThrow(() -> new PrestamoNoEncontrado(prestamoId));

        Prestamo devuelto = prestamo.conDevolucion(LocalDate.now());
        prestamoRepository.actualizar(devuelto);

        recursoRepository.buscarPorId(prestamo.isbn()).ifPresent(recurso -> {
            if (recurso instanceof Libro libro)
                recursoRepository.actualizar(libro.conDisponibilidad(true));
        });

        socioRepository.buscarPorId(prestamo.socioId()).ifPresent(socio ->
                socio.quitarPrestamo(prestamo.isbn())
        );

        long retraso = devuelto.diasRetraso();
        if (retraso > 0) {
            System.out.println("Devolucion con " + retraso + " dia(s) de retraso.");
        } else {
            System.out.println("Devolucion registrada a tiempo.");
        }

        return devuelto;
    }

    @Override
    public List<Prestamo> listarActivos() {
        return prestamoRepository.buscarActivos();
    }

    @Override
    public List<Prestamo> listarHistorialSocio(int socioId) {
        return prestamoRepository.buscarPorSocio(socioId);
    }
}