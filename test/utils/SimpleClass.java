package utils;

/**
 * Simple class used only for testing purposes.
 * @author aguirre
 *
 */
public class SimpleClass {

        public int x;

        public SimpleClass() {
                x = 0;
        }

        /*@ requires x == 0;
          @ ensures \result >= 0;
          @*/
        public int getX() {
                return (x); 
        }
        
        /*@ requires newX >= 0;
        @ ensures x >= 0;
        @*/
      public void setX(int newX) {
              x = newX; //mutGenLimit 1
      }        

      /*@ requires newX >= 0;
      @ ensures \result >= 0;
      @*/
    public int decX(int newX) {
            return (newX-1); //mutGenLimit 1
    }

    /*@ requires newX >= 0;
    @ ensures \result == newX*2 + 1;
    @*/
  public static int twicePlusOne(int newX) {
	  	return (newX - 2)/1; //mutGenLimit 2 
  }
  
  /*@ requires newX >= 0;
  @ ensures \result == newX*2 + 1;
  @*/
	public static int altTwicePlusOne(int newX) {
		  	return (newX - 0)/1; //mutGenLimit 3 
	}

    
    
    
}
