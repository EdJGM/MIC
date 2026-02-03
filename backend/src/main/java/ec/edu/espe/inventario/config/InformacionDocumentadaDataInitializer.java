package ec.edu.espe.inventario.config;

import ec.edu.espe.inventario.model.entity.InformacionDocumentada;
import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumento;
import ec.edu.espe.inventario.repository.InformacionDocumentadaRepository;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Inicializador de datos de prueba para Información Documentada
 * Solo se ejecuta si la base de datos está vacía
 */
@Component
@RequiredArgsConstructor
public class InformacionDocumentadaDataInitializer implements CommandLineRunner {

    private final InformacionDocumentadaRepository informacionDocumentadaRepository;
    private final MacroprocesoRepository macroprocesoRepository;
    private final ProcesoRepository procesoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar si no hay datos
        if (informacionDocumentadaRepository.count() > 0) {
            return;
        }

        // Verificar que existan macroprocesos y procesos
        if (macroprocesoRepository.count() == 0) {
            System.out.println("⚠️ No hay macroprocesos registrados. Ejecuta primero el inicializador de inventario de procesos.");
            return;
        }

        System.out.println("📄 Inicializando datos de prueba para Información Documentada...");

        // Obtener primer macroproceso y proceso para las relaciones
        Macroproceso macroproceso = macroprocesoRepository.findAll().get(0);
        Proceso proceso = procesoRepository.findAll().isEmpty() ? null : procesoRepository.findAll().get(0);

        // Crear documentos de ejemplo para cada sede
        
        // Documento 1 - MATRIZ
        InformacionDocumentada doc1 = new InformacionDocumentada();
        doc1.setFechaSolicitud(LocalDate.of(2024, 6, 18));
        doc1.setUnidad("UPDI");
        doc1.setSolicitadoPor("ABRIL PORRAS VICTOR HUGO");
        doc1.setSede("MATRIZ");
        doc1.setMacroproceso(macroproceso);
        doc1.setProcesoN1(proceso);
        doc1.setTipoDocumento("FORMATOS");
        doc1.setNombreDocumento("FOR CALENDARIOS TECNOLOGIAS");
        doc1.setMotivo("CREACION");
        doc1.setCodigoDocumento("UPDI-FOR-2024-V1-029");
        doc1.setEstado(EstadoDocumento.ACTIVO);
        doc1.setMes(6);
        doc1.setAnio(2024);
        doc1.setVersion("V1");
        doc1.setSecuencial(29);
        informacionDocumentadaRepository.save(doc1);

        // Documento 2 - MATRIZ
        InformacionDocumentada doc2 = new InformacionDocumentada();
        doc2.setFechaSolicitud(LocalDate.of(2024, 6, 18));
        doc2.setUnidad("UPDI");
        doc2.setSolicitadoPor("ABRIL PORRAS VICTOR HUGO");
        doc2.setSede("MATRIZ");
        doc2.setMacroproceso(macroproceso);
        doc2.setProcesoN1(proceso);
        doc2.setTipoDocumento("FORMATOS");
        doc2.setNombreDocumento("FOR CALENDARIO LICENCIATURA");
        doc2.setMotivo("CREACION");
        doc2.setCodigoDocumento("UPDI-FOR-2024-V1-030");
        doc2.setEstado(EstadoDocumento.ACTIVO);
        doc2.setMes(6);
        doc2.setAnio(2024);
        doc2.setVersion("V1");
        doc2.setSecuencial(30);
        informacionDocumentadaRepository.save(doc2);

        // Documento 3 - MATRIZ
        InformacionDocumentada doc3 = new InformacionDocumentada();
        doc3.setFechaSolicitud(LocalDate.of(2024, 8, 14));
        doc3.setUnidad("UPDI");
        doc3.setSolicitadoPor("BENITEZ GAICEDO BLANCA DE LAS MERCEDES");
        doc3.setSede("MATRIZ");
        doc3.setMacroproceso(macroproceso);
        doc3.setProcesoN1(proceso);
        doc3.setTipoDocumento("INFORMES");
        doc3.setNombreDocumento("INFORME LEVANTAMIENTO ACADEMICO");
        doc3.setMotivo("ACTUALIZACION");
        doc3.setCodigoDocumento("UPDI-INF-2024-V3-029");
        doc3.setEstado(EstadoDocumento.ACTIVO);
        doc3.setMes(8);
        doc3.setAnio(2024);
        doc3.setVersion("V3");
        doc3.setSecuencial(29);
        informacionDocumentadaRepository.save(doc3);

        // Documento 4 - LATACUNGA
        InformacionDocumentada doc4 = new InformacionDocumentada();
        doc4.setFechaSolicitud(LocalDate.of(2024, 8, 24));
        doc4.setUnidad("UAFA");
        doc4.setSolicitadoPor("NACATA LOACHAMIN NATHALY ALEXANDRA");
        doc4.setSede("LATACUNGA");
        doc4.setMacroproceso(macroproceso);
        doc4.setProcesoN1(proceso);
        doc4.setTipoDocumento("LINEAMIENTOS");
        doc4.setNombreDocumento("LINEAMIENTOS PARA LA EJECUCIÓN");
        doc4.setLugarEvento("https://docs.google.com/document/d/1-Ciw32dCioKpig1fXxcaF");
        doc4.setMotivo("CREACION");
        doc4.setCodigoDocumento("UAFA-LIN-2024-V1-001");
        doc4.setEstado(EstadoDocumento.ACTIVO);
        doc4.setMes(8);
        doc4.setAnio(2024);
        doc4.setVersion("V1");
        doc4.setSecuencial(1);
        informacionDocumentadaRepository.save(doc4);

        // Documento 5 - SANTO DOMINGO
        InformacionDocumentada doc5 = new InformacionDocumentada();
        doc5.setFechaSolicitud(LocalDate.of(2024, 9, 15));
        doc5.setUnidad("UTIC");
        doc5.setSolicitadoPor("USUARIO SANTO DOMINGO");
        doc5.setSede("SANTO_DOMINGO");
        doc5.setMacroproceso(macroproceso);
        doc5.setTipoDocumento("PROTOCOLOS");
        doc5.setNombreDocumento("PROTOCOLO DE SEGURIDAD INFORMATICA");
        doc5.setMotivo("CREACION");
        doc5.setCodigoDocumento("UTIC-PRO-2024-V1-001");
        doc5.setEstado(EstadoDocumento.ACTIVO);
        doc5.setMes(9);
        doc5.setAnio(2024);
        doc5.setVersion("V1");
        doc5.setSecuencial(1);
        informacionDocumentadaRepository.save(doc5);

        System.out.println("✅ Se crearon " + informacionDocumentadaRepository.count() + " documentos de prueba");
        System.out.println("   - Matriz: " + informacionDocumentadaRepository.findBySedeAndEstadoOrderByFechaSolicitudDesc("MATRIZ", EstadoDocumento.ACTIVO).size());
        System.out.println("   - Latacunga: " + informacionDocumentadaRepository.findBySedeAndEstadoOrderByFechaSolicitudDesc("LATACUNGA", EstadoDocumento.ACTIVO).size());
        System.out.println("   - Santo Domingo: " + informacionDocumentadaRepository.findBySedeAndEstadoOrderByFechaSolicitudDesc("SANTO_DOMINGO", EstadoDocumento.ACTIVO).size());
    }
}
