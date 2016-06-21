package utils;


public class BooleanOps {

    /**
    * Constructor comments
    * this is a comment
    */
    public BooleanOps() {
    }

    public static boolean and( boolean a, boolean b ) {
        boolean auxA = a; //mutGenLimit 3
        boolean auxB = b; //mutGenLimit 1
        if (!auxA || !auxB) { //mutGenLimit 1
            return false; //mutGenLimit 1
        }
        return true; //mutGenLimit 1
    }

    public static boolean or( boolean a, boolean b ) {
        boolean auxA = a; //mutGenLimit 1
        boolean auxB = b; //mutGenLimit 1
        return !(!auxA && !auxB); //mutGenLimit 1
    }

    public static boolean xor( boolean a, boolean b ) {
        boolean auxA = a; //mutGenLimit 1
        boolean auxB = b; //mutGenLimit 1
        boolean allTrue = !(!auxA || !auxB); //mutGenLimit 1
        boolean allFalse = !auxA && !auxB; //mutGenLimit 2
        return !(allTrue || allFalse); //mutGenLimit 1
    }

    public static boolean xnor( boolean a, boolean b ) {
        boolean auxA = a; //mutGenLimit 1
        boolean auxB = b; //mutGenLimit 1
        boolean isXnor = !(!auxA || !auxB) || !auxA && !auxB; //mutGenLimit 2
        return isXnor; //mutGenLimit 1
    }

    public java.lang.String toString() {
        return "";
    }

}
