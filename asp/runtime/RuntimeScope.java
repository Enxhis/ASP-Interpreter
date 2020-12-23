package no.uio.ifi.asp.runtime;

// For part 4:

import java.util.HashMap;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeScope {
    private RuntimeScope outer;
    private HashMap<String,RuntimeValue> decls = new HashMap<>();

    public RuntimeScope() {
        outer = null;
    }


    public RuntimeScope(RuntimeScope oScope) {
        outer = oScope;
    }


    public void assign(String id, RuntimeValue val) {
        decls.put(id, val);
    }


    public RuntimeValue find(String id, AspSyntax where) {
        RuntimeValue v = decls.get(id);
        if (v != null)
        return v;
        if (outer != null)
        return outer.find(id, where);

        RuntimeValue.runtimeError("Name " + id + " not defined!", where);
        return null;  // Required by the compiler.
    }
}
