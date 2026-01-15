# Use Java 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy pom.xml and source
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Expose Render port
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/*.jar"]

