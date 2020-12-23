package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // len
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }});
        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                for (int i = 0;  i < actualParams.size();  ++i) {
                    if (i > 0) System.out.print(" ");
                    System.out.print(actualParams.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }});
        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.println(actualParams.get(0).toString());
                String input = keyboard.nextLine();
                RuntimeValue v = new RuntimeStringValue(input);
                return v;
            }});
        // float
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                Double d = actualParams.get(0).getFloatValue("float", where);
                RuntimeValue v = new RuntimeFloatValue(d);
                return v;
            }});
        // int
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                long l = actualParams.get(0).getIntValue("int", where);
                RuntimeValue v = new RuntimeIntValue(l);
                return v;
            }});
        // str
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                String s = actualParams.get(0).toString();
                RuntimeValue v = new RuntimeStringValue(s);
                return v;
            }});
        // range
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                ArrayList<RuntimeValue> numbers = new ArrayList<>();
                long start = actualParams.get(0).getIntValue("int", where);
                long end = actualParams.get(1).getIntValue("int", where);
                for (long i = start; i < end; i++) {
                    RuntimeValue v = new RuntimeIntValue(i);
                    numbers.add(v);
                }
                RuntimeValue list = new RuntimeListValue(numbers);
                return list;
            }});
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
    int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
        RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
