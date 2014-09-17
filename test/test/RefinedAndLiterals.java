package test;

public class RefinedAndLiterals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		radiatedMethod(args);
	}
	
	public static void radiatedMethod(String[] args) {
		double  width,range,center,bias;
		double result;
		int number_parameters = args.length;
	    width  = ( number_parameters >= 1 ) ? Double.valueOf(args[0]) : 1.0;
	    range  = ( number_parameters >= 3 ) ? Double.valueOf(args[1]) : 1.0; //mutGenLimit 1
	    center = ( number_parameters >= 2 ) ? Double.valueOf(args[2]) : 0.5;
	    bias   = ( number_parameters >= 4 ) ? Double.valueOf(args[3]) : 0.5;
	    result = 2.0/width*(range*bias - center);
	    System.out.println(result);
	}

}
