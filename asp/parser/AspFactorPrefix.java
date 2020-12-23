package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorPrefix extends AspSyntax {
    String image;

    public AspFactorPrefix(int n) {
        super(n);
    }

    public static AspFactorPrefix parse(Scanner s) {
        enterParser("factor prefix");

        AspFactorPrefix aFacPrfx = new AspFactorPrefix(s.curLineNum());
        aFacPrfx.image = s.curToken().toString();

        switch (s.curToken().kind) {
            case plusToken:
            skip(s, plusToken);
            break;
            case minusToken:
            skip(s, minusToken);
            break;
        }
        leaveParser("factor prefix");
        return aFacPrfx;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(image + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
