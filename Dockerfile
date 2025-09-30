# Stage 1: Build the application
# Use the current official name for the OpenJDK 17 image
FROM eclipse-temurin:17-jdk-jammy AS build

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml to leverage Docker cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Grant execute permissions to the mvnw script
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the project source code
COPY src ./src

# Package the application, skipping the tests
RUN ./mvnw package -DskipTests


# Stage 2: Create the final, smaller image
# Use the current official name for the JRE 17 image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the executable JAR from the build stage
COPY --from=build /app/target/journalApp-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]