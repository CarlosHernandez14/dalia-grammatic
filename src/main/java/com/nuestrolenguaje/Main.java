package com.nuestrolenguaje;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.nuestrolenguaje.exceptionhandling.CustomErrorListener;

public class Main {
    private static final String EXTENSION = "manbel";
    private static final String DIRBASE = "src/test/resources/";

    public static void main(String[] args) throws IOException {
        String files[] = args.length == 0 ? new String[]{DIRBASE + "test." + EXTENSION} : args;

        for (String file : files) {
            try {
                // Leer contenido del archivo

                // Configurar parser
                CharStream input = CharStreams.fromFileName(file);
                manbelLexer lexer = new manbelLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                manbelParser parser = new manbelParser(tokens);

                CustomErrorListener errorListener = new CustomErrorListener();
                parser.removeErrorListeners(); // Eliminar los error listeners por defecto
                parser.addErrorListener(errorListener); // Agregar el listener personalizado

                // Parsear y visitar
                manbelParser.ProgramaContext tree = parser.programa();
                manbelCustomVisitor visitor = new manbelCustomVisitor();

                visitor.setErrorListener(errorListener);

                visitor.visit(tree);

                ResponseClass response = new ResponseClass(visitor.getTransformedCodeList(), errorListener.getErrors(), visitor.getOutput());

                System.out.println(response.getResponseAsJson());

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}