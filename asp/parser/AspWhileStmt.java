package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspWhileStmt extends AspCompoundStmt {
    AspExpr aExpr;
    AspSuite aSuite;

    public AspWhileStmt(int n) {
        super(n);
    }

    public static AspWhileStmt parse(Scanner s) {
        enterParser("while stmt");

        AspWhileStmt whilestmt = new AspWhileStmt(s.curLineNum());
        skip(s, whileToken);
        whilestmt.aExpr = AspExpr.parse(s);
        skip(s, colonToken);
        whilestmt.aSuite = AspSuite.parse(s);

        leaveParser("while stmt");
        return whilestmt;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("while ");
        aExpr.prettyPrint();
        prettyWrite(":");
        aSuite.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while (true) {
            RuntimeValue t = aExpr.eval(curScope);
            if (! t.getBoolValue("while loop test",this)) break;
            trace("while True: ...");
            aSuite.eval(curScope);
        }
        trace("while False:");
        return null;
    }
}
