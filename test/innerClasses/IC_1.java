package innerClasses;

public class IC_1 {
	
	
	public class IC_2 {
		
		
		public class IC_2_1 {
			
			public void radiatedMethod() {
				int i = 1;
				int j = i++; //mutGenLimit 1
			}
			
		}
		
		public void radiatedMethod() {
			int i = 1;
			int j = i++; //mutGenLimit 1
		}
		
	}
	
	
	public class IC_3 {
		
		
		public class IC_3_1 {
			
			public class IC_3_1_1 {
				
				public void radiatedMethod() {
					int i = 1;
					int j = i++; //mutGenLimit 1
				}
				
			}
			
			public class IC_3_1_2 {
				
				public class IC_3_1_2_1 {
					
					public void radiatedMethod() {
						int i = 1;
						int j = i++; //mutGenLimit 1
					}
					
				}
				
				public void radiatedMethod() {
					int i = 1;
					int j = i++; //mutGenLimit 1
				}
				
			}
			
			public void radiatedMethod() {
				int i = 1;
				int j = i++; //mutGenLimit 1
			}
			
		}
		
		
		public void radiatedMethod() {
			int i = 1;
			int j = i++; //mutGenLimit 1
		}
		
	}
	
	
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
	

}
