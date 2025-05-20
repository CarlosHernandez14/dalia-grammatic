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
                throw e;
            }
        }
        return null;
    }

    @Override
    public Object visitDeclaracionVariable(DeclaracionVariableContext ctx) {
        TypeSystem varType = TypeSystem.fromString(ctx.tipo().getText());
        
        for (int i = 0; i < ctx.ID().size(); i++) {
            String varName = ctx.ID(i).getText();
            
            if (ctx.expr(i) != null) {
                Object value = visit(ctx.expr(i));
                TypeSystem valueType = TypeSystem.getTypeFromValue(value);
                
                if (varType != valueType) {  // Validación estricta
                    throw new RuntimeException(String.format(
                        "Tipo incorrecto para '%s': esperaba %s, obtuvo %s",
                        varName, varType, valueType));
                }
                
                symbolValues.put(varName, value);
            } else {
                symbolValues.put(varName, getDefaultValue(varType));
            }
            symbolTypes.put(varName, varType);
        }
        return null;
    }

    @Override
    public Object visitAsig(AsigContext ctx) {
        String varName = ctx.ID().getText();
        
        if (!symbolTypes.containsKey(varName)) {
            throw new RuntimeException("Variable '" + varName + "' no declarada");
        }
        
        Object value = visit(ctx.expr());
        TypeSystem valueType = TypeSystem.getTypeFromValue(value);
        TypeSystem varType = symbolTypes.get(varName);
        
        if (!TypeSystem.isCompatible(varType, valueType)) {
            throw new RuntimeException("Tipo incompatible para " + varName + 
                   ": no se puede asignar " + valueType + " a " + varType);
        }
        
        if (varType == TypeSystem.FLOTANTE && valueType == TypeSystem.ENTERO) {
            value = ((Integer)value).floatValue();
        }
        
        symbolValues.put(varName, value);
        return value;
    }

    @Override
    public Object visitBinOpMulDiv(BinOpMulDivContext ctx) {
        return handleBinaryOperation(ctx.expr(0), ctx.expr(1), ctx.op.getText());
    }

    @Override
    public Object visitBinOpAddSub(BinOpAddSubContext ctx) {
        return handleBinaryOperation(ctx.expr(0), ctx.expr(1), ctx.op.getText());
    }

    @Override
    public Object visitBinOpLogical(BinOpLogicalContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));

        if (!(left instanceof Boolean) || !(right instanceof Boolean)) {
            throw new RuntimeException("Operadores lógicos requieren valores booleanos");
        }

        boolean leftBool = (Boolean) left;
        boolean rightBool = (Boolean) right;

        switch (ctx.op.getText()) {
            case "and":
                return leftBool && rightBool;
            case "or":
                return leftBool || rightBool;
            default:
                throw new RuntimeException("Operador lógico desconocido: " + ctx.op.getText());
        }
    }

    private Object handleBinaryOperation(ExprContext leftCtx, ExprContext rightCtx, String op) {
        Object left = visit(leftCtx);
        Object right = visit(rightCtx);
        TypeSystem leftType = TypeSystem.getTypeFromValue(left);
        TypeSystem rightType = TypeSystem.getTypeFromValue(right);
        
        if (!TypeSystem.canOperate(leftType, rightType, op)) {
            throw new RuntimeException(String.format(
                "Operación inválida: %s %s %s (tipos no compatibles)",
                leftType, op, rightType));
        }
        
        return TypeSystem.performOperation(op, left, right);
    }

    @Override
    public Object visitVariable(VariableContext ctx) {
        String varName = ctx.ID().getText();
        if (!symbolValues.containsKey(varName)) {
            throw new RuntimeException("Variable '" + varName + "' no existe");
        }
        Object value = symbolValues.get(varName);
        return value;
    }

    @Override
    public Object visitNumero(NumeroContext ctx) {
        String numText = ctx.NUM().getText();
        if (numText.contains(".")) {
            return Float.parseFloat(numText);
        }
        return Integer.parseInt(numText);
    }

    @Override
    public Object visitStringLiteral(StringLiteralContext ctx) {
        return ctx.STRING_LITERAL().getText().replaceAll("^\"|\"$", "");
    }

    @Override
    public Object visitBooleanLiteral(BooleanLiteralContext ctx) {
        String boolText = ctx.BOOLEAN_LITERAL().getText();
        return boolText.equals("verdadero");
    }

    @Override
    public CodeGenResult visitPrint(manbelParser.PrintContext ctx) {
        Object value = visit(ctx.expr());
        // Agrega al StringBuilder en lugar de imprimir
        output.append(value)                
            .append(System.lineSeparator());  // salto de línea
        return null;
    }

    @Override
    public Object visitUnaryOpNot(UnaryOpNotContext ctx) {
        Object value = visit(ctx.expr());

        if (!(value instanceof Boolean)) {
            throw new RuntimeException("El operador 'not' requiere un valor booleano");
        }

        return !(Boolean) value;
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
}