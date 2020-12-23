package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def;
    RuntimeScope defScope;
    String name;

    public RuntimeFunc(String s) {
        this.name = s;
    }

    public RuntimeFunc(AspFuncDef def, RuntimeScope defScope) {
        this.name = def.aNameList.get(0).name;
        this.def = def;
        this.defScope = defScope;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected String typeName() {
        return "func";
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
        if ((def.aNameList.size() - 1) != actualParams.size()) {
            runtimeError("Wrong number of parameters calling " + name, where);
        }
        RuntimeScope scope = new RuntimeScope(defScope);

        for (int i = 1; i < def.aNameList.size(); i++) {
            scope.assign(def.aNameList.get(i).name, actualParams.get(i-1));
        }
        try {
            def.aSuite.eval(scope);
        } catch (RuntimeReturnValue rrv) {
            return rrv.value;
        }
        return new RuntimeNoneValue();
    }
}
