package no.uio.ifi.asp.main;

import no.uio.ifi.asp.parser.AspExpr;
import no.uio.ifi.asp.parser.AspProgram;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Main {
    public static final String version = "2019-08-08";

    public static LogFile log = null;

    public static void main(String arg[]) {
        String fileName = null, baseFilename = null;
        boolean testExpr = false, testParser = false, testScanner = false,
        logE = false, logP = false, logS = false, logY = false;

        System.out.println("This is the IN2030 Asp interpreter (" +
        version + ")");
        System.out.println("Implemented by Rune Hille runehi@student.mn.uio.no and Enxhi Salaj enxhis@student.mn.uio.no");

        for (int i = 0;  i < arg.length;  i++) {
            String a = arg[i];

            if (a.equals("-logE")) {
                logE = true;
            } else if (a.equals("-logP")) {
                logP = true;
            } else if (a.equals("-logS")) {
                logS = true;
            } else if (a.equals("-logY")) {
                logY = true;
            } else if (a.equals("-testexpr")) {
                testExpr = true;
            } else if (a.equals("-testparser")) {
                testParser = true;
            } else if (a.equals("-testscanner")) {
                testScanner = true;
            } else if (a.startsWith("-")) {
                usage();
            } else if (fileName != null) {
                usage();
            } else {
                fileName = a;
            }
        }
        if (fileName == null) usage();

        baseFilename = fileName;
        if (baseFilename.endsWith(".asp"))
        baseFilename = baseFilename.substring(0,baseFilename.length()-4);
        else if (baseFilename.endsWith(".py"))
        baseFilename = baseFilename.substring(0,baseFilename.length()-3);

        log = new LogFile(baseFilename+".log");
        if (logE || testExpr) log.doLogEval = true;
        if (logP || testParser) log.doLogParser = true;
        if (logS || testScanner) log.doLogScanner = true;
        if (logY || testExpr || testParser) log.doLogPrettyPrint = true;

        Scanner s = new Scanner(fileName);
        if (testScanner)
        doTestScanner(s);
        else if (testParser)
        doTestParser(s);
        else if (testExpr)
        doTestExpr(s);
        else
        doRunInterpreter(s);

        if (log != null) log.finish();
        System.exit(0);
    }


    private static void doTestScanner(Scanner s) {
        do {
            s.readNextToken();
        } while (s.curToken().kind != eofToken);
    }


    private static void doTestParser(Scanner s) {
        AspProgram prog = AspProgram.parse(s);

        log.prettyWriteLn();
        prog.prettyPrint();
    }


    private static void doTestExpr(Scanner s) {
        RuntimeScope emptyScope = new RuntimeScope();

        while (s.curToken().kind != eofToken) {
            AspExpr e = AspExpr.parse(s);
            AspSyntax.skip(s, newLineToken);

            e.prettyPrint();  log.prettyWriteLn(" ==>");
            try {
                RuntimeValue res = e.eval(emptyScope);
                log.traceEval(res.showInfo(), e);
            } catch (RuntimeReturnValue rrv) {
                panic("Uncaught return exception!");
            }
        }
    }


    private static void doRunInterpreter(Scanner s) {
        AspProgram prog = AspProgram.parse(s);
        if (log.doLogPrettyPrint) {
            log.prettyWriteLn();
            prog.prettyPrint();
        }

        RuntimeScope lib = new RuntimeLibrary();
        RuntimeScope globals = new RuntimeScope(lib);
        try {
            prog.eval(globals);
        } catch (RuntimeReturnValue rrv) {
            panic("Uncaught return exception!");
        }
    }


    public static void error(String message) {
        System.out.println();
        System.err.println(message);
        if (log != null) log.noteError(message);
        System.exit(1);
    }


    public static void panic(String message, int lineNum) {
        String m = "*** ASP PANIC";
        if (lineNum > 0) m += " ON LINE " + lineNum;
        error(m + " ***: " + message);
    }


    public static void panic(String message) {
        panic(message, 0);
    }


    private static void usage() {
        error("Usage: java -jar asp.jar " +
        "[-log{E|P|S|Y}] [-test{expr|parser|scanner}] file");
    }
}
