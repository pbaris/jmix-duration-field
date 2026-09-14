[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Duration Field for Jmix

This add-on provides support for the `java.time.Duration` datatype and a related field, by converting the datatype in a human-readable format.

![](./docs/preview.png)

## Installation

The following table shows which version of the add-on is compatible with which version of the platform:

| Jmix Version | Add-on Version | Implementation                                    |
|--------------|----------------|---------------------------------------------------|
| 1.2.1        | 1.0.0          | gr.netmechanics.jmix:duration-field-starter:1.0.0 |
| 1.2.2        | 1.2.0          | gr.netmechanics.jmix:duration-field-starter:1.2.0 |
| 1.3.0        | 1.3.1          | gr.netmechanics.jmix:duration-field-starter:1.3.1 |
| 1.4.0        | 1.4.0          | gr.netmechanics.jmix:duration-field-starter:1.4.0 |
| 1.5.0        | 1.5.0          | gr.netmechanics.jmix:duration-field-starter:1.5.0 |
| 2.4.x        | 2.0.0          | gr.netmechanics.jmix:duration-field-starter:2.0.0 |
| 2.5.x        | 2.1.0          | gr.netmechanics.jmix:duration-field-starter:2.1.0 |
| 2.6.x        | 2.2.0          | gr.netmechanics.jmix:duration-field-starter:2.2.0 |
| 2.7.x        | 2.3.0          | gr.netmechanics.jmix:duration-field-starter:2.3.0 |
| 2.8.x        | 2.4.0          | gr.netmechanics.jmix:duration-field-starter:2.4.0 |
| 3.0.x        | 3.0.0          | gr.netmechanics.jmix:duration-field-starter:3.0.0 |

For manual installation, add the following dependencies to your `build.gradle`:

```gradle
implementation 'gr.netmechanics.jmix:duration-field-starter:<addon-version>'
```
## How to use the add-on

### Datatype

You can define an entity attribute with the `java.time.Duration` datatype using Studio.

![](./docs/studio1.png)

As a result, Studio generates the following attribute definition:

```java
@Column(name = "WORK_LOG") 
private Duration workLog;
```

### Field

In a detail view you can add the field from studio `Component Palette` window

![](./docs/studio2.png)

or through code

```xml
<view xmlns="http://jmix.io/schema/flowui/view"
        xmlns:nm="http://schemas.netmechanics.gr/jmix/ui"
        focusComponent="form">
    ...
    <layout>
        <formLayout id="form">
            <nm:durationField id="workLogField" property="workLog" />
        </formLayout>
        ...
    </layout>
</view>
```
### Configuration

The add-on provides the following configuration properties:

| Property                             | Default | Description                                                                                                                               |
|---------------------------------------|---------|---------------------------------------------------------------------------------------------------------------------------------------------|
| `jmix.durationField.shortLabels`     | `true`  | Whether to use short labels for the duration field                                                                                        |
| `jmix.durationField.hoursADay`       | `8`     | Working hours in a day (e.g. `7.5`), used to convert between hours and days/weeks/months/years                                           |
| `jmix.durationField.alwaysDisplayIn` | (none)  | When set (e.g. `days`, `hours`, `weeks`), the field always displays the whole duration as a single decimal number in that unit (e.g. `3.75d`) instead of a multi-part breakdown |

Set them in your app's `application.properties` (or `.yml`):

```properties
jmix.durationField.shortLabels=true
jmix.durationField.hoursADay=7.5
jmix.durationField.alwaysDisplayIn=days
```

#### `hoursADay` — customer-specific working day length

By default, 1 day = 8 working hours (and 1 week = 5 days, 1 month = 4 weeks, 1 year = 12 months, all cascading from it). If a customer works 7.5-hour days instead, set `hoursADay` accordingly — it affects both how durations are **displayed** and how typed values are **parsed**:

```properties
jmix.durationField.hoursADay=7.5
```

```java
// with hoursADay = 7.5
DurationFormatter.parse("2d", 7.5);          // -> Duration.ofHours(15)
DurationFormatter.format(Duration.ofHours(30), true, 7.5);  // -> "4d"
```

Users can still type any mix of units (`2w`, `16h`, `1mo`) — only the day/week/month/year *conversion ratio* changes, not what's accepted as input.

#### `alwaysDisplayIn` — always show a single unit

By default, durations display as a multi-part breakdown, e.g. `Duration.ofHours(25)` → `"3d 1h"`. For reporting/timesheet screens where you always want one consistent unit (e.g. days, so 30 hours of planned work reads as "3.75 days" instead of "3d 6h"), set:

```properties
jmix.durationField.alwaysDisplayIn=days
```

With that set, the field still lets users **type** `2 weeks` or `16 hours`, but always **displays** the value as a single decimal number in the configured unit:

| User enters | Displays as (`hoursADay=8`) | Displays as (`hoursADay=7.5`) |
|-------------|------------------------------|--------------------------------|
| `16h`       | `2d`                         | `2.13d`                        |
| `2w`        | `10d`                        | `10d`                          |
| `30h`       | `3.75d`                      | `4d`                           |

Accepted values are the same unit aliases used for parsing (`ms`, `s`, `m`, `h`, `d`, `w`, `mo`, `y`, or their long forms `days`, `hours`, etc.), case-insensitive. Numbers are rounded half-up to 2 decimal places, with trailing zeros stripped (`4.00d` → `4d`).

Leave it unset (the default) to keep the original multi-part breakdown behavior.
