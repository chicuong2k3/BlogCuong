# Learning Javascript for C# Developer

## The differences

| Grammar         | C#                 | Javascript                                                                      |
|-----------------|--------------------|---------------------------------------------------------------------------------|
| Comparison      | == and !=          | == and != with type conversion, === and !== without type conversion             |
| Data Types      | Your already know  | String, Number, Boolean, Array, Object, Function                                |
| Truthy Values   | true               | true, non-zero Number, non-empty String (except "false")                        |
| Falsy Values    | false              | false, null, undefined, "", "false", 0, [], {}                                  |
| Scope           |               | Global or Function. Variables order doesn't matter due to hoisting                   |
| Async           |  async and await   | Promises (new Javascript already support async and await keyword)               |

### Functions

In javascript, function are first class objects:
- Can be named or anonymous (equivalent to anonymous method in C#).
- Can be passed as an argument to other functions (equivalent to Delegate in C#).


### Closure

- In javascript, local variables in a function keep alive after the function has returned (created by using a nested function). Internal function can reference local variables inside returned function.
- In C#, variables in a method will be destroyed when the function return.

### Objects

- Access shared members through object's prototype.


