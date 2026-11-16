FROM eclipse-temurin:17-jdk-focal AS builder

ARG VERSION=0.4.3-SNAPSHOT

WORKDIR /ambrose

# Copy only necessary files for build; .dockerignore excludes unnecessary files
COPY pom.xml .
COPY ambrose-common ./ambrose-common
COPY pig ./pig
COPY hive ./hive
COPY cascading ./cascading
COPY scalding ./scalding

# Install Maven and build dependencies
RUN apt-get update && \
    apt-get install -y maven wget && \
    rm -rf /var/lib/apt/lists/*

# Build the project (skip tests for rapid iteration; run separately with `mvn -B test`)
RUN mvn -B clean package -DskipTests

# Runtime stage: minimal JDK only
FROM eclipse-temurin:17-jre-focal

ARG VERSION=0.4.3-SNAPSHOT

WORKDIR /ambrose

# Copy built artifact from builder stage
COPY --from=builder /ambrose/target/ambrose-common-0.4.3-SNAPSHOT.jar ./ambrose-common/
COPY --from=builder /ambrose/pig/target/ ./pig/
COPY --from=builder /ambrose/hive/target/ ./hive/
COPY --from=builder /ambrose/cascading/target/ ./cascading/
COPY --from=builder /ambrose/scalding/target/ ./scalding/

# Copy startup script and config
COPY bin/ ./bin/
COPY docker-compose.yml .

# Expose the demo port
EXPOSE 8080

# Default command to run the demo via the startup script
CMD ["./bin/ambrose-demo"]