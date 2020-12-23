package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspCompoundStmt {
    AspName aName;
    AspExpr aExpr;
    AspSuite aSuite;

    public AspForStmt(int n) {
        super(n);
    }

    public static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt forstmt = new AspForStmt(s.curLineNum());
        skip(s, forToken);
        forstmt.aName = AspName.parse(s);
        skip(s, inToken);
        forstmt.aExpr = AspExpr.parse(s);
        skip(s, colonToken);
        forstmt.aSuite = AspSuite.parse(s);

        leaveParser("for stmt");
        return forstmt;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        aName.prettyPrint();
        prettyWrite(" in ");
        aExpr.prettyPrint();
        prettyWrite(":");
        aSuite.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeListValue v = null;
        try {
            v = (RuntimeListValue) aExpr.eval(curScope);
        } catch (ClassCastException e) {
            RuntimeValue.runtimeError("For loop range is not a list!", this);
        }
        int i = 1;
        for (RuntimeValue rv : v.list) {
            trace("for #" + i + ": " + aName.name + " = " + rv.showInfo());
            curScope.assign(aName.name, rv);
            aSuite.eval(curScope);
            i++;
        }
        return v;
    }
}
