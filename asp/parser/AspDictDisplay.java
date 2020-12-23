package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.HashMap;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspDictDisplay extends AspAtom {
    ArrayList<AspStringLiteral> aStringLitList = new ArrayList<>();
    ArrayList<AspExpr> aExprList = new ArrayList<>();

    public AspDictDisplay(int n) {
        super(n);
    }

    public static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay aDict = new AspDictDisplay(s.curLineNum());
        skip(s, leftBraceToken);

        if (s.curToken().kind != rightBraceToken) {
            aDict.aStringLitList.add(AspStringLiteral.parse(s));
            skip(s, colonToken);
            aDict.aExprList.add(AspExpr.parse(s));
        }
        while (s.curToken().kind == commaToken) {
            skip(s, commaToken);
            aDict.aStringLitList.add(AspStringLiteral.parse(s));
            skip(s, colonToken);
            aDict.aExprList.add(AspExpr.parse(s));
        }
        skip(s, rightBraceToken);

        leaveParser("dict display");
        return aDict;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("{");
        int nPrinted = 0;

        for (int i = 0; i < aStringLitList.size(); i++) {
            if (nPrinted > 0) {
                prettyWrite(", ");
            }
            aStringLitList.get(i).prettyPrint();
            prettyWrite(":");
            aExprList.get(i).prettyPrint();
            nPrinted++;
        }
        prettyWrite("}");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        HashMap<String, RuntimeValue> dict = new HashMap<String, RuntimeValue>();
        for (int i = 0; i < aStringLitList.size(); i++) {
            RuntimeValue key = aStringLitList.get(i).eval(curScope);
            RuntimeValue value = aExprList.get(i).eval(curScope);
            dict.put(key.toString(), value);
        }
        return new RuntimeDictValue(dict);
    }
}
