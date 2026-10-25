# AutomaticFactus

Aplicación web para apoyar y automatizar procesos comerciales relacionados con cotizaciones y facturación electrónica. El backend está desarrollado con Java y Spring Boot, utiliza MySQL para la persistencia y puede ejecutarse en contenedores Docker.

El proyecto también contempla entornos separados de desarrollo y producción, con despliegues automatizados y verificaciones de salud.

## Contenido

- [Funcionalidades](#funcionalidades)
- [Tecnologías](#tecnologías)
- [Arquitectura y despliegue](#arquitectura-y-despliegue)
- [Ejecución local](#ejecución-local)
- [Configuración](#configuración)
- [API disponible](#api-disponible)
- [CI/CD](#cicd)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Seguridad](#seguridad)

## Funcionalidades

- Consulta y registro de clientes.
- Consulta de tipos de identificación y tipos de orden.
- Registro de órdenes y consulta del siguiente número de orden.
- Generación del contenido HTML de una cotización a partir de los datos recibidos.
- Consulta del siguiente número de cotización.
- Endpoints para comprobar la disponibilidad de la aplicación y el estado de la conexión con la base de datos.

El objetivo del proyecto es reducir tareas manuales en los procesos comerciales y facilitar la gestión de la información. La implementación funcional puede evolucionar a medida que se agreguen nuevas integraciones y módulos.

## Tecnologías

| Tecnología | Uso |
| --- | --- |
| Java 17 | Lenguaje de programación |
| Spring Boot 3.3.4 | Aplicación web y API |
| Spring Data JPA / Hibernate | Persistencia y acceso a datos |
| MySQL | Base de datos relacional |
| Maven | Dependencias y construcción |
| Thymeleaf | Renderizado de plantillas HTML de cotización |
| Docker | Construcción y ejecución en contenedores |
| GitHub Actions | Automatización de despliegues |

## Arquitectura y despliegue

La aplicación Spring Boot sirve la interfaz web y los endpoints de la API, y se conecta a MySQL mediante variables de entorno. Docker empaqueta la aplicación para ejecutarla en distintos entornos.

El repositorio incluye dos opciones de despliegue:

- **Render:** [render.yaml](render.yaml) define un servicio web basado en Docker con `/health` como health check.
- **Servidor propio (homelab):** [docker-compose.yml](docker-compose.yml) define el contenedor de desarrollo en la red Docker externa `homelab`. Los workflows de GitHub Actions para `main` y `develop` despliegan en los entornos respectivos.

En la infraestructura propia, los dominios de producción y desarrollo se publican mediante Cloudflare Tunnel y Nginx, según la configuración del servidor. La configuración del servidor, de la red y de la base de datos debe administrarse fuera del repositorio.

## Ejecución local

### Requisitos

- JDK 17.
- Maven 3.9 o compatible.
- Una instancia de MySQL accesible.
- Docker Engine y Docker Compose, si se ejecuta en contenedor.

### 1. Clonar el repositorio

```bash
git clone https://github.com/jhonatan0820/AutomaticFactus.git
cd AutomaticFactus
```

Para trabajar con la rama de desarrollo:

```bash
git switch develop
git pull origin develop
```

### 2. Configurar la conexión a MySQL

Define las variables de entorno indicadas en [Configuración](#configuración). Para una ejecución local con Maven, configúralas en la sesión del sistema o en el entorno de desarrollo.

### 3. Ejecutar con Maven

```bash
mvn spring-boot:run
```

La aplicación escucha en el puerto `8080` de forma predeterminada. Puedes cambiarlo mediante la variable `PORT`.

Para construir el artefacto:

```bash
mvn clean package
```

### Ejecutar con Docker Compose

El Compose incluido está preparado para el despliegue de desarrollo en el homelab: requiere un archivo `.env` y una red Docker externa llamada `homelab`. No publica un puerto del contenedor en el host. En otra instalación, crea esa red o adapta la configuración de red y agrega una publicación de puertos si necesitas acceder desde el host.

```bash
docker network create homelab
docker compose build
docker compose up -d
```

Comandos útiles:

```bash
docker compose logs -f
docker compose ps
docker compose down
```

Para comprobar el endpoint desde el host, usa la ejecución local con Maven o configura una publicación de puertos en Compose. Por ejemplo:

```bash
curl -i http://localhost:8080/health
```

## Configuración

La aplicación carga la configuración sensible desde variables de entorno. Para MySQL se requieren:

| Variable | Descripción | Ejemplo |
| --- | --- | --- |
| `URL_MYSQL` | URL JDBC de la base de datos | `jdbc:mysql://localhost:3306/automaticfactus` |
| `USER_MYSQL` | Usuario de MySQL | `usuario` |
| `PASS_MYSQL` | Contraseña de MySQL | `contraseña` |
| `PORT` | Puerto HTTP de la aplicación; opcional | `8080` |

Ejemplo de archivo `.env` para uso local:

```dotenv
URL_MYSQL=jdbc:mysql://localhost:3306/automaticfactus
USER_MYSQL=usuario
PASS_MYSQL=contraseña
PORT=8080
```

El archivo `.env` está excluido del control de versiones. También se puede usar `application-secrets.yml` local; el archivo es opcional y está excluido de Git. No reutilices credenciales de producción en desarrollo.

## API disponible

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/health` | Comprueba que el backend responde. Devuelve `ok`; no comprueba la base de datos. |
| `GET` | `/api/db-status` | Indica si la base de datos está lista. |
| `GET` | `/api/clientes/buscar?sigla=CC&numero=1234` | Busca un cliente por tipo y número de identificación. |
| `POST` | `/api/clientes/guardarCliente` | Crea o actualiza un cliente. |
| `POST` | `/api/orders/guardarOrden` | Guarda una orden. |
| `GET` | `/api/orders/proximo-numero?idTypeOrder=1` | Consulta el siguiente número de orden para el tipo indicado. |
| `GET` | `/api/tipos-identificacion` | Lista tipos de identificación. |
| `GET` | `/api/tipos-orden` | Lista tipos de orden. |
| `POST` | `/api/cotizacion` | Recibe los datos de una cotización en JSON y devuelve su contenido HTML. |
| `GET` | `/api/cotizacion/proximo-numero` | Consulta el siguiente número de cotización. |

Consulta los controladores y DTOs del proyecto para conocer los formatos de solicitud y respuesta de cada operación.

## CI/CD

Los workflows se ejecutan en runners `self-hosted`:

- [deploy.yml](.github/workflows/deploy.yml): despliega los cambios publicados en `main` al entorno de producción.
- [deploy-dev.yml](.github/workflows/deploy-dev.yml): despliega los cambios publicados en `develop` al entorno de desarrollo; también permite ejecución manual.

Ambos flujos realizan un backup previo, construyen y levantan el contenedor, y ejecutan un health check. El flujo de desarrollo intenta restaurar la versión anterior si falla el despliegue o el health check; el flujo de producción intenta hacerlo si falla el health check. Las rutas y los scripts de backup del servidor están configurados en los workflows y deben existir en el runner.

El health check `/health` verifica que el backend esté disponible. El endpoint `/api/db-status` puede utilizarse por separado para consultar el estado de la base de datos.

## Estructura del proyecto

```text
AutomaticFactus/
├── .github/
│   └── workflows/
│       ├── deploy.yml
│       └── deploy-dev.yml
├── src/
│   └── main/
│       ├── java/com/automaticfactus/
│       │   ├── controller/
│       │   ├── dtos/
│       │   ├── entities/
│       │   ├── model/
│       │   ├── render/
│       │   ├── repositories/
│       │   └── service/
│       └── resources/
│           ├── application.properties
│           ├── static/
│           └── templates/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── render.yaml
└── README.md
```

## Seguridad

- Mantén `.env`, `application-secrets.yml` y otras credenciales fuera de Git.
- Usa credenciales y bases de datos independientes para desarrollo y producción.
- Restringe el acceso a los secretos del runner y del servidor de despliegue.
- Revisa periódicamente las dependencias, los permisos de los backups y la configuración de los workflows.
- No expongas directamente los puertos internos del servidor cuando la infraestructura de publicación pueda enrutar el tráfico de forma segura.

## Entornos publicados

- **Producción:** URL no publicada por seguridad de la información.
- **Desarrollo:** [automaticfactus-dev.dotacioneszambrano.com](https://automaticfactus-dev.dotacioneszambrano.com/)

## Autor

**Jhonatan Traslaviña**  
Repositorio: [jhonatan0820/AutomaticFactus](https://github.com/jhonatan0820/AutomaticFactus)
