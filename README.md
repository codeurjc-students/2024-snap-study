# SnapStudy

## Alumno: Javier Rodríguez Salas
## Tutor: Micael Gallego

## [Enlace al blog](https://medium.com/@javiisalaas97)

## [Enlace a GitHub Project](https://github.com/orgs/codeurjc-students/projects/8)

## Funcionalidades de la aplicación

### Funcionalidades básicas

- **Registrarse**: un usuario anónimo debe poder crear una cuenta.
- **Iniciar sesión**: un usuario debe poder acceder a su cuenta introduciendo sus credenciales.
- **Buscar un grado y asignatura**: un usuario registrado debe poder ver los grados universitarios disponibles, así como las asignaturas de cada grado.
- **Seleccionar asignaturas de las que se quieren obtener los apuntes**: un usuario registrado debe poder seleccionar las asignaturas de las que desea obtener apuntes.
- **Borrar grado universitario**: un usuario con rol de administrador debe poder borrar un grado universitario.
- **Borrar asignatura**: un administrador debe poder borrar asignaturas de un grado universitario.
- **Dar de alta un grado**: un usuario con rol de administrador debe poder dar de alta un grado universitario.
- **Dar de alta una asignatura**: un usuario administrador debe poder crear una nueva asignatura en un grado universitario.
- **Editar perfil**: un usuario registrado debe poder editar la información de su perfil.
- **Subir imagen de perfil**: un usuario registrado debe poder subir una imagen para editar su imagen de perfil.
- **Editar grado universitario**: un usuario administrador debe poder editar la información de un grado universitario.
- **Subir un documento**: un usuario administrador debe poder subir un nuevo documento a una asignatura.
- **Descargar documento**: un usuario registrado debe poder descargar un documento.

### Funcionalidades avanzadas

- **Volcar los apuntes en la carpeta de Drive estructurados en subcarpetas**: un usuario registrado debe tener la opción para volcar los documentos de una asignatura seleccionada en una carpeta de Google Drive.
- **Almacenar documentos en AWS S3**: los documentos disponibles deben almacenarse en el servicio de AWS S3.
- **Búsqueda avanzadda**: un usuario debe poder buscar un documento mediante su nombre o su contenido.
- **Resúmenes de documentos**: al subir un documento a la plataforma se generará un resumen que estará disponible para el usuario.


## Aspectos principales de la aplicación web

### Entidades

- **Usuarios**
- **Grados universitarios**
- **Asignaturas**
- **Apuntes de asignatura**

Relación: un grado tiene múltiples asignaturas y una asignatura tiene apuntes. Los usuarios pueden obtener estos apuntes de asignaturas.

### Permisos de usuarios (qué puede hacer cada uno)

- **Usuario anónimo (no registrado)**: puede visualizar el contenido informativo de la web como, información de contacto, listado de grados universitarios y, además, puede registrarse como nuevo usuario.
- **Usuario registrado**: este tipo de usuario debe acceder al inicio de sesión con sus credenciales correspondientes para tener acceso a la principal funcionalidad de la aplicación web: puede obtener apuntes de las asignaturas que desee. Además, puede modificar sus datos personales como nombre de usuario, foto de perfil y cambio de contraseña.
- **Usuario administrador**: Tiene control total sobre la información de la web. Puede añadir nuevos grados universitarios, añadir nuevas asignaturas a grados ya existentes y subir nuevos apuntes a una asignatura.

### Imágenes

Los usuarios registrados podrán subir imágenes para modificar su foto de perfil.

### Gráficos

En el panel de administrador habrá un gráfico de barras donde se mostrarán los grados universitarios a los que más recurren los usuarios.

### Tecnología complementaria

Uso de la API de Google Drive para realizar el volcado de documentos pedidos por el usuario. Se creará una carpeta donde se añadirán subcarpetas, una por cada grado y dentro de cada grado, una subcarpeta por cada asignatura solicitada.
Almacenamiento de documentos en AWS Simple Storage Service (S3).
Uso de un indexador (Lucene/ElasticSearch) para realizar búsquedas avanzadas.
Integración de una IA para realizar resúmenes del contenido de un documento.

### Algoritmo o consulta avanzada

Buscador avanzado que permita buscar documentos mediante su nombre y contenido.

## Wireframe de pantallas y navegación

![Wireframe](images/Diseño.png)