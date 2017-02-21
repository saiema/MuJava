package arithmetic;

public class Smallest {

    public int smallest(int a, int b, int c, int d) {
        int smallest = 0;
        if (a < b && a < c && a < d) {
            smallest = a;
            smallest = a;
        }
        else if (b < a && b < c && b < d)
                    smallest = b;
        else if (c < b && c < a && c < d) {
                    smallest = c;
                    smallest = a;
        }
        else if (d < b && d < c && d < a)
                    smallest = d; //mutGenLimit 1
        else {
            smallest = a;
            smallest = a;
        }
        if (a > 0) {
        	a++;
        	a++;
        } else
        	a--;
        if (a < 0)
        	a++;
        else {
        	a++;
        	a++;
        }
        for (int i = 0; i < a; i++)
        	System.out.println(i);
        for (int i = 0; i < a; i++) {
        	System.out.println("hola");
        	System.out.println(i);
        }
        while (a > 0)
        	a--;
        while (b > 0) {
        	a++;
        	b++;
        }
        return smallest; //mutGenLimit 1
    } 

}
