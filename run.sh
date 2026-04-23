#!/usr/bin/env bash

set -euox pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

KAFKA_DIR="$SCRIPT_DIR/kafka"
KAFKA_BIN="$KAFKA_DIR/bin"
KAFKA_CONFIG="$KAFKA_DIR/config/server.properties"
KAFKA_CLUSTER_ID_FILE="$KAFKA_DIR/.cluster-id"

echo "STARTING KAFKA"

if [ ! -f "$KAFKA_CLUSTER_ID_FILE" ]; then
    echo "GENERATING CLUSTER ID"
    "$KAFKA_BIN/kafka-storage.sh" random-uuid > "$KAFKA_CLUSTER_ID_FILE"
fi

echo "FORMATTING KAFKA STORAGE"
"$KAFKA_BIN/kafka-storage.sh" format \
    --ignore-formatted \
    --standalone \
    -t "$(cat "$KAFKA_CLUSTER_ID_FILE")" \
    -c "$KAFKA_CONFIG"

"$KAFKA_BIN/kafka-server-start.sh" -daemon "$KAFKA_CONFIG"

sleep 4

echo "CREATING TOPICS"
"$KAFKA_BIN/kafka-topics.sh" --create --topic patients-topic \
    --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 || true

"$KAFKA_BIN/kafka-topics.sh" --create --topic appointments-topic \
    --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 || true

"$KAFKA_BIN/kafka-topics.sh" --create --topic patient-status-topic \
    --bootstrap-server localhost:9092 --partitions 2 --replication-factor 1 || true

"$KAFKA_BIN/kafka-topics.sh" --create --topic patient-status-reply-topic \
    --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1 || true

echo "STARTING CONSUMER"
(
    cd "$SCRIPT_DIR/consumer"
    mvn spring-boot:run
) &
CONSUMER_PID=$!

sleep 4

echo "STARTING PRODUCER"
(
    cd "$SCRIPT_DIR/producer"
    mvn spring-boot:run
) &
PRODUCER_PID=$!

cat <<EOF
System running:
Producer: http://localhost:8001
Consumer: http://localhost:8002
H2 DB:    http://localhost:8002/h2-console
EOF

wait $CONSUMER_PID $PRODUCER_PID
