# Contributing to Ambrose

Thank you for wanting to contribute to Ambrose! This project visualizes and monitors MapReduce data workflows supporting Apache Pig, Hive, Cascading, and Scalding runtimes.

## Development Setup

### Prerequisites

- Java 1.8 (or as specified in pom.xml)
- Maven 3.x
- Node.js and npm (for front-end assets)
- Docker and Docker Compose (for running the demo offline)

### Building and Testing

1. **Clone the repository:**
   ```bash
   git clone https://github.com/twitter/ambrose.git
   cd ambrose
   ```

2. **Install dependencies and build:**
   ```bash
   mvn -B clean package
   ```

3. **Run the test suite:**
   ```bash
   mvn -B test
   ```

4. **Run lint checks:**
   ```bash
   npm run lint
   mvn checkstyle:check
   ```

5. **Start the demo with Docker:**
   ```bash
   docker compose up -d
   mvn -B verify
   ```

### Adding New Tests

- Add JUnit tests in the `src/test/java` directory following the existing patterns
- Each new test file should be paired with the code it exercises
- Use Mockito to mock external dependencies (HBase, hRaven services)
- Aim for behavior-focused tests that verify specific code paths

### Code Style

- Java: Follow checkstyle rules (see checkstyle.xml). Run `mvn checkstyle:check` before committing.
- JavaScript: Follow the ESLint configuration in `.eslintrc.json`. Run `npm run lint` before committing.
- Commit messages should be clear and descriptive. Reference related issues when applicable.

### Reporting Issues

- Use the [GitHub Issues](https://github.com/twitter/ambrose/issues) tracker
- Include version information, steps to reproduce, and relevant logs
- Check existing issues before creating a new one

### Submitting Pull Requests

1. Fork the repository and create a branch from `main` or `master`
2. Ensure all tests pass (`mvn -B test` and `npm run lint`)
3. Update DEPENDENCIES.md if adding/bumping dependencies
4. Fill out the PR template (once added)
5. Address review feedback and re-run checks

## Repository Structure

- `common/` - Core services, models, and utilities used by all runtimes
- `pig/` - Pig runtime integration
- `hive/` - Hive runtime integration
- `cascading/` - Cascading runtime integration
- `scalding/` - Scalding runtime integration
- `common/src/main/resources/com/twitter/ambrose/server/web/js/` - Front-end client code

## Versioning

Releases follow semantic versioning: `<major>.<minor>.<patch>`
- Breaking backwards compatibility bumps the major
- New additions without breaking backwards compatibility bumps the minor
- Bug fixes and misc changes bump the patch