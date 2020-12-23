package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {
    AspComparison acomp;
    Boolean not = false;

    public AspNotTest(int n) {
        super(n);
    }

    public static AspNotTest parse(Scanner s) {
        enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());

        if (s.curToken().kind == notToken) {
            ant.not = true;
            skip(s, notToken);
        }
        ant.acomp = AspComparison.parse(s);

        leaveParser("not test");
        return ant;
    }
    @Override
    public void prettyPrint() {
        if (not) {
            prettyWrite(" not ");
        }
        acomp.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = acomp.eval(curScope);
        if (not) {
            v = v.evalNot(this);
        }
        return v;
    }
}
