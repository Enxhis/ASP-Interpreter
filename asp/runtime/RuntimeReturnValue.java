package no.uio.ifi.asp.runtime;


public class RuntimeReturnValue extends Exception {
    public int lineNum;
    RuntimeValue value;

    public RuntimeReturnValue(RuntimeValue v, int lNum) {
	value = v;  lineNum = lNum;
    }
    
}
