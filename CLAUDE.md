# CLAUDE.md
> This file is read automatically by Claude Code at the start of every session.
> It is the single source of truth for how Claude should behave in this codebase.
> Keep it honest. Keep it short. Every line should earn its place.

---
# Instructions

  Always explain reasoning before writing code. Show plan first, then implementation.

  When reviewing Java code, check for:
  - Optional misuse
  - Field injection (@Autowired on fields)


## 1) Philosophy

1. Write simple, unabstracted code. Do not introduce patterns unless absolutely necessary.
2. Explicit over implicit. Let the code read top-to-bottom like a script.
3. No premature optimization. Build the simplest thing that works.
4. Small diffs. Change only what needs to be changed.
5. Readability is king. Write code a junior engineer can read and understand immediately.

---

## 2) Project

- **Name**: vending-machine
- **Description**: REST API simulating coin insertion, product dispensing, and stock management
- **Language(s)**: Java 17
- **Framework(s)**: Spring Boot 2.7
- **Package manager**: Maven (`./mvnw`)
- **Architecture style**: Layered (Controller → Service → Repository)
- **Primary users**: Internal team / integration tests
- **Monorepo**: no

---

## 3) Non-Negotiables

If any item below is violated, the change is invalid.

1. No secrets in code, tests, logs, or docs.
2. No unvalidated external input reaches business logic.
3. No breaking API/schema changes without explicit migration notes.
4. No architectural boundary violations (no business logic in Controllers).
5. No TODO placeholders in shipped code.
6. No silent test skips.
7. No dependency additions without rationale.
8. No `@Autowired` field injection — constructor injection only.
9. No `Optional.get()` without first checking `isPresent()` or using `orElseThrow()`.

---

## 4) Commands

```bash
# Install dependencies (none needed — Maven wrapper included)

# Run tests
./mvnw test

# Compile
./mvnw compile

# Build
./mvnw package

# Run dev server
./mvnw spring-boot:run

# API docs (after server starts)
open http://localhost:8080/swagger-ui.html
```

---

## 5) Working Agreement

### Before changing code
- Read nearby code and follow local patterns.
- Identify constraints from existing tests, types, and interfaces.
- Prefer editing existing modules over creating new ones.

### While changing code
- Keep edits minimal and localized.
- Keep public interfaces stable unless explicitly requested.
- Handle failure paths as first-class behavior.

### After changing code
- Run `./mvnw test`. Fix all regressions.
- Verify error messages are actionable — they surface through `ErrorHandlingControllerAdvice`.
- Ensure commit-ready state: no debug leftovers, no dead code.

---

## 6) Code Style

### Naming
- Classes: PascalCase. Methods and variables: camelCase.
- Constants: UPPER_SNAKE_CASE.
- Booleans: prefix with `is`, `has`, `should`, `can`.
- Test methods: `given_when_then` format.

### Logging
- Use `@Slf4j` logger only. No `System.out.println`.
- Never log credentials, tokens, or sensitive payloads.

---

## 7) Architecture

### Package Structure
- `controller/`   — HTTP layer only. No business logic here.
- `service/`      — All business logic lives in *ServiceImpl classes.
- `repository/`   — Spring Data JPA repositories only.
- `model/`        — Request/response DTOs (never expose domain entities directly).
- `domain/`       — JPA entities.
- `validation/`   — Custom Bean Validation annotations.

### Dependency Direction
```
Controller → Service → Repository → Domain
```
Forbidden: Repository importing Service, Service importing Controller.

### Boundary Rules
- Domain logic cannot import framework-specific annotations beyond JPA.
- Transport layer (`controller/`) cannot contain business decisions.
- Persistence layer cannot leak JPA entities into the controller layer — use DTOs.

### Patterns to Follow
- Repository pattern for all data access.
- Constructor injection only (`@RequiredArgsConstructor` from Lombok is allowed).
- DTOs at the boundary — never pass `@Entity` objects to/from the API layer.
- Throw domain exceptions from Services; handle them in `ErrorHandlingControllerAdvice`.

### Patterns to Avoid
- No `@Autowired` on fields.
- No `Optional.get()` without guard.
- No raw `System.out.println` — use `@Slf4j`.
- No business rules in Controllers.

---

## 8) Error Handling

- Throw typed domain exceptions from Service layer (`IllegalArgumentException`, `IllegalStateException`).
- Never expose internal exception messages directly — all errors go through `ErrorHandlingControllerAdvice`.
- Return structured error responses from APIs:

```json
{
  "error": "human-readable message",
  "code": "MACHINE_CODE"
}
```

---

## 9) Testing

### Standards
- New logic requires tests. No exceptions.
- Bug fix requires a regression test.
- Test naming: `given_when_then` format (e.g., `givenSoldOutProduct_whenDispense_thenThrowsIllegalStateException`).
- Test classes under `src/test/` matching source package structure.

### What to Test by Layer

| Layer | What to test | What to mock |
|---|---|---|
| Domain / Models | Validation, state transitions | Nothing — pure logic |
| Services | Business rules, edge cases, error paths | Repositories |
| Controllers | Status codes, response shapes | Services |

### Coverage Requirements
- Cover: happy path, sold-out scenario, insufficient balance, invalid/null input.
- Framework: JUnit 5 + Mockito (via `spring-boot-starter-test`).

---

## 10) Security

- No secrets in code. Use environment variables.
- Validate all inputs using `@Valid` + Bean Validation at controller boundaries.
- Parameterize all database queries — Spring Data JPA handles this.
- Never log credentials, tokens, or PII.

---

## 11) Dependencies

Before adding a package:
1. Confirm no standard-library or existing dependency solution exists.
2. Check maintenance activity and license compatibility.
3. Record one-line rationale in PR description.

Approved defaults:
- HTTP/REST: Spring Web (already in pom.xml)
- Validation: Bean Validation via `spring-boot-starter-validation`
- ORM: Spring Data JPA
- Test framework: JUnit 5 + Mockito via `spring-boot-starter-test`

---

## 12) AI Interaction Rules

- Stop and think. Before making changes, identify exactly how data flows and what the actual problem is.
- Do not invent features. Only implement what was asked for.
- Verify. Run `./mvnw test` before handing the task back.
- Be brief. "Done, tests pass" is better than a paragraph of explanation.


