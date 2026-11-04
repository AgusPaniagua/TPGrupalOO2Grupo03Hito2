<h1 align="center">🚀 TP Grupal OO2 - Grupo 03</h1> <p align="center"> <img src="https://img.shields.io/badge/JAVA-ED8B00?style=for-the-badge" alt="Java"> <img src="https://img.shields.io/badge/CONTRIBUTORS-3-007ec6?style=for-the-badge&labelColor=555555" alt="Contributors"> </p> <p align="center"> <em>Proyecto correspondiente al hito 2 del TP del grupo 3 de la materia Programación Orientada a Objetos 2 del año 2026 (Universidad Nacional de Lanús)</em> </p>

## 👥 Integrantes
|  Nombre y Apellido    | Usuario GitHub |
| :--- | :--- |
|  **Agustín Paniagua** | [@AgusPaniagua](https://github.com/AgusPaniagua) |
|  **Fernando Fogonsa** | [@FerFogonsa](https://github.com/FerFogonsa) |
|  **Sergio Cadima**    | [@sergiocdima](https://github.com/sergiocdima) |

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Framework:** Spring Boot (Web, Thymeleaf, Validation, Data JPA, Security)
* **Herramienta de build:** Maven (con wrapper incluido)
* **Librerías:** Lombok, ModelMapper
* **Base de Datos:** MySQL
* **Control de Versiones:** Git & GitHub

---
## ⚙️ Pasos para correr la aplicación
 
### Requisitos previos
 
* **JDK 17** o superior instalado.
* **MySQL** instalado y corriendo en `localhost:3306`.
* **Git**.
> No hace falta instalar Maven: el proyecto incluye el wrapper (`mvnw`).

### 1. Clonar el repositorio
 
```bash
git clone https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2.git
cd TPGrupalOO2Grupo03Hito2
```
### 2. Crear la base de datos
 
La aplicación usa una base llamada `tp_hito_2_grupo_3`. **Se crea sola** al iniciar
(la URL incluye `createDatabaseIfNotExist=true`), así que este paso es opcional.
Si preferís crearla a mano desde MySQL Workbench o la consola:
 
```sql
CREATE DATABASE tp_hito_2_grupo_3;
```
 
Las tablas las genera Hibernate automáticamente al arrancar (`ddl-auto=update`).

### 3. Definir las variables de entorno
 
Las credenciales **no están incluidas en el repositorio**. Cada persona debe usar
los datos de **su propio MySQL local** y definirlos como variables de entorno
antes de ejecutar la aplicación.
 
| Variable | Descripción | Obligatoria | Valor por defecto |
| :--- | :--- | :---: | :--- |
| `DB_USER` | Usuario de MySQL | Sí | - |
| `DB_PASSWORD` | Contraseña de MySQL | Sí | - |
| `DB_URL` | URL de conexión | No | `jdbc:mysql://localhost:3306/tp_hito_2_grupo_3?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Argentina/Buenos_Aires` |
| `ADMIN_PASSWORD` | Contraseña de ADMIN | Sí | - |
| `MAIL_USER` | Usuario de GMAIL | Sí | - |
| `MAIL_PASSWORD` | Contraseña de GMAIL | Sí | - |
 
Solo hace falta definir `DB_URL` si tu MySQL usa otro puerto o querés otro nombre de base.
 
### 4. Ejecutar la aplicación
 
Elegí **una** de estas opciones según cómo trabajes.
 
#### Opción A: desde la terminal
 
<details>
<summary><b>Windows (PowerShell)</b></summary>
```powershell
$env:DB_USER="tu_usuario"
$env:DB_PASSWORD="tu_contraseña"
./mvnw spring-boot:run
```
 
En CMD (en vez de PowerShell) el comando es `mvnw spring-boot:run`, sin `./`.
</details>
<details>
<summary><b>Linux / macOS</b></summary>
```bash
export DB_USER=tu_usuario
export DB_PASSWORD=tu_contraseña
./mvnw spring-boot:run
```
</details>
> Las variables definidas en la terminal valen solo para esa sesión.
 
#### Opción B: desde el IDE
 
Ejecutá la clase principal `TpHito2Application` con las variables ya configuradas:
 
<details>
<summary><b>VS Code</b></summary>
En `.vscode/launch.json`, dentro de la configuración de la app:
 
```json
"env": {
    "DB_USER": "tu_usuario",
    "DB_PASSWORD": "tu_contraseña"
}
```
 
Ejecutá desde la vista **Run and Debug** (`Ctrl+Shift+D`) con esa configuración.
</details>
<details>
<summary><b>IntelliJ IDEA</b></summary>
Run → Edit Configurations → tu aplicación → campo **Environment variables**
(si no aparece: *Modify options* → *Environment variables*):
 
```
DB_USER=tu_usuario;DB_PASSWORD=tu_contraseña
```
</details>
<details>
<summary><b>Eclipse / STS</b></summary>
Run Configurations → pestaña **Environment** → *Add* (una entrada por variable).
</details>

### 🔧 Solución de problemas
 
| Error | Causa probable |
| :--- | :--- |
| `Could not resolve placeholder 'DB_USER'` | No se definieron las variables de entorno. |
| `Access denied for user` | Usuario o contraseña incorrectos. |
| `Communications link failure` | MySQL no está corriendo, o usa otro puerto (definir `DB_URL`). |
| `Port 8080 was already in use` | Hay otra aplicación usando el puerto 8080. Cerrala o cambiá `server.port`. |
 
---
 
## 📐 Diagrama de clases
 
[Ver diagrama de clases](https://drive.google.com/file/d/1RUXI5VZuCEs2UrrsgPaD6FVfayusrqAq/view?usp=drive_link)
 
---

## 📌 Casos de Uso

| Caso de Uso / Funcionalidad | Responsable |
| :--- | :--- |
| Precargar administrador - Otarga un user con rol admin | Agustin Paniagua - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUAdminPrecargadoInicioSesionAgustinPaniagua))_ |
| Registrar Unidad con Responsable - Crea una unidad(FoodTruck o PuestoDesarmable) y crea un user con rol Responsable | Agustin Paniagua - PR: _([link_](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CURegistroDeUnidadConResponsableAgustinPaniagua)) |
| Calcular canon - Calcula el canon de una unidad de venta | Agustin Paniagua - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUCalculoDeCanonAgustinPaniagua))_ |
|Reporte mayores canon- Da un informe de las unidades de venta con mayor canon | Agustin Paniagua - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUReporteMayoresCanonAgustinPaniagua))_ |
| Crud de empleado - alta, baja, modificacion, reactivacion de empledos con envio de email con sus credenciales | Agustin Paniagua - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUCrudDeEmpleadoAgustinPaniagua))_ |
| Alta de platos - alta y vista de platos creados por un responsable | Agustin Paniagua - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUAltaDePlatosAgustinPaniagua))_ |
| Calcular plato estrella - Muestra cual fue el plato mas vendido en un festival | Agustin Paniagua - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUPlatoEstrellaAgustinPaniagua))_ |
| Responsable - Filtros por uno o varios atributos (empleados no eliminados y platos) | Fernando Fogonsa - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUFiltrosYLiquidacionResponsableFernandoFogonsa))_ |
| Responsable - Liquidación de haberes | Fernando Fogonsa - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUFiltrosYLiquidacionResponsableFernandoFogonsa))_ |
| Empleado - Ver mi información (festivales y unidades) | Fernando Fogonsa - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUFiltrosYLiquidacionResponsableFernandoFogonsa))_ |
| Empleado - Sueldo por festival y descarga de recibo en PDF | Fernando Fogonsa - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUFiltrosYLiquidacionResponsableFernandoFogonsa))_ |
| Empleado - Ver festival activo y unidad actual | Fernando Fogonsa - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUFiltrosYLiquidacionResponsableFernandoFogonsa))_ |
| Empleado (Cajero) - Cerrar caja | Fernando Fogonsa - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUFiltrosYLiquidacionResponsableFernandoFogonsa))_ |
| Funciones de admin - ver festivales, unidades de x festival | Sergio Cadima - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUFuncionalidadAdministrador))_ |
| Reporte de recaudacion - Vista de reporte de recaudacion | Sergio Cadima - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUReporteDeRecaudacionSergioCadima))_ |
| Calculo rentabilidad neta - calcula rentabilida | Sergio Cadima - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUCalculoRentabilidadNetaSergioCadima))_ |
| Calculo rentabilidad neta entre fechas - calcula rentabilida entre dos fechas dadas | Sergio Cadima - PR: _([link](https://github.com/AgusPaniagua/TPGrupalOO2Grupo03Hito2/tree/CUCalculoRentabilidadNetaEntreFechasSergioCadima))_ |

---

## 🧪 Datos de prueba

Ejecutar `scripts/datos_prueba_fer.sql` en MySQL **después de levantar la aplicación una vez** (Hibernate crea las tablas).
Crea un festival activo hoy, una unidad con su responsable, empleados, platos y pedidos.
Usuarios (contraseña `Test1234`; el usuario es el email completo): `julian.acosta@epicentro.com` (responsable), `lucia.fernandez@epicentro.com`, `martin.gomez@epicentro.com`, `camila.rodriguez@epicentro.com` (cajera) y `nicolas.sosa@epicentro.com` (cajero) como empleados. `facundo.herrera@epicentro.com` es un empleado dado de baja.

---

## 📁 Estructura del proyecto
 
```
src/
├── main/
│   ├── java/com/grupo3/tp_hito_2/   # Código fuente
│   └── resources/
│       └── application.properties   # Configuración (usa variables de entorno)
└── test/                            # Tests
pom.xml                              # Dependencias y configuración de Maven
```
