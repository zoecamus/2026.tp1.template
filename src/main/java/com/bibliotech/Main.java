package com.bibliotech;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.service.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Dependencias
        RecursoRepository recursoRepo   = new InMemoryRecursoRepository();
        SocioRepository socioRepo       = new InMemorySocioRepository();
        PrestamoRepository prestamoRepo = new InMemoryPrestamoRepository();

        RecursoService recursoService   = new RecursoServiceImp1(recursoRepo);
        SocioService socioService       = new SocioServiceImp1(socioRepo);
        PrestamoService prestamoService = new PrestamoServiceImp1(recursoRepo, socioRepo, prestamoRepo);

        // Datos de prueba
        recursoService.registrarRecurso(new Libro(
                "978-0-13-468599-1", "Clean Code", "Robert C. Martin", 2008, Categoria.TECNOLOGIA, 431, true
        ));
        try {
            socioService.registrarSocio(1, "Ana", "Garcia", "30111222", "ana@uni.edu.ar", TipoSocio.ESTUDIANTE);
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- BiblioTech ---");
            System.out.println("1. Registrar libro");
            System.out.println("2. Registrar socio");
            System.out.println("3. Realizar prestamo");
            System.out.println("4. Registrar devolucion");
            System.out.println("5. Ver prestamos activos");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            opcion = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcion) {
                    case 1 -> {
                        System.out.print("ISBN: ");        String isbn   = scanner.nextLine();
                        System.out.print("Titulo: ");      String titulo = scanner.nextLine();
                        System.out.print("Autor: ");       String autor  = scanner.nextLine();
                        System.out.print("Anio: ");        int anio      = Integer.parseInt(scanner.nextLine());
                        System.out.print("Paginas: ");     int paginas   = Integer.parseInt(scanner.nextLine());
                        System.out.print("Categoria (CIENCIA/HISTORIA/LITERATURA/TECNOLOGIA/ARTE): ");
                        Categoria cat = Categoria.valueOf(scanner.nextLine().toUpperCase());

                        recursoService.registrarRecurso(
                                new Libro(isbn, titulo, autor, anio, cat, paginas, true)
                        );
                    }
                    case 2 -> {
                        System.out.print("ID: ");          int id        = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nombre: ");      String nombre = scanner.nextLine();
                        System.out.print("Apellido: ");    String apell  = scanner.nextLine();
                        System.out.print("DNI: ");         String dni    = scanner.nextLine();
                        System.out.print("Email: ");       String email  = scanner.nextLine();
                        System.out.print("Tipo (ESTUDIANTE/DOCENTE): ");
                        TipoSocio tipo = TipoSocio.valueOf(scanner.nextLine().toUpperCase());

                        socioService.registrarSocio(id, nombre, apell, dni, email, tipo);
                    }
                    case 3 -> {
                        System.out.print("ISBN: ");        String isbn    = scanner.nextLine();
                        System.out.print("ID socio: ");    int socioId    = Integer.parseInt(scanner.nextLine());
                        prestamoService.realizarPrestamo(isbn, socioId);
                    }
                    case 4 -> {
                        System.out.print("ID prestamo: "); int id = Integer.parseInt(scanner.nextLine());
                        prestamoService.registrarDevolucion(id);
                    }
                    case 5 -> {
                        var activos = prestamoService.listarActivos();
                        if (activos.isEmpty()) {
                            System.out.println("No hay prestamos activos.");
                        } else {
                            activos.forEach(p -> System.out.println(
                                    "  #" + p.id() + " | ISBN: " + p.isbn() +
                                            " | Socio: " + p.socioId() +
                                            " | Vence: " + p.fechaDevolucionEsperada()
                            ));
                        }
                    }
                    case 0 -> System.out.println("Hasta luego.");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (BibliotecaException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}