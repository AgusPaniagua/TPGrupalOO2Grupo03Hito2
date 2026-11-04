package com.grupo3.tp_hito_2.services.implementation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;
import com.grupo3.tp_hito_2.services.IReciboSueldoPdfService;

@Service("reciboSueldoPdfService")
public class ReciboSueldoPdfService implements IReciboSueldoPdfService {

    private static final float MARGEN = 50f;
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Locale AR = Locale.forLanguageTag("es-AR");

    @Override
    public byte[] generarReciboDeSueldo(ReciboSueldoDTO r) {
        try (PDDocument documento = new PDDocument();
             ByteArrayOutputStream salida = new ByteArrayOutputStream()) {

            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            PDType1Font normal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font negrita = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            try (PDPageContentStream contenido = new PDPageContentStream(documento, pagina)) {
                Escritor e = new Escritor(contenido, normal, negrita,
                        pagina.getMediaBox().getWidth(), pagina.getMediaBox().getHeight() - MARGEN);

                e.texto("EPICENTRO GOURMET", negrita, 20);
                e.salto(22);
                e.texto("Recibo de sueldo", normal, 14);
                e.salto(14);
                e.linea();

                e.seccion("Datos del empleado");
                e.fila("Nombre y apellido", r.getNombreCompleto(), false);
                e.fila("DNI", String.valueOf(r.getDni()), false);
                e.fila("Función", r.getTipoPersonal()
                        + (r.getDetalleRol() == null || r.getDetalleRol().isEmpty() ? "" : " - " + r.getDetalleRol()), false);
                e.fila("Antigüedad", r.getAniosAntiguedad() + " año(s)", false);

                e.seccion("Festival");
                e.fila("Festival", r.getNombreFestival(), false);
                e.fila("Período", r.getFechaInicio().format(FECHA) + " al " + r.getFechaFin().format(FECHA), false);
                e.fila("Duración", r.getDiasFestival() + " día(s)", false);
                e.fila("Unidad de venta", r.getNombreUnidad() + " (" + r.getCodigoUnidad() + ")", false);

                e.seccion("Detalle de la liquidación");
                e.fila("Sueldo base", dinero(r.getSueldoBase()), false);
                if (r.getPlusCategoria() > 0) {
                    e.fila("Plus por categoría", dinero(r.getPlusCategoria()), false);
                }
                if (r.getPlusAntiguedad() > 0) {
                    e.fila("Plus por antigüedad (" + r.getAniosAntiguedad() + " año/s)", dinero(r.getPlusAntiguedad()), false);
                }
                e.fila("Subtotal", dinero(r.getSubtotal()), false);
                e.fila("Tramos liquidados", r.getCantidadIntervalos() + " tramo(s) = "
                        + String.format(AR, "%.0f", r.getPorcentajeAplicado()) + "% del subtotal", false);
                e.linea();
                e.fila("TOTAL A COBRAR", dinero(r.getTotalACobrar()), true);

                e.salto(30);
                e.texto("Emitido el " + LocalDateTime.now().format(FECHA_HORA), normal, 9);
            }

            documento.save(salida);
            return salida.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo generar el recibo de sueldo en PDF.", ex);
        }
    }

    private String dinero(double valor) {
        return String.format(AR, "$ %,.2f", valor);
    }

    // Va escribiendo líneas de arriba hacia abajo sobre una página.
    private static class Escritor {
        private final PDPageContentStream cs;
        private final PDType1Font normal;
        private final PDType1Font negrita;
        private final float ancho;
        private float y;

        Escritor(PDPageContentStream cs, PDType1Font normal, PDType1Font negrita, float ancho, float yInicial) {
            this.cs = cs;
            this.normal = normal;
            this.negrita = negrita;
            this.ancho = ancho;
            this.y = yInicial;
        }

        void salto(float puntos) {
            y -= puntos;
        }

        void texto(String texto, PDType1Font fuente, float tamanio) throws IOException {
            cs.beginText();
            cs.setFont(fuente, tamanio);
            cs.newLineAtOffset(MARGEN, y);
            cs.showText(limpiar(texto));
            cs.endText();
        }

        void seccion(String titulo) throws IOException {
            salto(28);
            texto(titulo, negrita, 12);
            salto(8);
            linea();
        }

        void fila(String etiqueta, String valor, boolean destacar) throws IOException {
            salto(18);
            PDType1Font fuente = destacar ? negrita : normal;
            float tamanio = destacar ? 13 : 11;
            texto(etiqueta, fuente, tamanio);
            String v = limpiar(valor);
            float anchoValor = fuente.getStringWidth(v) / 1000f * tamanio;
            cs.beginText();
            cs.setFont(fuente, tamanio);
            cs.newLineAtOffset(ancho - MARGEN - anchoValor, y);
            cs.showText(v);
            cs.endText();
        }

        void linea() throws IOException {
            cs.moveTo(MARGEN, y);
            cs.lineTo(ancho - MARGEN, y);
            cs.stroke();
        }

        // La fuente estándar solo soporta Latin-1: cualquier otro carácter se reemplaza por '?'
        private String limpiar(String texto) {
            if (texto == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (char c : texto.toCharArray()) {
                boolean valido = (c >= 32 && c < 127) || (c >= 160 && c <= 255);
                sb.append(valido ? c : '?');
            }
            return sb.toString();
        }
    }
}
