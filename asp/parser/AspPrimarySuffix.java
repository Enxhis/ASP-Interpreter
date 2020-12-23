package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspPrimarySuffix extends AspSyntax {

    public AspPrimarySuffix(int n) {
        super(n);
    }

    public static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");

        AspPrimarySuffix aPrimSuf = null;

        if (s.curToken().kind == leftParToken) {
            aPrimSuf = AspArguments.parse(s);
        } else if (s.curToken().kind == leftBracketToken) {
            aPrimSuf = AspSubscription.parse(s);
        }
        leaveParser("primary suffix");
        return aPrimSuf;
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
