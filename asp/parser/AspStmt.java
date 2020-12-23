package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspStmt extends AspSyntax {

    public AspStmt(int n) {
        super(n);
    }

    public static AspStmt parse(Scanner s) {
        enterParser("stmt");

        AspStmt aspstmt = null;

        switch (s.curToken().kind) {
            case ifToken:
            case forToken:
            case defToken:
            case whileToken:
                aspstmt = AspCompoundStmt.parse(s);
                break;
            default:
                aspstmt = AspSmallStmtList.parse(s);
        }
        leaveParser("stmt");
        return aspstmt;
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
