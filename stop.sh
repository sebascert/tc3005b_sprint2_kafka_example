#!/usr/bin/env bash

set -euxo pipefail

echo "STOPPING SPRING APPS"
pkill -f ConsumerApplication || echo "no ConsumerApplication process"
pkill -f ProducerApplication || echo "no ProducerApplication process"

echo "STOPPING KAFKA"
pkill -f kafka.Kafka || echo "no kafka process"
