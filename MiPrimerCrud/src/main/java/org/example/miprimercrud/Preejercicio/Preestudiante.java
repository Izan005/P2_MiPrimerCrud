package org.example.miprimercrud.Preejercicio;

import java.sql.Date;

public class Preestudiante {

    private int nia;
    private String nombre;
    private Date fecha_nacimiento;

    public Preestudiante(int nia, String nombre, Date fecha_nacimiento) {
        this.nia = nia;
        this.nombre = nombre;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getNia() {
        return nia;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    @Override
    public String toString() {
        return "Preestudiante{" +
                "nia=" + nia +
                ", nombre='" + nombre + '\'' +
                ", fecha_nacimiento=" + fecha_nacimiento +
                '}';
    }
}
