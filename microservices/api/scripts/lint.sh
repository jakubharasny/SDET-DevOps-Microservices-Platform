#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
service_dir="$(cd "$script_dir/.." && pwd)"

(
  cd "$service_dir"
  mvn -q spotless:check
  mvn -q test
)

"$script_dir/format-openapi.sh"
"$script_dir/check-openapi-format.sh"
