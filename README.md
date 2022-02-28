# Reflections Gradle Plugin

This plugin uses [Reflections](https://github.com/ronmamo/reflections) to scan and index your project classes at compile
time to allow the usage at runtime without having a performance hit at startup and mitigate issues with Reflections
missing indices due to class loaders etc.

This plugin is modeled after and should be compatible
with [Gradle Reflections Plugin](https://github.com/manosbatsis/gradle-reflections-plugin). Part of the code setup as
well as this documentation is taken directly from the aforementioned. Please, visit that plugin for a legacy version and
note where credit is due.

<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Usage](#usage)
	- [Gradle Setup](#gradle)
	- [Java Setup](#java)

<!-- /TOC -->

## Usage

The idea is to use the plugin with Gradle to embed a pre-scanned metadata index in your jar, then utilise the embedded
index at runtime using Reflections.collect(). See the Gradle and Java sections below  
for an example.

### Gradle

The plugin is published
in [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.github.andavin.reflections.gradle.plugin), so it is
rather easy to use. This will embed the pre-scanned metadata into your jar as
`META-INF/reflections/projectName-reflections.xml`, with *projectName* substituted by your actual project name.

```gradle
// import the reflections plugin
plugins {
	id 'io.github.andavin.reflections.gradle.plugin' version '1.0'
}

// reflections plugin needs the compiled
// project classes, so either chain tasks
// with dependsOn as bellow or execute tasks explicitly
// when using the command line
reflections {
    dependsOn classes
}

jar {
    dependsOn reflections
}

// Add Reflections and dom4j dependencies
dependencies {
	implementation 'org.reflections:reflections:0.10.2'
	implementation 'dom4j:dom4j:2.1.3'
}
```

### Java

To utilise the pre-scanned metadata simply create a Reflections instance as:

```java
class ReflectionsMeta {
	// Collect and merge pre-scanned Reflection xml resources
	// and merge them into a Reflections instance
	Reflections reflections = Reflections.collect();
	// Or manually locate META-INF/reflections resources and
	// read them via the XMLSerializer
	Reflections reflections = new XMLSerializer().read(inputStream);
}
```
