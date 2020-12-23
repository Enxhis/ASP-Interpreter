package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> aExprList = new ArrayList<>();
    ArrayList<AspSuite> aSuiteList = new ArrayList<>();
    AspSuite lastSuite;

    public AspIfStmt(int n) {
        super(n);
    }

    public static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ifstmt = new AspIfStmt(s.curLineNum());
        skip(s, ifToken);
        ifstmt.aExprList.add(AspExpr.parse(s));
        skip(s, colonToken);
        ifstmt.aSuiteList.add(AspSuite.parse(s));

        while (s.curToken().kind == elifToken) {
            skip(s, elifToken);
            ifstmt.aExprList.add(AspExpr.parse(s));
            skip(s, colonToken);
            ifstmt.aSuiteList.add(AspSuite.parse(s));
        }
        if (s.curToken().kind == elseToken) {
            skip(s, elseToken);
            skip(s, colonToken);
            ifstmt.lastSuite = AspSuite.parse(s);
        }
        leaveParser("if stmt");
        return ifstmt;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("if ");
        int nPrinted = 0;

        for (int i = 0; i < aExprList.size(); i++) {
            if (nPrinted > 0) {
                prettyWrite("elif ");
            }
            aExprList.get(i).prettyPrint();
            prettyWrite(":");
            aSuiteList.get(i).prettyPrint();
            nPrinted++;
        }

        if (lastSuite != null) {
            prettyWrite("else");
            prettyWrite(":");
            lastSuite.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        int i = 0;

        for (; i < aExprList.size(); i++) {
            v = aExprList.get(i).eval(curScope);
            if (v.getBoolValue("bool", this) == true) {
                trace("if True alt #" + (i+1) + ": ...");
                v = aSuiteList.get(i).eval(curScope);
                return v;
            }
        }
        if (lastSuite != null) {
            trace("else: ...");
            v = lastSuite.eval(curScope);
            return v;
        }
        return null;
    }
}
