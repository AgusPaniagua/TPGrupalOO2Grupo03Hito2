package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter @NoArgsConstructor 
public class EmpleadoDTO {
    private Long idStaff;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @Positive(message = "El DNI debe ser positivo")
    private int dni;
    @NotBlank
    @Email
    @Size(max = 45,message= "El email no puede superar los 45 caracteres")
    private String email;
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate fechaDeNacimiento;
    @Positive(message = "El sueldo debe ser positivo")
    private double sueldoBase;
}
