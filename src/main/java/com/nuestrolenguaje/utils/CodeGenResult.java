package com.nuestrolenguaje.utils;

import com.nuestrolenguaje.ResponseClass.TransformedCode;

public class CodeGenResult {
    public final String py;
    public final String js;
    public CodeGenResult(String py, String js) {
        this.py = py;
        this.js = js;
    }
    public TransformedCode toTransformed() {
        return new TransformedCode(py, js);
    }
}