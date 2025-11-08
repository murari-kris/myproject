# Step 1: Use Maven to build your app
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Use lightweight Java runtime to run the app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Use the port your app listens on (Render uses 8080 by default)
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
