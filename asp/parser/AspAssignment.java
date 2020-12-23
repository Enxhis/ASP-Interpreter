package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspSmallStmt {
    ArrayList<AspSubscription> aSubList = new ArrayList<>();
    AspName aname;
    AspExpr aexp;

    public AspAssignment(int n) {
        super(n);
    }

    public static AspAssignment parse(Scanner s) {
        enterParser("assignment");

        AspAssignment aAssgn = new AspAssignment(s.curLineNum());
        aAssgn.aname = AspName.parse(s);

        while (s.curToken().kind == leftBracketToken) {
            aAssgn.aSubList.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        aAssgn.aexp = AspExpr.parse(s);

        leaveParser("assignment");
        return aAssgn;
    }

    @Override
    public void prettyPrint() {
        aname.prettyPrint();

        for (AspSubscription as: aSubList) {
            as.prettyPrint();
        }
        prettyWrite(" = ");
        aexp.prettyPrint();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue expr = aexp.eval(curScope);
        if (aSubList.size() != 0) {
            RuntimeValue list = aname.eval(curScope);
            RuntimeValue index = aSubList.get(0).eval(curScope);;

            for (int i = 1; i < aSubList.size(); i++) {
                list = list.evalSubscription(index, this);
                index = aSubList.get(i).eval(curScope);
            }
            trace(aname.name + "[" + index.showInfo() + "]" + " = " + expr.showInfo());
            list.evalAssignElem(index, expr, this);
        } else {
            curScope.assign(aname.name, expr);
            trace(aname.name + " = " + expr.showInfo());
        }
        return expr;
    }
}
