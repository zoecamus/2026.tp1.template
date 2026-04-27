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

        // 1- Verificar que el recurso existe
        Recurso recurso = recursoRepository.buscarPorId(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException(isbn));

        // 2- Verificar disponibilidad
        if (recurso instanceof Libro libro && !libro.disponible()) {
            throw new RecursoNoDisponibleException(isbn);
        }

        // 3- Verificar que el socio existe
        Socio socio = socioRepository.buscarPorId(socioId)
                .orElseThrow(() -> new SocioNoEncontradoException(socioId));

        // 4- Verificar límite de préstamos
        if (socio.alcanzaLimite()) {
            throw new LimitePrestamosException(socioId, socio.getTipo().getLimitePrestamos());
        }

        // 5- Marca libro como no disponible
        if (recurso instanceof Libro libro) {
            recursoRepository.actualizar(libro.conDisponibilidad(false));
        }

        // 6- Registra el préstamo
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

        System.out.printf(" Préstamo #%d registrado: '%s' → Socio %d. Devolver antes del %s%n",
                prestamo.id(), recurso.titulo(), socioId, prestamo.fechaDevolucionEsperada());

        return prestamo;
    }

    @Override
    public Prestamo registrarDevolucion(int prestamoId) throws BibliotecaException {

        // 1- Buscar préstamo activo
        Prestamo prestamo = prestamoRepository.buscarPorId(prestamoId)
                .filter(Prestamo::estaActivo)
                .orElseThrow(() -> new PrestamoNoEncontrado(prestamoId));

        // 2- Registra devolución
        Prestamo devuelto = prestamo.conDevolucion(LocalDate.now());
        prestamoRepository.actualizar(devuelto);

        // 3- Libera el recurso si es físico
        recursoRepository.buscarPorId(prestamo.isbn()).ifPresent(recurso -> {
            if (recurso instanceof Libro libro) {
                recursoRepository.actualizar(libro.conDisponibilidad(true));
            }
        });

        // 4- Actualizar estado del socio
        socioRepository.buscarPorId(prestamo.socioId()).ifPresent(socio -> {
            socio.quitarPrestamo(prestamo.isbn());
        });

        // 5. Informar sobre retraso
        long retraso = devuelto.diasRetraso();
        if (retraso > 0) {
            System.out.printf("Devolución con %d día(s) de retraso.%n", retraso);
        } else {
            System.out.println("Devolución registrada a tiempo.");
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
