package org.example.miprimercrud;

import java.time.LocalDate;

/**
 * @author Izan López Mora 1DAM
 * Clase que simula una fila de la tabla estudiantes
 */
public class Estudiante {
    private int nia;
    private String nombre;
    private LocalDate fecha_nacimiento;

    public Estudiante(LocalDate fecha_nacimiento, String nombre, int nia) {
        this.fecha_nacimiento = fecha_nacimiento;
        this.nombre = nombre;
        this.nia = nia;
    }

    public int getNia() {
        return nia;
    }

    public void setNia(int nia) {
        this.nia = nia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "nia=" + nia +
                ", nombre='" + nombre + '\'' +
                ", fecha_nacimiento=" + fecha_nacimiento +
                '}';
    }
}
