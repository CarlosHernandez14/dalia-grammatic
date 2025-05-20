package com.nuestrolenguaje.exceptionhandling;
public class SyntaxError {
    private final int line;
    private final int charPositionInLine;
    private final String msg;

    public SyntaxError(int line, int charPositionInLine, String msg) {
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.msg = msg;
    }

    public int getLine() {
        return line;
    }

    public int getCharPositionInLine() {
        return charPositionInLine;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "SyntaxError{" +
                "line=" + line +
                ", charPositionInLine=" + charPositionInLine +
                ", msg='" + msg + '\'' +
                '}';
    }
}