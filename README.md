# ✅ TodoIst

A terminal-based client-server task manager inspired by Todoist. Clients send plain-text commands over a socket connection; the server parses, validates and executes them against an in-memory, disk-persisted data store, and returns a human-readable response.

![Java](https://img.shields.io/badge/Java-23%2B-blue?logo=openjdk)
![Sockets](https://img.shields.io/badge/Networking-TCP%20Sockets-green)
![Concurrency](https://img.shields.io/badge/Concurrency-Virtual%20Threads-orange)
![Testing](https://img.shields.io/badge/Testing-JUnit%205-purple)

---

## Features

- **Accounts** — `register` and `login`, with credentials persisted on the server
- **Tasks** — create, update, delete, fetch and list tasks, each with an optional date, due date and description
- **Inbox** — tasks added without a date live in the user's Inbox; within the Inbox or on a given date, task names are unique
- **Dashboard** — `list-dashboard` shows everything scheduled for today
- **Collaborations** — shared projects with their own task lists; the creator can add participants and assign tasks to them
- **Concurrent clients** — the server handles every connection on its own virtual thread (`Executors.newVirtualThreadPerTaskExecutor()`)
- **Persistence** — users and collaborations are serialized to disk on shutdown and reloaded on the next startup
- **Friendly errors** — invalid input and business-rule violations are reported to the user as plain messages
- **`help`** — every command group exposes a `help` (or `help <command>`) command that prints its syntax and a short explanation

---

## Architecture

```
┌─────────────────────────────┐
│          Client             │
│                             │
│   Console I/O ──► Socket    │
└───────────┬─────────────────┘
            │    line-based, plain-text protocol
            │
┌───────────┼─────────────────────────────────────────┐
│           │          Server                         │
│           ▼                                         │
│  TodoIstServer ──► ClientHandler (one per client)   │
│                          │                          │
│              logged out  │  logged in               │
│                  ▼                  ▼               │
│ AccountCommandFactory         CommandFactory        │
│                                      │              │
│                    ┌─────────────────┼──────────────┐
│                    ▼                                ▼
│          TaskCommandFactory              CollaborationCommandFactory
│                    │                                │
│                    ▼                                ▼
│       Task & TaskCluster    -- model layer --   Collaboration
│                    │                                │
│                    └───────────────┬────────────────┘
│                                    ▼                │ 
│                       InMemoryTodoIstRepository     │
│                    (serialized to repository.dat)   │
└─────────────────────────────────────────────────────┘
```

Each connected client is handled on its own virtual thread. Before login, input is routed through `AccountCommandFactory` (`register` / `login` / `help`); after login, it goes through `CommandFactory`, which dispatches to either the task commands (acting on the user's own `TaskCluster`, or a collaboration's, when a `collaboration=<name>` parameter is present) or the collaboration commands.

---

## Data Model

A **task** has:

| Attribute | Required | Notes |
|---|---|---|
| `name` | yes | Together with `date`, identifies the task |
| `date` | no | Tasks without a date go to the **Inbox** |
| `dueDate` | no | |
| `description` | no | |
| `assignee` | no | Only meaningful for tasks inside a collaboration |

A task name must be unique within the Inbox, and unique for any given date — but the same name can exist once in the Inbox *and* once on a date at the same time, since they're tracked separately.

A **collaboration** is a shared project: it has its own task cluster, a creator, and a set of participants. Only the creator can delete it or add new participants; deleting a collaboration deletes all of its tasks.

---

## Tech Stack

- **Java 23+** — the project is configured to compile with preview features enabled (constructors that run validation statements before calling `super(...)`)
- **`java.net.Socket` / `ServerSocket`** — plain TCP networking, no external framework
- **Virtual threads** — one per connected client
- **Java serialization** (`ObjectInputStream` / `ObjectOutputStream`) — persistence of users and collaborations to `repository.dat`
- **JUnit 5** — unit tests for commands, models and the repository

---

## Getting Started

### Prerequisites

- JDK 23 or newer

### Build

The project doesn't use Maven/Gradle — compile it directly with `javac`. Because it relies on a Java preview feature, compilation and execution both need `--enable-preview`:

```bash
javac --enable-preview --release 23 -d out $(find src -name "*.java")
```

### First-time setup

`TodoIstServer` expects `repository.dat` to already exist the first time it starts (it loads it eagerly in its constructor). Create an empty one before the first run:

```bash
java --enable-preview -cp out Main
```

This writes an empty `repository.dat` to the working directory. You only need to do this once — after that, the server persists and reloads its own data on every start/stop.

### Running the server

```bash
java --enable-preview -cp out bg.sofia.uni.fmi.mjt.project.todoist.server.ServerRunner
```

The server listens on port `7777`. At the prompt:

| Command | Effect |
|---|---|
| `start` | Start accepting client connections |
| `stop` | Disconnect all clients and save `repository.dat` |
| `quit` | Exit the runner |

### Running a client

```bash
java --enable-preview -cp out bg.sofia.uni.fmi.mjt.project.todoist.client.TodoIstClient
```

| Command | Effect |
|---|---|
| `connect` | Connect to the server at `localhost:7777` |
| `disconnect` | Close the current connection |
| `quit` | Exit the client |

---

## Usage

Dates use the `dd/MM/yyyy` format. Parameters shown as `|param=value|` are optional. Type `help` for a list of commands, or `help <command>` for a specific command's syntax.

### Before login

```
register <username> <password>
login <username> <password>
help
```

### Tasks

```
add-task name=<name> |date=<date>| |dueDate=<due date>| |description=<description>|
update-task name=<name> |date=<date>| |newDate=<new date>| |newDueDate=<new due date>| |newDescription=<new description>|
delete-task name=<name> |date=<date>|
get-task name=<name> |date=<date>|
list-tasks |date=<date>|
list-dashboard
finish-task name=<name> |date=<date>|
```

### Collaborations

```
add-collaboration collaboration=<name>
delete-collaboration collaboration=<name>
list-collaborations
add-user collaboration=<name> user=<username>
list-users collaboration=<name>
assign-task collaboration=<name> user=<username> task=<name>
list-tasks collaboration=<name>
```

---

## Project Structure

```
src/
├── Main.java                          # one-off: bootstraps an empty repository.dat
└── bg/sofia/uni/fmi/mjt/project/todoist/
    ├── client/
    │   └── TodoIstClient.java         # console client entry point
    └── server/
        ├── ServerRunner.java          # server entry point (start/stop/quit console)
        ├── TodoIstServer.java         # accepts connections, owns the repository
        ├── ClientHandler.java         # per-client session (login state, command loop)
        ├── commands/
        │   ├── CommandFactory.java    # dispatches logged-in commands
        │   ├── HelpCommand.java
        │   ├── accountcommand/        # register, login, help, unknown-command
        │   ├── taskcommands/          # add/update/delete/get/list/finish-task, list-dashboard
        │   ├── colaborationcommands/  # add/delete/list-collaboration, add-user, list-users, assign-task
        │   └── commandparameters/     # CommandParameter, ParameterType (name=value parsing)
        ├── model/                     # Task, TaskCluster (+ impl), DateStringFormater
        ├── collaboration/             # Collaboration, CollaborationImpl
        ├── user/                      # User, UserImpl
        ├── repository/                # TodoIstRepository, InMemoryTodoIstRepository (persistence)
        └── exception/                 # CommandException hierarchy (account/, collaboration/, task/)

test/                                  # JUnit 5 tests, mirroring the src package structure
```

---

## Potential Improvements

- **`list-tasks --completed`** — filtering/listing already-completed tasks isn't implemented yet
- **Labels** — the bonus label feature (`add-label`, `label-task`, `list-tasks --label`, ...) hasn't been built
- **File-based error logging** — unexpected server errors are only reported inline; writing stack traces to a log file (with request context) would make debugging production issues easier
- **Crash-safe persistence** — data is only written to `repository.dat` on a graceful `stop()`; periodic snapshots or a write-ahead approach would survive an unclean shutdown