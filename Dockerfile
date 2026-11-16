FROM eclipse-temurin:17-jdk-focal AS builder

ARG VERSION=0.4.3-SNAPSHOT

WORKDIR /ambrose

# Copy only necessary files for build; .dockerignore excludes unnecessary files
COPY pom.xml .
COPY common ./common
COPY pig ./pig
COPY hive ./hive
COPY cascading ./cascading
COPY scalding ./scalding
COPY bin ./bin

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Build the project (skip tests for rapid iteration; run separately with `mvn -B test`)
RUN mvn -B clean package -DskipTests

# Runtime stage: minimal JRE plus python for the demo web server
FROM eclipse-temurin:17-jre-focal

ARG VERSION=0.4.3-SNAPSHOT

WORKDIR /ambrose

# Install python3 for the demo web server
RUN apt-get update && \
    apt-get install -y python3 && \
    rm -rf /var/lib/apt/lists/*

# Copy built artifacts from builder stage
COPY --from=builder /ambrose/common/target/ ./common/target/
COPY --from=builder /ambrose/pig/target/ ./pig/target/
COPY --from=builder /ambrose/hive/target/ ./hive/target/
COPY --from=builder /ambrose/cascading/target/ ./cascading/target/
COPY --from=builder /ambrose/scalding/target/ ./scalding/target/

# Copy startup script and web resources
COPY bin/ ./bin/
COPY common/src/main/resources/ ./common/src/main/resources/

# Expose the demo port
EXPOSE 8080

# Default command to run the demo via the startup script
CMD ["./bin/ambrose-demo"]