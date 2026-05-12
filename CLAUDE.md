# CLAUDE.md — Design Pattern Training

## Repository Purpose

This is a Java learning repository demonstrating Gang of Four (GoF) design patterns. Each pattern is implemented as a self-contained, runnable `.java` file. The `tasks/` directory contains practical exercises that require applying multiple patterns to refactor real-world-style code.

## Project Structure

```
design-pattern-training/
├── creational/          # Object creation patterns
│   ├── SingletonPattern.java
│   ├── FactoryPattern.java
│   ├── AbstractFactoryPattern.java
│   ├── BuilderPattern.java
│   ├── PrototypePattern.java
│   └── ObjectPoolPattern.java
├── structural/          # Class/object composition patterns
│   ├── AdapterPattern.java
│   ├── BridgePattern.java
│   ├── CompositePattern.java
│   ├── DecoratorPattern.java
│   ├── FacadePattern.java
│   ├── FlyweightPattern.java
│   └── ProxyPattern.java
├── behaviour/           # Object communication patterns
│   ├── StrategyPattern.java
│   ├── ObserverPattern.java
│   └── IteratorPattern.java
└── tasks/
    └── notification/
        ├── assignment_brief.md          # Exercise spec
        ├── problem/LegacyNotifier.java  # God-class to refactor
        └── solution/                    # Scaffold to implement
            ├── AlertFactory.java
            ├── NotificationService.java
            └── RefactoredNotifier.java
```

## No Build System

There is no Maven, Gradle, or any build toolchain. Compile and run with `javac`/`java` directly:

```bash
# Compile a single file (from repo root)
javac creational/SingletonPattern.java

# Compile a package (from repo root)
javac behaviour/*.java

# Run a pattern demo
java -cp . creational.SingletonPattern
java -cp . behaviour.StrategyPattern

# Compile the full task
javac tasks/notification/problem/LegacyNotifier.java
java -cp . tasks.notification.problem.LegacyNotifier
```

For structural patterns (no package declaration), compile and run from their directory or the repo root:

```bash
javac structural/AdapterPattern.java
java -cp structural AdapterPattern
```

## Package Conventions (Important Inconsistency)

| Directory     | Package declaration         |
|---------------|-----------------------------|
| `creational/` | `package creational;`       |
| `behaviour/`  | `package behaviour;`        |
| `structural/` | **none** (default package)  |
| `tasks/notification/problem/`  | `package tasks.notification.problem;` |
| `tasks/notification/solution/` | `package tasks.notification.solution;` |

When adding new structural patterns, match the existing convention (no package declaration). When adding to `creational/` or `behaviour/`, include the package declaration.

## File/Class Conventions

Each pattern file follows this structure:

- **One public outer class** named `<PatternName>Pattern` (e.g., `StrategyPattern`)
- **All pattern participants as `static` nested classes/interfaces** inside the outer class
- **A `main` method** in the outer class that demonstrates the pattern with printed output
- **No external dependencies** — pure Java SE, no third-party libraries
- **Self-verifying demos**: where behavior must be validated (e.g., the tasks), use a `private static void verify(boolean, String)` helper rather than a test framework

## Implemented Patterns

### Creational
| Pattern | Key idea | Main types used |
|---------|----------|-----------------|
| Singleton | Double-checked locking with `volatile` | `LazySingleton` |
| Factory | Registry map of `Function<Map,T>` creators; supports runtime registration | `DataSourceFactory`, `DataSource` |
| Abstract Factory | Family of products via factory interface | `UiFactory`, `WindowsFactory`, `MacFactory` |
| Builder | Fluent builder with required-field validation in `build()` | `Person.Builder` |
| Prototype | Deep clone via `Cloneable`; explicit deep copy of mutable fields | `Person`, `Address` |
| Object Pool | `BlockingQueue` pool; blocks callers when at capacity | `ConnectionPool`, `SimpleConnection` |

### Structural
| Pattern | Key idea | Main types used |
|---------|----------|-----------------|
| Adapter | Wraps an incompatible `GoogleCloudStorageLibrary` behind `FileStorage` | `GcsFileStorage` |
| Bridge | Separates payment type (abstraction) from gateway (implementation) | `Payment`, `PaymentGateway` |
| Composite | Uniform tree of files/folders via `FileSystemNode` | `FileLeaf`, `Folder` |
| Decorator | Stackable beverage add-ons via `BeverageDecorator` base class | `MilkDecorator`, `SugarDecorator` |
| Facade | Simplifies multi-subsystem home theater into two methods | `HomeTheaterFacade` |
| Flyweight | Shares intrinsic `ParticleType` state across many `Particle` objects | `ParticleFactory` |
| Proxy | Caching proxy around `RealDataFetcher` using `ConcurrentHashMap` | `CachedDataFetcher` |

### Behavioural
| Pattern | Key idea | Main types used |
|---------|----------|-----------------|
| Strategy | Swappable `DiscountStrategy` on `ShoppingCart` | `PercentageDiscount`, `FixedAmountDiscount` |
| Observer | `Button` subject notifies `ClickListener` observers; supports add/remove | `TriggerApiClickListener`, `TrackerClickListener` |
| Iterator | Custom `Iterable<Integer>` `Range` integrates with Java for-each | `Range` |

## Active Task: Notification System Refactor

`tasks/notification/` is an **incomplete exercise**. The solution scaffold exists but is not implemented:

- `solution/NotificationService.java` — `notify()` returns an empty list (stub)
- `solution/AlertFactory.java` — `createService()` returns an empty service (stub)

The goal is to refactor `problem/LegacyNotifier.java` (a god class) into a design that uses:
1. **Strategy** — `FormattingStrategy` with `TextFormatter` and `JsonFormatter`
2. **Observer** — `NotificationService` as subject; `Emailer`, `SMSSender`, `FileLogger` as subscribers
3. **Factory Method** — `AlertFactory.createService()` assembles the correct formatter + subscribers

The `verify()` assertions in `RefactoredNotifier.main()` define the required external behavior. Do not change the assertions.

## Git Workflow

- Primary development branch: `claude/add-claude-documentation-DOGn3`
- Main branch: `main`
- Push with: `git push -u origin <branch-name>`

## What to Avoid

- Do not introduce a build system unless explicitly requested.
- Do not add external library dependencies (keep it pure Java SE).
- Do not change the `verify()` assertions in any `main` method — they define the contract.
- Do not add package declarations to files in `structural/` (they have none by convention).
- Do not change the existing pattern demo logic — add new patterns in new files.
