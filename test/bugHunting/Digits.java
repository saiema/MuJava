package bugHunting;

import java.lang.System;

class Digits{

	public char[] digits(char [] digit){
		int c, i,flag2 =0;
		int flag1=0;

		c = digit.length;
		char [] res = new char[c];
		System.out.print("\n");

		for (i=1;i<c;i++) { //mutGenLimit 1
			if(digit[i]=='-'){
				flag1=i;
				break;
			}
			else flag1=c; 
		}

		if(digit[0]=='-') flag2=1;
		int j=0;  

		for(i=flag1-1; i>=flag2; i--){ 
			if(flag2==1 && i==1){
				res[j] = '-';
				j++;
				System.out.print("-");
			}

			res[j] = digit[i];
			System.out.println(res[j]);
			j++;    
		}

		System.out.print("That's all, have a nice day!");
		return res; //mutGenLimit 1

	}  
}
