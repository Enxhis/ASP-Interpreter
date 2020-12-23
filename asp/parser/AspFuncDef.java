package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspCompoundStmt {
    public ArrayList<AspName> aNameList = new ArrayList<>();
    public AspSuite aSuite;

    public AspFuncDef(int n) {
        super(n);
    }

    public static AspFuncDef parse(Scanner s) {
        enterParser("func def");

        skip(s, defToken);
        AspFuncDef funcdef = new AspFuncDef(s.curLineNum());
        funcdef.aNameList.add(AspName.parse(s));
        skip(s, leftParToken);

        if (s.curToken().kind == nameToken) {
            funcdef.aNameList.add(AspName.parse(s));
        }
        while (s.curToken().kind == commaToken) {
            skip(s, commaToken);
            funcdef.aNameList.add(AspName.parse(s));
        }
        skip(s, rightParToken);
        skip(s, colonToken);
        funcdef.aSuite = AspSuite.parse(s);

        leaveParser("func def");
        return funcdef;
    }

    @Override
    public void prettyPrint() {
        int i = 0;
        prettyWrite("def ");
        aNameList.get(i).prettyPrint();
        i++;
        prettyWrite(" (");

        if (aNameList.size() > 1) {
            aNameList.get(i).prettyPrint();
            i++;
            for (; i < aNameList.size(); i++) {
                prettyWrite(", ");
                aNameList.get(i).prettyPrint();
            }
        }
        prettyWrite(")");
        prettyWrite(":");
        aSuite.prettyPrint();
        prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("def " + aNameList.get(0).name);
        RuntimeFunc f = new RuntimeFunc(this, curScope);
        curScope.assign(aNameList.get(0).name, f);

        return null;
    }
}
