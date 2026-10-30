# VetAndes — Plan de pruebas por rúbrica (Versión B)

## P1 (4pts): CRUD y errores

1. `GET /propietarios` → 200 y cinco propietarios.
2. `GET /prestaciones` → 200 y nueve prestaciones. Crear PRS-010 → 201, editar → 204, estado → 200, borrar nueva no usada → 204.
3. `POST /prestaciones` con código repetido `PRS-001` → 409. Con precio cero → 400 con `campos.precio`. `GET /prestaciones/99999` → 404.
4. Crear mascota con propietarioId 1, editar → 204, borrar mascota sin atenciones → 204.
5. Repetir nombre `Luna` para propietarioId 1 → 409; propietario 99999 → 404. Borrar mascotaId 1 → 409.

## P2 (4pts): atenciones, rollback, reporte

6. Antes del registro inválido ejecuta `SELECT COUNT(*) FROM atencion;` y `SELECT COUNT(*) FROM detalle_atencion;`. POST con mascotaId 1 y 3 detalles (1, 2, 3) → 409 en el tercero (vacuna exclusiva de gatos); ambos conteos siguen idénticos.
7. POST con mascotaId 1 y detalles 1, 2, 5 → 201; subtotal 175, descuento 17.50, total 157.50. Query SQL `02_verificar_datos.sql`.
8. PATCH anulando atención 2 dentro de 48h con motivo 10..200 → 200. Anular 3 (más de 48h) → 409; anular 4 (anulada) → 409. Motivo corto → 400.
9. GET /atenciones?mascota=Lu&especie=PERRO&estado=REGISTRADA → filtrado; informe por especie excluye anuladas y usa JPQL GROUP BY.

## P3 (4pts): seguridad

10. Login `admin`, `recepcion`, `veterinario` → 200 con token, `tipo=Bearer`, 3600 segundos, roles/permisos; inactivo/contraseña incorrecta → 401.
11. Sin token a `/mascotas` → 401. Token alterado → 401, con formato de error común.
12. Con token RECEPCIONISTA, PATCH anular y GET reporte → 403. Con token VETERINARIO, POST mascota y POST atención → 403. Con token ADMIN, todas las operaciones autorizadas.
13. Revisar password en tabla usuarios: empieza por `$2a$`, no aparece `Vet2026*`.

## P4 y P5: integración futura con vetandes-frontend

14. Frontend Angular: core/shared/layout/features, HTML/CSS reales, header de altura fija, lazy routing y sin datos escritos a mano.
15. Formulario reactivo y `propietarios` desde API; mensajes de `campos` debajo del campo; errores 409 mostrados sin bloquear.
16. Atenciones dinámicas 1..8, sin repetidos, precio/subtotal instantáneos solo como vista previa, cálculo definitivo del backend; no perder líneas en 409.
17. Login fuera del layout; `sessionStorage` con vencimiento; AuthInterceptor solo API; 401 cierra sesión, 403 no; guards + acceso denegado. Probar URL `/atenciones/nueva` con usuario VETERINARIO.

## Evidencias y trazabilidad

Capturas: login, cada CRUD, atención con descuentos, rollback (ambos SELECT antes/después), reporte, 403 backend, acceso denegado SPA. Colección Postman: 200/201/400/401/403/404/409. README en ambos repos. PDF `ApellidoLP2ExamenU2.pdf`. Mínimo seis commits reales y tag `v1.0-examen-u2`.


## Resultados de pruebas automatizadas - 30/10/2026

Estudiante: Josias Pari Mamani

Comando: .\mvnw.cmd test

Resultado: BUILD SUCCESS

- PermisosPorRolTest: 3 pruebas correctas.
- AtencionCalculosTest: 3 pruebas correctas.
- AtencionServiceTest: 2 pruebas correctas.
- Total: 8 pruebas ejecutadas.
- Fallos: 0.
- Errores: 0.
- Pruebas omitidas: 0.

Se verificaron los permisos por rol, los calculos de atenciones
y las pruebas del servicio de atenciones.
Validacion Postman 30/10/2026: login ADMIN HTTP 200 OK; autenticacion JWT y permisos devueltos correctamente.
Validacion Swagger: usuario VETERINARIO rechazado con HTTP 403 al crear mascota.
