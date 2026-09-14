# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# OpenWolf

@.wolf/OPENWOLF.md

This project uses OpenWolf for context management. Read and follow .wolf/OPENWOLF.md every session. Check .wolf/cerebrum.md before generating code. Check .wolf/anatomy.md before reading files.

## Project overview

Duration Field is a Jmix add-on (Java/Gradle) that adds a `java.time.Duration` datatype and a matching `<nm:durationField>` UI component to Jmix Flow-UI applications, rendering durations in Jira-style human-readable form (e.g. `1d 1h`, `1h 30m 15s`) instead of raw numbers.

Group: `gr.netmechanics.jmix`. Current version: `3.0.0` (see `gradle.properties`), tracking Jmix platform `3.0.x`. Past version compatibility (Jmix version → add-on version) is documented in `README.md`.

## Build commands

```bash
./gradlew clean assemble publishToMavenLocal   # build both modules and publish to local Maven repo (the pre-configured `.run` config)
./gradlew build                                 # compile + run tests + assemble
./gradlew :duration-field:test                  # run tests for the core module only
```

There is no standalone Jmix application here to run — this is a library add-on consumed by other Jmix projects via `publishToMavenLocal` or a Maven repo.

Publishing (`build.gradle`, `subprojects` block) pushes to either the Netmechanics Nexus (if `nexus_username`/`nexus_password` Gradle properties are set) or GitHub Packages (`GITHUB_USERNAME`/`GITHUB_TOKEN` env vars) — this only matters for release workflows (`.github/workflows/release.yml`), not day-to-day development.

## Module structure

Two Gradle subprojects, wired together in `settings.gradle` (build files are named `<module>.gradle`, not `build.gradle`):

- **`duration-field`** — the actual Jmix module (`io.jmix.core:jmix-core-starter`, `io.jmix.flowui:jmix-flowui-starter`). Contains the datatype, the UI component, the XML loader, and Studio metadata.
- **`duration-field-starter`** — thin Spring Boot autoconfiguration wrapper (`api project(':duration-field')`) that lets consuming apps pull in the add-on with a single dependency. Has `jmix.entitiesEnhancing.enabled = false` since it has no entities of its own.

### How the pieces connect

1. **`DurationFormatter`** (`duration-field/.../df/DurationFormatter.java`) — pure, stateless formatting/parsing logic. Converts `Duration` ↔ human-readable strings using Jira-style working-time units (1 day = 8h, 1 week = 5 days, 1 month = 4 weeks, 1 year = 12 months). Supports `shortLabels` (`1d 1h`) vs long labels (`1 day 1 hour`). This is the one class with real business logic and the most likely place to need edits/tests when changing formatting behavior.
2. **`DurationDatatype`** (`.../datatype/DurationDatatype.java`) — implements Jmix's `Datatype<Duration>` (`@DatatypeDef(id = "duration", defaultForClass = true)`, `@Ddl("bigint")`), delegating to `DurationFormatter`. Reads the `jmix.durationField.shortLabels` config property (default `true`) via `@Value`. This is what makes `Duration` a first-class Jmix attribute type usable directly from Studio.
3. **`DurationConverter`** (`.../datatype/DurationConverter.java`) — JPA `AttributeConverter<Duration, Long>` (`@Converter(autoApply = true)`), storing durations as millisecond `bigint` columns in the DB.
4. **`DurationField`** (`.../component/DurationField.java`) — the Flow-UI component, a thin `TypedTextField<Duration>` subclass.
5. **`DurationFieldLoader`** (`.../DurationFieldLoader.java`) — XML layout loader for `<nm:durationField>`, wiring up standard Flow-UI attributes (label, placeholder, required, sizing, etc.) plus the field-specific `clearButtonVisible` and `title`.
6. **`DurationFieldConfiguration`** (`.../DurationFieldConfiguration.java`) — the module's `@Configuration`, annotated `@JmixModule(dependsOn = FlowuiConfiguration.class)`, registers the component/loader pair via `ComponentRegistrationBuilder` and loads `module.properties`.
7. **`StudioComponents`** (`.../kit/StudioComponents.java`) — `@StudioUiKit` metadata (`@StudioComponent`/`@StudioProperty`) that makes `durationField` appear in Jmix Studio's component palette and property inspector, mirroring the same XML attributes the loader handles.
8. **`ui.xsd`** (`duration-field/src/main/resources/ui.xsd`) — XML schema for the `nm:` namespace (`http://schemas.netmechanics.gr/jmix/ui`) used to validate/autocomplete `<nm:durationField>` in view XML.
9. **`DurationFieldAutoConfiguration`** (in `duration-field-starter`) — Spring Boot `@AutoConfiguration` that imports `DurationFieldConfiguration`, registered via `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`. This is the actual entry point when a consuming app adds the `duration-field-starter` dependency.

When changing parsing/formatting rules, `DurationFormatter` is the single source of truth used by both display (`DurationDatatype.format`) and DB round-tripping is separate (`DurationConverter` always uses raw millis, unaffected by label formatting).

## Configuration property

- `jmix.durationField.shortLabels` (boolean, default `true`) — read by `DurationDatatype`; also exposed as a `shortLabels` XML attribute on the component itself (loader currently does not read a per-instance override for it — check `DurationFieldLoader`/`DurationField` before assuming per-field override works).
