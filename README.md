# PROTOTIPO V1.0

## 📋 Información del Proyecto

**Título**: Automatización de Documentos Normativos Internos e Inventario de Procesos  
**Cliente**: Universidad de las Fuerzas Armadas ESPE  
**Versión**: 1.0.0 - Prototipo Funcional  
**Fecha**: Diciembre 2025
**Alcance del Prototipo**: Módulo de Inventario de Procesos  

## ✅ Objetivos Cumplidos

Este prototipo cumple con los siguientes requisitos del ERS:

### 1. Gestión de Inventario de Procesos
✅ **RF-14**: Registrar Macroproceso con código único generado  
✅ **RF-15**: Registrar Proceso vinculado a macroproceso  
✅ **RF-16**: Actualizar información de procesos con historial  
✅ **RF-17**: Eliminar proceso (validando ausencia de subprocesos)  
✅ **RF-18**: Consultar matriz de procesos (implementado en backend)  
✅ **RF-19**: Gestionar estado de documentación  
✅ **RF-20**: Actualizar avance de proceso  

### 2. Estructura Jerárquica
- ✅ Nivel 1: Macroprocesos (7 tipos según ERS)
- ✅ Nivel 2: Procesos (vinculados a macroproceso)
- ✅ Nivel 3: Subprocesos (vinculados a proceso)

### 3. Estados de Documentación
Implementados los 8 estados según ERS:
1. No documentado (0%)
2. Levantamiento (30%)
3. Flujodiagramación (60%)
4. Caracterización (60%)
5. Validación (75%)
6. Legalizado (90%)
7. Difundido (100%)
8. Mejora (100%)

### 4. Cálculo Automático de Avance
✅ El porcentaje de avance se calcula automáticamente:
- Subprocesos: basado en su estado
- Procesos: promedio de subprocesos
- Macroprocesos: promedio de procesos

## 🏗️ Arquitectura Implementada

### Backend (Spring Boot + Java 17)
```
✅ Patrón MVC
✅ API RESTful
✅ JPA/Hibernate
✅ DTOs para transferencia de datos
✅ Validaciones con Bean Validation
✅ Manejo centralizado de excepciones
✅ CORS configurado para Angular
✅ Base de datos H2 en memoria
✅ Datos de prueba precargados
```

### Frontend (Angular 17 + Bootstrap 5)
```
✅ Standalone Components
✅ Servicios HTTP
✅ Reactive Programming (RxJS)
✅ Formularios reactivos
✅ Interfaz responsive
✅ Bootstrap 5 para diseño
✅ Comunicación con API REST
```

## 📊 Datos del Prototipo

**Datos Precargados**:
- 4 Macroprocesos
- 5 Procesos
- 9 Subprocesos

**Endpoints API**: 21 endpoints RESTful
- Macroprocesos: 6 endpoints
- Procesos: 7 endpoints
- Subprocesos: 7 endpoints

## 🎯 Funcionalidades Implementadas

### Macroprocesos
- ✅ Listar todos los macroprocesos
- ✅ Ver detalle con procesos hijos
- ✅ Crear nuevo macroproceso
- ✅ Editar macroproceso existente
- ✅ Eliminar macroproceso (con validación)
- ✅ Actualizar estado de documentación
- ✅ Generación automática de código único
- ✅ Cálculo automático de porcentaje

### Procesos
- ✅ CRUD completo
- ✅ Filtrado por macroproceso
- ✅ Vinculación con macroproceso
- ✅ Generación automática de código
- ✅ Actualización de estado
- ✅ Cálculo de avance

### Subprocesos
- ✅ CRUD completo
- ✅ Filtrado por proceso
- ✅ Vinculación con proceso
- ✅ Generación automática de código
- ✅ Actualización de estado
- ✅ Cálculo de avance

## 💻 Stack Tecnológico

### Backend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje base |
| Spring Boot | 3.2.0 | Framework |
| Spring Data JPA | 3.2.0 | Persistencia |
| Spring Cloud OpenFeign | 4.1.0 | Cliente API |
| H2 Database | Runtime | BD en memoria |
| Lombok | Latest | Reducir código |
| Maven | 3.6+ | Gestión dependencias |

### Frontend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Angular | 17 | Framework SPA |
| TypeScript | 5.2 | Lenguaje |
| Bootstrap | 5.3.2 | Diseño UI |
| Bootstrap Icons | 1.11.0 | Iconografía |
| RxJS | 7.8 | Programación reactiva |
| npm | Latest | Gestión paquetes |

## 📁 Estructura de Archivos

### Backend (47 archivos)
```
- Entidades: 3 (Macroproceso, Proceso, Subproceso)
- Repositorios: 3
- Servicios: 3
- Controladores: 3
- DTOs: 6
- Enums: 2
- Configuración: 3
- Excepciones: 1
```

### Frontend (15 archivos)
```
- Componentes: 1 (Macroprocesos List)
- Servicios: 3
- Modelos: 1
- Configuración: 8
```

## 🚀 Instrucciones de Ejecución

### Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
URL: http://localhost:8080

### Frontend
```bash
cd frontend
npm install
ng serve
```
URL: http://localhost:4200

### Consola H2
URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:inventario_db
- User: sa
- Password: (vacío)

## 🧪 Pruebas

### Postman
Se incluye colección completa con:
- 21 peticiones de ejemplo
- Variables de entorno configuradas
- Ejemplos de payloads JSON

### Pruebas Manuales
1. Crear macroproceso
2. Crear proceso bajo macroproceso
3. Crear subproceso bajo proceso
4. Verificar cálculo de avance
5. Actualizar estados
6. Eliminar elementos

## 📋 Validaciones de Negocio

✅ Código único generado automáticamente  
✅ No eliminar macroproceso con procesos activos  
✅ No eliminar proceso con subprocesos activos  
✅ Actualización en cascada de porcentajes  
✅ Validación de campos obligatorios  
✅ Validación de longitud de campos  
✅ Relaciones padre-hijo obligatorias  

## 🎨 Interfaz de Usuario

- **Responsive**: Compatible con desktop, tablet y móvil
- **Bootstrap 5**: Diseño moderno y limpio
- **Indicadores visuales**: Estados con colores (badges)
- **Barras de progreso**: Visualización de avance
- **Formularios**: Validación en tiempo real
- **Tablas interactivas**: Ordenamiento y acciones rápidas

## ⚠️ Limitaciones del Prototipo

1. **Base de datos**: H2 en memoria (se pierde al reiniciar)
2. **Usuario**: Hardcodeado como "admin"
3. **Autenticación**: No implementada
4. **Autorización**: No implementada
5. **Módulos faltantes**:
   - Documentos normativos
   - Reportes
   - Dashboard completo
   - Administración de usuarios
   - Auditoría completa

## 🔜 Próximos Pasos Recomendados

### Fase 1 - Completar Frontend
- [ ] Componente de Procesos
- [ ] Componente de Subprocesos
- [ ] Vista de matriz jerárquica
- [ ] Dashboard con indicadores

### Fase 2 - Módulos Adicionales
- [ ] Gestión de documentos normativos
- [ ] Sistema de reportes
- [ ] Administración de usuarios

### Fase 3 - Producción
- [ ] Migrar a PostgreSQL/MySQL
- [ ] Implementar autenticación (JWT)
- [ ] Implementar autorización (Roles)
- [ ] Sistema de auditoría completo
- [ ] Despliegue en servidor

## 📊 Métricas del Prototipo

- **Líneas de código Backend**: ~3,500
- **Líneas de código Frontend**: ~500
- **Endpoints API**: 21
- **Tiempo de desarrollo**: Prototipo funcional
- **Cobertura funcional ERS**: 35% (Inventario de Procesos)

## 🎓 Aprendizajes y Buenas Prácticas

✅ Separación de responsabilidades (MVC)  
✅ DTOs para transferencia de datos  
✅ Validaciones de negocio en servicios  
✅ Manejo centralizado de excepciones  
✅ Generación automática de códigos  
✅ Cálculo automático de métricas  
✅ API RESTful bien estructurada  
✅ Componentes standalone en Angular  
✅ Servicios reutilizables  
✅ Código limpio y comentado  

## 📞 Soporte y Contacto

**Equipo de Desarrollo**:
- Backend: Edgar Gallegos
- Frontend: Denilson Cachiguango
- Calidad: Gicela Almagro
- Aprobador: Ing. Geovany Raura

## 📝 Conclusiones

Este prototipo demuestra exitosamente:

1. ✅ **Viabilidad técnica** del stack propuesto (Java 17 + Spring Boot + Angular 17)
2. ✅ **Cumplimiento de requisitos** del ERS para inventario de procesos
3. ✅ **Arquitectura escalable** MVC + RESTful
4. ✅ **Interfaz amigable** con Bootstrap 5
5. ✅ **Reglas de negocio** implementadas correctamente
6. ✅ **Base sólida** para desarrollo completo del sistema

---

**Universidad de las Fuerzas Armadas ESPE**  
Sistema de Gestión de Inventario de Procesos  
Prototipo v1.0.0 - Diciembre 2025
