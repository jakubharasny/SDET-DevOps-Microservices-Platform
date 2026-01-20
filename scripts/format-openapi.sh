#!/usr/bin/env bash
set -euo pipefail

file="docs/openapi/api.json"

if [[ ! -f "$file" ]]; then
  echo "OpenAPI file not found at $file"
  exit 1
fi

tmp="$(mktemp)"
python3 - <<'PY' > "$tmp"
import json
from pathlib import Path

path = Path("docs/openapi/api.json")
data = json.loads(path.read_text())
json.dump(data, __import__("sys").stdout, indent=2)
print()
PY

mv "$tmp" "$file"
