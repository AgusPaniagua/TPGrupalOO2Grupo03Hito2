package com.grupo3.tp_hito_2.helpers;

import org.hibernate.Hibernate;

import com.grupo3.tp_hito_2.entities.Cajero;
import com.grupo3.tp_hito_2.entities.Cocinero;
import com.grupo3.tp_hito_2.entities.Staff;

public final class StaffHelper {

    public static final String COCINERO = "Cocinero";
    public static final String CAJERO = "Cajero";
    public static final String PERSONAL = "Personal";

    private StaffHelper() {
    }

    // Si el Staff llegó como proxy de Hibernate (carga LAZY), lo reemplaza por la instancia real
    // para que instanceof Cocinero / Cajero funcione.
    public static Staff real(Staff staff) {
        return (Staff) Hibernate.unproxy(staff);
    }

    public static String tipo(Staff staff) {
        Staff real = real(staff);
        if (real instanceof Cocinero) {
            return COCINERO;
        }
        if (real instanceof Cajero) {
            return CAJERO;
        }
        return PERSONAL;
    }

    // Especialidad (cocinero) o turno (cajero)
    public static String detalle(Staff staff) {
        Staff real = real(staff);
        if (real instanceof Cocinero) {
            String especialidad = ((Cocinero) real).getEspecialidad();
            return especialidad == null ? "" : especialidad;
        }
        if (real instanceof Cajero) {
            String turno = ((Cajero) real).getTurnoDeTrabajo();
            return turno == null ? "" : turno;
        }
        return "";
    }

    public static String nombreCompleto(Staff staff) {
        return staff.getNombre() + " " + staff.getApellido();
    }
}
