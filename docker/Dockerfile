#################################################
# Base image for the Angular build container
#################################################
FROM node:20.12.1 as angular

# Define the working directory where Angular commands will be executed
WORKDIR /angular

# Copy the package.json and package-lock.json
COPY frontend/package*.json ./

# Install all necessary dependencies
RUN npm install

# Copy the Angular application source code
COPY frontend/ .

# Build the Angular application for production
RUN npm run build -- --configuration production


#################################################
# Base image for the build container
#################################################
FROM maven:3.9.6-eclipse-temurin-17 as backends

# Define the working directory where commands will be executed
WORKDIR /project

# Copy the project dependencies
COPY backend/pom.xml /project/

# Download the project dependencies
RUN mvn clean verify --fail-never

# Copy the project source code
COPY backend/src /project/src

# Copy the credentials.json and emailTemplate.html files into the mounted volume
COPY backend/credentials.json /project/config/
COPY backend/emailTemplate.html /project/email/

COPY --from=angular /angular/dist/frontend/browser/ /project/src/main/resources/static/

RUN mvn clean package -o -DskipTests=true


#COPY AND RUN ON APP CONTAINER
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /usr/src/app

COPY --from=backends /project/target/*.jar /usr/src/app

# Copy the credentials file and email template from the build image to the volume
COPY --from=backends /project/config/credentials.json /usr/src/app/config/
COPY --from=backends /project/email/emailTemplate.html /usr/src/app/email/

EXPOSE 8443

CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]