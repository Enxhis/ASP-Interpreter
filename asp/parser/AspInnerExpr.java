package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspInnerExpr extends AspAtom {
    AspExpr aExpr;

    public AspInnerExpr(int n) {
        super(n);
    }

    public static AspInnerExpr parse(Scanner s) {
        enterParser("inner expr");

        AspInnerExpr aInnerExpr = new AspInnerExpr(s.curLineNum());
        skip(s, leftParToken);
        aInnerExpr.aExpr = AspExpr.parse(s);
        skip(s, rightParToken);

        leaveParser("inner expr");
        return aInnerExpr;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("(");
        aExpr.prettyPrint();
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return aExpr.eval(curScope);
    }
}
