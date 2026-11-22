# Agent Rules — SDET / DevOps Microservices Platform

*(Antigravity-native mirror of `.cursorrules`; keep both aligned with `project-overview.md`.)*

## Always Do First
- Read `./project-overview.md` before proposing or making changes, and state which phase (1–5) the request belongs to.
- `./project-overview.md` is the single source of truth. If the plan changes, update it first.
- Current progress lives in the **Progress Tracker** section of `project-overview.md` (Phase 1 DONE; Phase 2 active).

## Phase Discipline
- Default path: Phase 1 (Compose) → Phase 2 (CI) → Phase 3 (local k8s kind/k3d) → Phase 4 (preview env) → Phase 5 (optional cloud).
- If a request belongs to a later phase than current progress:
  - Do not execute immediately.
  - Explain missing prerequisites and propose the compliant next step.
  - Ask for explicit override phrase: `OVERRIDE: PROCEED ANYWAY`.

## Scope Bombs (override required unless approved in overview)
- AWS/EKS/Terraform/cloud deployment
- Splitting monorepo into multiple repos
- Adding major new cross-cutting stacks (observability platform, service mesh, etc.)
- Introducing multiple new services beyond what `project-overview.md` lists

## Phase 1 Baseline (do not regress)
- Compose stack must keep `frontend` + `api` + `kafka` + `kafka-consumer` + `mysql` runnable.
- Async query statuses: `PENDING` → `PROCESSING` → `DONE` | `FAILED`.
- Verify stack-affecting changes with `./scripts/phase1-smoke.sh` and service `mvn test` (including Spotless check).
- Host MySQL on `3306` conflicts with Compose; document stop/remap (`MYSQL_HOST_PORT=3307`).

## Phase 2 Guardrails (while Phase 2 is current)
- Prefer GitHub Actions + GHCR; no cloud cluster work.
- Path-filter workflows per service under `microservices/<service>/**`.
- Keep local Compose as the developer runtime; do not replace it with k8s yet.

## Output & Communication Style
- Prefer practical, minimal solutions; do not invent architecture beyond `project-overview.md`.
- Keep communication blunt, pragmatic, concise, and actionable. No corporate filler or theory dumps.
