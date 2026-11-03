package com.grupo3.tp_hito_2.services.implementation;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.dtos.EmpleadoDTO;
import com.grupo3.tp_hito_2.entities.Cajero;
import com.grupo3.tp_hito_2.entities.Cocinero;
import com.grupo3.tp_hito_2.entities.Rol;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.TipoStaff;
import com.grupo3.tp_hito_2.entities.User;
import com.grupo3.tp_hito_2.entities.UserRole;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.repositories.IUserRepository;
import com.grupo3.tp_hito_2.services.IEmpleadoService;
import com.grupo3.tp_hito_2.utils.GeneradorPassword;

import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoService implements IEmpleadoService {

    private final IStaffRepository staffRepository;
    private final IUserRepository userRepository;
    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeneradorPassword generadorPassword;
    private final EmailService emailService;
    private static final int EDAD_MINIMA = 18;

    public EmpleadoService(IStaffRepository staffRepository, IUserRepository userRepository,
            IUnidadDeVentaRepository unidadDeVentaRepository,
            PasswordEncoder passwordEncoder, GeneradorPassword generadorPassword, EmailService emailService) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.passwordEncoder = passwordEncoder;
        this.generadorPassword = generadorPassword;
        this.emailService = emailService;
    }

    private UnidadDeVenta unidadDelResponsable(String userName) {
        return unidadDeVentaRepository.findByResponsableUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("No tenes una unidad de venta asignada"));
    }

    private void validarDto(EmpleadoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (dto.getApellido() == null || dto.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (dto.getDni() <= 0) {
            throw new IllegalArgumentException("El dni no puede ser negativo o 0");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        } else if (normalizarEmail(dto).length() > 45) {
            throw new IllegalArgumentException("El email no puede tener mas de 45 caracteres");
        } else if (!normalizarEmail(dto).matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Formato de email invalido");
        }
        if (dto.getSueldoBase() <= 0) {
            throw new IllegalArgumentException("El sueldo debe ser mayor a 0");
        }
        if (dto.getFechaDeNacimiento() == null ||
                Period.between(dto.getFechaDeNacimiento(), LocalDate.now()).getYears() < EDAD_MINIMA) {
            throw new IllegalArgumentException("El empleado debe tener al menos " + EDAD_MINIMA + " años");
        }
        if (dto.getTipo() == null) {
            throw new IllegalArgumentException("El tipo no puede ser nulo");
        } else {
            if (dto.getTipo() == TipoStaff.CAJERO) {
                if (dto.getTurnoDeTrabajo() == null || dto.getTurnoDeTrabajo().isBlank()) {
                    throw new IllegalArgumentException("El turno de trabajo es obligatorio");
                }
            }
            if (dto.getTipo() == TipoStaff.COCINERO) {
                if (dto.getEspecialidad() == null || dto.getEspecialidad().isBlank()) {
                    throw new IllegalArgumentException("La especialidad es obligatoria");
                }
                if (dto.getPlusFijo() == null) {
                    throw new IllegalArgumentException("El plus fijo es obligatorio");
                }
                if (dto.getPlusFijo() <= 0) {
                    throw new IllegalArgumentException("El plus no puede ser negativo o 0");
                }
            }
        }

    }

    private String normalizarEmail(EmpleadoDTO dto) {
        return dto.getEmail().trim().toLowerCase();
    }

    @Override
    @Transactional
    public Staff altaEmpleado(String username, EmpleadoDTO dto) {
        UnidadDeVenta unidadConResponsable = unidadDelResponsable(username);
        validarDto(dto);
        String email = normalizarEmail(dto);
        Optional<Staff> empleadoExistente = staffRepository.findByDni(dto.getDni());
        if (empleadoExistente.isPresent()) {
            if (empleadoExistente.get().isActivo()) {
                throw new IllegalArgumentException("Ya existe un empleado activo con ese DNI");
            }
            throw new IllegalArgumentException("El empleado esta dado de baja");
        }
        Optional<User> emailExistente = userRepository.findByUsername(email);
        if (emailExistente.isPresent()) {
            throw new IllegalArgumentException("El email ya esta siendo usado");
        }
        String password = generadorPassword.generarPasword();
        User user = new User();
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        UserRole userRole = new UserRole(user, Rol.EMPLEADO);
        user.getUserRoles().add(userRole);

        Staff empleado = switch (dto.getTipo()) {
            case COCINERO -> {
                Cocinero cocinero = new Cocinero();
                cocinero.setEspecialidad(dto.getEspecialidad().trim());
                cocinero.setPlusFijo(dto.getPlusFijo());
                yield cocinero;
            }
            case CAJERO -> {
                Cajero cajero = new Cajero();
                cajero.setTurnoDeTrabajo(dto.getTurnoDeTrabajo().trim());
                yield cajero;
            }
        };
        empleado.setNombre(dto.getNombre().trim());
        empleado.setApellido(dto.getApellido().trim());
        empleado.setDni(dto.getDni());
        empleado.setFechaDeNacimiento(dto.getFechaDeNacimiento());
        empleado.setSueldoBase(dto.getSueldoBase());
        empleado.setFechaDeIngreso(LocalDate.now());
        empleado.setActivo(true);
        empleado.setUser(user);
        empleado.setUnidadDeVenta(unidadConResponsable);
        Staff nuevoEmpleado = staffRepository.save(empleado);
        emailService.enviarCredenciales(user.getUsername(), dto.getNombre().trim(), password, false);
        return nuevoEmpleado;
    }

    @Override
    @Transactional
    public Staff reactivarEmpleado(String username, EmpleadoDTO dto) {
        UnidadDeVenta unidadConResponsable = unidadDelResponsable(username);
        validarDto(dto);
        String email = normalizarEmail(dto);
        Staff empleado = staffRepository.findByDni(dto.getDni()).orElseThrow(
                () -> new IllegalArgumentException("No existe un empleado con ese DNI"));
        if (empleado.getUnidadDeVenta() == null ||
                !empleado.getUnidadDeVenta().getIdUnidadDeVenta().equals(unidadConResponsable.getIdUnidadDeVenta())) {
            throw new IllegalArgumentException("Ese empleado pertenece a otra unidad.");
        }
        User user = empleado.getUser();
        if (user == null) {
            throw new IllegalStateException("El empleado no tiene usuario asociado");
        }
        Optional<User> emailExistente = userRepository.findByUsername(email);
        if (emailExistente.isPresent() && emailExistente.get().getId() != user.getId()) {
            throw new IllegalArgumentException("El email ya esta siendo usado");
        }
        if (empleado.isActivo()) {
            throw new IllegalArgumentException("El empleado ya esta activo");
        }
        if (empleado instanceof Cajero cajero) {
            if (dto.getTipo() != TipoStaff.CAJERO) {
                throw new IllegalArgumentException("No se puede cambiar el tipo de un empleado ya registrado.");
            }
            cajero.setTurnoDeTrabajo(dto.getTurnoDeTrabajo().trim());

        } else if (empleado instanceof Cocinero cocinero) {
            if (dto.getTipo() != TipoStaff.COCINERO) {
                throw new IllegalArgumentException("No se puede cambiar el tipo de un empleado ya registrado.");
            }
            cocinero.setEspecialidad(dto.getEspecialidad().trim());
            cocinero.setPlusFijo(dto.getPlusFijo());
        } else {
            throw new IllegalArgumentException("Ese registro no es un empleado reactivable.");
        }

        user.setUsername(email);
        String password = generadorPassword.generarPasword();
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        empleado.setNombre(dto.getNombre().trim());
        empleado.setApellido(dto.getApellido().trim());
        empleado.setFechaDeNacimiento(dto.getFechaDeNacimiento());
        empleado.setSueldoBase(dto.getSueldoBase());
        empleado.setFechaDeIngreso(LocalDate.now());
        empleado.setActivo(true);
        Staff empleadoReactivo = staffRepository.save(empleado);
        emailService.enviarCredenciales(user.getUsername(), dto.getNombre().trim(), password, true);
        return empleadoReactivo;
    }

    @Override
    @Transactional
    public Staff modificarEmpleado(String username, EmpleadoDTO dto) {
        UnidadDeVenta unidad = unidadDelResponsable(username);
        validarDto(dto);
        Staff empleado = staffRepository.findById(dto.getIdStaff()).orElseThrow(() -> new IllegalArgumentException(
                "No existe un empeado con ese ID"));
        if (empleado.getUnidadDeVenta() == null || !empleado.getUnidadDeVenta().getIdUnidadDeVenta()
                .equals(unidad.getIdUnidadDeVenta())) {
            throw new IllegalArgumentException("Ese empleado pertenece a otra unidad");
        }
        if (!empleado.isActivo()) {
            throw new IllegalArgumentException("No se puede modificar un empledo dado de baja");
        }
        if (empleado instanceof Cajero && dto.getTipo() != TipoStaff.CAJERO) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de un empleado");
        }
        if (empleado instanceof Cocinero && dto.getTipo() != TipoStaff.COCINERO) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de un empleado");
        }
        if (!(empleado instanceof Cajero) &&
                !(empleado instanceof Cocinero)) {

            throw new IllegalArgumentException(
                    "Ese registro no corresponde a un empleado modificable");
        }
        String email = normalizarEmail(dto);
        Optional<User> emailExistente = userRepository.findByUsername(email);
        if (empleado.getDni() != dto.getDni() && staffRepository.findByDni(dto.getDni()).isPresent()) {
            throw new IllegalArgumentException("El DNI ya pertenece a otro empleado");
        }
        User user = empleado.getUser();
        if (user == null) {
            throw new IllegalStateException("El empleado no tiene un usuario asociado");
        }
        if (emailExistente.isPresent() && emailExistente.get().getId() != user.getId()) {
            throw new IllegalArgumentException("El email ya esta siendo utilizado por otro usuario");
        }
        empleado.setNombre(dto.getNombre().trim());
        empleado.setApellido(dto.getApellido().trim());
        empleado.setDni(dto.getDni());
        empleado.setFechaDeNacimiento(dto.getFechaDeNacimiento());
        empleado.setSueldoBase(dto.getSueldoBase());
        user.setUsername(email);
        if (empleado instanceof Cajero cajero) {
            cajero.setTurnoDeTrabajo(dto.getTurnoDeTrabajo().trim());
        } else if (empleado instanceof Cocinero cocinero) {
            cocinero.setEspecialidad(dto.getEspecialidad().trim());
            cocinero.setPlusFijo(dto.getPlusFijo());
        }
        return staffRepository.save(empleado);
    }

    

}
