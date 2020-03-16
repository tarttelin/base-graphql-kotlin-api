---
id: architecture
title: Architecture
---

How the service hangs together

```mermaid
sequenceDiagram
    participant Mobile
    participant Cloudfront
    participant ALB
    Mobile->>Service: Request customer info
    Note right of Service: Call backends to get<br/> various detailed info
    Service-->>Mobile: List of numbers on account
```
