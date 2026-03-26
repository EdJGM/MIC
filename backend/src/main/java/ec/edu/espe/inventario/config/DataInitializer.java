package ec.edu.espe.inventario.config;

import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.entity.ObjetivoEspecifico;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.entity.Subproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ObjetivoEspecificoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import ec.edu.espe.inventario.repository.SubprocesoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Inicializa la base de datos con datos de prueba
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final MacroprocesoRepository macroprocesoRepository;
    private final ProcesoRepository procesoRepository;
    private final SubprocesoRepository subprocesoRepository;
    private final ObjetivoEspecificoRepository objetivoEspecificoRepository;

    @Override
    public void run(String... args) {
        log.info("Iniciando carga de datos de prueba...");

        if (objetivoEspecificoRepository.count() == 0) {
            cargarObjetivosEspecificos();
            log.info("Objetivos especificos cargados exitosamente");
        }

        if (macroprocesoRepository.count() == 0) {
            cargarDatosPrueba();
            log.info("Datos de prueba cargados exitosamente");
        } else {
            log.info("La base de datos ya contiene datos");
        }
    }
    
    private void cargarDatosPrueba() {
        // ── Macroproceso 1: Gestión Académica ──────────────────────────────────
        Macroproceso mp1 = crearMacroproceso("VDC-MP-001", TipoMacroproceso.VDC,
            "Gestión Académica",
            "Macroproceso responsable de la gestión integral de actividades académicas",
            "VDC", "Dr. Juan Pérez",
            "Garantizar la excelencia académica y formación integral de estudiantes",
            EstadoDocumentacion.CARACTERIZACION);

        // N1
        Proceso p1n1 = crearProcesoN1("VDC-MP-001-P-01", "Diseño Curricular",
            "Proceso de diseño y actualización de mallas curriculares",
            "Diseñar mallas curriculares actualizadas y pertinentes",
            mp1, EstadoDocumentacion.VALIDACION);

        // N2 bajo p1n1
        Proceso p1n2a = crearProcesoN2("VDC-MP-001-P-01-N2-01", "Análisis Curricular",
            "Revisión y análisis de la malla curricular vigente",
            mp1, p1n1, EstadoDocumentacion.VALIDACION);

        crearSubprocesoN1("VDC-MP-001-P-01-N2-01-SP-01", "Análisis de Necesidades Formativas",
            "Identificación de necesidades del mercado laboral", p1n2a, EstadoDocumentacion.LEGALIZADO);
        crearSubprocesoN1("VDC-MP-001-P-01-N2-01-SP-02", "Diseño de Perfil de Egreso",
            "Definición de competencias y resultados de aprendizaje", p1n2a, EstadoDocumentacion.DIFUNDIDO);

        Proceso p1n2b = crearProcesoN2("VDC-MP-001-P-01-N2-02", "Aprobación Curricular",
            "Proceso de aprobación por organismos competentes",
            mp1, p1n1, EstadoDocumentacion.CARACTERIZACION);

        crearSubprocesoN1("VDC-MP-001-P-01-N2-02-SP-01", "Aprobación Curricular Interna",
            "Revisión por el HCU", p1n2b, EstadoDocumentacion.CARACTERIZACION);

        // N1
        Proceso p2n1 = crearProcesoN1("VDC-MP-001-P-02", "Gestión de Matrículas",
            "Proceso de inscripción y matrícula de estudiantes",
            "Garantizar proceso de matrícula eficiente y transparente",
            mp1, EstadoDocumentacion.LEGALIZADO);

        // N2 bajo p2n1
        Proceso p2n2 = crearProcesoN2("VDC-MP-001-P-02-N2-01", "Registro de Aspirantes",
            "Inscripción de nuevos aspirantes al sistema",
            mp1, p2n1, EstadoDocumentacion.DIFUNDIDO);

        crearSubprocesoN1("VDC-MP-001-P-02-N2-01-SP-01", "Recepción de Documentos",
            "Recibir y verificar documentos de aspirantes", p2n2, EstadoDocumentacion.DIFUNDIDO);
        crearSubprocesoN1("VDC-MP-001-P-02-N2-01-SP-02", "Validación de Documentos",
            "Verificación formal de documentación presentada", p2n2, EstadoDocumentacion.LEGALIZADO);

        // ── Macroproceso 2: Investigación ─────────────────────────────────────
        Macroproceso mp2 = crearMacroproceso("VII-MP-002", TipoMacroproceso.VII,
            "Gestión de Investigación",
            "Macroproceso para fomentar y gestionar proyectos de investigación",
            "VII", "Dra. María González",
            "Promover investigación científica de alto impacto",
            EstadoDocumentacion.FLUJODIAGRAMACION);

        Proceso p3n1 = crearProcesoN1("VII-MP-002-P-01", "Formulación de Proyectos",
            "Elaboración y presentación de propuestas de investigación",
            "Generar proyectos de investigación innovadores",
            mp2, EstadoDocumentacion.LEVANTAMIENTO);

        Proceso p3n2 = crearProcesoN2("VII-MP-002-P-01-N2-01", "Elaboración de Propuestas",
            "Redacción técnica de propuestas", mp2, p3n1, EstadoDocumentacion.LEVANTAMIENTO);

        crearSubprocesoN1("VII-MP-002-P-01-N2-01-SP-01", "Identificación de Líneas de Investigación",
            "Definir líneas estratégicas de investigación", p3n2, EstadoDocumentacion.LEVANTAMIENTO);
        crearSubprocesoN1("VII-MP-002-P-01-N2-01-SP-02", "Elaboración de Propuesta",
            "Redacción técnica de la propuesta", p3n2, EstadoDocumentacion.NO_DOCUMENTADO);

        // ── Macroproceso 3: Administración ────────────────────────────────────
        Macroproceso mp3 = crearMacroproceso("VAD-MP-003", TipoMacroproceso.VAD,
            "Gestión Financiera",
            "Administración de recursos financieros institucionales",
            "VAD", "Ing. Carlos Ramírez",
            "Optimizar uso de recursos financieros",
            EstadoDocumentacion.VALIDACION);

        Proceso p4n1 = crearProcesoN1("VAD-MP-003-P-01", "Planificación Presupuestaria",
            "Elaboración del presupuesto anual institucional",
            "Planificar presupuesto alineado a objetivos estratégicos",
            mp3, EstadoDocumentacion.VALIDACION);

        Proceso p4n2 = crearProcesoN2("VAD-MP-003-P-01-N2-01", "Formulación de Presupuesto",
            "Consolidación de requerimientos presupuestarios",
            mp3, p4n1, EstadoDocumentacion.VALIDACION);

        crearSubprocesoN1("VAD-MP-003-P-01-N2-01-SP-01", "Recolección de Necesidades",
            "Identificar necesidades presupuestarias de todas las unidades",
            p4n2, EstadoDocumentacion.CARACTERIZACION);
        crearSubprocesoN1("VAD-MP-003-P-01-N2-01-SP-02", "Consolidación Presupuestaria",
            "Integrar y consolidar todas las solicitudes",
            p4n2, EstadoDocumentacion.VALIDACION);

        // ── Macroproceso 4: Tecnología ────────────────────────────────────────
        Macroproceso mp4 = crearMacroproceso("UTIC-MP-004", TipoMacroproceso.UTIC,
            "Soporte Tecnológico",
            "Gestión de infraestructura y servicios tecnológicos",
            "UTIC", "Ing. Ana Flores",
            "Proveer servicios tecnológicos de calidad",
            EstadoDocumentacion.NO_DOCUMENTADO);

        Proceso p5n1 = crearProcesoN1("UTIC-MP-004-P-01", "Mesa de Ayuda",
            "Atención y resolución de incidencias tecnológicas",
            "Resolver incidencias tecnológicas de manera eficiente",
            mp4, EstadoDocumentacion.NO_DOCUMENTADO);

        Proceso p5n2 = crearProcesoN2("UTIC-MP-004-P-01-N2-01", "Gestión de Tickets",
            "Registro y seguimiento de solicitudes tecnológicas",
            mp4, p5n1, EstadoDocumentacion.NO_DOCUMENTADO);

        crearSubprocesoN1("UTIC-MP-004-P-01-N2-01-SP-01", "Recepción de Tickets",
            "Registro de solicitudes y problemas tecnológicos",
            p5n2, EstadoDocumentacion.NO_DOCUMENTADO);
    }

    private Macroproceso crearMacroproceso(String codigo, TipoMacroproceso tipo, String nombre,
                                           String descripcion, String unidadEstrategica,
                                           String responsable, String objetivos,
                                           EstadoDocumentacion estado) {
        Macroproceso mp = new Macroproceso();
        mp.setCodigo(codigo);
        mp.setTipo(tipo);
        mp.setNombre(nombre);
        mp.setDescripcion(descripcion);
        mp.setUnidadEstrategica(unidadEstrategica);
        mp.setResponsablePrincipal(responsable);
        mp.setObjetivosEstrategicos(objetivos);
        mp.setEstadoDocumentacion(estado);
        mp.setCreadoPor("admin");
        mp.setActualizadoPor("admin");
        mp.setPorcentajeAvance(estado.getPorcentajeAsociado());
        return macroprocesoRepository.save(mp);
    }

    private Proceso crearProcesoN1(String codigo, String nombre, String descripcion,
                                   String objetivos, Macroproceso macroproceso,
                                   EstadoDocumentacion estado) {
        Proceso p = new Proceso();
        p.setCodigo(codigo);
        p.setNivel(1);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setObjetivos(objetivos);
        p.setMacroproceso(macroproceso);
        p.setEstadoDocumentacion(estado);
        p.setCreadoPor("admin");
        p.setActualizadoPor("admin");
        p.setPorcentajeAvance(estado.getPorcentajeAsociado());
        return procesoRepository.save(p);
    }

    private Proceso crearProcesoN2(String codigo, String nombre, String descripcion,
                                   Macroproceso macroproceso, Proceso procesoPadre,
                                   EstadoDocumentacion estado) {
        Proceso p = new Proceso();
        p.setCodigo(codigo);
        p.setNivel(2);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setMacroproceso(macroproceso);
        p.setProcesoPadre(procesoPadre);
        p.setEstadoDocumentacion(estado);
        p.setCreadoPor("admin");
        p.setActualizadoPor("admin");
        p.setPorcentajeAvance(estado.getPorcentajeAsociado());
        return procesoRepository.save(p);
    }

    private Subproceso crearSubprocesoN1(String codigo, String nombre, String descripcion,
                                         Proceso procesoN2, EstadoDocumentacion estado) {
        Subproceso sp = new Subproceso();
        sp.setCodigo(codigo);
        sp.setNivel(1);
        sp.setNombre(nombre);
        sp.setDescripcion(descripcion);
        sp.setProceso(procesoN2);
        sp.setEstadoDocumentacion(estado);
        sp.setCreadoPor("admin");
        sp.setActualizadoPor("admin");
        sp.setPorcentajeAvance(estado.getPorcentajeAsociado());
        return subprocesoRepository.save(sp);
    }

    private void cargarObjetivosEspecificos() {
        crearObjetivoEspecifico(
            "Excelencia Academica",
            "Garantizar la calidad y pertinencia de la formacion academica mediante programas actualizados"
        );

        crearObjetivoEspecifico(
            "Investigacion e Innovacion",
            "Promover la investigacion cientifica y tecnologica que contribuya al desarrollo nacional"
        );

        crearObjetivoEspecifico(
            "Vinculacion con la Sociedad",
            "Fortalecer los vinculos con la comunidad mediante proyectos de extension y servicio social"
        );

        crearObjetivoEspecifico(
            "Gestion Institucional Eficiente",
            "Optimizar los procesos administrativos para una gestion transparente y eficaz"
        );

        crearObjetivoEspecifico(
            "Desarrollo Tecnologico",
            "Implementar soluciones tecnologicas que mejoren los servicios institucionales"
        );

        crearObjetivoEspecifico(
            "Bienestar Universitario",
            "Promover el desarrollo integral de la comunidad universitaria"
        );
    }

    private ObjetivoEspecifico crearObjetivoEspecifico(String nombre, String descripcion) {
        ObjetivoEspecifico objetivo = new ObjetivoEspecifico();
        objetivo.setNombre(nombre);
        objetivo.setDescripcion(descripcion);
        objetivo.setCreadoPor("admin");
        objetivo.setActualizadoPor("admin");
        return objetivoEspecificoRepository.save(objetivo);
    }
}
