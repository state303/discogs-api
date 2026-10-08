# Discogs API prototype

This repository is an early Java/Gradle project skeleton. It does not contain
an implemented API. For new deployments, use
[Go OpenDiscogs API](https://github.com/dsub-io/go-open-discogs-api) and
[Go OpenDiscogs Batch](https://github.com/dsub-io/go-open-discogs-batch).

The canonical PostgreSQL schema is maintained in
[open-discogs-model](https://github.com/dsub-io/open-discogs-model).
The separate [Java OpenDiscogs API](https://github.com/dsub-io/open-discogs-api)
is deprecated.

## Development

The existing Gradle project uses JDK 17. Build it with the checked-in wrapper:

```sh
./gradlew check assemble --no-daemon
```

See [CI and contributions](docs/ci.md) for PR checks and release behavior.
