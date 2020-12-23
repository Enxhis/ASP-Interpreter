package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSubscription extends AspPrimarySuffix {
    AspExpr aExpr;

    public AspSubscription(int n) {
        super(n);
    }

    public static AspSubscription parse(Scanner s) {
        enterParser("subscription");

        AspSubscription aSub = new AspSubscription(s.curLineNum());
        skip(s, leftBracketToken);
        aSub.aExpr = AspExpr.parse(s);
        skip(s, rightBracketToken);

        leaveParser("subscription");
        return aSub;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("[");
        aExpr.prettyPrint();
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aExpr.eval(curScope);
        return v;
    }
}
