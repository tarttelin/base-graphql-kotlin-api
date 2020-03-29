---
id: getting-started
title: Getting Started
---

Communities API is the backend GraphQL api for the mobile app.

## Pre-requisites

Depends on integrating with an OIDC to deliver auth credentials

## Running locally

Once checked out, run the tests with

```$bash
$ ./gradlew clean build
```

The build runs tests, checks that the coverage is above the minimum thresholds, enforces code lint and runs static
code analysis. All of these must pass for the build to succeed.

## Documentation

We are using [docusaurus](https://docusaurus.io/en/) to generate the documentation from markdown. The documentation
includes sequence diagrams. These are created inline with [mermaid-js](https://mermaid-js.github.io/mermaid/#/) to
render the markdown code block.
