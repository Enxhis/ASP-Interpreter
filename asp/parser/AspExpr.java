package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax {
    ArrayList<AspAndTest> andTestList = new ArrayList<>();

    public AspExpr(int n) {
        super(n);
    }

    public static AspExpr parse(Scanner s) {
        enterParser("expr");

        AspExpr ae = new AspExpr(s.curLineNum());
        ae.andTestList.add(AspAndTest.parse(s));

        while (s.curToken().kind == orToken) {
            skip(s, orToken);
            ae.andTestList.add(AspAndTest.parse(s));
        }
        leaveParser("expr");
        return ae;
    }


    @Override
    public void prettyPrint() {
        int nPrinted = 0;

        for (AspAndTest aat: andTestList) {
            if (nPrinted > 0) {
                prettyWrite(" or ");
            }
            aat.prettyPrint();
            ++nPrinted;
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = andTestList.get(0).eval(curScope);
        for (int i = 1; i < andTestList.size(); ++i) {
            if (v.getBoolValue("or operand", this))
                return v;
            v = andTestList.get(i).eval(curScope);
        }
	return v;
    }
}
