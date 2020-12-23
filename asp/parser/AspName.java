package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspName extends AspAtom {
    public String name;

    public AspName(int n) {
        super(n);
    }

    public static AspName parse(Scanner s) {
        enterParser("name");

        AspName aName = new AspName(s.curLineNum());
        aName.name = s.curToken().name;
        skip(s, nameToken);

        leaveParser("name");
        return aName;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(this.name);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(name, this);
    }
}
