package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> smallStmtList = new ArrayList<>();
    Boolean lastSemiCol = false;

    public AspSmallStmtList(int n) {
        super(n);
    }

    public static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");

        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
        assl.smallStmtList.add(AspSmallStmt.parse(s));

        while (s.curToken().kind != newLineToken) {
            skip(s, semicolonToken);
            if (s.curToken().kind == newLineToken) {
                break;
            } else {
                assl.smallStmtList.add(AspSmallStmt.parse(s));
            }
        }
        skip(s, newLineToken);

        leaveParser("small stmt list");
        return assl;
    }

    @Override
    public void prettyPrint() {
        int i = 0;
        smallStmtList.get(i).prettyPrint();
        i++;

        for (; i < smallStmtList.size(); i++) {
            prettyWrite("; ");
            smallStmtList.get(i).prettyPrint();
        }
        prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        for (AspSmallStmt s : smallStmtList) {
            v = s.eval(curScope);
        }
        return v;
    }
}
