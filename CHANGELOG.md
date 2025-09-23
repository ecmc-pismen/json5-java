# Changelog

All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## 3.0.0 - 2025-09-23

### Added

- Option: `stringifyUnixInstants`
- Option: `stringifyAscii`
- Option: `allowNaN`
- Option: `allowInfinity`
- Option: `quoteSingle`
- Option: `quoteless`
- Option: `allowBinaryLiterals`
- Option: `allowOctalLiterals`
- Option: `allowHexFloatingLiterals`
- Option: `allowLongUnicodeEscapes`
- Option: `allowTrailingData`
- Option: `parseComments`
- Option: `writeComments`
- Option: `insertFinalNewline`
- Option: `digitSeparatorStrategy`
- Option: `duplicateBehaviour`

### Changed

- `Json5Null` is no longer a singleton as it allows individual comments
- `Json5Primitive` holds any primitive value besides `Json5Null`
- `Json5Options` with more advanced builder

### Removed

- `Json5Boolean`, `Json5Hexadecimal`, `Json5Number` and `Json5String` in favor of `Json5Primitive`.

## 2.0.1 - 2025-09-03

### Changed

- Update dependencies

### Fixed

- Fix unit tests on Windows (#18) - thanks to @Zim-Inn

## 2.0.0 - 2022-02-02

### Changed

- For consistency, all methods that return a Json5 data type have been refactored to use the same naming convention

## 1.0.1 - 2022-02-22

### Changed

- Json5Parser#parse will return <kbd>null</kbd> if provided Json5Lexer does not contain any data

## 1.0.0 - 2022-02-21

### Added

- Object-oriented access to all Json5 types
- Parse json5 data by InputStream / Reader / String
- Serialize json5 data to OutputStream / Writer / String
- Json5Options with builder pattern (Json5OptionsBuilder) to configure Json5
  - options: allowInvalidSurrogates, quoteSingle, trailingComma, indentFactor
