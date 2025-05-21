package com.nuestrolenguaje;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nuestrolenguaje.ResponseClass.TransformedCode;
import com.nuestrolenguaje.exceptionhandling.CustomErrorListener;
import com.nuestrolenguaje.manbelParser.AsigContext;
import com.nuestrolenguaje.manbelParser.BinOpAddSubContext;
import com.nuestrolenguaje.manbelParser.BinOpLogicalContext;
import com.nuestrolenguaje.manbelParser.BinOpMulDivContext;
import com.nuestrolenguaje.manbelParser.BooleanLiteralContext;
import com.nuestrolenguaje.manbelParser.DeclaracionVariableContext;
import com.nuestrolenguaje.manbelParser.ExprContext;
import com.nuestrolenguaje.manbelParser.InstruccionContext;
import com.nuestrolenguaje.manbelParser.NumeroContext;
import com.nuestrolenguaje.manbelParser.ParensContext;
import com.nuestrolenguaje.manbelParser.PrintContext;
import com.nuestrolenguaje.manbelParser.ProgramaContext;
import com.nuestrolenguaje.manbelParser.StringLiteralContext;
import com.nuestrolenguaje.manbelParser.UnaryOpNotContext;
import com.nuestrolenguaje.manbelParser.VariableContext;
import com.nuestrolenguaje.utils.CodeGenResult;

public class manbelCustomVisitor extends manbelBaseVisitor<Object> {

    private final Map<String, TypeSystem> symbolTypes = new HashMap<>();
    private final Map<String, Object> symbolValues = new HashMap<>();

    private List<TransformedCode> transformedCodeList = new ArrayList<>();
    
    // Para manejar errores semanticos
    private CustomErrorListener errorListener;

    private StringBuilder output = new StringBuilder();

    public void setErrorListener(CustomErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public String getOutput() {
        return output.toString();
    }

    public List<TransformedCode> getTransformedCodeList() {
        return transformedCodeList;
    }

    @Override
    public Object visitPrograma(ProgramaContext ctx) {
        
        for (InstruccionContext instr : ctx.instruccion()) {
            try {
                visit(instr);
            } catch (Exception e) {
                // Acumula el mensaje de error en lugar de imprimirlo en stderr
                errorListener.addSemanticError(e.getMessage(), instr.getStart().getLine(), instr.getStart().getCharPositionInLine());
            }
        }
        return null;
    }

    @Override
    public Object visitDeclaracionVariable(DeclaracionVariableContext ctx) {
        StringBuilder py = new StringBuilder();
        StringBuilder js = new StringBuilder();

        for (int i = 0; i < ctx.ID().size(); i++) {
            String name = ctx.ID(i).getText();
            Object value;
            String initPy, initJs;

            try {
                if (ctx.expr(i) != null) {
                    value    = visit(ctx.expr(i));
                    initPy   = CodeGenHelper.gen(ctx.expr(i)).py;
                    initJs   = CodeGenHelper.gen(ctx.expr(i)).js;
                    TypeSystem vt = TypeSystem.getTypeFromValue(value);
                    TypeSystem declared = TypeSystem.fromString(ctx.tipo().getText());
                    if (vt != declared) {
                        throw new RuntimeException(
                            String.format("Tipo incorrecto para '%s': esperaba %s, obtuvo %s",
                                        name, declared, vt));
                    }
                } else {
                    TypeSystem declared = TypeSystem.fromString(ctx.tipo().getText());
                    value  = getDefaultValue(declared);
                    initPy = CodeGenHelper.defaultValuePy(declared);
                    initJs = CodeGenHelper.defaultValueJs(declared);
                }
                symbolTypes.put(name, TypeSystem.fromString(ctx.tipo().getText()));
                symbolValues.put(name, value);
            } catch (RuntimeException e) {
                errorListener.addSemanticError(
                    e.getMessage(),
                    ctx.getStart().getLine(),
                    ctx.getStart().getCharPositionInLine()
                );
                // para seguir, asigna default
                TypeSystem declared = TypeSystem.fromString(ctx.tipo().getText());
                value  = getDefaultValue(declared);
                initPy = CodeGenHelper.defaultValuePy(declared);
                initJs = CodeGenHelper.defaultValueJs(declared);
                symbolTypes.put(name, declared);
                symbolValues.put(name, value);
            }

            // Concatena múltiples variables separadas por coma:
            if (i > 0) {
                py.append("\n");
                js.append("\n");
            }
            py.append(name).append(" = ").append(initPy);
            js.append("let ").append(name).append(" = ").append(initJs).append(";");
        }

        // Al final, emite UNA entrada:
        transformedCodeList.add(new TransformedCode(py.toString(), js.toString()));
        return null;
    }

    @Override
    public Object visitAsig(AsigContext ctx) {
        String name = ctx.ID().getText();
        Object value;
        String exprPy, exprJs;

        try {
            if (!symbolTypes.containsKey(name)) {
                throw new RuntimeException("Variable '" + name + "' no declarada");
            }
            value    = visit(ctx.expr());
            exprPy   = CodeGenHelper.gen(ctx.expr()).py;
            exprJs   = CodeGenHelper.gen(ctx.expr()).js;
            TypeSystem declared = symbolTypes.get(name);
            TypeSystem vt = TypeSystem.getTypeFromValue(value);
            if (!TypeSystem.isCompatible(declared, vt)) {
                throw new RuntimeException(
                    String.format("Tipo incompatible para '%s': esperaba %s, obtuvo %s",
                                name, declared, vt));
            }
        } catch (RuntimeException e) {
            errorListener.addSemanticError(
                e.getMessage(),
                ctx.getStart().getLine(),
                ctx.getStart().getCharPositionInLine()
            );
            // valor neutro para seguir
            TypeSystem declared = symbolTypes.getOrDefault(name, TypeSystem.ENTERO);
            value  = getDefaultValue(declared);
            exprPy = CodeGenHelper.defaultValuePy(declared);
            exprJs = CodeGenHelper.defaultValueJs(declared);
        }

        symbolValues.put(name, value);
        transformedCodeList.add(new TransformedCode(
            name + " = " + exprPy,
            name + " = " + exprJs + ";"
        ));
        return value;
    }

    @Override
    public Object visitBinOpMulDiv(BinOpMulDivContext ctx) {
        // semántica
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        Object result = handleBinaryOperation(left, right, ctx.op.getText());
        // codegen
        CodeGenResult l = CodeGenHelper.gen(ctx.expr(0));
        CodeGenResult r = CodeGenHelper.gen(ctx.expr(1));
        String op = ctx.op.getText();
        
        return result;
    }

    @Override
    public Object visitBinOpAddSub(BinOpAddSubContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        Object result = handleBinaryOperation(left, right, ctx.op.getText());

        CodeGenResult l = CodeGenHelper.gen(ctx.expr(0));
        CodeGenResult r = CodeGenHelper.gen(ctx.expr(1));
        String op = ctx.op.getText();
        
        return result;
    }

    @Override
    public Object visitBinOpLogical(BinOpLogicalContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        if (!(left instanceof Boolean) || !(right instanceof Boolean)) {
            throw new RuntimeException("Operadores lógicos requieren booleanos");
        }
        boolean lb = (Boolean) left;
        boolean rb = (Boolean) right;
        String op = ctx.op.getText();
        boolean result;
        if ("and".equals(op)) result = lb && rb;
        else result = lb || rb;

        CodeGenResult l = CodeGenHelper.gen(ctx.expr(0));
        CodeGenResult r = CodeGenHelper.gen(ctx.expr(1));
        String pyOp = op;
        
        return result;
    }

    private Object handleBinaryOperation(Object left, Object right, String op) {
        TypeSystem lt = TypeSystem.getTypeFromValue(left);
        TypeSystem rt = TypeSystem.getTypeFromValue(right);
        if (!TypeSystem.canOperate(lt, rt, op)) {
            throw new RuntimeException(
                String.format("Operación inválida: %s %s %s", lt, op, rt)
            );
        }
        return TypeSystem.performOperation(op, left, right);
    }

    @Override
    public Object visitVariable(VariableContext ctx) {
        String name = ctx.ID().getText();
        if (!symbolValues.containsKey(name)) {
            throw new RuntimeException("Variable '" + name + "' no existe");
        }
        Object value = symbolValues.get(name);
        
        return value;
    }

    @Override
    public Object visitNumero(NumeroContext ctx) {
        String t = ctx.NUM().getText();
        Object num = t.contains(".") ? Float.parseFloat(t) : Integer.parseInt(t);
        return num;
    }

    @Override
    public Object visitStringLiteral(StringLiteralContext ctx) {
        String lit = ctx.STRING_LITERAL().getText();
        String val = lit.substring(1, lit.length()-1);
        transformedCodeList.add(
            new TransformedCode(lit, lit)
        );
        return val;
    }

    @Override
    public Object visitBooleanLiteral(BooleanLiteralContext ctx) {
        String lit = ctx.BOOLEAN_LITERAL().getText();
        boolean val = "verdadero".equals(lit);
        String py = val ? "True" : "False";
        String js = val ? "true" : "false";
        transformedCodeList.add(
            new TransformedCode(py, js)
        );
        return val;
    }

    @Override
    public Object visitPrint(PrintContext ctx) {
        // --- Semántica como antes ---
        Object val = visit(ctx.expr());
        output.append(val).append(System.lineSeparator());

        // --- Codegen ÚNICO por instrucción ---
        CodeGenResult cg = CodeGenHelper.gen(ctx.expr());
        transformedCodeList.add(new TransformedCode(
            "print(" + cg.py + ")",
            "console.log(" + cg.js + ");"
        ));
        return null;
    }

    @Override
    public Object visitUnaryOpNot(UnaryOpNotContext ctx) {
        Object value = visit(ctx.expr());
        if (!(value instanceof Boolean)) {
            throw new RuntimeException("El operador 'not' requiere booleano");
        }
        boolean result = !(Boolean) value;

        CodeGenResult exprCg = CodeGenHelper.gen(ctx.expr());
        transformedCodeList.add(
            new TransformedCode(
                "not " + exprCg.py,
                "!" + exprCg.js
            )
        );
        return result;
    }

    @Override
    public Object visitParens(ParensContext ctx) {
        // semántica
        Object value = visit(ctx.expr());
        // codegen
        CodeGenResult cg = CodeGenHelper.gen(ctx.expr());
        transformedCodeList.add(
            new TransformedCode(
                "(" + cg.py + ")",
                "(" + cg.js + ")"
            )
        );
        return value;
    }


    private Object getDefaultValue(TypeSystem type) {
        switch(type) {
            case ENTERO: return 0;
            case FLOTANTE: return 0.0f;
            case BOLEANO: return false;
            case CADENA: return "";
            default: throw new IllegalArgumentException("Tipo desconocido");
        }
    }

    public Map<String, String> getTablaSimbolos() {
        Map<String, String> tabla = new LinkedHashMap<>();
        symbolTypes.forEach((name, type) -> {
            tabla.put(name, type + " = " + symbolValues.get(name));
        });
        return tabla;
    }


    private static class CodeGenHelper {
        public static CodeGenResult gen(ExprContext ctx) {
            if (ctx instanceof NumeroContext) {
                String t = ((NumeroContext) ctx).NUM().getText();
                return new CodeGenResult(t, t);
            } else if (ctx instanceof StringLiteralContext) {
                String lit = ((StringLiteralContext) ctx).STRING_LITERAL().getText();
                return new CodeGenResult(lit, lit);
            } else if (ctx instanceof BooleanLiteralContext) {
                String lit = ((BooleanLiteralContext) ctx).BOOLEAN_LITERAL().getText();
                String py = "verdadero".equals(lit) ? "True" : "False";
                String js = "verdadero".equals(lit) ? "true" : "false";
                return new CodeGenResult(py, js);
            } else if (ctx instanceof VariableContext) {
                String name = ((VariableContext) ctx).ID().getText();
                return new CodeGenResult(name, name);
            } else if (ctx instanceof ParensContext) {
                CodeGenResult inner = gen(((ParensContext) ctx).expr());
                return new CodeGenResult("(" + inner.py + ")", "(" + inner.js + ")");
            } else if (ctx instanceof BinOpAddSubContext) {
                BinOpAddSubContext b = (BinOpAddSubContext) ctx;
                CodeGenResult l = gen(b.expr(0));
                CodeGenResult r = gen(b.expr(1));
                String op = b.op.getText();
                return new CodeGenResult(l.py + " " + op + " " + r.py,
                                         l.js + " " + op + " " + r.js);
            } else if (ctx instanceof BinOpMulDivContext) {
                BinOpMulDivContext b = (BinOpMulDivContext) ctx;
                CodeGenResult l = gen(b.expr(0));
                CodeGenResult r = gen(b.expr(1));
                String op = b.op.getText();
                return new CodeGenResult(l.py + " " + op + " " + r.py,
                                         l.js + " " + op + " " + r.js);
            } else if (ctx instanceof BinOpLogicalContext) {
                BinOpLogicalContext b = (BinOpLogicalContext) ctx;
                CodeGenResult l = gen(b.expr(0));
                CodeGenResult r = gen(b.expr(1));
                String pyOp = b.op.getText();
                String jsOp = "and".equals(pyOp) ? "&&" : "||";
                return new CodeGenResult(l.py + " " + pyOp + " " + r.py,
                                         l.js + " " + jsOp + " " + r.js);
            } else if (ctx instanceof UnaryOpNotContext) {
                CodeGenResult inner = gen(((UnaryOpNotContext) ctx).expr());
                return new CodeGenResult("not " + inner.py, "!" + inner.js);
            } else {
                throw new UnsupportedOperationException(
                    "CodeGen not implemented for " + ctx.getClass().getSimpleName()
                );
            }
        }

        public static String defaultValuePy(TypeSystem t) {
            switch (t) {
                case ENTERO:   return "0";
                case FLOTANTE: return "0.0";
                case BOLEANO:  return "False";
                case CADENA:   return "\"\"";
                default:       return "";
            }
        }
        public static String defaultValueJs(TypeSystem t) {
            switch (t) {
                case ENTERO:   return "0";
                case FLOTANTE: return "0.0";
                case BOLEANO:  return "false";
                case CADENA:   return "\'\'";
                default:       return "";
            }
        }
    }
}