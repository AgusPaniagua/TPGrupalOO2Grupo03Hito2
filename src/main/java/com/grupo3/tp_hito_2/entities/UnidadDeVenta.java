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
public class UnidadDeVenta {

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

    public UnidadDeVenta(String nombreComercial, double superficie, String codigoUnico, Festival festival){
        this.nombreComercial=nombreComercial;
        this.superficie=superficie;
        this.codigoUnico=codigoUnico;
        this.festival=festival;
    }
}
