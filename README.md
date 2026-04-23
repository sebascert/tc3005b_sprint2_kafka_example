# TC3005B Sprint2 Kafka Example

## Execution

Run backend with.

```bash
# First install kafka with ./install_kafka.sh
./run.sh
```

Stop frontend with.

```bash
./stop.sh
```

Run frontends with.

```bash
cd admin_frontend && npm run dev
cd visual_frontend && npm run dev
```

# Architecture Diagram

```mermaid
flowchart LR
    %% Frontends
    F1["Frontend 1 (Doctor System)"]
    F2["Frontend 2 (Visualization System)"]

    %% Producer
    P["Producer Backend (Spring Boot API)"]

    %% Kafka
    subgraph K["Kafka"]
        T1["patients-topic"]
        T2["appointments-topic"]
        T3["patient-status-topic"]
        T4["patient-status-reply-topic"]
    end

    %% Consumer
    C["Consumer Backend (Spring Boot)"]

    %% Connections
    F1 -->|"HTTP"| P
    F2 -->|"HTTP"| P

    P -->|"send messages"| T1
    P -->|"send messages"| T2
    P -->|"send messages"| T3
    P -->|"send messages"| T4

    T1 -->|"consume"| C
    T2 -->|"consume"| C
    T3 -->|"consume"| C
    T4 -->|"consume"| C

    C -->|"respond"| T4
    T4 -->|"respond"| P
```
