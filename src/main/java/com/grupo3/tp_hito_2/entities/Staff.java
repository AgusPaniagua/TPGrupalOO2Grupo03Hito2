package com.grupo3.tp_hito_2.entities;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.CascadeType;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long idStaff;

    @ToString.Include
    private String nombre;

    @ToString.Include
    private String apellido;

    @Column(unique = true, nullable = false)
    @ToString.Include
    private int dni;

    private LocalDate fechaDeNacimiento;

    private LocalDate fechaDeIngreso;

    @ToString.Include
    private double sueldoBase;

    // El nombre del campo debe ser "unidadDeVenta": UnidadDeVenta lo referencia con mappedBy = "unidadDeVenta"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_de_venta_id")
    private UnidadDeVenta unidadDeVenta;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Staff(String nombre, String apellido, int dni, LocalDate fechaDeNacimiento,
                 LocalDate fechaDeIngreso, double sueldoBase, UnidadDeVenta unidadDeVenta, User user) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaDeIngreso = fechaDeIngreso;
        this.sueldoBase = sueldoBase;
        this.unidadDeVenta = unidadDeVenta;
        if (user == null) {
            throw new IllegalArgumentException("El miembro del staff responsable debe tener un usuario y contraseña asignados.");
        }
        this.user = user;
    }

    // La antigüedad (en años) se calcula a partir de la fecha de ingreso, no se guarda en la base.
    public int getAntiguedad() {
        if (fechaDeIngreso == null) {
            return 0;
        }
        return Period.between(fechaDeIngreso, LocalDate.now()).getYears();
    }
}
