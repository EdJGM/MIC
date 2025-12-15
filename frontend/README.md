# Sistema de Gestión de Inventario de Procesos - ESPE

Prototipo funcional desarrollado para la Universidad de las Fuerzas Armadas ESPE.

## 🏗️ Arquitectura del Sistema

- **Backend**: Java 17 + Spring Boot 3.2.0
- **Frontend**: Angular 17 + Bootstrap 5
- **Base de Datos**: H2 (en memoria para prototipo)
- **Comunicación API**: RESTful + Feign Client
- **Patrón**: MVC

## 📋 Requisitos Previos

### Backend
- Java 17 o superior
- Maven 3.6+
- IntelliJ IDEA (recomendado)

### Frontend
- Node.js 18+ y npm
- Angular CLI 17

## 🚀 Instalación y Ejecución

### Backend (Spring Boot)

1. Navegar al directorio del backend:
```bash
cd backend
```

2. Compilar el proyecto con Maven:
```bash
mvn clean install
```

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

O desde IntelliJ:
- Abrir el proyecto backend
- Ejecutar `InventarioProcesosApplication.java`

El backend estará disponible en: `http://localhost:8080`

### Frontend (Angular)

1. Navegar al directorio del frontend:
```bash
cd frontend
```

2. Instalar dependencias:
```bash
npm install
```

3. Ejecutar la aplicación:
```bash
ng serve
```

O con npm:
```bash
npm start
```

El frontend estará disponible en: `http://localhost:4200`

## 📚 Estructura del Proyecto

### Backend
```
backend/
├── src/main/java/ec/edu/espe/inventario/
│   ├── config/              # Configuraciones (CORS, DataInitializer)
│   ├── controller/          # Controladores REST
│   ├── exception/           # Manejo de excepciones
│   ├── model/
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   └── enums/          # Enumeraciones
│   ├── repository/         # Repositorios JPA
│   └── service/            # Lógica de negocio
└── src/main/resources/
    └── application.properties
```

### Frontend
```
frontend/
├── src/app/
│   ├── components/
│   │   └── macroprocesos-list/  # Componente de macroprocesos
│   ├── models/                   # Interfaces TypeScript
│   ├── services/                 # Servicios HTTP
│   ├── app.component.ts
│   ├── app.config.ts
│   └── app.routes.ts
└── src/environments/
    └── environment.ts
```

## 🔌 API Endpoints

### Macroprocesos
- `GET /api/macroprocesos` - Listar todos
- `GET /api/macroprocesos/{id}` - Obtener por ID
- `POST /api/macroprocesos` - Crear nuevo
- `PUT /api/macroprocesos/{id}` - Actualizar
- `DELETE /api/macroprocesos/{id}` - Eliminar
- `PATCH /api/macroprocesos/{id}/estado?nuevoEstado=VALOR` - Actualizar estado

### Procesos
- `GET /api/procesos` - Listar todos
- `GET /api/procesos/{id}` - Obtener por ID
- `GET /api/procesos/macroproceso/{macroprocesoId}` - Por macroproceso
- `POST /api/procesos` - Crear nuevo
- `PUT /api/procesos/{id}` - Actualizar
- `DELETE /api/procesos/{id}` - Eliminar
- `PATCH /api/procesos/{id}/estado?nuevoEstado=VALOR` - Actualizar estado

### Subprocesos
- `GET /api/subprocesos` - Listar todos
- `GET /api/subprocesos/{id}` - Obtener por ID
- `GET /api/subprocesos/proceso/{procesoId}` - Por proceso
- `POST /api/subprocesos` - Crear nuevo
- `PUT /api/subprocesos/{id}` - Actualizar
- `DELETE /api/subprocesos/{id}` - Eliminar
- `PATCH /api/subprocesos/{id}/estado?nuevoEstado=VALOR` - Actualizar estado

## 🧪 Probar con Postman

1. Importar colección de Postman (crear archivo JSON con los endpoints)
2. Configurar variable de entorno: `baseUrl = http://localhost:8080/api`
3. Probar cada endpoint

Ejemplo de petición POST para crear macroproceso:
```json
{
  "tipo": "VDC",
  "nombre": "Gestión Académica",
  "descripcion": "Macroproceso de gestión académica institucional",
  "unidadEstrategica": "VDC",
  "responsablePrincipal": "Dr. Juan Pérez",
  "objetivosEstrategicos": "Garantizar excelencia académica",
  "estadoDocumentacion": "NO_DOCUMENTADO"
}
```

## 📊 Datos de Prueba

El sistema incluye un `DataInitializer` que carga automáticamente:
- 4 Macroprocesos
- 5 Procesos
- 9 Subprocesos

Esto permite probar inmediatamente todas las funcionalidades.

## 🎯 Funcionalidades Implementadas

### Módulo de Inventario de Procesos
✅ CRUD completo de Macroprocesos
✅ CRUD completo de Procesos
✅ CRUD completo de Subprocesos
✅ Jerarquía de 3 niveles (Macroproceso → Proceso → Subproceso)
✅ Generación automática de códigos únicos
✅ Estados de documentación (8 estados)
✅ Cálculo automático de porcentaje de avance
✅ Actualización en cascada de porcentajes
✅ Validaciones de negocio
✅ Datos de prueba precargados

### Frontend
✅ Interfaz responsive con Bootstrap 5
✅ Listado de macroprocesos con tabla interactiva
✅ Formulario de creación/edición
✅ Indicadores visuales de estado y progreso
✅ Manejo de errores
✅ Integración con API REST

## 🔐 Consola H2

Para ver la base de datos en tiempo real:
1. Ir a: `http://localhost:8080/h2-console`
2. Configuración:
   - JDBC URL: `jdbc:h2:mem:inventario_db`
   - User Name: `sa`
   - Password: (dejar vacío)

## 📝 Modelo de Datos

### Macroproceso (Nivel 1)
- Código único (generado automáticamente)
- Tipo (REC, UTIC, USGN, VDC, VAD, VAG, VII)
- Nombre, descripción
- Unidad estratégica
- Responsable principal
- Objetivos estratégicos
- Estado de documentación
- Porcentaje de avance (calculado automáticamente)

### Proceso (Nivel 2)
- Vinculado a un Macroproceso
- Código único (generado automáticamente)
- Nombre, descripción, objetivos
- Estado de documentación
- Porcentaje de avance (calculado automáticamente)

### Subproceso (Nivel 3)
- Vinculado a un Proceso
- Código único (generado automáticamente)
- Nombre, descripción
- Estado de documentación
- Porcentaje de avance (basado en estado)

## 🎨 Estados de Documentación

1. **No documentado** (0%)
2. **Levantamiento** (30%)
3. **Flujodiagramación** (60%)
4. **Caracterización** (60%)
5. **Validación** (75%)
6. **Legalizado** (90%)
7. **Difundido** (100%)
8. **Mejora** (100%)

## 🛠️ Tecnologías Utilizadas

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Web
- Spring Cloud OpenFeign
- H2 Database
- Lombok
- Jakarta Validation

### Frontend
- Angular 17 (Standalone Components)
- Bootstrap 5.3.2
- Bootstrap Icons
- RxJS
- TypeScript 5.2

## 🚧 Limitaciones del Prototipo

- Base de datos en memoria (se pierde al reiniciar)
- Usuario hardcodeado como "admin"
- Sin autenticación/autorización
- Sin módulo de documentos normativos
- Sin módulo de reportes
- Sin módulo de administración

## 📌 Próximos Pasos

1. Implementar componentes para Procesos y Subprocesos
2. Agregar vista de matriz jerárquica
3. Implementar dashboard con indicadores
4. Agregar módulo de documentos normativos
5. Implementar sistema de reportes
6. Agregar autenticación y autorización
7. Migrar a base de datos persistente (PostgreSQL/MySQL)
8. Implementar auditoría completa

## 👥 Equipo de Desarrollo

- **Analista/Desarrollador Backend**: Edgar Gallegos
- **Analista/Desarrollador Frontend**: Denilson Cachiguango
- **Analista de Calidad**: Gicela Almagro
- **Aprobador**: Ing. Geovany Raura

## 📄 Licencia

Este proyecto es un prototipo académico para la Universidad de las Fuerzas Armadas ESPE.

---

**Universidad de las Fuerzas Armadas ESPE**
*Sistema de Gestión de Inventario de Procesos*
Versión 1.0.0 - Diciembre 2025
