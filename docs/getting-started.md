---
id: getting-started
title: Getting Started
---

Phone Service is the backend GraphQL api for the mobile app.

## Pre-requisites

Depends on integrating with an OIDC to deliver auth credentials

## Running locally

Once checked out, run the tests with

```$bash
$ ./gradlew clean build
```

The build runs tests, checks that the coverage is above the minimum thresholds, enforces code lint and runs static
code analysis. All of these must pass for the build to succeed.
