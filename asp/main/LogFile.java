package no.uio.ifi.asp.main;

import java.io.*;

import no.uio.ifi.asp.parser.*;
import no.uio.ifi.asp.scanner.Token;

public class LogFile {
    public boolean doLogEval = false,
    doLogParser = false,
    doLogPrettyPrint = false,
    doLogScanner = false;

    private String logFileName;
    private int nLogLines = 0;
    private int parseLevel = 0;
    private String prettyLine = "";
    private int prettyIndentation = 0;

    public LogFile(String fname) {
        logFileName = fname;
    }


    public void finish() {
        if (prettyLine.length() > 0)
        prettyWriteLn();
    }


    private void writeLogLine(String data) {
        try {
            PrintWriter log =
            new PrintWriter(
            new OutputStreamWriter(
            new FileOutputStream(logFileName,nLogLines>0),
            "UTF-8"));
            log.println(data);  ++nLogLines;
            log.close();
        } catch (IOException e) {
            Main.error("Asp error: Cannot create log file "+logFileName+"!");
        }
    }

    /*
    * Make a note in the log file that an error has occured.
    * (If the log file is not in use, request is ignored.)
    *
    * @param message  The error message
    */
    public void noteError(String message) {
        if (nLogLines > 0)
        writeLogLine(message);
    }

    /**
    * Make a note in the log file that a source line has been read.
    * This note is only made if the user has requested appropriate logging.
    *
    * @param lineNum  The line number
    * @param line     The actual line
    */
    public void noteSourceLine(int lineNum, String line) {
        if (doLogParser || doLogScanner) {
            writeLogLine(String.format("%4d: %s",lineNum,line));
        }
    }


    /*
    * Make a note in the log file that another token has been read.
    * This note will only be made if the user has requested it.
    */
    public void noteToken(Token tok) {
        if (doLogScanner)
        writeLogLine("Scanner: " + tok.showInfo());
    }


    /**
    * Make a note of entering or leaving a parser method.
    *
    * @param nonterm  The non-terminal being parsed
    */
    public void enterParser(String nonTerm) {
        writeParseInfo(nonTerm);
        ++parseLevel;
    }

    public void leaveParser(String nonTerm) {
        --parseLevel;
        writeParseInfo("/" + nonTerm);
    }

    private void writeParseInfo(String nonTerm) {
        if (! doLogParser) return;

        String indent = "";
        for (int i = 1;  i <= parseLevel;  ++i)
        indent += "  ";
        writeLogLine(indent + "<" + nonTerm + ">");
    }


    public void traceEval(String message, AspSyntax what) {
        if (doLogEval)
        writeLogLine("Trace line "+what.lineNum+": "+message);
    }


    public void prettyWrite(String s) {
        if (prettyLine.equals("")) {
            for (int i = 1;  i <= prettyIndentation;  i++)
            prettyLine += "  ";
        }
        prettyLine += s;
    }

    public void prettyWriteLn(String s) {
        prettyWrite(s);  prettyWriteLn();
    }

    public void prettyWriteLn() {
        if (doLogPrettyPrint)
        writeLogLine(prettyLine);
        prettyLine = "";
    }

    public void prettyIndent() {
        prettyIndentation++;
    }

    public void prettyDedent() {
        prettyIndentation--;
    }
}
