package com.grupo3.tp_hito_2.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grupo3.tp_hito_2.dtos.CierreCajaDTO;
import com.grupo3.tp_hito_2.dtos.ParticipacionDTO;
import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;
import com.grupo3.tp_hito_2.services.ICierreCajaService;
import com.grupo3.tp_hito_2.services.IEmpleadoPortalService;
import com.grupo3.tp_hito_2.services.IReciboSueldoPdfService;

// Vistas y acciones del rol EMPLEADO (la seguridad /empleado/** -> ROLE_EMPLEADO está en SecurityConfiguration).
// Todas las acciones se hacen sobre el usuario autenticado (Principal): nunca se recibe el id de otro empleado.
@Controller
@RequestMapping("/empleado")
public class EmpleadoPortalController {

    private final IEmpleadoPortalService empleadoPortalService;
    private final IReciboSueldoPdfService reciboSueldoPdfService;
    private final ICierreCajaService cierreCajaService;

    public EmpleadoPortalController(IEmpleadoPortalService empleadoPortalService,
                                    IReciboSueldoPdfService reciboSueldoPdfService,
                                    ICierreCajaService cierreCajaService) {
        this.empleadoPortalService = empleadoPortalService;
        this.reciboSueldoPdfService = reciboSueldoPdfService;
        this.cierreCajaService = cierreCajaService;
    }

    // CU Empleado 1: ver su información
    @GetMapping("/mi-informacion")
    public String miInformacion(Principal principal, Model model) {
        try {
            model.addAttribute("info", empleadoPortalService.verMiInformacion(principal.getName()));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "empleado-informacion";
    }

    // CU Empleado 2: sueldo recibido en cada festival
    @GetMapping("/sueldos")
    public String misSueldos(Principal principal, Model model) {
        try {
            model.addAttribute("recibos", empleadoPortalService.verSueldosPorFestival(principal.getName()));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "empleado-sueldos";
    }

    // CU Empleado 2: descarga del recibo de sueldo en PDF
    @GetMapping("/sueldos/{idFestival}/recibo")
    public ResponseEntity<byte[]> descargarRecibo(@PathVariable Long idFestival, Principal principal) {
        try {
            ReciboSueldoDTO recibo = empleadoPortalService.reciboDeFestival(principal.getName(), idFestival);
            byte[] pdf = reciboSueldoPdfService.generarReciboDeSueldo(recibo);
            String nombreArchivo = "recibo-sueldo-" + recibo.getNombreFestival().replaceAll("[^A-Za-z0-9]+", "-") + ".pdf";
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment().filename(nombreArchivo).build().toString())
                    .body(pdf);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // CU Empleado 3: ver si hoy tiene que trabajar en un festival activo y en qué unidad
    @GetMapping("/asignacion-actual")
    public String asignacionActual(Principal principal, Model model) {
        try {
            Optional<ParticipacionDTO> asignacion = empleadoPortalService.verAsignacionActual(principal.getName());
            model.addAttribute("asignacion", asignacion.orElse(null));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "empleado-asignacion";
    }

    // CU Empleado 4 (Cajero): ver el estado de la caja de hoy
    @GetMapping("/caja")
    public String caja(Principal principal, Model model) {
        try {
            model.addAttribute("caja", cierreCajaService.verCajaDelDia(principal.getName()));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "empleado-caja";
    }

    // CU Empleado 4 (Cajero): cerrar la caja al finalizar el día (Post/Redirect/Get)
    @PostMapping("/caja/cerrar")
    public String cerrarCaja(Principal principal, RedirectAttributes redirectAttributes) {
        try {
            CierreCajaDTO cierre = cierreCajaService.cerrarCaja(principal.getName());
            redirectAttributes.addFlashAttribute("exito", "Caja cerrada correctamente. Total recaudado: $ "
                    + String.format("%,.2f", cierre.getTotalRecaudado()));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/empleado/caja";
    }
}
