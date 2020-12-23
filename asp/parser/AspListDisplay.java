package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> aExprList = new ArrayList<>();

    public AspListDisplay(int n) {
        super(n);
    }

    public static AspListDisplay parse(Scanner s) {
        enterParser("list display");

        AspListDisplay aListDisp = new AspListDisplay(s.curLineNum());
        skip(s, leftBracketToken);

        if (s.curToken().kind != rightBracketToken) {
            aListDisp.aExprList.add(AspExpr.parse(s));
            while (s.curToken().kind == commaToken) {
                skip(s, commaToken);
                aListDisp.aExprList.add(AspExpr.parse(s));
            }
        }
        skip(s, rightBracketToken);
        leaveParser("list display");
        return aListDisp;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("[");
        int nPrinted = 0;

        for (AspExpr ae: aExprList) {
            if (nPrinted > 0) {
                prettyWrite(", ");
            }
            ae.prettyPrint();
            nPrinted++;
        }
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> list = new ArrayList<>();

        if (aExprList.isEmpty()) {
            return new RuntimeListValue(list);
        }

        for (int i = 0; i < aExprList.size(); ++i) {
            list.add(aExprList.get(i).eval(curScope));
        }
    RuntimeValue v = new RuntimeListValue(list);
    return v;
    }
}
