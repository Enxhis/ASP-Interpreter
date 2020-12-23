package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactor extends AspSyntax {
    ArrayList<AspPrimary> aPrimaryList = new ArrayList<>();
    ArrayList<AspFactorPrefix> aFactorPrfxList = new ArrayList<>();
    ArrayList<AspFactorOpr> aFactorOprList = new ArrayList<>();
    Boolean prefix = false;


    public AspFactor(int n) {
        super(n);
    }

    public static AspFactor parse(Scanner s) {
        enterParser("factor");

        AspFactor aFactor = new AspFactor(s.curLineNum());

        if (s.curToken().kind == plusToken || s.curToken().kind == minusToken) {
            aFactor.aFactorPrfxList.add(AspFactorPrefix.parse(s));
            aFactor.prefix = true;
        } else {
            aFactor.aFactorPrfxList.add(null);
        }
        aFactor.aPrimaryList.add(AspPrimary.parse(s));

        while (true) {
            if (s.curToken().kind == astToken || s.curToken().kind == slashToken ||
            s.curToken().kind == percentToken || s.curToken().kind == doubleSlashToken) {
                aFactor.aFactorOprList.add(AspFactorOpr.parse(s));

                if (s.curToken().kind == plusToken || s.curToken().kind == minusToken) {
                    aFactor.aFactorPrfxList.add(AspFactorPrefix.parse(s));
                    aFactor.aPrimaryList.add(AspPrimary.parse(s));
                } else {
                    aFactor.aFactorPrfxList.add(null);
                    aFactor.aPrimaryList.add(AspPrimary.parse(s));
                }
            } else {
                break;
            }
        }
        leaveParser("factor");
        return aFactor;
    }

    @Override
    public void prettyPrint() {
        int i = 0;
        int j = 0;
        int k = 0;

        if (prefix) {
            aFactorPrfxList.get(i).prettyPrint();
        }
        i++;
        aPrimaryList.get(j).prettyPrint();
        j++;

        while(j <= aFactorOprList.size()) {
            aFactorOprList.get(k).prettyPrint();

            if (aFactorPrfxList.get(i) != null) {
                aFactorPrfxList.get(i).prettyPrint();
            }
            aPrimaryList.get(j).prettyPrint();
            i++;
            j++;
            k++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = aPrimaryList.get(0).eval(curScope);
        if (aFactorPrfxList.get(0) != null) {
            switch (aFactorPrfxList.get(0).image) {
                case "+":
                v = v.evalPositive(this);
                break;
                case "-":
                v = v.evalNegate(this);
                break;
            }
        }
        for (int i = 0; i < aFactorOprList.size(); i++) {
            RuntimeValue v2 = aPrimaryList.get(i+1).eval(curScope);
            if (aFactorPrfxList.get(i+1) != null) {
                switch (aFactorPrfxList.get(i+1).image) {
                    case "+":
                    v2 = v2.evalPositive(this);
                    break;
                    case "-":
                    v2 = v2.evalNegate(this);
                    break;
                }
            }
            switch (aFactorOprList.get(i).image) {
                case "*":
                v = v.evalMultiply(v2, this);
                break;
                case "/":
                v = v.evalDivide(v2, this);
                break;
                case "%":
                v = v.evalModulo(v2, this);
                break;
                case "//":
                v = v.evalIntDivide(v2, this);
                break;
            }
        }
        return v;
    }
}
