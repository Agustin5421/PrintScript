# Printscript 1.0
A simple language for printing text.

Currently, the language only supports declaring variables, binary operations, and printing text.

Also, formatter and static code analyzer are available.


## Lexer
The lexer is responsible for tokenizing the input text.

It is created with an InputString, which is an excerpt of the source code to be tokenized, along with a list of TokenTypeGetter that stores the token checks.
Some of these are: operandPatternChecker, syntaxPatternChecker, dataTypePatternChecker, literalTypeTokenChecker, identifierTypeChecker.
This is also used for the different versions of PrintScript. In version "1.0", the DataType does not include boolean, but in "1.1" it does, so it is simply added to the list.
When trying to use a boolean where its implementation does not exist, it will throw an error.  

The lexer iterates through the InputString and checks if the token is of any type. If it is, it adds it to the list of tokens. 
This iterative process continues until there are no more tokens to add for that statement.
A statement is complete when it encounters a semicolon, or when it encounters an if statement, which is a special case with "{}".


## Parser
The parser is responsible for parsing the tokens and building the AST.

The parser has a list of StatementParser and ExpressionParser. The StatementParser are responsible for parsing variable declarations and assignments, if statements, and call functions. The ExpressionParser are responsible for parsing binary expressions, call functions, identifiers, and literals. 
The parser iterates over the tokens provided by the lexer and parses them with the corresponding StatementParser and ExpressionParser.


## Interpreter
The interpreter is responsible for interpreting the AST.

It works with pattern matching, where a node is passed and depending on its type, the corresponding strategy is executed. 
For node strategies, StrategyContainer is used, which is a dictionary that has the node type as the key and the strategy to be executed as the value. 
This strategy is a function that receives the node and the context and returns a result according to its node type. 
It also uses a ValueCollector, which also has a StrategyContainer, but this only works for Identifier, Literals, BinaryExpressions, and CallExpression (readInput() and readEnv()). This is used to collect the values of the nodes and return them to the interpreter.



## Formatter
The formatter is responsible for formatting the source code.



## Linter
The linter is responsible for analyzing the source code and checking for errors.



