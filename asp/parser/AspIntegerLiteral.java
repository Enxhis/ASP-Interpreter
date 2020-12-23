package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIntegerLiteral extends AspAtom {
    long image;

    public AspIntegerLiteral(int n) {
        super(n);
    }

    public static AspIntegerLiteral parse(Scanner s) {
        enterParser("integer literal");

        AspIntegerLiteral aInt = new AspIntegerLiteral(s.curLineNum());
        aInt.image = s.curToken().integerLit;
        skip(s, integerToken);

        leaveParser("integer literal");
        return aInt;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("" + image);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeIntValue v = new RuntimeIntValue(image);
        return v;
    }
}
