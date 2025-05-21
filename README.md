# SnapStudy

## Alumno: Javier Rodríguez Salas
## Tutor: Micael Gallego

## [Enlace al blog](https://medium.com/@javiisalaas97)

## [Enlace a GitHub Project](https://github.com/orgs/codeurjc-students/projects/8)

## [Funcionalidades de la aplicación](https://medium.com/@javiisalaas97/funcionalidades-de-snapstudy-65b5d5421d41)

## [Aspectos principales de la aplicación web](https://medium.com/@javiisalaas97/aspectos-principales-de-snapstudy-2b9f56dfd1bd)

## [Crear credenciales para Google Drive](https://medium.com/@javiisalaas97/creaci%C3%B3n-de-credenciales-para-google-drive-api-c92acd46848c)

## Wireframe de pantallas y navegación

![Wireframe](images/Diseño.png)

## Fase 1

### Pantallas

#### Página principal
Una landing page donde puedes usar un buscador, ver los grados disponibles o acceder a información de SnapStudy.

![Landing Page](images/mainpage.png)

#### Inicio de sesión
En esta pantalla podrás acceder a tu cuenta.

![Login](images/login.png)

#### Registro
En esta pantalla podrás crear una nueva cuenta.

![Registro](images/signin.png)

#### About
En esta página encontrarás información sobre la funcionalidad y objetivo de SnapStudy, además de conocer la tecnología con la que trabaja.

![Información de SnapStudy](images/about.png)

#### Explore
Representa el mismo listado de grados incluido en la página principal.

![Listado de grados](images/explore.png)

#### Lista de asignaturas
Muestra un listado de las asignaturas de un grado.

![Listado de asignaturas](images/subjectlist.png)

#### Listado de documentos
Representa el listado de recursos disponibles para una asignatura.

![Listado de documentos](images/documentlist.png)

#### Búsqueda
Muestra todas las coincidencias encontradas a la hora de realizar una búsqueda.

![Resultados de búsqueda](images/search.png)

#### Panel principal de administrador
Listado de grados con opciones de borrado y creación de nuevos.

![Panel principal de administrador](images/adminpannel.png)

#### Listado de asignaturas (administrador)
Listado de asignaturas con opciones de borrado y creación de nuevas.

![Listado de asignaturas - panel administrador](images/subjectsadmin.png)

#### Listado de documentos (administrador)
Listado de recursos disponibles para una asignatura con opciones para añadir nuevos, previsualizar o eliminar existentes.

![Listado de documentos - panel administrador](images/documentlistadmin.png)

#### Añadir recurso (administrador)
Popup que permite subir un nuevo recurso a una asignatura.

![Añadir recurso](images/adddocument.png)

#### Añadir asignatura (administrador)
Panel que permite al administrador crear una nueva asignatura.

![Añadir asignatura](images/addsubject.png)

#### Añadir grado (administrador)
Panel que permite al administrador crear un nuevo grado.

![Añadir grado](images/adddegree.png)

#### Listado de documentos (usuario registrado)
Listado de documentos de una asignatura, donde se permite descargar y previsualizar estos.

![Listado de documentos usuario registrado](images/documentlistuser.png)

#### Perfil (usuario registrado)
Información principal del usuario, donde puedes modificar esta información o añadir una foto de perfil.

![Perfil](images/profile.png)

#### Añadir imagen de perfil (usuario registrado)
Popup que permite subir una nueva imagen de perfil.

![Subir imagen de perfil](images/popupimage.png)

### Diagrama de navegación

![Diagrama de navegación](images/diagrama.png)

### Diagrama de entidades de base de datos

![Diagrama de entidades de base de datos](images/bbdd.png)

### Diagrama de clases backend

![Diagrama de clases backend](images/backend.png)

### Diagrama de clases frontend SPA

![Diagrama de clases frontend SPA](images/frontend.png)

### Construcción de la imagen Docker

Para construir la imagen docker de SnapStudy debemos seguir los siguientes pasos:

1. Clonar el repositorio
```
https://github.com/codeurjc-students/2024-snap-study.git
```

2. Navegar hasta el directorio "docker" dentro del proyecto
```
cd 2024-snap-study/docker
```

3. Instalar docker en nuestro sistema [here](https://docs.docker.com/engine/install/)

4. Busca docker y ejecútalo

5. Haz login en tu cuenta de Docker
```
docker login
```

7. Para generar la imagen Docker, ejecuta
```
./create_image.sh
```

Este script contiene los siguientes comandos
```
# Establece el contexto de compilación para el padre y compila usando Dockerfile desde el directorio de Docker
docker build -t jrodriguezs2020/snapstudy -f docker/Dockerfile .
# Sube la imagen a DockerHub
docker push jrodriguezs2020/snapstudy
# Ejecuta la imagen usando docker-compose desde el directorio de Docker
docker-compose up
```

### Instrucciones de ejecución de la aplicación dockerizada

Una hemos seguido la [Guía para crear la infraestructura de SnapStudy](), para ejecutar la imagen Docker de SnapStudy debemos seguir los siguientes pasos:

1. Clonar el repositorio
```
https://github.com/codeurjc-students/2024-snap-study.git
```

2. Navegar hasta el directorio "docker" dentro del proyecto
```
cd 2024-snap-study/docker
```

3. Instalar docker en nuestro sistema [here](https://docs.docker.com/engine/install/)

4. Busca docker y ejecútalo

5. Ejecuta el comando
```
docker-compose up
```

6. Tenemos una versión más simplificada de la aplicación que no dispone de todos los servicios cloud que usa la aplicación, pero tiene todas las funcionalidades básicas. Esta es la [versión 1.0.0](https://github.com/codeurjc-students/2024-snap-study/releases/tag/v1.0.0) Podemos elegir entre usar el servicio de AWS S3 para el almacenamiento de los archivos o MinIO en local.
Para usar AWS S3 es necesario tener una cuenta AWS y configurar un bucket de S3. Únicamente necesitamos sustituir los campos change-me en el archivo docker-compose.yml, en este caso AWS_ACCESS_KEY_ID y AWS_SECRET_ACCESS_KEY que se corresponden con el Access Key y Secrect Key de la cuenta AWS.

Si deseamos usar MinIO, y no vamos a ejecutar la aplicación web con docker-compose, debemos tener instalado en nuestro sistema este recurso [instalación](https://min.io/docs/minio/windows/operations/installation.html). Una vez instalado abrimos una consola de comandos y debemos navegar hasta la ruta donde se encuentre el archivo minio.exe y, una vez en la ruta, debemos ejecutar el siguiente comando
```
minio.exe server D:/minio --console-address ":9001"
```
De esta forma tendremos un sistema de almacenamiento en local que simula a AWS S3.

Ten en cuenta que si decides usar MinIO con docker-compose debes ajustar las rutas a la IP del contenedor docker que se cree, además de iniciar MinIO con docker, aquí te dejo un ejemplo
```
version: "3.9"
services:
  web:  
    image: jrodriguezs2020/snapstudy
    ports:
      - "8443:8443"
    environment:
      - DB_USER=change_me
      - DB_URL=change_me
      - DB_PASSWORD=change_me
      - AWS_ACCESS_KEY_ID=change_me
      - AWS_SECRET_ACCESS_KEY=change_me
    depends_on:
      - db
      - minio
    restart: on-failure

  db:
    image: mysql:8.0.22
    restart: always
    ports:
      - "3307:3306"
    environment:
      - MYSQL_DATABASE=snapstudy
      - MYSQL_ROOT_PASSWORD=DAWWebapp09+
    volumes:
      - mysql:/var/lib/mysql

  minio:
    image: minio/minio:latest
    ports:
      - "9000:9000"
    environment:
      - MINIO_ROOT_USER=minioadmin
      - MINIO_ROOT_PASSWORD=minioadmin
    command: server /data
    volumes:
      - minio_data:/data
    restart: always

volumes:
  mysql:
  minio_data:
```

Podemos conocer la IP de nuestro contenedor docker así
```
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' docker-minio-1
```

7. Además debemos modificar el valor de la variable useMinIO de S3Service.java a true:
```
private static final boolean useMinIO = true;
```

8. Debemos cargar el archivo "credentials.json", el cual contendrá las credenciales para poder usar el cliente de Google Drive para el volcado de documentos a Drive. Para hacernos con este archivo de credenciales podemos seguir el siguente [post](https://medium.com/@javiisalaas97/creaci%C3%B3n-de-credenciales-para-google-drive-api-c92acd46848c)

9. Para que todo el proceso de notificación del volcado de documentos en Google Drive, debemos cambiar el valor de la variable de entorno APPLICATION_PASS_MAIL. Esta es una contraseña de aplicación que permite enviar los emails desde la cuenta snapstudy.drive@gmail.com. Como es lógico, deberás cambiar el email, para ello basta con añadir el nuevo email a la variable de entorno APPLICATION_MAIL. Por último, debemos modificar el valor de la variable de entorno GOOGLE_DRIVE_FOLDER. Esta varible hace referencia a la carpeta "padre/raiz" dónde queremos almacenar todas las carpetas de los usuarios.

10. Una vez finalizado el paso anterior, abre un navegador y busca [https://localhost:8443](https://localhost:8443)