# shortLabels Support for DurationField

**Date:** 2026-06-10

## Summary

Wire the existing `shortLabels` property on `DurationField` to actually affect how duration values are displayed. Currently the property is stored and loaded from XML but never passed to the formatter. The fix routes formatting through a per-field `DurationDatatype` instance configured with the correct `shortLabels` value.

## Problem

`DurationField` has a `shortLabels` field and setter, and `DurationFieldLoader` already reads it from XML — but `setShortLabels()` only stores the value. The field always displays using the Spring-registered `DurationDatatype` singleton, which always calls `DurationFormatter.format(duration)` (short labels only). The per-field setting has no effect.

## Design

### DurationDatatype

Add a `shortLabels` field and a two-constructor pattern:

- `DurationDatatype()` — no-arg, used by Spring for singleton registration, defaults `shortLabels = true`
- `DurationDatatype(boolean shortLabels)` — for per-field instances created by `DurationField`

Update both `format()` overloads to call `DurationFormatter.format(duration, shortLabels)` instead of the single-arg overload.

The Spring singleton continues to work identically (no-arg → `shortLabels=true`). Parse behaviour is unchanged — the parser already accepts both short and long labels.

### DurationField

- Add `isShortLabels()` getter.
- Update `setShortLabels(boolean)` to also call `setDatatype(new DurationDatatype(shortLabels))`, pushing a new per-field datatype instance to the underlying `TypedTextField`.

Initial state is correct by default: `TypedTextField` auto-discovers the Spring-registered `DurationDatatype` (short labels), which matches the field's default of `shortLabels = true`. No initialisation code is needed.

### StudioComponents

Add `shortLabels` as a `@StudioProperty` so Jmix Studio exposes it in the properties panel:

```java
@StudioProperty(xmlAttribute = "shortLabels", category = GENERAL,
    type = StudioPropertyType.BOOLEAN, defaultValue = "true")
```

## Files Changed

| File | Change |
|------|--------|
| `DurationDatatype.java` | Add `shortLabels` field, two constructors, update `format()` |
| `DurationField.java` | Add `isShortLabels()`, update `setShortLabels()` to call `setDatatype()` |
| `StudioComponents.java` | Add `shortLabels` `@StudioProperty` |

## Out of Scope

- No changes to `DurationFormatter` — it already supports the `boolean shortLabels` parameter.
- No changes to `DurationFieldLoader` — it already calls `resultComponent::setShortLabels`.
- No changes to `DurationConverter` — DB persistence is unaffected.
