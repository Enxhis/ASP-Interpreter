package no.uio.ifi.asp.runtime;

import java.util.HashMap;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeDictValue extends RuntimeValue {
    HashMap<String, RuntimeValue> dict = new HashMap<String, RuntimeValue>();

    public RuntimeDictValue(HashMap<String, RuntimeValue> dict) {
        this.dict = dict;
    }

    @Override
    protected String typeName() {
        return "dict";
    }

    @Override
    public String toString() {
        return dict.toString();
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        RuntimeValue v = new RuntimeIntValue(dict.size());
        return v;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            RuntimeValue value = (RuntimeValue)dict.get(v.toString());
            return value;
        }
        runtimeError("Type error for [...].", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;  // Required by the compiler!
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue value, AspSyntax where) {
        if (!(inx instanceof RuntimeStringValue))
            runtimeError("Index is not an text string!", where);
        String index = inx.toString();
        dict.put(index, value);
    }
}
