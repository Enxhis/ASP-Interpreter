package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom {
    String image;

    public AspBooleanLiteral(int n) {
        super(n);
    }

    public static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");

        AspBooleanLiteral aBool = new AspBooleanLiteral(s.curLineNum());
        aBool.image = s.curToken().toString();

        if (s.curToken().kind == trueToken) {
            skip(s, trueToken);
        } else if (s.curToken().kind == falseToken) {
            skip(s, falseToken);
        }
        leaveParser("boolean literal");
        return aBool;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(image);
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        boolean b = (image == "True") ? true : false;
        return new RuntimeBoolValue(b);
    }
}
