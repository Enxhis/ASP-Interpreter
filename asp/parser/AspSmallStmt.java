package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspSmallStmt extends AspStmt {

    public AspSmallStmt(int n) {
        super(n);
    }

    public static AspSmallStmt parse(Scanner s) {
        enterParser("small stmt");

        AspSmallStmt ass = null;

        if (s.anyEqualToken()) {
            ass = AspAssignment.parse(s);
        } else if (s.curToken().kind == passToken) {
            ass = AspPassStmt.parse(s);
        } else if (s.curToken().kind == returnToken) {
            ass = AspReturnStmt.parse(s);
        } else {
            ass = AspExprStmt.parse(s);
        }
        leaveParser("small stmt");
        return ass;
    }

    @Override
    public void prettyPrint() {
        this.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = this.eval(curScope);
        return v;
    }
}
