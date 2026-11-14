# Dependency Upgrade Plan for Ambrose

This document lists the dependencies that are significantly behind current versions
and should be upgraded to improve security, compatibility, and maintainability.

## Current State

The `pom.xml` property files pin several dependencies to old versions that are
multiple majors behind current releases. These were appropriate when the project
was active (2012-2026) but present maintenance risk.

## Outdated Dependencies

| Property | Current Version | Recommended Version | Reason |
|---|---|---|---|
| `apache.hadoop.version` | 1.2.1 | 3.3.x | Hadoop 1.x series is end-of-life; critical security fixes and YARN support are in 2.x/3.x |
| `apache.hive.version` | 0.13.0 | 3.1.x | Hive 0.13 is end-of-life; major API changes, security fixes, and Tez support in 3.x |
| `apache.hbase.version` | 0.94.25 | 2.4.x | HBase 0.94 is end-of-life; coprocessor, replication, and security fixes in 2.x |
| `apache.pig.version` | 0.14.0 | 0.17.x | Pig 0.14 is end-of-life; better Tez integration, LLAP, and UDF improvements in 0.17 |
| `cascading.version` | 2.5.5 | 3.1.2 | Cascading 2.x is end-of-life; improved Hadoop 3.x compatibility in 3.x |
| `scala.version` | 2.10.4 | 2.12.18 | Scala 2.10 is end-of-life; language features and JVM compatibility in 2.12 |
| `fasterxml.jackson.version` | 2.4.4 | 2.15.x | Jackson 2.4 is end-of-life; better performance, streaming, and JSON features in 2.15 |
| `google.guava.version` | 18.0 | 31.1-jre | Guava 18 is end-of-life; new utilities, better JVM compatibility in 31.x |
| `twitter.hraven.version` | 0.9.16 | 0.21.0 | hRaven has significant bug fixes and HBase compatibility updates |
| `mortbay.jetty.version` | 6.1.260124 | 9.4.51 | Jetty 6.x is end-of-life; use Eclipse Jetty 9.x for Servlet 4.0 and HTTP/2 support |
| `mortbay.jetty.servlet.version` | 3.0.20100224 | 4.0.13 | Jetty Servlet 3.0 is end-of-life; Servlet 4.0 features in 4.0.x |
| `reflections.version` | 0.9.9 | 0.10.2 | Minor bug fixes and class scanning improvements |
| `thoughtworks.xstream.version` | 1.4.7 | 1.4.20 | XStream has security fixes and better JSON/Java support in 1.4.20 |
| `slf4j.version` | 1.7.5 | 2.0.9 | slf4j 1.7.5 is old; 2.x has better marker support and lambda support |

## Action Items

1. **Update pom.xml properties** to recommended versions (see above table)
2. **Run `mvn versions:display-dependency-updates`** to identify additional transitive updates
3. **Update dependabot.yml** if new package ecosystems are needed
4. **Create/Update docs/DEPENDENCY_UPGRADE_PLAN.md** with actual upgrade findings
5. **Test build** with updated dependencies and fix any compilation errors
6. **Update CI coverage rules** (JaCoCo minimum coverage) after upgrade

## Notes

- Some hadoop/hive/hbase versions have strict compatibility requirements; upgrade
  one major at a time and test thoroughly.
- The `scala.version` and `scalding.version` must match; scalding 0.12.0 is for Scala 2.10.
- Consider migrating away from deprecated components (Jetty, hRaven) to newer alternatives
  if the use case permits.
- After upgrading, re-run the full test suite and update JaCoCo coverage thresholds.