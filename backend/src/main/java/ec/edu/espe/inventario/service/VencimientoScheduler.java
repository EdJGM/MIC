package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.entity.InformacionDocumentada;
import ec.edu.espe.inventario.model.enums.TipoNotificacion;
import ec.edu.espe.inventario.repository.InformacionDocumentadaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduler que verifica diariamente documentos próximos a vencer
 * según el parámetro DIAS_ALERTA_VENCIMIENTO del sistema (RF-38).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VencimientoScheduler {

    private static final String PARAM_DIAS_ALERTA = "DIAS_ALERTA_VENCIMIENTO";
    private static final String DESTINATARIO_ID = "admin-espe-001";
    private static final String MODULO = "INFORMACION_DOCUMENTADA";

    private final ParametroSistemaService parametroSistemaService;
    private final InformacionDocumentadaRepository informacionDocumentadaRepository;
    private final NotificacionService notificacionService;

    /**
     * Se ejecuta todos los días a las 08:00.
     * Busca documentos cuya fechaProtocolo cae dentro de los próximos
     * DIAS_ALERTA_VENCIMIENTO días y genera una notificación por cada uno.
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void verificarVencimientos() {
        log.info("Iniciando verificación de vencimientos de documentos");

        int diasAlerta;
        try {
            diasAlerta = parametroSistemaService.obtenerValorNumerico(PARAM_DIAS_ALERTA);
        } catch (Exception e) {
            log.warn("No se pudo leer el parámetro {}: {}. Se usa valor por defecto 1.", PARAM_DIAS_ALERTA, e.getMessage());
            diasAlerta = 1;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(diasAlerta);

        List<InformacionDocumentada> proximos =
                informacionDocumentadaRepository.findActivosConFechaProtocoloEntre(hoy, limite);

        if (proximos.isEmpty()) {
            log.info("No hay documentos próximos a vencer en los próximos {} día(s)", diasAlerta);
            return;
        }

        log.info("Se encontraron {} documento(s) próximos a vencer", proximos.size());

        for (InformacionDocumentada doc : proximos) {
            String titulo = "Documento próximo a vencer";
            String mensaje = String.format(
                    "El documento \"%s\" (código: %s) vence el %s.",
                    doc.getNombreDocumento(),
                    doc.getCodigoDocumento(),
                    doc.getFechaProtocolo()
            );

            notificacionService.crear(
                    TipoNotificacion.DOCUMENTO_PROXIMO_VENCER,
                    titulo,
                    mensaje,
                    DESTINATARIO_ID,
                    doc.getId(),
                    MODULO
            );
        }

        log.info("Verificación de vencimientos completada. {} notificación(es) generadas.", proximos.size());
    }
}
