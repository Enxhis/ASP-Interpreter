package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspCompoundStmt extends AspStmt {

    public AspCompoundStmt(int n) {
        super(n);
    }

    public static AspCompoundStmt parse(Scanner s) {
        enterParser("compound stmt");

        AspCompoundStmt acs = null;

        switch (s.curToken().kind) {
            case ifToken:
                acs = AspIfStmt.parse(s);
                break;
            case forToken:
                acs = AspForStmt.parse(s);
                break;
            case defToken:
                acs = AspFuncDef.parse(s);
                break;
            case whileToken:
                acs = AspWhileStmt.parse(s);
                break;
            default:
                Main.error("Parser Error: AspCompoundStmt");
        }
        leaveParser("compound stmt");
        return acs;
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
