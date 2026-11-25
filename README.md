# Job Tracker

Aplicación full stack para llevar el seguimiento de postulaciones laborales: empresa, puesto, estado del proceso, origen y notas.

Es un proyecto chico, pensado para practicar y mostrar el stack **Spring Boot + PostgreSQL + React** con buenas prácticas de backend: capas separadas, validación, manejo de errores, migraciones y tests.

## Stack

| Capa | Tecnologías |
| --- | --- |
| Backend | Java 21, Spring Boot 3.5, Spring Web, Spring Data JPA, Bean Validation |
| Base de datos | PostgreSQL 16, migraciones con Flyway |
| Frontend | React 19, Vite |
| Tests | JUnit 5, Mockito, `@WebMvcTest`, `@DataJpaTest` (H2), Vitest + Testing Library |
| DevOps | Docker Compose (base de datos), GitHub Actions (CI) |

## Cómo correrlo

Requisitos: Java 21, Maven 3.9+, Node 22+ y Docker.

```bash
# 1. Base de datos
docker compose up -d db

# 2. Backend (http://localhost:8080)
cd backend
mvn spring-boot:run

# 3. Frontend (http://localhost:5173), en otra terminal
cd frontend
npm install
npm run dev
```

El frontend reenvía las llamadas a `/api` al backend mediante el proxy de Vite, así que no hace falta configurar CORS en desarrollo.

La conexión a la base se puede cambiar con las variables `DB_URL`, `DB_USER` y `DB_PASSWORD` (por defecto apuntan al contenedor de `docker-compose.yml`).

## Tests

```bash
cd backend && mvn test          # servicio (Mockito), controller (MockMvc) y repositorio (H2)
cd frontend && npx vitest run   # componentes React
```

## API

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/applications?status=APPLIED` | Lista postulaciones (filtro opcional por estado) |
| GET | `/api/applications/{id}` | Detalle |
| POST | `/api/applications` | Crea una postulación (`201` + header `Location`) |
| PUT | `/api/applications/{id}` | Reemplaza los datos de una postulación |
| PATCH | `/api/applications/{id}/status` | Cambia solo el estado |
| DELETE | `/api/applications/{id}` | Elimina (`204`) |

Estados posibles: `APPLIED`, `SCREENING`, `TECHNICAL`, `OFFER`, `REJECTED`.

Ejemplo:

```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -d '{"company": "Acme", "title": "Java Developer", "source": "LinkedIn"}'
```

Los errores se devuelven en formato [RFC 7807](https://www.rfc-editor.org/rfc/rfc7807) (`ProblemDetail`). Por ejemplo, una validación fallida responde `400` con el detalle por campo en `errors`.

## Estructura

```text
backend/src/main/java/com/facumedero/jobtracker
├── domain       Entidad JPA (JobApplication) y enum de estados
├── repository   Interfaz de Spring Data JPA
├── service      Lógica de negocio y transacciones
├── dto          Requests y responses (records) con validación
└── web          Controller REST y manejo global de excepciones
backend/src/main/resources/db/migration   Migraciones de Flyway
frontend/src                              Componentes React y cliente de la API
```

## Decisiones de diseño

- **DTOs separados de la entidad:** la API no expone el modelo de persistencia directamente.
- **Flyway + `ddl-auto: validate`:** el esquema se versiona con migraciones y Hibernate solo comprueba que coincida con las entidades.
- **Inyección por constructor:** sin `@Autowired` en campos, lo que facilita los tests unitarios.
- **Tests por capas:** unitarios sin Spring para el servicio, y *test slices* (`@WebMvcTest`, `@DataJpaTest`) que levantan solo la parte necesaria del contexto.

Para repasar los conceptos de Spring que aparecen en el código, ver [docs/SPRING_NOTES.md](docs/SPRING_NOTES.md).

## Posibles mejoras

- Autenticación con Spring Security y usuarios.
- Paginación y búsqueda por texto.
- Tests de integración con Testcontainers sobre PostgreSQL real.
- Dockerfile para backend y frontend, y despliegue.
