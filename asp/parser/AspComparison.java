package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspComparison extends AspSyntax {
    ArrayList<AspTerm> aspTermsList = new ArrayList<>();
    ArrayList<AspCompOpr> aspCompOprsList = new ArrayList<>();

    public AspComparison(int n) {
        super(n);
    }

    public static AspComparison parse(Scanner s) {
        enterParser("comparison");

        AspComparison aComp = new AspComparison(s.curLineNum());
        aComp.aspTermsList.add(AspTerm.parse(s));
        Boolean isComOpr = true;

        while (isComOpr) {
            switch (s.curToken().kind) {
                case lessToken:
                case greaterToken:
                case doubleEqualToken:
                case greaterEqualToken:
                case lessEqualToken:
                case notEqualToken:
                    aComp.aspCompOprsList.add(AspCompOpr.parse(s));
                    aComp.aspTermsList.add(AspTerm.parse(s));
                    break;
                default:
                    isComOpr = false;
                    break;
            }
        }
        leaveParser("comparison");
        return aComp;
    }

    @Override
    public void prettyPrint() {
        int k = 0;
        int i = 0;
        aspTermsList.get(k).prettyPrint();
        k++;

        while(k <= aspCompOprsList.size()) {
            aspCompOprsList.get(i).prettyPrint();
            aspTermsList.get(k).prettyPrint();
            i++;
            k++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aspTermsList.get(0).eval(curScope);
        RuntimeValue vTmp = v;

        for (int i = 1; i < aspTermsList.size(); i++) {
            RuntimeValue v2 = aspTermsList.get(i).eval(curScope);

            switch (aspCompOprsList.get(i-1).image) {
                case "<":
                vTmp = v.evalLess(v2, this);
                break;
                case ">":
                vTmp = v.evalGreater(v2, this);
                break;
                case "==":
                vTmp = v.evalEqual(v2, this);
                break;
                case ">=":
                vTmp = v.evalGreaterEqual(v2, this);
                break;
                case "<=":
                vTmp = v.evalLessEqual(v2, this);
                break;
                case "!=":
                vTmp = v.evalNotEqual(v2,this);
                break;
            }
        }
        return vTmp;
    }
}
