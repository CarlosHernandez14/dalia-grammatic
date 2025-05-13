package com.nuestrolenguaje;

public enum TypeSystem {
    ENTERO, FLOTANTE, BOLEANO, CADENA, UNKNOWN;
    
    public static TypeSystem fromString(String typeStr) {
        if (typeStr == null) return UNKNOWN;
        switch(typeStr.toLowerCase()) {
            case "entero": return ENTERO;
            case "flotante": return FLOTANTE;
            case "boleano": return BOLEANO;
            case "cadena": return CADENA;
            default: return UNKNOWN;
        }
    }
    
    public static boolean isCompatible(TypeSystem target, TypeSystem source) {
        return target == source;  
    }
    
    public static TypeSystem getTypeFromValue(Object value) {
        if (value == null) return UNKNOWN;
        if (value instanceof Integer) return ENTERO;
        if (value instanceof Float || value instanceof Double) return FLOTANTE;
        if (value instanceof Boolean) return BOLEANO;
        if (value instanceof String) return CADENA;
        return UNKNOWN;
    }
    
    public static boolean isNumeric(TypeSystem type) {
        return type == ENTERO || type == FLOTANTE;
    }
    
    public static boolean canOperate(TypeSystem left, TypeSystem right, String op) {
        // Operaciones numéricas: Solo permite entre tipos idénticos
        if (op.matches("[+\\-*/]")) {
            return left == right && isNumeric(left);
        }
        // Concatenación de cadenas
        if (op.equals("+")) {
            return (left == CADENA && right == CADENA);
        }
        return false;
    }

    public static Object performOperation(String op, Object left, Object right) {
        TypeSystem leftType = getTypeFromValue(left);
        TypeSystem rightType = getTypeFromValue(right);

        // Validar si la operación es compatible
        if (!canOperate(leftType, rightType, op)) {
            throw new RuntimeException("Operación no soportada entre " + leftType + " y " + rightType + " con operador '" + op + "'");
        }

        // Concatenación de cadenas
        if (op.equals("+") && leftType == CADENA && rightType == CADENA) {
            return (String) left + (String) right;
        }

        // Operaciones numéricas
        if (isNumeric(leftType) && isNumeric(rightType)) {
            double l = ((Number) left).doubleValue();
            double r = ((Number) right).doubleValue();

            switch (op) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/":
                    if (r == 0) throw new ArithmeticException("División por cero");
                    return l / r;
                default: throw new RuntimeException("Operador no soportado: " + op);
            }
        }

        throw new RuntimeException("Operación no soportada entre " + leftType + " y " + rightType);
    }
}