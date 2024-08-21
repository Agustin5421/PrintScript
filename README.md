# Proyecto Java

Este proyecto está desarrollado en Java y utiliza Gradle como sistema de construcción. A continuación se describen los paquetes principales del proyecto:

## Paquetes

## `lexer`
Este paquete contiene clases e interfaces relacionadas con el análisis léxico del código fuente.

#### `Lexer`
Esta clase principal implementa la interfaz `Progressable` y se encarga de extraer tokens del código fuente. Utiliza patrones de texto para identificar diferentes tipos de tokens y notifica a los observadores sobre el progreso del análisis.



## `token`
Este paquete contiene clases e interfaces relacionadas con la validación y tipos de tokens en el proyecto.

#### `validators.DataTypeTokenChecker`
Esta clase implementa `TypeGetter` y se encarga de determinar el tipo de token para valores de tipo cadena y número.
#### `validators.IdentifierTypeChecker`
Esta clase implementa `TypeGetter` y se encarga de validar y determinar el tipo de token para identificadores.
#### `validators.OperationTypeTokenChecker`
Esta clase implementa `TypeGetter` y se encarga de determinar el tipo de token para operadores.
#### `validators.TagTypeTokenChecker`
Esta clase implementa `TypeGetter` y se encarga de determinar el tipo de token para palabras reservadas y símbolos.
#### `validators.TokenTypeChecker`
Esta clase implementa `TypeGetter` y se encarga de orquestar la validación de tokens utilizando una lista de validadores.
#### `validators.TypeGetter`
Esta interfaz define el método `getType` que debe ser implementado por cualquier clase que determine el tipo de un token.

#### `types.TokenDataType`
Esta enumeración define los tipos de datos de tokens, como `STRING_TYPE`, `NUMBER_TYPE` y `OPERAND`.
#### `types.TokenTagType`
Esta enumeración define los tipos de etiquetas de tokens, como `IDENTIFIER`, `ASSIGNATION`, `SEMICOLON`, `DECLARATION`, `SYNTAX`, `INVALID`, `OPEN_PARENTHESIS`, `CLOSE_PARENTHESIS` y `COMMA`.
#### `types.TokenType`
Esta interfaz es implementada por todas las enumeraciones de tipos de tokens.
#### `types.TokenValueType`
Esta enumeración define los tipos de valores de tokens, como `STRING` y `NUMBER`.



## `parser`
Este paquete contiene clases y interfaces relacionadas con el análisis sintáctico de tokens en el proyecto.

#### `Parser`
Esta clase principal implementa la interfaz `Progressable` y se encarga de orquestar el proceso de análisis sintáctico, dividiendo los tokens en declaraciones y utilizando los parsers adecuados para cada tipo de declaración.

#### `InstructionParser`
Esta interfaz define los métodos `parse` y `shouldParse` que deben ser implementados por cualquier parser de instrucciones.

#### `statements.AssignmentParser`
Esta clase implementa `StatementParser` y se encarga de analizar y crear nodos AST para las expresiones de asignación.
#### `statements.CallFunctionParser`
Esta clase implementa `StatementParser` y se encarga de analizar y crear nodos AST para las llamadas a funciones.
#### `statements.StatementParser`
Esta interfaz extiende `InstructionParser` y es implementada por los parsers de declaraciones.
#### `statements.VariableDeclarationParser`
Esta clase implementa `StatementParser` y se encarga de analizar y crear nodos AST para las declaraciones de variables.

#### `expressions.BinaryExpressionParser`
Esta clase implementa `ExpressionParser` y se encarga de analizar y crear nodos AST para las expresiones binarias.
#### `expressions.ExpressionParser`
Esta interfaz extiende `InstructionParser` y es implementada por los parsers de expresiones.
#### `expressions.LiteralParser`
Esta clase implementa `ExpressionParser` y se encarga de analizar y crear nodos AST para los literales.



## `ast`
Este paquete contiene clases e interfaces relacionadas con la representación del Árbol de Sintaxis Abstracta (AST) en el proyecto.

#### `root.AstNodeType`
Esta enumeración define los diferentes tipos de nodos AST, como `ASSIGNMENT_EXPRESSION`, `CALL_EXPRESSION`, `EXPRESSION_STATEMENT`, `VARIABLE_DECLARATION`, `IDENTIFIER`, `NUMBER_LITERAL`, `STRING_LITERAL` y `BINARY_EXPRESSION`.
#### `root.Program`
Esta clase representa el programa completo como un nodo AST, conteniendo una lista de declaraciones y sus posiciones de inicio y fin.

#### `literal.Literal`
Esta interfaz representa un literal en el AST y extiende `Expression`. Define el método `value` para obtener el valor del literal.
#### `literal.LiteralFactory`
Esta clase se encarga de crear instancias de `Literal` a partir de tokens, soportando literales de tipo `STRING` y `NUMBER`.
#### `literal.NumberLiteral`
Esta clase implementa `Literal` para representar literales numéricos en el AST, incluyendo su valor y posiciones de inicio y fin.
#### `literal.StringLiteral`
Esta clase implementa `Literal` para representar literales de cadena en el AST, incluyendo su valor y posiciones de inicio y fin.
#### `identifier.Identifier`
Esta clase representa un identificador en el AST, incluyendo su nombre y posiciones de inicio y fin.
#### `identifier.IdentifierParser`
Esta clase implementa `ExpressionParser` y se encarga de analizar y crear nodos AST para identificadores.

#### `expressions.Expression`
Esta interfaz representa una expresión en el AST y extiende `AstNode`.
#### `expressions.BinaryExpression`
Esta clase implementa `Expression` para representar expresiones binarias en el AST, incluyendo los operandos izquierdo y derecho, el operador, y las posiciones de inicio y fin.



## `interpreter`
Este paquete contiene clases e interfaces relacionadas con la interpretación del Árbol de Sintaxis Abstracta (AST) y la ejecución del programa.

#### `VariablesRepository`
Esta clase se encarga de almacenar y gestionar las variables del programa, permitiendo agregar y recuperar variables de tipo cadena y número.
#### `Interpreter`
Esta clase principal implementa la interfaz `Progressable` y se encarga de ejecutar el programa representado por el AST. Utiliza un `AstNodeVisitor` para visitar y evaluar cada nodo del AST.
#### `AstNodeVisitor`
Esta clase implementa la interfaz `NodeVisitor` y se encarga de visitar y evaluar los nodos del AST, actualizando el `VariablesRepository` con los resultados de las evaluaciones.

#### `runtime.Evaluator`
Esta interfaz define el método `evaluate` que debe ser implementado por cualquier clase que evalúe nodos del AST.
#### `runtime.ExpressionEvaluator`
Esta clase implementa `Evaluator` y se encarga de evaluar expresiones en el AST, manejando operaciones binarias, literales y variables.



## `formatter`
Este paquete contiene clases e interfaces relacionadas con el formateo del código fuente basado en el Árbol de Sintaxis Abstracta (AST).

#### `MainFormatter`
Esta clase principal se encarga de formatear el programa representado por el AST utilizando una lista de formatters específicos para cada tipo de declaración.
#### `MainFormatterInitializer`
Esta clase se encarga de inicializar el `MainFormatter` con los formatters necesarios.

#### `statement.Formatter`
Esta interfaz define los métodos `shouldFormat` y `format` que deben ser implementados por cualquier formatter de declaraciones.
#### `statement.AsignmentFormatter`
Esta clase implementa `Formatter` y se encarga de formatear las expresiones de asignación en el AST.
#### `statement.ExpressionFormatter`
Esta clase implementa `Formatter` y se encarga de formatear las expresiones en el AST.
#### `statement.FunctionCallFormatter`
Esta clase implementa `Formatter` y se encarga de formatear las llamadas a funciones en el AST.
#### `statement.VariableDeclarationFormatter`
Esta clase implementa `Formatter` y se encarga de formatear las declaraciones de variables en el AST.



## `cli`
Este paquete contiene la clase principal `Cli` que se encarga de ejecutar el programa desde la línea de comandos.

#### `Cli`
Esta clase principal se encarga de leer el contenido de un archivo, analizarlo léxica y sintácticamente, y luego ejecutar el programa representado por el Árbol de Sintaxis Abstracta (AST). Utiliza observadores para mostrar el progreso del análisis y la ejecución.



## `buildSrc`
Este proyecto contiene configuraciones y scripts de construcción personalizados para el proyecto principal.

#### `checkstyle.printscript.gradle`
Este script configura el plugin `checkstyle` para el proyecto, especificando la versión de la herramienta y la ubicación del archivo de configuración. También define la generación de reportes en formato HTML.
#### `jacoco.printscript.gradle`
Este script configura el plugin `jacoco` para el proyecto, especificando la versión de la herramienta y definiendo tareas para generar reportes de cobertura de código en formato HTML. También establece reglas de verificación de cobertura.



## `utils`
Este paquete contiene clases y interfaces utilitarias que son utilizadas en toda la aplicación.

#### `printer.ProgressType`
Esta enumeración define los diferentes estados de progreso con sus respectivos códigos de color.

#### `observers.Observable`
Esta interfaz define los métodos para agregar, eliminar y notificar observadores.
#### `observers.Observer`
Esta interfaz define el método `update` que es llamado cuando un `Observable` notifica a sus observadores.
#### `observers.Progressable`
Esta interfaz extiende `Observable` y añade el método `getProgress` para obtener el progreso actual.
#### `observers.ProgressObserver`
Esta clase implementa `Observer` y se encarga de actualizar y mostrar el progreso utilizando `ProgressPrinter`.
#### `observers.ProgressPrinter`
Esta clase se encarga de imprimir una barra de progreso en la consola con el estado actual del progreso.


## Instalación

Para construir y ejecutar el proyecto, utiliza los siguientes comandos de Gradle:

```sh
./gradlew build
./gradlew run