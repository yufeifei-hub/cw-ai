# Scotland Yard Game-Playing AI

A Java game-playing AI developed as a three-person group coursework
project at the University of Bristol.

## Overview

This repository contains an AI agent for the Scotland Yard board game.
The agent selects moves for Mr X using a combination of Monte Carlo Tree
Search, depth-limited Minimax and heuristic state evaluation.

The project explores how different search strategies can be combined to
make decisions within the game's move-time constraints.

## AI Approach

The agent combines the following techniques:

- Monte Carlo Tree Search with selection, expansion, evaluation and
  backpropagation
- UCB-based selection to balance exploration and exploitation
- Depth-limited Minimax with alpha-beta pruning
- A custom game-state representation for simulating future moves
- Heuristic evaluation of candidate states

The MCTS evaluation stage uses a depth-3 Minimax search instead of a
purely random rollout.

The heuristic considers factors including:

- Distance between Mr X and the detectives
- Available escape routes
- Reveal rounds
- Remaining transport tickets
- Distance from previously revealed positions

## Repository Structure

```text
src/main/java/uk/ac/bris/cs/scotlandyard/ui/ai/
├── MyAi.java
├── MctsSearch.java
├── MctsNode.java
├── MiniMax.java
├── NewGameState.java
├── ScoreMain.java
└── DistanceMainCalculator.java
```

The `repo/` directory is a local Maven repository containing
University-provided Scotland Yard dependencies required to build the
coursework project. These packages are not available from the public
Maven repositories.

## Building

Requirements:

- Java 17
- Maven

To compile the project and run its tests:

```bash
mvn clean test
```

The build command has been verified against the current repository.

## Coursework Context

This repository was created as a collaborative three-person coursework
project at the University of Bristol. It is an academic team project
rather than an independently maintained open-source game.
