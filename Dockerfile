FROM eclipse-temurin:17-jdk-focal

ARG VERSION=0.4.3-SNAPSHOT

WORKDIR /ambrose

COPY pom.xml .
COPY ambrose-common ./ambrose-common
COPY pig ./pig
COPY hive ./hive
COPY cascading ./cascading
COPY scalding ./scalding

# Install Maven and build
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Build the project
RUN mvn -B clean package -DskipTests

# Expose the demo port
EXPOSE 8080

# Default command to run the demo
CMD ["mvn", "-B", "exec:java", "-pl", "common", "-Dexec.mainClass=com.twitter.ambrose.server.ScriptStatusServer"]