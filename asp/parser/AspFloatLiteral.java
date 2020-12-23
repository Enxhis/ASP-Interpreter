package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom {
    double image;

    public AspFloatLiteral(int n) {
        super(n);
    }

    public static AspFloatLiteral parse(Scanner s) {
        enterParser("float literal");

        AspFloatLiteral aFloat = new AspFloatLiteral(s.curLineNum());
        aFloat.image = s.curToken().floatLit;
        skip(s, floatToken);

        leaveParser("float literal");
        return aFloat;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("" + image);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(image);
    }
}
