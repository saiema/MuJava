package arithmetic;

public class Smallest {

    public int smallest(int a, int b, int c, int d) {
        int smallest = 0;
        if (a < b && a < c && a < d)
                    smallest = a;
        if (b < a && b < c && b < d)
                    smallest = b;
        if (c < b && c < a && c < d)
                    smallest = c;
        if (d < b && d < c && d < a)
                    smallest = d;
        return smallest;
    } 

}
