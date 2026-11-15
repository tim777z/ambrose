FROM eclipse-temurin:17-jdk-focal

ARG VERSION=0.4.3-SNAPSHOT

WORKDIR /ambrose

# Copy only the necessary modules for the demo
COPY pom.xml .
COPY ambrose-common ./ambrose-common

# Install Maven and build only the common module
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

RUN mvn -B -pl ambrose-common clean package -DskipTests

# Expose the demo port
EXPOSE 8080

# Default command to run the demo
CMD ["mvn", "-B", "exec:java", "-pl", "common", "-Dexec.mainClass=com.twitter.ambrose.server.ScriptStatusServer"]