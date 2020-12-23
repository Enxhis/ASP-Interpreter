package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
        curFileName = fileName;
        indents.push(0);

        try {
            sourceFile = new LineNumberReader(
            new InputStreamReader(
            new FileInputStream(fileName),
            "UTF-8"));
        } catch (IOException e) {
            scannerError("Cannot read " + fileName + "!");
        }
    }


    private void scannerError(String message) {
        String m = "Asp scanner error";
        if (curLineNum() > 0)
        m += " on line " + curLineNum();
        m += ": " + message;

        Main.error(m);
    }


    public Token curToken() {
        while (curLineTokens.isEmpty()) {
            readNextLine();
        }
        return curLineTokens.get(0);
    }


    public void readNextToken() {
        if (! curLineTokens.isEmpty())
        curLineTokens.remove(0);
    }


    private void readNextLine() {
        curLineTokens.clear();

        // Read the next line:
        String line = null;
        try {
            line = sourceFile.readLine();
            if (line == null) {
                sourceFile.close();
                sourceFile = null;
            } else {
                Main.log.noteSourceLine(curLineNum(), line);
            }
        } catch (IOException e) {
            sourceFile = null;
            scannerError("Unspecified I/O error!");
        }

        // If EOF: add eofToken. If last line is indented, add dedentTokens.
        if (line == null) {
            for (int n: indents) {
                if (n > 0) {
                    curLineTokens.add(new Token(dedentToken,curLineNum()));
                }
            }
            curLineTokens.add(new Token(eofToken,curLineNum()));
            for (Token t: curLineTokens)
            Main.log.noteToken(t);
            return;
        }
        line = expandLeadingTabs(line);
        if (line == "" || line.charAt(0) == '#') {
            return;
        }
        determineIndentLevel(line);
        char[] lineChars = line.toCharArray();

        // Checking each character in the current line.
        int i = 0;
        while (i < lineChars.length) {
            if (lineChars[i] == ' ') {
                i++;
                continue;
            } else if (lineChars[i] == '#') {
                break;
            } else if (isLetterAZ(lineChars[i])) {
                i = handleName(lineChars, i);
            } else if ( isDigit(lineChars[i]) ) {
                i = handleNumber(lineChars, i);
            } else if (lineChars[i] == '"' || lineChars[i] == '\'') {
                i = handleStringLiteral(lineChars, i);
            } else {
                i = checkDelimitersAndOperators(lineChars, i);
            }
            i++;
        }

        // This means that the line consists only of space characters.
        if (curLineTokens.isEmpty()) {
            return;
        }
        curLineTokens.add(new Token(newLineToken,curLineNum()));

        for (Token t: curLineTokens)
        Main.log.noteToken(t);
    }

    /**
    * Checks if any delimiter or operator tokens match the chars currently being
    * read.
    * First it checks the tokens which contain two characters, then it checks
    * the tokens which contain one character. If nothing matches, then we have
    * an illegal character.
    *
    * @param   lineChars   the line currently being read.
    * @param   i           index of start character in token
    * @return              index after ending character in token.
    */
    private int checkDelimitersAndOperators(char[] lineChars, int i) {
        Token token = new Token(nameToken, curLineNum());
        token.name = "" + lineChars[i];
        for (TokenKind tk: EnumSet.range(astToken,semicolonToken)) {
            if ((i+1 < lineChars.length &&
            (token.name + lineChars[i+1]).equals(tk.image))) {
                token.kind = tk;
                i++;
                curLineTokens.add(token);
                return i++;
            }
        }
        for (TokenKind tk: EnumSet.range(astToken,semicolonToken)) {
            if (token.name.equals(tk.image)) {
                token.kind = tk;
                curLineTokens.add(token);
                return i;
            }
        }
        if (token.kind == nameToken) {
            String error = "Illegal character: \'" + lineChars[i] + "\'!";
            scannerError(error);
        }
        return i;
    }

    /**
    * Reads a number and creates integerToken or floatToken.
    * Is prompted by any digit 0 - 9.
    *
    * @param   lineChars   the line currently being read.
    * @param   i           index of start digit.
    * @return              index after ending digit.
    */
    private int handleNumber(char[] lineChars, int i) {
        String number = "";
        char start = lineChars[i];
        int startindex = i;

        do {
            if (lineChars[i] == '.' && number.contains(".")) {
                String error = "Illegal float literal: " + number + "!";
                scannerError(error);
            }
            number += lineChars[i];
            i++;
            if (i < lineChars.length && isDigit(lineChars[i]) && (start != '0' || number.contains("."))) {
                continue;
            } else if (i+1 < lineChars.length && lineChars[i] == '.' && isDigit(lineChars[i+1])) {
                continue;
            } else {
                break;
            }
        } while (i < lineChars.length);

        if (i < lineChars.length && lineChars[i] == '.') {
            number += lineChars[i];
        }

        if (number.endsWith(".")) {
            String error = "Illegal float literal: " + number + "!";
            scannerError(error);
        }

        Token token;
        if (number.contains(".")) {
            token = new Token(floatToken, curLineNum());
            token.floatLit = Double.parseDouble(number);
        } else {
            token = new Token(integerToken, curLineNum());
            token.integerLit = Long.parseLong(number);
        }
        curLineTokens.add(token);
        return startindex + number.length() - 1;
    }

    /**
    * Reads a name and creates name token or matching keyword token.
    * Is prompted by any letter.
    * Reads until next character is different from letter or digit.
    * Then a nameToken() or keyword token is created and added.
    *
    * @param   lineChars   the line currently being read.
    * @param   i           index of start character.
    * @return              index after ending character.
    */
    private int handleName(char[] lineChars, int i) {
        String word = "";
        while(i < lineChars.length && (isLetterAZ(lineChars[i]) ||
        isDigit(lineChars[i]))) {
            word += lineChars[i];
            i++;
        }
        Token token = new Token(nameToken, curLineNum());
        token.name = word;
        token.checkResWords();
        curLineTokens.add(token);
        i--;
        return i;
    }

    /**
    * Reads string literal and creates stringLit() token.
    * Is prompted by characters " or ', which begins a string literal.
    * Reads until another " or ', which marks the end of the string.
    * Then a stringLit() is created and added.
    *
    * @param   lineChars   the line currently being read.
    * @param   i           index of the opening quotation mark.
    * @return              index after the closing quotation mark.
    */
    private int handleStringLiteral(char[] lineChars, int i) {
        char start = lineChars[i];
        i++;
        if (i >= lineChars.length) {
            scannerError("String literal not terminated!");
        }
        String string = "";
        while(i < lineChars.length && lineChars[i] != start) {
            string += lineChars[i];
            i++;
        }

        if (i >= lineChars.length && lineChars[i-1] != start) {
            scannerError("String literal not terminated!");
        }
        Token token = new Token(stringToken, curLineNum());
        token.stringLit = string;
        curLineTokens.add(token);
        return i;
    }

    /**
    * Finds indent level of current line and adds appropriate Tokens.
    * If current line is more indented than the previous, then an indentToken
    * is added. If current line is dedented, then an appropriate number of
    * dedentTokens are added.
    *
    * @param   line    the line currently being read.
    */
    private void determineIndentLevel(String line) {
        int n = findIndent(line);

        if (n == line.length() || line.charAt(n) == '#') {
            return;
        }

        if (n > indents.peek()) {
            indents.push(n);
            curLineTokens.add(new Token(indentToken,curLineNum()));
        }
        while(n < indents.peek()) {
            indents.pop();
            curLineTokens.add(new Token(dedentToken,curLineNum()));
        }
        if (n != indents.peek()) {
            scannerError("Indentation error!");
        }
    }

    public int curLineNum() {
        return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
        int indent = 0;

        while (indent<s.length() && s.charAt(indent)==' ') indent++;
        return indent;
    }

    private String expandLeadingTabs(String s) {
        String newS = "";
        for (int i = 0;  i < s.length();  i++) {
            char c = s.charAt(i);
            if (c == '\t') {
                do {
                    newS += " ";
                } while (newS.length()%TABDIST > 0);
            } else if (c == ' ') {
                newS += " ";
            } else {
                newS += s.substring(i);
                break;
            }
        }
        return newS;
    }

    private boolean isLetterAZ(char c) {
        return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }

    private boolean isDigit(char c) {
        return '0'<=c && c<='9';
    }

    public boolean isCompOpr() {
        TokenKind k = curToken().kind;
        //-- Must be changed in part 2:
        return false;
    }

    public boolean isFactorPrefix() {
        TokenKind k = curToken().kind;
        //-- Must be changed in part 2:
        return false;
    }

    public boolean isFactorOpr() {
        TokenKind k = curToken().kind;
        //-- Must be changed in part 2:
        return false;
    }

    public boolean isTermOpr() {
        TokenKind k = curToken().kind;
        //-- Must be changed in part 2:
        return false;
    }

    public boolean anyEqualToken() {
        for (Token t: curLineTokens) {
            if (t.kind == equalToken) return true;
            if (t.kind == semicolonToken) return false;
        }
        return false;
    }
}
