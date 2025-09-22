# json5-java 
[![Build](https://img.shields.io/github/actions/workflow/status/marhali/json5-java/ci.yml?branch=main&style=for-the-badge)](https://github.com/marhali/json5-java/actions)
[![Release](https://img.shields.io/github/v/release/marhali/json5-java?style=for-the-badge)](https://github.com/marhali/json5-java/releases)
[![JavaDoc](https://javadoc.io/badge2/de.marhali/json5-java/javadoc.svg?style=for-the-badge)](https://javadoc.io/doc/de.marhali/json5-java)
[![Coverage](https://img.shields.io/codecov/c/github/marhali/json5-java?style=for-the-badge)](https://codecov.io/gh/marhali/json5-java)
[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg?style=for-the-badge)](https://paypal.me/marhalide)

This is a reference implementation of the [JSON5 standard](https://json5.org/) in Java 11+, 
capable of parsing and serialization of JSON5 data.

This library is an enhanced version of [Synt4xErr0r4 / json5](https://github.com/Synt4xErr0r4/json5), 
which provides a better full-fledged API inspired by Google's [Gson](https://github.com/google/gson) library.

## Features

- Fully supports JSON5 according to the [specification](https://spec.json5.org/)
- Extensive API for interacting with elements, inspired by Google's [Gson](https://github.com/google/gson) library
- Supports comment parsing and writing (if they can be associated with an [Json5Element](src/main/java/de/marhali/json5/Json5Element.java))
- Fine-grained [configuration options](#configuration-options)
- No runtime dependencies – ensures a clean supply chain

## Installation

Download the [latest release](https://github.com/marhali/json5-java/releases/latest) manually or add it as a Maven dependency.
Don't worry the project is already in the [Maven Central Repository](https://central.sonatype.com/artifact/de.marhali/json5-java). See the configuration below for your favorite build system.

### Add via Maven

```xml
<dependencies>
    <dependency>
        <groupId>de.marhali</groupId>
        <artifactId>json5-java</artifactId>
        <version>3.0.0</version>
    </dependency>
</dependencies>
```

### Add via Gradle

```kotlin
repositories {
  mavenCentral()
}

dependencies {
  implementation("de.marhali:json5-java:3.0.0")
}
```

## Usage

This library can be used by either configuring a [Json5](src/main/java/de/marhali/json5/Json5.java) 
instance or by using the underlying [Json5Parser](src/main/java/de/marhali/json5/stream/Json5Parser.java) 
and [Json5Writer](src/main/java/de/marhali/json5/stream/Json5Writer.java).

The following section describes how to use this library with the 
[Json5](src/main/java/de/marhali/json5/Json5.java) core class.

### Configure Json5 instance

See [Configuration Options](#configuration-options) for a full overview of possible options.

```java
import de.marhali.json5.config.Json5Options;
import de.marhali.json5.Json5;

// Create Json5 instance using builder pattern to configure desired options
Json5 json5 = Json5.builder(builder -> builder
    .quoteless()
    .quoteSingle()
    .parseComments()
    .writeComments()
    .prettyPrinting()
    .build()
);

// Using configuration object
Json5Options options = Json5Options.builder()
    // ...
    .build();
Json5 json5 = new Json5(options);
```

### Parsing

During parsing, a JSON5 file or string is converted into the corresponding [Json5Element's](src/main/java/de/marhali/json5/Json5Element.java).

```java
import de.marhali.json5.Json5;
import de.marhali.json5.Json5Element;

Json5 json5 = ...

// Parse from a String
Json5Element element = 
        json5.parse("{ 'key': 'value', 'array': ['first val','second val'] }");

// ...

// Parse from a Reader or InputStream
try (InputStream stream = ...) {
    Json5Element element = json5.parse(stream);
    // ...
} catch (IOException e) {
    // ...
}
```

### Serialization

During serialization, [Json5Element's](src/main/java/de/marhali/json5/Json5Element.java) are converted to their string representation so that they can be written to a file, for example.

```java
import de.marhali.json5.Json5;
import de.marhali.json5.Json5Element;

Json5Element element = ...

// Serialize to a String literal
String jsonString = json5.serialize(element);

// ...

// Serialize to a Writer or OutputStream        
try (OutputStream stream = ...) {
    json5.serialize(element, stream);
    // ...
} catch (IOException e) {
    // ...
}
```

## Documentation
Detailed javadoc documentation can be found at [javadoc.io](https://javadoc.io/doc/de.marhali/json5-java).

### API

This library provides a few core classes to interact with JSON5 elements.

- [Json5](src/main/java/de/marhali/json5/Json5.java): Core class for parsing and serialization
- [Json5Options](src/main/java/de/marhali/json5/config/Json5Options.java): Library configuration and options builder
- [Json5Element](src/main/java/de/marhali/json5/Json5Element.java): Root class for every JSON5 element
- [Json5Null](src/main/java/de/marhali/json5/Json5Null.java): Class representing the JSON5 `null` value
- [Json5Object](src/main/java/de/marhali/json5/Json5Object.java): Represents a JSON5 object
- [Json5Array](src/main/java/de/marhali/json5/Json5Array.java): Represents a JSON5 array
- [Json5Primitive](src/main/java/de/marhali/json5/Json5Primitive.java): Holds any primitive value (`Boolean`, `Number` or `String`)

> For a better understanding of how to use the API, take a look at the [unit tests](src/test/java/de/marhali/json5).

### Configuration Options
This library supports a few customizations to adjust the behaviour of parsing and serialization.
For a detailed explanation see the [Json5Options](src/main/java/de/marhali/json5/config/Json5Options.java) class.

- stringifyUnixInstants
- stringifyAscii
- allowNaN
- allowInfinity
- allowInvalidSurrogates
- quoteSingle
- quoteless
- allowBinaryLiterals
- allowOctalLiterals
- allowHexFloatingLiterals
- allowLongUnicodeEscapes
- allowTrailingData
- parseComments
- writeComments
- trailingComma
- insertFinalNewline
- digitSeparatorStrategy
- duplicateBehaviour
- indentFactor

> To get started using this library, `Json5Options.DEFAULT` may be a good starting point, as these are the recommended options.

## License
This library is released under the [Apache 2.0 license](LICENSE).

Partial parts of the project are based on [Gson](https://github.com/google/gson) and [Synt4xErr0r4 / json5](https://github.com/Synt4xErr0r4/json5). The affected classes contain the respective license notice. 

## Contact

Marcel Haßlinger - [@marhali_de](https://twitter.com/marhali_de) - [Portfolio Website](https://marhali.de)

Project Link: [https://github.com/marhali/json5-java](https://github.com/marhali/json5-java)

## Donation

If this library helps you to reduce development time, you can give me a [cup of coffee](https://paypal.me/marhalide) :)
