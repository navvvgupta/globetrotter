# Use Java 21 (Temurin/AdoptOpenJDK)
FROM eclipse-temurin:21-jdk AS build

# Set working directory
WORKDIR /app

# Copy all files to the image
COPY . .

# Make Maven Wrapper executable and build the project
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# --- Runtime image ---
FROM eclipse-temurin:21-jdk

# Set working directory in runtime container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
