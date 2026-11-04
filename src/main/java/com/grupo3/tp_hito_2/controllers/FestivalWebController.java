package com.grupo3.tp_hito_2.controllers;

import com.grupo3.tp_hito_2.dtos.RankingUnidadDTO;
import com.grupo3.tp_hito_2.repositories.IUserRepository;
import com.grupo3.tp_hito_2.services.implementation.RankingUnidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.grupo3.tp_hito_2.services.IFestivalService;
import com.grupo3.tp_hito_2.services.IUnidadDeVentaService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.core.Authentication;
import com.grupo3.tp_hito_2.entities.User;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.entities.Festival;
import java.util.List;

@Controller
public class FestivalWebController {

        private final IFestivalService festivalService;
        private final IUnidadDeVentaService unidadDeVentaService;
        private final RankingUnidadService rankingUnidadService;
        private final IUserRepository userRepository;

        public FestivalWebController(
                        IFestivalService festivalService,
                        IUnidadDeVentaService unidadDeVentaService,
                        RankingUnidadService rankingUnidadService, IUserRepository userRepository) {

                this.festivalService = festivalService;
                this.unidadDeVentaService = unidadDeVentaService;
                this.rankingUnidadService = rankingUnidadService;
                this.userRepository = userRepository;
        }

        @GetMapping("/")
        public String inicio(Model model) {
                model.addAttribute("festivales", festivalService.getAll());
                return "festivales";
        }

        @GetMapping("/festivales")
        public String listarFestivales(Model model) {
                model.addAttribute("festivales", festivalService.getAll());
                return "festivales";
        }

        @GetMapping("/unidades-de-venta")
        public String seleccionarFestivalParaUnidades(Model model) {
                model.addAttribute("festivales", festivalService.getAll());
                return "seleccionar-festival-unidades";
        }

        @GetMapping("/ranking-recaudacion")
        public String seleccionarFestivalParaRanking(Model model) {
                model.addAttribute("festivales", festivalService.getAll());
                return "seleccionar-festival-ranking";
        }

        @GetMapping("/ranking-recaudacion/consultar")
        public String consultarRankingRecaudacion(

                        @RequestParam Long idFestival, Model model) {

                var festival = festivalService.getAll().stream()
                                .filter(f -> idFestival.equals(f.getIdFestival()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No existe el festival con ID: " + idFestival));

                List<RankingUnidadDTO> ranking = rankingUnidadService.generarRankingRecaudacion(idFestival);

                model.addAttribute("festival", festival);
                model.addAttribute("ranking", ranking);
                model.addAttribute("festivales", festivalService.getAll());
                return "ranking-recaudacion";
        }

        @GetMapping("/festivales/{idFestival}/unidades-de-venta")
        public String verUnidadesDeVenta(
                        @PathVariable Long idFestival,
                        Model model) {

                var festival = festivalService.getAll().stream()
                                .filter(f -> idFestival.equals(f.getIdFestival()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No existe el festival con ID: " + idFestival));

                model.addAttribute("festival", festival);
                var unidades = unidadDeVentaService.traerPorFestival(idFestival);

                System.out.println("Festival seleccionado: " + idFestival);
                System.out.println("Cantidad de unidades: " + unidades.size());

                model.addAttribute("unidadesDeVenta", unidades);
                return "unidades-de-venta";
        }

        @GetMapping("/festivales/{idFestival}/ranking-recaudacion")
        public String verRankingRecaudacion(
                        @PathVariable Long idFestival,
                        Model model) {

                var festival = festivalService.getAll().stream().filter(f -> idFestival.equals(f.getIdFestival()))
                                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                                                "No existe el festival con ID: " + idFestival));

                List<RankingUnidadDTO> ranking = rankingUnidadService.generarRankingRecaudacion(idFestival);

                model.addAttribute("festival", festival);
                model.addAttribute("ranking", ranking);

                return "ranking-recaudacion";
        }

        @GetMapping("/reporte-recaudacion")
        public String seleccionarFestivalParaReporte(Authentication authentication, Model model) {

                // Obtener el usuario que inició sesión
                User usuario = userRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No se encontró el usuario"));

                // Buscar las unidades asignadas a ese usuario
                List<UnidadDeVenta> unidades = unidadDeVentaService.findByResponsableId((long) usuario.getId());

                // Obtener los festivales de esas unidades, sin repetirlos
                List<Festival> festivales = unidades.stream()
                                .map(UnidadDeVenta::getFestival)
                                .distinct()
                                .toList();

                model.addAttribute("festivales", festivales);

                return "seleccionar-festival-recaudacion";
        }

        @GetMapping("/reporte-recaudacion/consultar")
        public String consultarReporteRecaudacion(
                        @RequestParam Long idFestival,
                        Authentication authentication,
                        Model model) {

                // Obtener el usuario autenticado
                User usuario = userRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No se encontró el usuario"));

                // Obtener sus unidades asignadas
                List<UnidadDeVenta> unidades = unidadDeVentaService.findByResponsableId((long) usuario.getId());

                // Obtener los ID de las unidades que pertenecen al festival
                List<Long> idsUnidades = unidades.stream().filter(u -> u.getFestival() != null &&
                                u.getFestival().getIdFestival() == idFestival).map(UnidadDeVenta::getIdUnidadDeVenta)
                                .toList();

                if (idsUnidades.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "No tenés unidades asignadas a ese festival");
                }

                // Obtener el ranking completo del festival
                List<RankingUnidadDTO> rankingCompleto = rankingUnidadService.generarRankingRecaudacion(idFestival);

                // Filtrar para mostrar solamente las unidades del responsable
                List<RankingUnidadDTO> rankingResponsable = rankingCompleto.stream()
                                .filter(r -> idsUnidades.contains(r.getIdUnidadDeVenta()))
                                .toList();
                double totalRecaudacion = rankingResponsable.stream()
                                .mapToDouble(RankingUnidadDTO::getRecaudacion)
                                .sum();

                model.addAttribute("totalRecaudacion", totalRecaudacion);
                var festival = festivalService.getAll().stream()
                                .filter(f -> idFestival.equals(f.getIdFestival()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No existe el festival seleccionado"));

                model.addAttribute("festival", festival);
                model.addAttribute("ranking", rankingResponsable);

                return "reporte-recaudacion";
        }
        @GetMapping("/responsable/reporte-recaudacion")
        public String mostrarReporteRecaudacionResponsable(
                Authentication authentication,
                Model model) {

                User usuario = userRepository.findByUsername(authentication.getName())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "No se encontró el usuario"));

                List<UnidadDeVenta> unidadesResponsable =
                        unidadDeVentaService.findByResponsableId((long) usuario.getId());

                if (unidadesResponsable.isEmpty()) {
                        throw new IllegalArgumentException(
                                "El responsable no tiene una unidad asignada");
                }

                Festival festival = unidadesResponsable.get(0).getFestival();

                Long idFestival = festival.getIdFestival();

                List<RankingUnidadDTO> ranking =
                        rankingUnidadService.generarRankingRecaudacion(idFestival);

                double totalRecaudacion = ranking.stream()
                        .mapToDouble(RankingUnidadDTO::getRecaudacion)
                        .sum();

                model.addAttribute("festival", festival);
                model.addAttribute("ranking", ranking);
                model.addAttribute("totalRecaudacion", totalRecaudacion);

                return "reporte-recaudacion";
        }

        @GetMapping("/responsable-home")
        public String mostrarPanelResponsable() {
                return "responsable-home";
        }
}