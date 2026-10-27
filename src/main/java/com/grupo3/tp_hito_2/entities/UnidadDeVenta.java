package com.grupo3.tp_hito_2.entities;

import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity 
@Getter @Setter @NoArgsConstructor
@ToString (onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UnidadDeVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long idUnidadDeVenta;
    
    @ToString.Include
    private String nombreComercial;
    
    @ToString.Include
    private double superficie;
    
    @Column(unique = true, nullable = false)
    @ToString.Include
    private String codigoUnico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OneToMany(mappedBy = "unidadDeVenta", cascade = CascadeType.ALL)
    private Set<Plato> platosOfrecidos = new HashSet<>();

    @OneToMany(mappedBy = "unidadDeVenta", cascade = CascadeType.ALL)
    private Set<Staff> personalACargo = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "responsable_id")
    private Staff responsable;

    public UnidadDeVenta(String nombreComercial, double superficie, String codigoUnico, Festival festival, Staff responsable){
        this.nombreComercial=nombreComercial;
        if(superficie<=0){
            throw new IllegalArgumentException("La superficie no puede ser 0 o negativo");
        }
        this.superficie=superficie;
        if(codigoUnico == null){
            throw new IllegalArgumentException("El código no puede ser nulo.");
        }
        if(codigoUnico.length()!=10){
            throw new IllegalArgumentException("El código debe tener exactamente 10 caracteres.");
        }
        this.codigoUnico=codigoUnico;
        this.festival=festival;
        this.responsable=responsable;
    }

    public abstract double calcularCanon();
}
