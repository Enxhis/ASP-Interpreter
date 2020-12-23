package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorOpr extends AspSyntax {
    String image;

    public AspFactorOpr(int n) {
        super(n);
    }

    public static AspFactorOpr parse(Scanner s) {
        enterParser("factor opr");

        AspFactorOpr aFacOpr = new AspFactorOpr(s.curLineNum());
        aFacOpr.image = s.curToken().toString();

        switch (s.curToken().kind) {
            case astToken:
            skip(s, astToken);
            break;
            case slashToken:
            skip(s, slashToken);
            break;
            case percentToken:
            skip(s, percentToken);
            break;
            case doubleSlashToken:
            skip(s, doubleSlashToken);
            break;
        }
        leaveParser("factor opr");
        return aFacOpr;
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
