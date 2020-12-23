package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspCompOpr extends AspSyntax {
    String image;

    public AspCompOpr(int n) {
        super(n);
    }

    public static AspCompOpr parse(Scanner s) {
        enterParser("comp opr");

        AspCompOpr aCmpOpr = new AspCompOpr(s.curLineNum());
        aCmpOpr.image = s.curToken().toString();
        skip(s, s.curToken().kind);

        leaveParser("comp opr");
        return aCmpOpr;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(" " + image + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
