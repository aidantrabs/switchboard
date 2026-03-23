# architecture

switchboard follows hexagonal architecture (ports and adapters).

## layers

```
domain/          ← pure java. zero framework imports.
application/
  port/
    input/       ← interfaces: what the system can do (use cases)
    output/      ← interfaces: what the system needs (persistence, events)
  service/       ← use case implementations
adapter/
  input/rest/    ← spring controllers (primary adapters)
  output/
    persistence/ ← jpa (secondary adapters)
    messaging/   ← kafka
    cache/       ← redis
infrastructure/
  config/        ← spring config, security, kafka, redis wiring
```

## rules

1. **domain layer** has zero framework imports. no spring, no jpa, no jakarta
   annotations. pure java only.
2. **application layer** depends on domain. defines inbound ports (use case
   interfaces) and outbound ports (persistence/event interfaces). must not
   import from adapters.
3. **adapters** implement ports. input adapters (rest controllers) call inbound
   ports. output adapters (jpa, kafka, redis) implement outbound ports.
4. **adapters never depend on each other.** persistence doesn't know about
   messaging. rest doesn't know about jpa.
5. **infrastructure** wires everything together via spring configuration.

## data flow

**write path**: REST controller → inbound port → application service → domain
logic → outbound port → persistence adapter → database + event adapter → kafka

**read path (sdk)**: SDK → REST client endpoint → application service → persistence
adapter → response (cached in redis)

**real-time sync**: kafka event → sdk consumer → local cache update

## evaluation pipeline

1. flag disabled → return default variant
2. targeting rules (priority order, AND conditions, first match wins)
3. rollout percentage (murmurhash3 deterministic hash)
4. default variant

## boundary enforcement

archunit tests verify all layer boundaries at build time. if any rule is
violated, the build fails.
