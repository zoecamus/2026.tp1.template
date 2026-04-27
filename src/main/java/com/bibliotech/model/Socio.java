package com.bibliotech.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Socio {

    private final int id;
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final String email;
    private final TipoSocio tipo;
    private final List<String> isbnPrestados; // ISBNs actualmente en préstamo

    public Socio(int id, String nombre, String apellido, String dni, String email, TipoSocio tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.tipo = tipo;
        this.isbnPrestados = new ArrayList<>();
    }

    public int getId()           { return id; }
    public String getNombre()    { return nombre; }
    public String getApellido()  { return apellido; }
    public String getDni()       { return dni; }
    public String getEmail()     { return email; }
    public TipoSocio getTipo()   { return tipo; }

    public List<String> getIsbnPrestados() {
        return Collections.unmodifiableList(isbnPrestados);
    }

    public void agregarPrestamo(String isbn) {
        isbnPrestados.add(isbn);
    }

    public void quitarPrestamo(String isbn) {
        isbnPrestados.remove(isbn);
    }

    public boolean alcanzaLimite() {
        return isbnPrestados.size() >= tipo.getLimitePrestamos();
    }

    @Override
    public String toString() {
        return String.format("[%d] %s %s (%s) - %s | Préstamos activos: %d/%d",
                id, nombre, apellido, dni, tipo, isbnPrestados.size(), tipo.getLimitePrestamos());
    }
}