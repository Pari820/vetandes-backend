# VetAndes API — Examen Parcial II, Versión B

Backend REST de clínica veterinaria, adaptado a partir de la **arquitectura propia de PharmaBackend** (Spring Boot 4, Oracle, JPA, Flyway, JWT, DTO, services, controllers, seguridad y excepciones). Es un proyecto **independiente**: no contiene las entidades `Venta`, `Producto` ni `Categoria`.

## 1. Requisitos

- IntelliJ IDEA y JDK 21 o superior (recomendado 21; Maven Wrapper incluido).
- Oracle 23 / FREEPDB1 en Docker, puerto de tu entorno `1522`.
- DBeaver conectado a `SYSTEM` en `FREEPDB1`.
- Puerto `8080` libre. Si ejecutas AutoAndes/PharmaBackend, detén ese proceso antes.

## 2. Crear usuario y base de datos (solo la primera vez)

En DBeaver, conexión **SYSTEM / FREEPDB1** (NO `CDB$ROOT`): ejecuta `scripts/01_crear_usuario_oracle.sql`. Creará el usuario `VETANDES` sin modificar `AUTOANDES` ni `PHARMADB`.

| Campo | Valor |
|---|---|
| Host | `localhost` |
| Puerto | `1522` |
| Service Name | `FREEPDB1` |
| Usuario del nuevo esquema | `VETANDES` |
| Contraseña local de ejemplo | `VetAndes2026` |

Este paso se hace **una sola vez**: si el usuario ya existe, no repitas `CREATE USER`. Al iniciar la aplicación, Flyway ejecuta automáticamente `V1__esquema_y_semilla.sql` en el esquema `VETANDES`.

## 3. Iniciar backend

1. Extrae el ZIP. Abre la carpeta `vetandes-backend` (donde está `pom.xml`) en IntelliJ.
2. Espera la carga de Maven y ejecuta `VetAndesApplication.java` con el botón verde. También puedes utilizar CMD en esa carpeta:

```bat
mvnw.cmd spring-boot:run
```

3. Verifica `Started VetAndesApplication` en la consola.
4. Swagger UI: http://localhost:8080/swagger-ui.html (también `/swagger-ui/index.html`).
5. La conexión usa, por defecto, `jdbc:oracle:thin:@//localhost:1522/FREEPDB1`. Variables opcionales: `VETANDES_DB_URL`, `VETANDES_DB_USER`, `VETANDES_DB_PASSWORD`, `VETANDES_JWT_SECRET` (secreto JWT en **BASE64**, ≥32 bytes decodificados), `SERVER_PORT`.

Para comprobar los datos abre DBeaver con la conexión `VETANDES` y ejecuta `scripts/02_verificar_datos.sql`.

## 4. Usuarios semilla (solo para evaluación local)

| Usuario | Contraseña | Rol | Estado |
|---|---|---|---|
| `admin` | `Vet2026*` | ADMIN | activo |
| `recepcion` | `Vet2026*` | RECEPCIONISTA | activo |
| `veterinario` | `Vet2026*` | VETERINARIO | activo |
| `inactivo` | `Vet2026*` | RECEPCIONISTA | inactivo, responde 401 |

Contraseñas almacenadas con **BCrypt**, nunca en texto plano. Cambiar secreto y credenciales antes de cualquier despliegue fuera de la práctica.

## 5. Endpoints (`/api/v1`)

| Método | Ruta | Permiso |
|---|---|---|
| POST | `/auth/login` | público |
| GET | `/auth/me` | autenticado |
| GET | `/propietarios` | MASCOTA_LEER |
| GET/POST | `/prestaciones?especie=PERRO` | PRESTACION_LEER / PRESTACION_ESCRIBIR |
| GET/PUT/DELETE | `/prestaciones/{id}` | PRESTACION_LEER / PRESTACION_ESCRIBIR |
| PATCH | `/prestaciones/{id}/estado` | PRESTACION_ESCRIBIR |
| GET/POST | `/mascotas?propietarioId=1` | MASCOTA_LEER / MASCOTA_ESCRIBIR |
| GET/PUT/DELETE | `/mascotas/{id}` | MASCOTA_LEER / MASCOTA_ESCRIBIR |
| POST/GET | `/atenciones?mascota&especie&estado&desde&hasta` | ATENCION_REGISTRAR / ATENCION_LEER |
| GET | `/atenciones/{id}` | ATENCION_LEER |
| PATCH | `/atenciones/{id}/anular` | ATENCION_ANULAR |
| GET | `/reportes/atenciones-por-especie?desde&hasta` | REPORTE_VER |

La búsqueda de prestaciones por especie incluye las de `TODAS`. `POST` responde 201; `PUT` y `DELETE` responden 204; las consultas devuelven 200. Los errores comparten `timestamp`, `status`, `error`, `mensaje`, `ruta` y `campos` para validaciones.

## 6. Demostración de RN-03 y RN-04

Ingresa como `recepcion`. Swagger → Authorize → introduce **solo el token JWT**, sin escribir `Bearer` delante.

Registro válido (mascota 1, PERRO, prestaciones válidas):

```json
{
  "mascotaId": 1,
  "peso": 22.50,
  "diagnostico": "Control preventivo y vacunación",
  "detalles": [
    {"prestacionId": 1, "cantidad": 1},
    {"prestacionId": 2, "cantidad": 1},
    {"prestacionId": 5, "cantidad": 1}
  ]
}
```

Resultado esperado: `subtotal = 175.00`, `descuento = 17.50`, `total = 157.50`. **El primer correlativo nuevo es `AT-000005`**. Es el backend quien calcula importes.

Registro inválido para evidenciar RN-03 (tercer detalle incompatible): cambia el tercero por `{"prestacionId": 3, "cantidad": 1}` (vacuna de GATO para PERRO). Debe producir **409** y no guardar ninguna fila de esa operación. Comprueba las cantidades de filas antes y después en DBeaver.

Para RN-05 anula `AT-000002` (menos de 48h) con:

```json
{"motivo":"Se reagendó la atención a solicitud del propietario"}
```

Una atención anterior a 48h (`AT-000003`) debe producir **409**, al igual que una ya anulada.

## 7. Seguridad y reglas

- **ADMIN:** todos los permisos.
- **RECEPCIONISTA:** consulta prestaciones, mascotas y atenciones; escribe mascotas y registra atenciones. No anula ni ve reportes.
- **VETERINARIO:** consulta prestaciones, mascotas y atenciones; escribe prestaciones; anula atenciones y consulta reportes. No escribe mascotas ni registra atenciones.
- Sin token o con token inválido/vencido: 401 JSON. Con token válido pero permiso insuficiente: 403 JSON.
- `@PreAuthorize` verifica los permisos **en el backend**. `PermisosPorRol` calcula la unión de permisos para varios roles. JWT tiene 3600s; Spring Security usa STATELESS.
- RN-01: código de prestación único y 409 al borrar si se usó. RN-02: no duplicados por dueño ni borrado de mascotas con atenciones. RN-03: detalles 1..8, cantidades 1..3, no duplicados, prestaciones activas y compatibles. RN-04: 10% con tres o más prestaciones diferentes. RN-05: anulación de REGISTRADA, dentro de 48h y con motivo de 10..200.
- La persistencia de atención usa `@Transactional`, con cabecera y detalles en una sola transacción. El reporte usa una consulta JPQL `GROUP BY`, no `findAll()` en memoria.
- CORS permite `http://localhost:4200`, los verbos requeridos y `Authorization`.

## 8. Archivos para pruebas

- `postman/VetAndes_Examen_VersionB.postman_collection.json`: endpoints, permisos y casos positivos/negativos.
- `src/test/`: pruebas unitarias de reglas, permisos y servicio. Corre `mvnw.cmd test`.
- `scripts/02_verificar_datos.sql`: inspección de tablas/semillas/órdenes en Oracle.
- `docs/PLAN_PRUEBAS.md`: evidencias para cada pregunta, incluyendo rollback y errores.

## 9. Git y entrega

Crear repositorio propio `vetandes-backend`, ramas `main`, `develop`, `examen-b-<apellido>`; registrar commits reales por funcionalidad y etiquetar `v1.0-examen-u2`. El ZIP viene sin `.git`, sin `target` y sin credenciales personales. No se han simulado commits históricos.

## Estado de validación

Código fuente, estructura y casos revisados estáticamente. **La compilación Maven completa y la integración con Oracle de tu computadora deben ejecutarse allí**; no deben darse por verificadas hasta hacerlo. Registra cualquier error real para corregirlo.
