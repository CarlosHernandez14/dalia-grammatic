package com.nuestrolenguaje;

import java.util.List;

import com.google.gson.Gson;
import com.nuestrolenguaje.exceptionhandling.CustomErrorListener.CompilerError;

public class ResponseClass {
    
    private List<TransformedCode> transformedCodeList;
    
    private List<CompilerError> errors;

    private String output;

    public ResponseClass(List<TransformedCode> transformedCodeList, List<CompilerError> errors, String output) {
        this.transformedCodeList = transformedCodeList;
        this.errors = errors;
        this.output = output;
    }

    
    public String getResponseAsJson() {{
        Gson gson = new Gson();
        return gson.toJson(this);
    }}

    public List<TransformedCode> getTransformedCodeList() {
        return transformedCodeList;
    }

    public void setTransformedCodeList(List<TransformedCode> transformedCodeList) {
        this.transformedCodeList = transformedCodeList;
    }

    public List<CompilerError> getErrors() {
        return errors;
    }

    public void setErrors(List<CompilerError> errors) {
        this.errors = errors;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public static class TransformedCode {

        private String pythonCode;
        private String javaScriptCode;

        public TransformedCode(String pythonCode, String javaScriptCode) {
            this.pythonCode = pythonCode;
            this.javaScriptCode = javaScriptCode;
        }
        
        public String getPythonCode() {
            return pythonCode;
        }

        public void setPythonCode(String pythonCode) {
            this.pythonCode = pythonCode;
        }

        public String getJavaScriptCode() {
            return javaScriptCode;
        }

        public void setJavaScriptCode(String javaScriptCode) {
            this.javaScriptCode = javaScriptCode;
        }

    }
}
