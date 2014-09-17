package main;

public class Gcd {
	
	/*@
	@ requires a >= 0 && b >= 0;
	@ ensures \result >= 0;
	@ ensures (\exists int q; \result*q == a) &&
	@ (\exists int q; \result*q == b) &&
	@ (\forall int c;
	@ (\exists int q; c*q == a) && (\exists int q; c*q == b);
	@ (\exists int q; c*q == \result));
	@*/
	public static int gcd(int a, int b) {

	  if (a == 0) {
		  return b;
	  }
	  else
	  {
	    while (b != 0) {
	      if (a > b) {
	        a = a - b;
	      } else {
	        b = b - a;
	      }
	    } //mutGenLimit 1
	    return a;
	  } //mutGenLimit 1
	} 
	 
	
	
}
