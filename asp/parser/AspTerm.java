package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTerm extends AspSyntax {
    ArrayList<AspFactor> aFactorList = new ArrayList<>();
    ArrayList<AspTermOpr> aTermOprList = new ArrayList<>();

    public AspTerm(int n) {
        super(n);
    }

    public static AspTerm parse(Scanner s) {
        enterParser("term");

        AspTerm aterm = new AspTerm(s.curLineNum());
        aterm.aFactorList.add(AspFactor.parse(s));
        Boolean isTermOpr = true;

        while (isTermOpr) {
            switch (s.curToken().kind) {
                case plusToken:
                case minusToken:
                aterm.aTermOprList.add(AspTermOpr.parse(s));
                aterm.aFactorList.add(AspFactor.parse(s));
                break;
                default:
                isTermOpr = false;
                break;
            }
        }
        leaveParser("term");
        return aterm;
    }

    @Override
    public void prettyPrint() {
        int k = 0;
        int i = 0;
        aFactorList.get(k).prettyPrint();
        k++;

        while(k <= aTermOprList.size()) {
            aTermOprList.get(i).prettyPrint();
            aFactorList.get(k).prettyPrint();
            i++;
            k++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aFactorList.get(0).eval(curScope);
        for (int i = 1; i < aFactorList.size(); i++) {
            RuntimeValue v2 = aFactorList.get(i).eval(curScope);
            if (aTermOprList.get(i-1).image == "+") {
                v = v.evalAdd(v2, this);
            } else if (aTermOprList.get(i-1).image == "-") {
                v = v.evalSubtract(aFactorList.get(i).eval(curScope), this);
            }
        }
        return v;
    }
}
