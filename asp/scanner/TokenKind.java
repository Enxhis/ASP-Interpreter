package no.uio.ifi.asp.scanner;

import no.uio.ifi.asp.main.*;

public enum TokenKind {
    // Names and literals:
    nameToken("name"),
    integerToken("integer literal"),
    floatToken("float literal"),
    stringToken("string literal"),

    // Keywords:
    andToken("and"),
    asToken("as"),              // Not used in Asp
    assertToken("assert"),      // Not used in Asp
    breakToken("break"),        // Not used in Asp
    classToken("class"),        // Not used in Asp
    continueToken("continue"),  // Not used in Asp
    defToken("def"),
    delToken("del"),            // Not used in Asp
    elifToken("elif"),
    elseToken("else"),
    exceptToken("except"),      // Not used in Asp
    falseToken("False"),
    finallyToken("finally"),    // Not used in Asp
    forToken("for"),
    fromToken("from"),          // Not used in Asp
    globalToken("global"),      // Not used in Asp
    ifToken("if"),
    importToken("import"),      // Not used in Asp
    inToken("in"),
    isToken("is"),              // Not used in Asp
    lambdaToken("lambda"),      // Not used in Asp
    noneToken("None"),
    nonlocalToken("nonlocal"),  // Not used in Asp
    notToken("not"),
    orToken("or"),
    passToken("pass"),
    raiseToken("raise"),        // Not used in Asp
    returnToken("return"),
    trueToken("True"),
    tryToken("try"),            // Not used in Asp
    whileToken("while"),
    withToken("with"),          // Not used in Asp
    yieldToken("yield"),        // Not used in Asp

    // Operators:
    // ampToken("&"),
    astToken("*"),
    // barToken("|"),
    // doubleAstToken("**"),
    doubleEqualToken("=="),
    // doubleGreaterToken(">>"),
    // doubleLessToken("<<"),
    doubleSlashToken("//"),
    greaterToken(">"),
    greaterEqualToken(">="),
    // hatToken("^"),
    lessToken("<"),
    lessEqualToken("<="),
    minusToken("-"),
    notEqualToken("!="),
    percentToken("%"),
    plusToken("+"),
    slashToken("/"),
    // tildeToken("~"),

    // Delimiters:
    // ampEqualToken("&="),
    // astEqualToken("*="),
    // atToken("@"),
    // barEqualToken("|="),
    colonToken(":"),
    commaToken(","),
    // dotToken("."),
    // doubleAstEqualToken("**="),
    // doubleGreaterEqualToken(">>="),
    // doubleLessEqualToken("<<="),
    // doubleSlashEqualToken("//="),
    equalToken("="),
    // hatEqualToken("^="),
    leftBraceToken("{"),
    leftBracketToken("["),
    leftParToken("("),
    // minusEqualToken("-="),
    // percentEqualToken("%="),
    // plusEqualToken("+="),
    rightBraceToken("}"),
    rightBracketToken("]"),
    rightParToken(")"),
    semicolonToken(";"),
    // slashEqualToken("/="),

    // Format tokens:
    indentToken("INDENT"),
    dedentToken("DEDENT"),
    newLineToken("NEWLINE"),
    eofToken("E-o-f");

    String image;

    TokenKind(String s) {
	image = s;
    }

    public String toString() {
	return image;
    }
}
