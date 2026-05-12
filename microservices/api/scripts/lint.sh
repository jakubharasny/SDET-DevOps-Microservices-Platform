#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
service_dir="$(cd "$script_dir/.." && pwd)"

(
  cd "$service_dir"
  if ! mvn -q spotless:check; then
    echo "Spotless violations detected in api. Applying automatic formatting..."
    mvn -q spotless:apply
    echo "Formatting applied in api. Commit the updated files, then push again."
    exit 1
  fi
  mvn -q test
)

"$script_dir/format-openapi.sh"
"$script_dir/check-openapi-format.sh"
