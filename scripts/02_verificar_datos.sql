SELECT 'PROPIETARIOS' recurso,COUNT(*) total FROM propietario
UNION ALL SELECT 'PRESTACIONES',COUNT(*) FROM prestacion
UNION ALL SELECT 'MASCOTAS',COUNT(*) FROM mascota
UNION ALL SELECT 'ATENCIONES',COUNT(*) FROM atencion
UNION ALL SELECT 'DETALLES',COUNT(*) FROM detalle_atencion
UNION ALL SELECT 'USUARIOS',COUNT(*) FROM usuarios;
SELECT a.numero,a.estado,a.subtotal,a.descuento,a.total,d.prestacion_id,d.cantidad,d.precio_unitario,d.subtotal detalle_subtotal
FROM atencion a JOIN detalle_atencion d ON d.atencion_id=a.id ORDER BY a.id,d.id;
SELECT username,activo,password FROM usuarios;
