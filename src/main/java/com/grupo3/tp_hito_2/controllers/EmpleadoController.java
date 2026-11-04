package com.grupo3.tp_hito_2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupo3.tp_hito_2.services.IEmpleadoService;
import com.grupo3.tp_hito_2.dtos.EmpleadoDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/responsable/empleados")
public class EmpleadoController {
    private final IEmpleadoService empleadoService;

    public EmpleadoController(IEmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public String listarEmpleados(Authentication authentication, Model model) {
        List<EmpleadoDTO> empleados = empleadoService.listarEmpleados(
                authentication.getName());
        model.addAttribute("empleados", empleados);
        return "empleados";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("empleadoDTO", new EmpleadoDTO());
        return "empleado-form";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute("empleadoDTO") EmpleadoDTO dto,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            empleadoService.altaEmpleado(authentication.getName(), dto);
            redirectAttributes.addFlashAttribute(
                    "mensaje", "Empleado registrado correctamente. Se enviaron sus credenciales por email");
            return "redirect:/responsable/empleados/nuevo";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/responsable/empleados/nuevo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarEmpleado(
            @PathVariable Long id,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            EmpleadoDTO dto = empleadoService.obtenerEmpleado(
                    authentication.getName(), id);

            model.addAttribute("empleadoDTO", dto);
            model.addAttribute("modoEdicion", true);

            return "empleado-form";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/responsable/empleados";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarEmpleado(
            @PathVariable Long id,
            @ModelAttribute("empleadoDTO") EmpleadoDTO dto,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            dto.setIdStaff(id);

            empleadoService.modificarEmpleado(
                    authentication.getName(), dto);

            redirectAttributes.addFlashAttribute(
                    "mensaje", "Empleado modificado correctamente");

            return "redirect:/responsable/empleados";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/responsable/empleados/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String darDeBajaEmpleado(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            EmpleadoDTO dto = new EmpleadoDTO();
            dto.setIdStaff(id);

            empleadoService.darDeBajaEmpleado(
                    authentication.getName(), dto);

            redirectAttributes.addFlashAttribute(
                    "mensaje", "Empleado dado de baja correctamente");

            return "redirect:/responsable/empleados";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/responsable/empleados";
        }
    }

    @PostMapping("/{id}/reactivar")
    public String reactivarEmpleado(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            empleadoService.reactivarEmpleado(
                    authentication.getName(), id);

            redirectAttributes.addFlashAttribute(
                    "mensaje", "Empleado reactivado correctamente. Se enviaron nuevas credenciales por email.");

            return "redirect:/responsable/empleados";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/responsable/empleados";
        }
    }

}
