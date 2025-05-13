grammar manbel;

programa: instruccion+ EOF;

instruccion
    : declaracionVariable
    | asig '.'
    | expr '.'
    | def
    | print 
    ;

asig: ID ':' expr;

declaracionVariable : tipo ID (':' expr)? (',' ID (':' expr)?)* '.';

tipo: ENTERO | FLOTANTE | BOLEANO | CADENA;

def: condicional | ciclo;

condicional: 'si' '(' expr ')' bloque ('sino' bloque)?;
ciclo: 'para' '(' asig '.' expr '.' asig ')' bloque;

bloque: '{' instruccion* '}' | instruccion;

print: IMPRIMIR '(' expr ')' '.';

expr: expr op=('*'|'/') expr             # BinOpMulDiv
    | expr op=('+'|'-') expr             # BinOpAddSub
    | expr op=('>'|'<'|'=='|'!=') expr   # BinOpComp
    | expr op=('and'|'or') expr          # BinOpLogical
    | 'not' expr                         # UnaryOpNot
    | '(' expr ')'                       # Parens
    | ID                                 # Variable
    | NUM                                # Numero
    | STRING_LITERAL                     # StringLiteral
    | BOOLEAN_LITERAL                    # BooleanLiteral
    ;

// Tokens
ENTERO: 'entero';
FLOTANTE: 'flotante';
BOLEANO: 'boleano';
CADENA: 'cadena';
SI: 'si';
SINO: 'sino';
PARA: 'para';
IMPRIMIR: 'imprimir';

NUM: [0-9]+ ('.' [0-9]+)?;
STRING_LITERAL: '"' .*? '"';
BOOLEAN_LITERAL: 'verdadero' | 'falso';
ID: [a-zA-Z_][a-zA-Z0-9_]*;

AND: 'and';
OR: 'or';
NOT: 'not';

WS: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;