[![Stories in Ready](https://badge.waffle.io/plankp/LCode.png?label=ready&title=Ready)](https://waffle.io/plankp/LCode)
# LCode
When Javascript meets Lambda expressions (This only applied to pre-1.0)

[![Build Status](https://travis-ci.org/plankp/LCode.svg?branch=master)](https://travis-ci.org/plankp/LCode)

# How to test this?

You will need jdk8 (or higher) to be installed.

Currently, the parser only converts the predefined source code into an AST.
The optimizer and potentially, the runtime is not being worked on so far. (It will be worked on when the grammar supports most pre-1.0 constructs)

To run the tests, do `./gradlew check`. To test the default script, do `./gradlew run` instead.

# Incompatibilities between LCode and LCode pre-1.0

### Function calling syntax

pre-1.0: `([2])`

now: `[2]()`

### Identity function

pre-1.0: `_`

now: Does not exist as of now. Usage will most likely be different compared to pre-1.0.

### Iota generator

pre-1.0: `_10` or `_10.0`

now: Does not exist as of now. Will have different syntax.

### Map syntax

pre-1.0: `{ a, 10 b, 11 }`

now: `{ $a: 10 $b: 11 }` Note: this is the closest representation since `$` detonates a symbol

### List syntax

pre-1.0: `'('(1) '(2) '(3))`

now: Above works, however, can be reduced into `'('1 '2 '3)`

-----

Made by Plankp T. and released under the MIT License
