package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import com.grupo3.tp_hito_2.entities.TipoUnidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class RegistroUnidadDTO {
    //UNIDAD
    @NotBlank(message = "El nombre es obligatorio")
    String nombreComercial;
    @Positive(message = "La superficie debe ser mayor a 0")
    double superficie;
    @NotBlank(message = "El codigo es obligatorio")
    String codigoUnico;
    @NotNull(message = "Elegi un festival")
    Long idFestival;
    @NotNull(message = "Elegi un tipo de unidad")
    TipoUnidad tipo;
    String patente;
    boolean conexionElectrica;
    Integer cantidadDeCarpas;
    Integer tiempoDeMontaje;

    //RESPONSABLE
    @NotBlank(message = "El nombre es obligatorio")
    String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    String apellido;
    @NotNull(message = "El DNI es obligatorio")
    @Positive(message = "El DNI debe ser positivo")
    Integer dni;
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    LocalDate fechaDeNacimiento;
    @NotNull(message = "La fecha de ingreso es obligatoria")
    LocalDate fechaDeIngreso;

    //USUARIO
    @NotBlank(message = "El usuario es obligatoria")
    String username;
    @NotBlank(message = "La contraseña es obligatoria")
    String password;
}
