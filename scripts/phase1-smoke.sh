#!/usr/bin/env bash
# Phase 1 local smoke: health + sync API + async Kafka query flow.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
COMPOSE_FILE="${ROOT}/deploy/compose/docker-compose.yml"
FRONTEND_URL="${FRONTEND_URL:-http://localhost:8080}"
API_URL="${API_URL:-http://localhost:8081}"
MAX_WAIT_SECONDS="${MAX_WAIT_SECONDS:-180}"

log() { printf '%s\n' "$*"; }
die() { printf 'ERROR: %s\n' "$*" >&2; exit 1; }

wait_http() {
  local url="$1"
  local label="$2"
  local elapsed=0
  until curl -fsS "$url" >/dev/null 2>&1; do
    if (( elapsed >= MAX_WAIT_SECONDS )); then
      die "${label} not ready after ${MAX_WAIT_SECONDS}s (${url})"
    fi
    sleep 3
    elapsed=$((elapsed + 3))
  done
  log "OK  ${label}"
}

log "==> Waiting for frontend health"
wait_http "${FRONTEND_URL}/actuator/health" "frontend health"

log "==> Waiting for API ping"
wait_http "${API_URL}/api/ping" "api ping"

log "==> Checking countries"
countries="$(curl -fsS "${API_URL}/api/countries")"
echo "$countries" | jq -e 'type == "array" and length > 0 and .[0].country != null and .[0].currencyCode != null' >/dev/null \
  || die "countries payload unexpected: ${countries}"
log "OK  countries"

log "==> Creating async query"
create_payload='{"message":"phase1-smoke"}'
create_response="$(curl -fsS -X POST "${API_URL}/api/queries" \
  -H 'Content-Type: application/json' \
  -d "${create_payload}")"
query_id="$(printf '%s' "$create_response" | jq -r '.id // empty')"
[[ -n "$query_id" ]] || die "could not parse query id from: ${create_response}"
log "OK  query created id=${query_id}"

log "==> Polling query until DONE"
elapsed=0
status=""
while true; do
  details="$(curl -fsS "${API_URL}/api/queries/${query_id}")"
  status="$(printf '%s' "$details" | jq -r '.status // empty')"
  if [[ "$status" == "DONE" ]]; then
    log "OK  async Kafka flow completed"
    break
  fi
  if [[ "$status" == "FAILED" ]]; then
    die "query failed: ${details}"
  fi
  if (( elapsed >= MAX_WAIT_SECONDS )); then
    die "query did not complete after ${MAX_WAIT_SECONDS}s (last status=${status:-unknown})"
  fi
  sleep 2
  elapsed=$((elapsed + 2))
done

log "==> Phase 1 smoke passed"
log "Compose file: ${COMPOSE_FILE}"
