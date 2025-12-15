package ec.edu.espe.inventario.config;

import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.entity.Subproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import ec.edu.espe.inventario.repository.SubprocesoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializa la base de datos con datos de prueba
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final MacroprocesoRepository macroprocesoRepository;
    private final ProcesoRepository procesoRepository;
    private final SubprocesoRepository subprocesoRepository;
    
    @Override
    public void run(String... args) {
        log.info("Iniciando carga de datos de prueba...");
        
        if (macroprocesoRepository.count() == 0) {
            cargarDatosPrueba();
            log.info("Datos de prueba cargados exitosamente");
        } else {
            log.info("La base de datos ya contiene datos");
        }
    }
    
    private void cargarDatosPrueba() {
        // Macroproceso 1: Gestión Académica
        Macroproceso mp1 = crearMacroproceso(
            "VDC-MP-001",
            TipoMacroproceso.VDC,
            "Gestión Académica",
            "Macroproceso responsable de la gestión integral de actividades académicas",
            "VDC",
            "Dr. Juan Pérez",
            "Garantizar la excelencia académica y formación integral de estudiantes",
            EstadoDocumentacion.CARACTERIZACION
        );
        
        // Procesos del Macroproceso 1
        Proceso p1 = crearProceso(
            "VDC-MP-001-P-001",
            "Diseño Curricular",
            "Proceso de diseño y actualización de mallas curriculares",
            "Diseñar mallas curriculares actualizadas y pertinentes",
            mp1,
            EstadoDocumentacion.VALIDACION
        );
        
        // Subprocesos del Proceso 1
        crearSubproceso(
            "VDC-MP-001-P-001-SP-001",
            "Análisis de Necesidades Formativas",
            "Identificación de necesidades del mercado laboral y tendencias",
            p1,
            EstadoDocumentacion.LEGALIZADO
        );
        
        crearSubproceso(
            "VDC-MP-001-P-001-SP-002",
            "Diseño de Perfil de Egreso",
            "Definición de competencias y resultados de aprendizaje",
            p1,
            EstadoDocumentacion.DIFUNDIDO
        );
        
        crearSubproceso(
            "VDC-MP-001-P-001-SP-003",
            "Aprobación Curricular",
            "Proceso de aprobación de la malla curricular por organismos competentes",
            p1,
            EstadoDocumentacion.CARACTERIZACION
        );
        
        Proceso p2 = crearProceso(
            "VDC-MP-001-P-002",
            "Gestión de Matrículas",
            "Proceso de inscripción y matrícula de estudiantes",
            "Garantizar proceso de matrícula eficiente y transparente",
            mp1,
            EstadoDocumentacion.LEGALIZADO
        );
        
        crearSubproceso(
            "VDC-MP-001-P-002-SP-001",
            "Registro de Aspirantes",
            "Inscripción de nuevos aspirantes al sistema",
            p2,
            EstadoDocumentacion.DIFUNDIDO
        );
        
        crearSubproceso(
            "VDC-MP-001-P-002-SP-002",
            "Validación de Documentos",
            "Verificación de documentación presentada por aspirantes",
            p2,
            EstadoDocumentacion.LEGALIZADO
        );
        
        // Macroproceso 2: Investigación
        Macroproceso mp2 = crearMacroproceso(
            "VII-MP-002",
            TipoMacroproceso.VII,
            "Gestión de Investigación",
            "Macroproceso para fomentar y gestionar proyectos de investigación",
            "VII",
            "Dra. María González",
            "Promover investigación científica de alto impacto",
            EstadoDocumentacion.FLUJODIAGRAMACION
        );
        
        Proceso p3 = crearProceso(
            "VII-MP-002-P-001",
            "Formulación de Proyectos",
            "Elaboración y presentación de propuestas de investigación",
            "Generar proyectos de investigación innovadores",
            mp2,
            EstadoDocumentacion.LEVANTAMIENTO
        );
        
        crearSubproceso(
            "VII-MP-002-P-001-SP-001",
            "Identificación de Líneas de Investigación",
            "Definir líneas estratégicas de investigación institucional",
            p3,
            EstadoDocumentacion.LEVANTAMIENTO
        );
        
        crearSubproceso(
            "VII-MP-002-P-001-SP-002",
            "Elaboración de Propuesta",
            "Redacción técnica de la propuesta de investigación",
            p3,
            EstadoDocumentacion.NO_DOCUMENTADO
        );
        
        // Macroproceso 3: Administración
        Macroproceso mp3 = crearMacroproceso(
            "VAD-MP-003",
            TipoMacroproceso.VAD,
            "Gestión Financiera",
            "Administración de recursos financieros institucionales",
            "VAD",
            "Ing. Carlos Ramírez",
            "Optimizar uso de recursos financieros",
            EstadoDocumentacion.VALIDACION
        );
        
        Proceso p4 = crearProceso(
            "VAD-MP-003-P-001",
            "Planificación Presupuestaria",
            "Elaboración del presupuesto anual institucional",
            "Planificar presupuesto alineado a objetivos estratégicos",
            mp3,
            EstadoDocumentacion.VALIDACION
        );
        
        crearSubproceso(
            "VAD-MP-003-P-001-SP-001",
            "Recolección de Necesidades",
            "Identificar necesidades presupuestarias de todas las unidades",
            p4,
            EstadoDocumentacion.CARACTERIZACION
        );
        
        crearSubproceso(
            "VAD-MP-003-P-001-SP-002",
            "Consolidación Presupuestaria",
            "Integrar y consolidar todas las solicitudes presupuestarias",
            p4,
            EstadoDocumentacion.VALIDACION
        );
        
        // Macroproceso 4: Tecnología
        Macroproceso mp4 = crearMacroproceso(
            "UTIC-MP-004",
            TipoMacroproceso.UTIC,
            "Soporte Tecnológico",
            "Gestión de infraestructura y servicios tecnológicos",
            "UTIC",
            "Ing. Ana Flores",
            "Proveer servicios tecnológicos de calidad",
            EstadoDocumentacion.NO_DOCUMENTADO
        );
        
        Proceso p5 = crearProceso(
            "UTIC-MP-004-P-001",
            "Mesa de Ayuda",
            "Atención y resolución de incidencias tecnológicas",
            "Resolver incidencias tecnológicas de manera eficiente",
            mp4,
            EstadoDocumentacion.NO_DOCUMENTADO
        );
        
        crearSubproceso(
            "UTIC-MP-004-P-001-SP-001",
            "Recepción de Tickets",
            "Registro de solicitudes y problemas tecnológicos",
            p5,
            EstadoDocumentacion.NO_DOCUMENTADO
        );
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
    
    private Proceso crearProceso(String codigo, String nombre, String descripcion,
                                 String objetivos, Macroproceso macroproceso,
                                 EstadoDocumentacion estado) {
        Proceso p = new Proceso();
        p.setCodigo(codigo);
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
    
    private Subproceso crearSubproceso(String codigo, String nombre, String descripcion,
                                       Proceso proceso, EstadoDocumentacion estado) {
        Subproceso sp = new Subproceso();
        sp.setCodigo(codigo);
        sp.setNombre(nombre);
        sp.setDescripcion(descripcion);
        sp.setProceso(proceso);
        sp.setEstadoDocumentacion(estado);
        sp.setCreadoPor("admin");
        sp.setActualizadoPor("admin");
        sp.setPorcentajeAvance(estado.getPorcentajeAsociado());
        return subprocesoRepository.save(sp);
    }
}
