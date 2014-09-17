package test;

public class PCI_3 extends PCI_2 {
	
	
	public void radiatedMethod() {
		PCI_3 pci3 = new PCI_3();
		PCI_1 pci1 = pci3; //mutGenLimit 1
		PCI_2 pci2 = new PCI_2();
		PCI_2 pci22 = pci2; //mutGenLimit 1
		someMethod(pci22); //mutGenLimit 1
		boolean someBool = someOtherMethod(pci3) && someOtherMethod(pci22) && someOtherMethod(pci1); //mutGenLimit 1
	}
	
	public void radiatedMethod2() {
		PCI_3 pci3 = new PCI_3(); //mutGenLimit 1
		PCI_2 pci2 = new PCI_2(); //mutGenLimit 1
		someMethod(new PCI_1()); //mutGenLimit 1
		boolean someBool = someOtherMethod(new PCI_1()) && someOtherMethod(new PCI_2()) && someOtherMethod(new PCI_3()); //mutGenLimit 1
	}
	
	public void someMethod(PCI_1 arg1) {
		
	}
	
	public boolean someOtherMethod(PCI_1 arg1) {
		return true;
	}
	

}
