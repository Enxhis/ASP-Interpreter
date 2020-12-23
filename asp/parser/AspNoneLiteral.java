package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNoneLiteral extends AspAtom {
    String image;

    public AspNoneLiteral(int n) {
        super(n);
    }

    public static AspNoneLiteral parse(Scanner s) {
        enterParser("none literal");

        AspNoneLiteral aNone = new AspNoneLiteral(s.curLineNum());
        aNone.image = s.curToken().toString();
        skip(s, noneToken);

        leaveParser("none literal");
        return aNone;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(image);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeNoneValue();
    }
}
