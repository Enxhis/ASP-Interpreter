package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspStringLiteral extends AspAtom {
    String stringLit;

    public AspStringLiteral(int n) {
        super(n);
    }

    public static AspStringLiteral parse(Scanner s) {
        enterParser("string literal");

        AspStringLiteral aStrLit = new AspStringLiteral(s.curLineNum());
        aStrLit.stringLit = s.curToken().stringLit;
        skip(s, stringToken);

        leaveParser("string literal");
        return aStrLit;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("\"");
        prettyWrite(stringLit);
        prettyWrite("\"");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(stringLit);
    }
}
