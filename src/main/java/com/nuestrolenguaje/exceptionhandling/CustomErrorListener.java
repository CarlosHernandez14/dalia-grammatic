package com.nuestrolenguaje.exceptionhandling;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class CustomErrorListener extends BaseErrorListener {
    private ArrayList<CompilerError> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, 
                            Object offendingSymbol, 
                            int line, int charPositionInLine, 
                            String msg, RecognitionException e) {
        // Guarda el error en la lista
        CompilerError error = new CompilerError(line, charPositionInLine, "SyntaxError", msg);
        // Capturamos errores sintácticos
        errors.add(error);

        //System.out.println("Errores encontrados:");
        //errors.forEach(System.out::println);

        // Imprime el error en consola (opcional)
        //System.err.println("Custom error at line " + line + ":" + charPositionInLine + " - " + msg);
    }

    public void addSemanticError(String message, int line, int column) {
        // Capturamos errores semánticos
        errors.add(new CompilerError(line, column, "SemanticError", message));
    }

    public void saveErrorsToJson(String fileName) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(fileName)) {
            // Serializa la lista de errores a JSON y escribe el archivo
            gson.toJson(errors, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getErrorsAsJson() {
        Gson gson = new Gson();
        return gson.toJson(errors);
    }

    public List<CompilerError> getErrors() {
        return errors;
    }



    // Clase interna (o externa) para representar un error
    public static class CompilerError {
        int line;
        int column;
        String type;    // "SyntaxError" o "SemanticError"
        String message;

        public CompilerError(int line, int column, String type, String message) {
            this.line = line;
            this.column = column;
            this.type = type;
            this.message = message;
        }
    }
}
