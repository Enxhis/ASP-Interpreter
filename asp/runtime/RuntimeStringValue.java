package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
    String stringValue;

    public RuntimeStringValue(String v) {
        stringValue = v;
    }

    @Override
    protected String typeName() {
        return "string";
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public String showInfo() {
        return "'" + stringValue + "'";
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        try {
            long l = Long.parseLong(stringValue);
            return l;
        } catch (NumberFormatException e) {
            runtimeError("Cannot convert '" + stringValue + "' to " + what, where);
            return 0;
        }
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        try {
            double d = Double.parseDouble(stringValue);
            return d;
        } catch (NumberFormatException e) {
            runtimeError("Cannot convert '" + stringValue + "' to " + what, where);
            return 0.0;
        }
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        RuntimeValue v = new RuntimeIntValue(stringValue.length());
        return v;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (stringValue == "") ? false : true;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(stringValue == "");
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeStringValue(stringValue + v.toString());
        }
        runtimeError("Type error for +.", where);
        return null;  // Required by the compiler.
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            String retString = "";
            long v2 = v.getIntValue(" operand", where);

            for (int i = 0; i < v2; i++) {
                retString = retString + stringValue;
            }
            return new RuntimeStringValue(retString);
        }
        runtimeError("Type error for *.", where);
        return null;  // Required by the compiler.
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.toString()) < 0);
        }
        runtimeError("Type error for <.", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.toString()) > 0);
        }
        runtimeError("Type error for >.", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.equals(v.toString()));
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.toString()) >= 0);
        }
        runtimeError("Type error for >=.", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.toString()) <= 0);
        }
        runtimeError("Type error for <=.", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue != v.toString());
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;  // Required by the compiler!
    }
}
