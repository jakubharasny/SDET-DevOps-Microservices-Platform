#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
service_dir="$(cd "$script_dir/.." && pwd)"

(
  cd "$service_dir"
  mvn -q -DskipTests compile
  if ! mvn -q spotless:check; then
    echo "Spotless violations detected in frontend. Applying automatic formatting..."
    mvn -q spotless:apply
    echo "Formatting applied in frontend. Commit the updated files, then push again."
    exit 1
  fi
  mvn -q test
)
