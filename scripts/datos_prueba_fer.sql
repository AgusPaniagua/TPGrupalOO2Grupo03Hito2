-- =====================================================================
-- Datos de prueba: casos de uso de Responsable (filtros, liquidación) y
-- Empleado (información, sueldos + PDF, asignación actual, cierre de caja).
--
-- Ejecutar DESPUÉS de levantar la aplicación una vez (Hibernate crea las tablas).
-- Todas las fechas son relativas a CURDATE(): el festival siempre queda "activo hoy".
-- Contraseña de TODOS los usuarios de prueba: Test1234
--
-- Usuarios (todos @epicentro.com):
--   julian.acosta (RESPONSABLE)
--   lucia.fernandez (lavaplatos), martin.gomez (cocinero), camila.rodriguez (cajera noche),
--   nicolas.sosa (cajero mañana)  -> EMPLEADO
--   facundo.herrera (cajero dado de baja) -> EMPLEADO inactivo
-- =====================================================================

-- Festival activo hoy: 38 días (igual al ejemplo del enunciado)
INSERT INTO costo (por_superficie, por_montaje, plus_por_electricidad, sueldo_base)
VALUES (1500, 800, 500, 100000);
SET @costo_id = LAST_INSERT_ID();

INSERT INTO festival (nombre, temporada, fecha_inicio, fecha_fin, id_costo)
VALUES ('Festival Sabores de Buenos Aires', CURDATE(), DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 27 DAY), @costo_id);
SET @festival_id = LAST_INSERT_ID();

-- Usuarios
INSERT INTO `user` (username, password, enabled, created_at, updated_at) VALUES ('julian.acosta@epicentro.com', '$2a$10$z88MU0w5wkk/A6UcqvynbOV0ZBpyFfLJNmVjyMrK8d0EmbP27KnxS', 1, NOW(6), NOW(6));
SET @u_resp = LAST_INSERT_ID();
INSERT INTO user_role (user_id, role, created_at, updated_at) VALUES (@u_resp, 'RESPONSABLE', NOW(6), NOW(6));

INSERT INTO `user` (username, password, enabled, created_at, updated_at) VALUES ('lucia.fernandez@epicentro.com', '$2a$10$z88MU0w5wkk/A6UcqvynbOV0ZBpyFfLJNmVjyMrK8d0EmbP27KnxS', 1, NOW(6), NOW(6));
SET @u_lava = LAST_INSERT_ID();
INSERT INTO user_role (user_id, role, created_at, updated_at) VALUES (@u_lava, 'EMPLEADO', NOW(6), NOW(6));

INSERT INTO `user` (username, password, enabled, created_at, updated_at) VALUES ('martin.gomez@epicentro.com', '$2a$10$z88MU0w5wkk/A6UcqvynbOV0ZBpyFfLJNmVjyMrK8d0EmbP27KnxS', 1, NOW(6), NOW(6));
SET @u_coc = LAST_INSERT_ID();
INSERT INTO user_role (user_id, role, created_at, updated_at) VALUES (@u_coc, 'EMPLEADO', NOW(6), NOW(6));

INSERT INTO `user` (username, password, enabled, created_at, updated_at) VALUES ('camila.rodriguez@epicentro.com', '$2a$10$z88MU0w5wkk/A6UcqvynbOV0ZBpyFfLJNmVjyMrK8d0EmbP27KnxS', 1, NOW(6), NOW(6));
SET @u_noche = LAST_INSERT_ID();
INSERT INTO user_role (user_id, role, created_at, updated_at) VALUES (@u_noche, 'EMPLEADO', NOW(6), NOW(6));

INSERT INTO `user` (username, password, enabled, created_at, updated_at) VALUES ('nicolas.sosa@epicentro.com', '$2a$10$z88MU0w5wkk/A6UcqvynbOV0ZBpyFfLJNmVjyMrK8d0EmbP27KnxS', 1, NOW(6), NOW(6));
SET @u_man = LAST_INSERT_ID();
INSERT INTO user_role (user_id, role, created_at, updated_at) VALUES (@u_man, 'EMPLEADO', NOW(6), NOW(6));

INSERT INTO `user` (username, password, enabled, created_at, updated_at) VALUES ('facundo.herrera@epicentro.com', '$2a$10$z88MU0w5wkk/A6UcqvynbOV0ZBpyFfLJNmVjyMrK8d0EmbP27KnxS', 1, NOW(6), NOW(6));
SET @u_baja = LAST_INSERT_ID();
INSERT INTO user_role (user_id, role, created_at, updated_at) VALUES (@u_baja, 'EMPLEADO', NOW(6), NOW(6));

-- Unidad de venta (Food Truck) a cargo del responsable
INSERT INTO unidad_de_venta (nombre_comercial, superficie, codigo_unico, festival_id, responsable_id)
VALUES ('Sabores del Sur', 20, 'SDSUR-0001', @festival_id, @u_resp);
SET @unidad_id = LAST_INSERT_ID();
INSERT INTO food_truck (id_unidad_de_venta, patente, conexion_electrica) VALUES (@unidad_id, 'AB123CD', 1);

-- Personal (sueldo base $100.000; plus fijo por categoría: cocinero 150.000 / ayudante 100.000 / lavaplatos 50.000)
-- Lavaplatos: 38 días -> $225.000 (ejemplo del enunciado)
INSERT INTO staff (nombre, apellido, dni, fecha_de_nacimiento, fecha_de_ingreso, sueldo_base, unidad_de_venta_id, user_id, activo)
VALUES ('Lucia', 'Fernandez', 38412567, '1995-04-10', DATE_SUB(CURDATE(), INTERVAL 2 YEAR), 100000, @unidad_id, @u_lava, 1);
INSERT INTO cocinero (id_staff, especialidad, plus_fijo) VALUES (LAST_INSERT_ID(), 'Lavaplatos', 50000);

INSERT INTO staff (nombre, apellido, dni, fecha_de_nacimiento, fecha_de_ingreso, sueldo_base, unidad_de_venta_id, user_id, activo)
VALUES ('Martin', 'Gomez', 33781204, '1988-09-21', DATE_SUB(CURDATE(), INTERVAL 5 YEAR), 100000, @unidad_id, @u_coc, 1);
INSERT INTO cocinero (id_staff, especialidad, plus_fijo) VALUES (LAST_INSERT_ID(), 'Cocinero', 150000);

-- Cajeros (la antigüedad se calcula con la fecha de ingreso)
INSERT INTO staff (nombre, apellido, dni, fecha_de_nacimiento, fecha_de_ingreso, sueldo_base, unidad_de_venta_id, user_id, activo)
VALUES ('Camila', 'Rodriguez', 36590318, '1992-02-14', DATE_SUB(CURDATE(), INTERVAL 3 YEAR), 100000, @unidad_id, @u_noche, 1);
INSERT INTO cajero (id_staff, turno_de_trabajo) VALUES (LAST_INSERT_ID(), 'Noche');

INSERT INTO staff (nombre, apellido, dni, fecha_de_nacimiento, fecha_de_ingreso, sueldo_base, unidad_de_venta_id, user_id, activo)
VALUES ('Nicolas', 'Sosa', 41205963, '1999-12-01', DATE_SUB(CURDATE(), INTERVAL 6 MONTH), 100000, @unidad_id, @u_man, 1);
INSERT INTO cajero (id_staff, turno_de_trabajo) VALUES (LAST_INSERT_ID(), 'Mañana');

-- Empleado dado de baja LÓGICA (activo = 0; no debe aparecer en filtros ni en la liquidación)
INSERT INTO staff (nombre, apellido, dni, fecha_de_nacimiento, fecha_de_ingreso, sueldo_base, unidad_de_venta_id, user_id, activo)
VALUES ('Facundo', 'Herrera', 35127640, '1990-06-30', DATE_SUB(CURDATE(), INTERVAL 1 YEAR), 100000, @unidad_id, @u_baja, 0);
INSERT INTO cajero (id_staff, turno_de_trabajo) VALUES (LAST_INSERT_ID(), 'Noche');

-- Platos
INSERT INTO plato (nombre, precio, costo_de_produccion, unidad_de_venta_id) VALUES ('Choripan', 3000, 1000, @unidad_id);
SET @p1 = LAST_INSERT_ID();
INSERT INTO plato (nombre, precio, costo_de_produccion, unidad_de_venta_id) VALUES ('Hamburguesa completa', 5000, 2000, @unidad_id);
SET @p2 = LAST_INSERT_ID();
INSERT INTO plato (nombre, precio, costo_de_produccion, unidad_de_venta_id) VALUES ('Milanesa al pan', 4000, 1500, @unidad_id);
SET @p3 = LAST_INSERT_ID();

-- Pedidos de HOY (para el cierre de caja: total $15.000) y uno de ayer
INSERT INTO pedido (fecha_de_transaccion, id_unidad_de_venta) VALUES (CURDATE(), @unidad_id);
SET @ped1 = LAST_INSERT_ID();
INSERT INTO pedido_plato (sub_total, cantidad, id_pedido, id_plato) VALUES (6000, 2, @ped1, @p1);   -- 2 choripanes
INSERT INTO pedido_plato (sub_total, cantidad, id_pedido, id_plato) VALUES (5000, 1, @ped1, @p2);   -- 1 hamburguesa completa

INSERT INTO pedido (fecha_de_transaccion, id_unidad_de_venta) VALUES (CURDATE(), @unidad_id);
SET @ped2 = LAST_INSERT_ID();
INSERT INTO pedido_plato (sub_total, cantidad, id_pedido, id_plato) VALUES (4000, 1, @ped2, @p3);   -- 1 milanesa al pan

INSERT INTO pedido (fecha_de_transaccion, id_unidad_de_venta) VALUES (DATE_SUB(CURDATE(), INTERVAL 1 DAY), @unidad_id);
SET @ped3 = LAST_INSERT_ID();
INSERT INTO pedido_plato (sub_total, cantidad, id_pedido, id_plato) VALUES (5000, 1, @ped3, @p2);

-- Resultados esperados (festival de 38 días -> 3 tramos de 15 días = 150%):
--   Lucia (lavaplatos) = 1,5 * (100.000 + 50.000)       = 225.000
--   Martin (cocinero)  = 1,5 * (100.000 + 150.000)      = 375.000
--   Camila (3 años)    = 1,5 * (100.000 + 3 * 5.000)    = 172.500
--   Nicolas (0 años)   = 1,5 * 100.000                  = 150.000
--   TOTAL unidad                                       = 922.500
-- Cierre de caja de hoy (camila.rodriguez o nicolas.sosa): 2 pedidos, $15.000
