package sort;

import utils.IntWrapper;

public class Sorting {
	
	
	public static void main(String[] args) {
		IntWrapper a = new IntWrapper(2);
		IntWrapper b = new IntWrapper(3);
		int num1 = 1;
		num1 = -num1;  //mutGenLimit 1
		System.out.println("Before swap : a = " + a + " | b = " + b +'\n');
		swap(a,b);
		System.out.println("After swap : a = " + a + " | b = " + b +'\n');
	}
	
	public static int[] selectionSort(int[] data){
		int lenD = data.length;
		int j = 0;
		int tmp = 0;
		for(int i=0;i<lenD;i++){
			j = i;
			for(int k = i;k<lenD;k++) {  
				if(data[j]>data[k]){
					j = k;
				}
			}
			tmp = data[i];
			data[i] = data[j];
			data[j] = tmp;
		}
		return data;
	}
	
	public static int[] mergeSort(int[] data){
		int lenD = data.length;
		if(lenD<=1){
			return data;
		}
		else{
			int[] sorted = new int[lenD];
			int middle = lenD/2;
			int rem = lenD-middle;
			int[] L = new int[middle];
			int[] R = new int[rem];
			System.arraycopy(data, 0, L, 0, middle);
			System.arraycopy(data, middle, R, 0, rem);
			L = mergeSort(L);
			R = mergeSort(R);
			sorted = merge(L, R);
			return sorted;
		}
	}

	private static int[] merge(int[] L, int[] R){
		int lenL = L.length;
		int lenR = R.length;
		int[] merged = new int[lenL+lenR];
		int i = 0;
		int j = 0;
		while(i<lenL||j<lenR){ //mutGenLimit 1
			if(i<lenL & j<lenR){
				if(L[i]<=R[j]){
					merged[i+j] = L[i];
					i++; //mutGenLimit 1
				}
				else{
					merged[i+j] = R[j];
					j++; //mutGenLimit 1
				}
			}
			else if(i<lenL){
				merged[i+j] = L[i];
				i++; //mutGenLimit 0
			}
			else if(j<lenR){
				merged[i+j] = R[j];
				j++; //mutGenLimit 0
			}
		}
		return merged;
	}
	
	public boolean ordered(int[] arr, boolean inc) {
		boolean isOrdered = true;
		if (arr.length > 0) {
			int lastVal = arr[0];
			for (int v = 1 ; v < arr.length ; v++) { //mutGenLimit 1
				if (inc && lastVal > v) {
					isOrdered = false;
				}
				if (!inc && lastVal < v) {
					isOrdered = false;
				}
				if (!isOrdered) break;
				lastVal = v;
			}
		}
		return isOrdered;
	}
	
	public static void swap(IntWrapper num1, IntWrapper num2) {
		/*
		 * num1 = num1 + num2;
           num2 = num1 - num2;
           num1 = num1 - num2;
		 */
		int a = num1.value();
		int b = num2.value();
		Integer auxB = -b; //mutGenLimit 1
		Integer auxX = -a; //mutGenLimit 0
		a = a - auxB; //mutGenLimit 0
		b = a + auxB;
		auxB = b;
		a = a + -auxB; //mutGenLimit 1
		num1.setValue(a);
		num2.setValue(b);
	}

	
	public static void swap2(IntWrapper num1, IntWrapper num2) {
		/*
		 * num1 = num1 + num2;
           num2 = num1 - num2;
           num1 = num1 - num2;
		 */
		int a = num1.value();
		int b = num2.value();
		Integer auxB = -+b; //mutGenLimit 1
		Integer auxX = -a; //mutGenLimit 0
		a = a - auxB; //mutGenLimit 0
		b = a + auxB;
		auxB = +b; //mutGenLimit 1
		a = a + -auxB; //mutGenLimit 1
		num1.setValue(a);
		num2.setValue(b);
	}

}
