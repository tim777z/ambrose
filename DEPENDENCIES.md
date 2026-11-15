# Dependency Versions - Ambrose

This document records the current dependency versions in pom.xml and their latest available releases.

## Current vs Latest Dependency Versions

| Dependency | Current Version | Latest Version | Notes |
|---|---|---|---|
| `apache.hadoop.version` | 1.2.1 | latest: 3.3.4 | Hadoop 1.x series end-of-life; consider 3.x.x for new projects |
| `apache.hbase.version` | 0.94.25 | latest: 3.0.0 | HBase 0.94 series is ancient; 3.0.0 is a major rewrite |
| `apache.hive.version` | 0.13.0 | latest: 4.0.0 | Hive 0.13 is from 2015; 4.x supports Spark |
| `apache.pig.version` | 0.14.0 | latest: 0.18.0 | Pig 0.14 is from 2015 |
| `cascading.version` | 2.5.5 | latest: 3.1.0 | Cascading 2.x series end-of-life |
| `fasterxml.jackson.version` | 2.4.4 | latest: 2.23.0 | Jackson 2.4.4 from 2015 |
| `google.guava.version` | 18.0 | latest: 33.8.0-jre | Guava 18 from 2015 |
| `junit.version` | 4.12 | latest: 4.13.2 | Minor patch update |
| `mortbay.jetty.servlet.version` | 3.0.20100224 | N/A | Jetty 9.x/11.x recommended |
| `mortbay.jetty.version` | 6.1.26 | N/A | Jetty 9.x/11.x recommended |
| `reflections.version` | 0.9.9 | latest: 0.10.2 | Minor version update |
| `scala.version` | 2.10.4 | latest: 2.13.18 | Scala 2.10 reached EOL 2019 |
| `scalding.version` | 0.12.0 | latest: 0.16.0 | Scalding 0.12 from 2015 |
| `slf4j.version` | 1.7.5 | latest: 2.0.20 | SLF4J 1.7 LTS still supported |
| `thoughtworks.xstream.version` | 1.4.7 | latest: 1.4.21 | Minor version update |
| `twitter.hraven.version` | 0.9.16 | latest: 1.0.1 | hRaven core library |

## Recommended Incremental Update Path

For a safer incremental update, consider these staged bumps (each paired with `mvn -B verify`):

1. **Jackson**: 2.4.4 → 2.23.0 (widely used, minimal breaking changes)
2. **Guava**: 18.0 → 33.8.0-jre (API-compatible for most usage)
3. **JUnit**: 4.12 → 4.13.2 (bugfix release)
4. **Reflections**: 0.9.9 → 0.10.2 (minor)
5. **SLF4J**: 1.7.5 → 2.0.20 (LTS, careful API diffs)
6. **HBase**: 0.94.25 → 3.0.0 (major - requires code changes, not recommended without migration plan)
7. **Hive/Pig/Scala**: Major version bumps require significant refactoring

## Dependency Audit

Add the following to `.github/workflows/ci.yml` to run OWASP Dependency-Check:

```yaml
- run: mvn org.owasp:dependency-check-maven:check
```

This will flag any critical CVEs in the project's dependency tree.

## Lockfile Management

This project currently has no lockfiles (Maven `pom.xml` acts as its own lockfile). To establish reproducible builds:

- Commit the `pom.xml` as the dependency lockfile
- Consider adding `maven-enforcer-plugin` with `ban-dependencies` rule to prohibit transitive dependency violations
- Enable Maven's `--strict-checksums` and `--strict-signoff` flags for publish builds