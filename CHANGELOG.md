# Changelog

All notable changes to this repository are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- `security-core`: `Requester`, `RequesterProvider` and `PermissionPolicy`.
- `security-autoconfigure` / `security-starter`: method security and the `appCustomPermissionEvaluator`, which
  resolves `hasPermission(...)` against every `PermissionPolicy` bean, without a web dependency.
- Dummy project and reusable CI workflows.

[Unreleased]: https://github.com/positivinh/virtuoso-security/commits/main
