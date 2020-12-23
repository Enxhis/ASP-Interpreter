package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {
    ArrayList<AspPrimarySuffix> aPriSufList = new ArrayList<>();
    AspAtom aAtom;

    public AspPrimary(int n) {
        super(n);
    }

    public static AspPrimary parse(Scanner s) {
        enterParser("primary");

        AspPrimary aPrimary = new AspPrimary(s.curLineNum());
        aPrimary.aAtom = AspAtom.parse(s);

        while (s.curToken().kind == leftBracketToken ||
               s.curToken().kind == leftParToken) {
            aPrimary.aPriSufList.add(AspPrimarySuffix.parse(s));
        }
        leaveParser("primary");
        return aPrimary;
    }

    @Override
    public void prettyPrint() {
        aAtom.prettyPrint();

        for (AspPrimarySuffix aps: aPriSufList) {
            aps.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aAtom.eval(curScope);
        if (!aPriSufList.isEmpty()) {
            for (AspPrimarySuffix aps: aPriSufList) {
                if (aps instanceof AspSubscription) {
                    RuntimeValue sub = aps.eval(curScope);
                    v = v.evalSubscription(sub, this);
                } else if (aps instanceof AspArguments) {
                    RuntimeListValue args = (RuntimeListValue) aps.eval(curScope);
                    trace("Call function " + v.toString() + " with params " + args.showInfo());
                    v = v.evalFuncCall(args.list, this);
                }
            }
        }
        return v;
    }
}
