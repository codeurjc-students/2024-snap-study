#################################################
# Imagen base para el contenedor de compilación de Angular
#################################################
FROM node:20.12.1 as angular

# Define el directorio de trabajo donde ejecutar comandos para Angular
WORKDIR /angular

# Copia el package.json y package-lock.json (o yarn.lock si usas yarn)
COPY frontend/package*.json ./

# Instala todas las dependencias necesarias
RUN npm install

# Copia el código fuente de la aplicación Angular
COPY frontend/ .

# Construye la aplicación Angular para producción
RUN npm run build -- --configuration production


#################################################
# Imagen base para el contenedor de compilación
#################################################
FROM maven:3.9.6-eclipse-temurin-17 as backends

# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /project

# Copia las dependencias del proyecto
COPY backend/pom.xml /project/

# Descarga las dependencias del proyecto
RUN mvn clean verify --fail-never

# Copia el código del proyecto
COPY backend/src /project/src

COPY --from=angular /angular/dist/frontend/browser/ /project/src/main/resources/static/

RUN mvn clean package -o -DskipTests=true


#COPY AND RUN ON APP CONTAINER
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /usr/src/app

COPY --from=backends /project/target/*.jar /usr/src/app

EXPOSE 8443

CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]