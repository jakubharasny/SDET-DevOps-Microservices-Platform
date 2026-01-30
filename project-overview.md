SDET / DevOps Microservices Platform — Project Overview

This document is the single source of truth for the architecture, goals, constraints, and development phases of this project.

Goal: build a small microservices system plus a realistic DevOps/SDET delivery platform around it:
- Docker + Compose
- GitHub Actions CI pipelines
- Kubernetes deployments via Helm
- GitOps deployment via ArgoCD
- Contract testing with PactFlow
- Mocking with WireMock
- PR preview environments

This is a portfolio-quality, interview-ready platform build. It is intentionally staged in phases so the project grows in a controlled, realistic way (no premature cloud / no random enterprise cosplay).


1) Purpose

This project proves practical competence in:
- microservices architecture and service boundaries
- CI/CD automation and quality gates
- Kubernetes packaging and deployments
- GitOps and PR preview environments
- real-world test strategy for microservices:
  - integration tests
  - WireMock stubs
  - contract testing with PactFlow (consumer/provider verification)

The project is NOT about building a large product.
The project IS about showing ability to build and automate infrastructure like a real team would.


2) Key Constraints (non-negotiable)

Architecture constraints
- Monorepo until explicitly decided otherwise
- Services must not import each other’s code directly
- Services communicate only through:
  - HTTP API calls
  - asynchronous messaging (later phase)
  - published contracts (PactFlow)
- Each service owns its:
  - Dockerfile
  - Helm chart
  - CI pipeline

Project constraints
- No cloud work by default.
- Cloud only when Phase 5 is reached OR explicitly overridden.
- Prefer minimal, realistic design over theoretical complexity.

Folder structure constraints (important)
- We will not create the full target folder structure upfront.
- We will add folders only when they become relevant in the current phase.
- This avoids clutter and keeps the repo understandable.


3) Progress Tracker (Project State)

This section defines the current project phase and completed milestones. Cursor agents must use it to decide whether a request is in-scope for the current phase. If a request belongs to a later phase, the agent must warn and request an override.

Current Phase: Phase 2 — CI Foundation (GitHub Actions + GHCR)


Phase 1 — Local Development (Docker Compose)

Goal: Local multi-service environment running reliably with basic testing.

- [x] Project scaffold created (monorepo structure)
- [x] First microservice created (frontend Spring Boot + Thymeleaf)
- [x] Frontend Dockerfile created and image builds successfully
- [x] Local run via Docker Compose (at least frontend + one additional service)
- [x] Basic health endpoint added (/actuator/health or similar)
- [x] Basic integration test runs locally (smoke test)
- [x] Developer README for local run instructions (docs/local-development.md)

Exit criteria for Phase 1:
- Docker Compose can run multiple services locally
- At least one automated test can be executed locally
- Clear documentation exists for running the system


Phase 2 — CI Foundation (GitHub Actions + GHCR)

Goal: Every service has CI build/test and produces versioned container images.

- [ ] GitHub repository created and initial push completed
- [ ] GitHub Actions pipeline created for frontend service
- [ ] CI builds and runs unit tests on PRs
- [ ] CI builds Docker image and publishes to GHCR on merge/main
- [ ] CI triggers only relevant service pipelines (path-based filtering)
- [ ] Optional: vulnerability scan or dependency check included

Exit criteria for Phase 2:
- CI is reliable and fast
- Images are published automatically
- PR gate prevents broken builds/tests from merging


Phase 3 — Local Kubernetes (kind/k3d)

Goal: Deploy to real Kubernetes locally using Helm.

- [ ] kind or k3d cluster created and documented
- [ ] Helm chart created per service
- [ ] Helm deploy works locally for all services
- [ ] Ingress configured and reachable locally
- [ ] Optional: ArgoCD installed locally
- [ ] Optional: GitOps sync works from GitHub repo to local cluster

Exit criteria for Phase 3:
- Services run on local Kubernetes
- Helm packaging works
- Ingress route works end-to-end locally


Phase 4 — Preview Environments (PR namespaces)

Goal: PR-based preview environment per pull request.

- [ ] PR pipeline deploys preview namespace (preview-pr-<id>)
- [ ] WireMock deployed/configured per preview environment
- [ ] Pact verification gate runs in preview env
- [ ] Preview URL output is posted in PR comments
- [ ] Preview namespace auto-deleted on PR close

Exit criteria for Phase 4:
- Preview environments are fully automated
- PRs produce a testable environment + verification gates
- Cleanup is automatic and reliable


Phase 5 — Optional Cloud Deployment

Goal: Live demo environment (only when needed).

- [ ] Hosting chosen (VPS or managed Kubernetes)
- [ ] Cluster set up (K3s on VPS OR managed k8s)
- [ ] ArgoCD bootstrapped in cloud cluster
- [ ] GitOps deploy works end-to-end
- [ ] Domain + HTTPS configured (optional)
- [ ] Observability stack added (optional)

Exit criteria for Phase 5:
- A working live demo exists
- Deployment is reproducible via GitOps
- No manual “ssh cowboy” steps required


4) System Overview

4.1 Services

Minimum set (early stages)
- frontend (Spring Boot + Thymeleaf)
  - simplest UI entry point
  - displays data / calls backend API

Planned services (added gradually)
- api (Spring Boot REST)
  - domain endpoints (posts/users/etc.)
  - used by frontend
- wiremock (mock downstream dependencies)
  - used heavily in preview environments
- kafka-consumer (optional later)
  - demonstrates async processing, messaging pattern

Service rules
- Each service is independently buildable and deployable.
- Each service has a clean README.
- No code sharing between services (shared libs kept minimal and justified).


4.2 Infrastructure

Local dev
- Docker Compose for running multi-service system locally
- Local MySQL database for SQL practice (optional)

CI/CD
- GitHub Actions:
  - unit + integration tests
  - build Docker images
  - publish to GHCR
  - run contract pipeline steps (PactFlow)

Kubernetes
- Local cluster via kind/k3d in Phase 3
- Deploy with Helm charts
- Ingress for routing

GitOps
- ArgoCD in Phase 3+
- Sync from Git repo to cluster

Preview environments
- Per-PR namespace creation
- Deploy PR version of services
- WireMock per preview
- Pact verification gates


5) Testing Strategy

5.1 Unit tests
- Standard unit tests per service
- Run in CI

5.2 Integration tests
- Service-level integration tests
- Compose-based integration runs locally
- CI runs integration tests on PRs

5.3 WireMock usage
Purpose:
- stub downstream dependencies
- simulate failures, timeouts, partial data
- enable stable preview environments

5.4 Contract Testing (PactFlow)
Consumer workflow:
- consumer tests generate Pact contract
- publish contract to PactFlow
- tag/version contracts (branch/PR/main)

Provider workflow:
- provider verification uses latest contracts
- verification results published back
- PR gating prevents breaking changes


6) CI/CD Workflow (target state)

6.1 Build pipeline per service
Triggered by changes in microservices/<service>/**
- compile + unit test
- build Docker image
- publish image to GHCR (main branch)
- publish contract artifacts (where relevant)

6.2 PR Preview pipeline (Phase 4)
Triggered on PR open/update:
- create namespace preview-pr-<id>
- deploy Helm charts with PR image tags
- deploy WireMock stubs
- run smoke tests
- run Pact provider verification
- comment PR with preview URL
Triggered on PR close:
- destroy namespace


7) Tech Stack

Languages:
- Java (Spring Boot)

Automation:
- GitHub Actions

Container:
- Docker, Docker Compose

Kubernetes:
- kind/k3d locally, Helm packaging
- ArgoCD for GitOps
- Ingress NGINX (or similar)

Testing:
- JUnit, REST Assured (or similar)
- WireMock
- Pact + PactFlow

Registry:
- GitHub Container Registry (GHCR)

Editor:
- Cursor (AI-assisted coding)
- Use .cursorrules for consistent behaviour + phase discipline


8) Repository Structure (incremental approach)

We will add folders when needed (phase-by-phase).
Do not create the entire target tree upfront.

Current focus (Phase 1):
- microservices/frontend
- microservices/api
- project-overview.md
- .cursorrules

Phase 1 additions (only when needed):
- deploy/compose (Docker Compose files)
- deploy/mysql (local MySQL schema + seed)
- docs/ (local development docs, e.g. docs/local-development.md)


9) Current Structure (as of now)

This reflects the reality of the repo/project today, not the ideal future state.

- microservices/
  - frontend/
  - api/
- deploy/
  - compose/
  - mysql/
- docs/
  - local-development.md
  - local-database.md
- project-overview.md
- .cursorrules


10) Latest Progress

- Initial monorepo structure created
- Frontend microservice created:
  - Spring Boot + Thymeleaf UI
  - Runs locally via mvn spring-boot:run
- API microservice created:
  - Spring Boot REST
  - /api/ping endpoint
- Countries API added (/api/countries) backed by JSON catalog
- Docker Compose runs multiple services locally
- Frontend health endpoint available at /actuator/health
- Basic smoke test added for frontend health
- Frontend renders countries table from API data
- OpenAPI schema generated during API tests (docs/openapi/api.json)
- Test layout split into unit/component/integration folders
- Playwright E2E test added (tests/e2e-java)
- Local MySQL for SQL practice (schema + seed)
- Phase 1 exit criteria met; Phase 2 started
- Frontend calls API to render EU country currencies


11) How to use this document

- This file defines architecture, scope, and phases.
- Cursor agents must treat it as source of truth.
- When plan changes:
  - update this file first
  - then implement
