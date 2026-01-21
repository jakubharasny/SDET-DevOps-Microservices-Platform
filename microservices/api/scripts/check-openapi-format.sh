#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd "$script_dir/../../.." && pwd)"
file="$repo_root/docs/openapi/api.json"

if [[ ! -f "$file" ]]; then
  exit 0
fi

tmp="$(mktemp)"
python3 - <<PY > "$tmp"
import json
from pathlib import Path

path = Path("$repo_root") / "docs" / "openapi" / "api.json"
data = json.loads(path.read_text())
json.dump(data, __import__("sys").stdout, indent=2)
print()
PY

if ! diff -u "$file" "$tmp" >/dev/null; then
  echo "OpenAPI JSON is not formatted. Run microservices/api/scripts/format-openapi.sh"
  rm "$tmp"
  exit 1
fi

rm "$tmp"
