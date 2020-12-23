package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public abstract class AspSyntax {
    public int lineNum;

    AspSyntax(int n) {
        lineNum = n;
    }


    abstract void prettyPrint();
    abstract RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue;


    static void parserError(String message, int lineNum) {
        String m = "Asp parser error";
        if (lineNum > 0) m += " on line " + lineNum;
        m += ": " + message;
        Main.error(m);
    }


    public static void test(Scanner s, TokenKind tk) {
        if (s.curToken().kind != tk)
        parserError("Expected " + tk + " but found " +
        s.curToken().kind + "!", s.curLineNum());
    }


    public static void test(Scanner s, TokenKind tk1, TokenKind tk2) {
        if (s.curToken().kind!=tk1 && s.curToken().kind!=tk2)
        parserError("Expected " + tk1 + " or " + tk2 + " but found " +
        s.curToken().kind + "!", s.curLineNum());
    }


    public static void skip(Scanner s, TokenKind tk) {
        test(s, tk);
        s.readNextToken();
    }


    protected static void enterParser(String nonTerm) {
        Main.log.enterParser(nonTerm);
    }

    protected static void leaveParser(String nonTerm) {
        Main.log.leaveParser(nonTerm);
    }


    protected static void prettyDedent() {
        Main.log.prettyDedent();
    }

    protected static void prettyIndent() {
        Main.log.prettyIndent();
    }

    protected static void prettyWrite(String s) {
        Main.log.prettyWrite(s);
    }

    protected static void prettyWriteLn() {
        Main.log.prettyWriteLn();
    }

    protected static void prettyWriteLn(String s) {
        Main.log.prettyWriteLn(s);
    }


    void trace(String what) {
        Main.log.traceEval(what, this);
    }
}
