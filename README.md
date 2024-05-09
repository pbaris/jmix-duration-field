[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Duration Field for Jmix

This add-on provides support for the `java.time.Duration` data type through a specific UI component, by converting the type in a human-readable format.

![](./docs/preview1.png)

![](./docs/preview2.png)

## Installation

Add to your project's `build.gradle` dependencies:

```gradle
implementation 'gr.netmechanics.jmix:duration-field-starter:2.2.0'
```
## How to use the add-on

When you define an entity attribute of `java.time.Duration` type, the framework will use the custom JPA converter and datatype provided by the add-on

```java
@Column(name = "WORK_LOG") 
private Duration workLog;
```
In an edit screen you can add the field from studio `Component Palette` window

![](./docs/palette.png)

or through code

```xml
<window xmlns="http://jmix.io/schema/ui/window"
        xmlns:nm="http://schemas.netmechanics.gr/jmix/ui"
        focusComponent="form">
    ...
    <layout spacing="true">
        <formLayout id="form">
            <nm:durationField id="workLogField" property="workLog" />
        </formLayout>
        ...
    </layout>
</window>
```

## Messages

Component has tooltip specified by default in two languages, English and Greek. 

You can modify it by giving the corresponding attributes of the field in the code, or overwriting the messages.

| Message Key                                | Attribute   |
|--------------------------------------------| ----------- |
| gr.netmechanics.jmix.durationField/tooltip | description |
