package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {
    public ArrayList<RuntimeValue> list = new ArrayList<>();

    public RuntimeListValue(ArrayList<RuntimeValue> list) {
        this.list = list;
    }

    @Override
    protected String typeName() {
        return "list";
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public String showInfo() {
        String retStr = "[";
        Boolean b = false;
        for (RuntimeValue v : list) {
            if (b) retStr = retStr + ", ";
            retStr = retStr + v.showInfo();
            b = true;
        }
        retStr = retStr + "]";
        return retStr;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        RuntimeValue v = new RuntimeIntValue(list.size());
        return v;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            RuntimeValue ret = list.get((int)v.getIntValue("[...] operand", where));
            return ret;
        } else {
            runtimeError("A list index must be an integer!", where);
        }
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            ArrayList<RuntimeValue> retList = new ArrayList<>();
            long v2 = v.getIntValue(" operand", where);

            for (int i = 0; i < v2; i++) {
                retList.addAll(list);
            }
            return new RuntimeListValue(retList);
        }
        runtimeError("Type error for *.", where);
        return null;  // Required by the compiler.
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
        if (!(inx instanceof RuntimeIntValue)) runtimeError("Index is not an integer!", where);
        int index = (int) inx.getIntValue("integer", where);
        if (index < list.size()) {
            list.set(index, value);
        } else {
            runtimeError("Index out of bounds.", where);
        }
    }
}
