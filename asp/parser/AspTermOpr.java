package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTermOpr extends AspSyntax {
    String image;

    public AspTermOpr(int n) {
        super(n);
    }

    public static AspTermOpr parse(Scanner s) {
        enterParser("term opr");

        AspTermOpr aTermOpr = new AspTermOpr(s.curLineNum());

        switch (s.curToken().kind) {
            case plusToken:
            case minusToken:
            aTermOpr.image = s.curToken().toString();
            skip(s, s.curToken().kind);
        }
        leaveParser("term opr");
        return aTermOpr;
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
