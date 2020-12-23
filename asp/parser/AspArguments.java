package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> aExprList = new ArrayList<>();

    public AspArguments(int n) {
        super(n);
    }

    public static AspArguments parse(Scanner s) {
        enterParser("arguments");

        skip(s, leftParToken);
        AspArguments aArgs = new AspArguments(s.curLineNum());

        if (s.curToken().kind != rightParToken) {
            while (true) {
                aArgs.aExprList.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);
        leaveParser("arguments");
        return aArgs;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("(");
        int i = 0;

        if (aExprList.size() > 0) {
            aExprList.get(i).prettyPrint();
            i++;
        }
        for (; i < aExprList.size(); i++) {
            prettyWrite(", ");
            aExprList.get(i).prettyPrint();
        }
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> alist = new ArrayList<>();

        for (AspExpr ae : aExprList) {
            alist.add(ae.eval(curScope));
        }
        RuntimeListValue v = new RuntimeListValue(alist);

        return v;
    }
}
