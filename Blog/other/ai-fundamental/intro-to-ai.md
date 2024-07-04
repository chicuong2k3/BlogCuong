# Introduction to Artificial Intelligence

> [!QUOTE]
> What we know is a drop, what we don't know is an ocean.
> <div>- Issac Newton -</div>

John Searle, one of the pioneers in the field of artificial intelligence, categorized AI into 2 types: Weak AI and Strong AI. Strong AI refers to systems that have human-level cognition, while Weak AI refers to systems that can simulate human-like behaviour in certain contexts but lacks true understanding or awarenesss.

On the other hand, Stuart Russell classifies AI in the following manner:

|          | Human-like             | Rational           |
|----------|------------------------|--------------------|
| Thinking | Thinking like a human  | Thinking rationally|
| Acting   | Acting like a human    | Acting rationally  |


Thinking like a human corresponds to Strong AI, and acting rationally corresponds to Weak AI.

## Turing Test

In the 1950s, Alan Turing posed the question: "How can we determine if a machine is intelligent?" He proposed the idea that: A man communicates via text messages with both a human and a machine, all of whom are isolated from each other. If he cannot distinguish whether he is talking with a machine or a human, then the machine is considered intelligent.

## Chinese Room Argument

John Searle argued that one does not need to be intelligent to act intelligently. He introduced the following thought experiment: A man who does not understand Chinese is given a book of instructions on how to respond to specific questions. This man sits in a room and receives questions in Chinese through a window. He uses the instruction book to find the answers without understanding the questions. The people outside will think that this man understands Chinese!

## Rational Agent

Acting rationally means maximizing the expected outcome based on the given information. Rational actions do not necessarily require thinking.

> [!WARNING]
> Rational does not mean omniscient because information may be incomplete. Rational does not mean predicting the future and the outcome may differ from expectations.

For example, if you see your friend across the street, what is the rational way to act? You look to the left and to the right to ensure no cars are passing before crossing the street, which is a rational action. But if a rock falls on your head while you are crossing 🤕, that’s an unforeseen outcome.

High-level rationality includes gathering information, exploring, learning, and autonomy.

Autonomy means the agent's actions must be independent of its initial state. The agent must be capable of learning in the future. For instance, as a child, your parents tell you that certain things are dangerous or not good, but as you grow up, you do not necessarily have to follow those rules.

## Agent and Environment

An **agent** is an entity that is capable of perception and action.

The **agent function** is a mapping from any given sequence of perceptions to an action:
$$
\begin{equation*}
f:\mathcal{P}^{*}\rightarrow A
\end{equation*}
$$

An **agent program** is the implementation of an agent function.

## PEAS

PEAS stands for Performance Measure, Environment, Actuators and Sensors.

## Different Levels of AI

- Level 0: Simple reflex 
- Level 1: Search, planning based on some knowledge base.
- Level 2: Learning, exploration.
- Level 3: Automic feature generation, high-level abstraction.
