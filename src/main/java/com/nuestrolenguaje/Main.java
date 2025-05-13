package com.nuestrolenguaje;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
    private static final String EXTENSION = "manbel";
    private static final String DIRBASE = "src/test/resources/";

    public static void main(String[] args) throws IOException {
        String files[] = args.length == 0 ? new String[]{"test." + EXTENSION} : args;

        for (String file : files) {
            try {
                // Leer contenido del archivo
                String fileContent = String.join("\n", Files.readAllLines(Paths.get(DIRBASE + file)));

                // Configurar parser
                CharStream input = CharStreams.fromString(fileContent);
                manbelLexer lexer = new manbelLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                manbelParser parser = new manbelParser(tokens);

                // Parsear y visitar
                manbelParser.ProgramaContext tree = parser.programa();
                manbelCustomVisitor visitor = new manbelCustomVisitor();
                visitor.visit(tree);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}