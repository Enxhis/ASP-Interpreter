package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {
    ArrayList<AspStmt> aStmtList = new ArrayList<>();
    AspSmallStmtList assl;

    public AspSuite(int n) {
        super(n);
    }

    public static AspSuite parse(Scanner s) {
        enterParser("suite");

        AspSuite aSuite = new AspSuite(s.curLineNum());

        if (s.curToken().kind == newLineToken) {
            skip(s, newLineToken);
            skip(s, indentToken);
            while (s.curToken().kind != dedentToken) {
                aSuite.aStmtList.add(AspStmt.parse(s));
            }
            skip(s, dedentToken);
        } else {
            aSuite.assl = AspSmallStmtList.parse(s);
        }
        leaveParser("suite");
        return aSuite;
    }

    @Override
    public void prettyPrint() {

        if (aStmtList.isEmpty()) {
            prettyWriteLn();
            assl.prettyPrint();
        } else {
            prettyWriteLn();
            prettyIndent();

            for (AspStmt as: aStmtList) {
                as.prettyPrint();
            }
            prettyDedent();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;

        if (assl != null) {
            v = assl.eval(curScope);
        } else {
            for (AspStmt as : aStmtList) {
                v = as.eval(curScope);
            }
        }
        return v;
    }
}
