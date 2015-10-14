package bugHunting;

public class ForReusingVars {
	
	
	public void m() {
		int i, j, k;
		for (i = 0, j = 1, k = 2; i + j + k < 10; i++, j++, k++) {
			
		}
	}

}
