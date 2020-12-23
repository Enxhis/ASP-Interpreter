package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspReturnStmt extends AspSmallStmt {
    AspExpr aExpr;

    public AspReturnStmt(int n) {
        super(n);
    }

    public static AspReturnStmt parse(Scanner s) {
        enterParser("return stmt");

        AspReturnStmt aRet = new AspReturnStmt(s.curLineNum());
        skip(s, returnToken);
        aRet.aExpr = AspExpr.parse(s);

        leaveParser("return stmt");
        return aRet;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("return ");
        aExpr.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aExpr.eval(curScope);
        trace("return "+ v.showInfo());
        throw new RuntimeReturnValue(v,lineNum);
    }
}
