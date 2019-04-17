package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.Configuration;
import mujava.api.MutationOperator;
import mujava.app.MutantInfo;
import mujava.op.PRVO;
import mujava.op.util.MutantCodeWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class PRVOTests {
	
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public PRVOTests(Property p, List<MutantInfo> mutantsInfo) {
		this.prop = p;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	/*
	 * TESTS PARA AGREGAR(prvo):
	 * 1 - asignación con parte derecha null 							//LISTO
	 * 2 - asignación con parte izquierda x.c con c un campo de c		//LISTO		
	 * 3 - asignación con una variable como parte derecha				//LISTO
	 * 4 - asignación con un método como parte derecha
	 * 5 - mutación dentro del cuerpo un for							//LISTO
	 * 6 - mutación dentro del cuerpo de un while						//LISTO
	 * 7 - mutación dentro de un try
	 * 8 - mutación dentro de un switch									//LISTO
	 */

	
	@Parameters
	public static Collection<Object[]> firstValues() {
		TestingTools.setVerbose(true);
		/*
		 * ORIGINAL
		 * current = this.header.next;
		 */
		List<Pattern> mceSLLBE_right = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBE_right = new LinkedList<Pattern>();
		mceSLLBE_right.add(Pattern.compile("current = this\\.header\\.next\\.next; //mutGenLimit 0"));
		mceSLLBE_right.add(Pattern.compile("current = this\\.header; //mutGenLimit 0"));
		mceSLLBE_right.add(Pattern.compile("current = header\\.next; //mutGenLimit 0"));
		mceSLLBE_right.add(Pattern.compile("current = current\\.next; //mutGenLimit 0"));
		mcneSLLBE_right.add(Pattern.compile("current = this\\.header\\.next; //mutGenLimit 0"));
		mcneSLLBE_right.add(Pattern.compile(".+//mutGenLimit [^0]+"));
		Property propSLLBEVarField_right = new Property(MutationOperator.PRVOR, "list/SinglyLinkedListBinaryExpressionVarField", "contains", 4, 3, mceSLLBE_right, mcneSLLBE_right);
		
		/*
		 * Original code
		 * 
		 * current = this.header.next; //mutGenLimit 1
		 * 
		 */
		List<Pattern> mceSLLBE_left = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBE_left = new LinkedList<Pattern>();
		mceSLLBE_left.add(Pattern.compile("current\\.next = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLBE_left.add(Pattern.compile("current\\.value = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLBE_left.add(Pattern.compile("this\\.header\\.next = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLBE_left.add(Pattern.compile("this\\.header\\.value = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLBE_left.add(Pattern.compile("this\\.header = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLBE_left.add(Pattern.compile("value_param = this\\.header\\.next; //mutGenLimit 0"));
		//mceSLLBE_left.add(Pattern.compile("test = this\\.header\\.next; //mutGenLimit 0"));
		Property propSLLBEVarField_left = new Property(MutationOperator.PRVOL, "list/SinglyLinkedListBinaryExpressionVarField", "contains", 6, 0, mceSLLBE_left, mcneSLLBE_left);
		
		
		/*
		 * ORIGINAL
		 * Object test = current;
		 */
		List<Pattern> mceSLLVD_0 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_0 = new LinkedList<Pattern>();
		mceSLLVD_0.add(Pattern.compile("Object test = current\\.next; //mutGenLimit 0"));
		mceSLLVD_0.add(Pattern.compile("Object test = current\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_0.add(Pattern.compile("Object test = current\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLVD_0.add(Pattern.compile("Object test = current\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLVD_0.add(Pattern.compile("Object test = current\\.value; //mutGenLimit 0"));
		mceSLLVD_0.add(Pattern.compile("Object test = null; //mutGenLimit 0"));
		mceSLLVD_0.add(Pattern.compile("Object test = result; //mutGenLimit 0"));
		mceSLLVD_0.add(Pattern.compile("Object test = super\\.clone\\(\\)\\.getClass\\(\\); //mutGenLimit 0"));
		mcneSLLVD_0.add(Pattern.compile("Object !test = [a-zA-Z_0-9]+; //mutGenLimit 0"));
		mcneSLLVD_0.add(Pattern.compile("Object test = current\\.clone\\(\\); //mutGenLimit 0"));
		mcneSLLVD_0.add(Pattern.compile(".+//mutGenLimit [^0]+"));
		Property propSLLVariableDeclaration_0 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", 79 /*original: 83*/, 73 /*original: 77*/, mceSLLVD_0, mcneSLLVD_0);
		
		List<Pattern> mceSLLVD_1 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_1 = new LinkedList<Pattern>();
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.clone\\(\\)\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.clone\\(\\)\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.clone\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.desiredAssertionStatus\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getAnnotations\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getCanonicalName\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getClassLoader\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getClasses\\(\\); //mutGenLimit 0"));
		mceSLLVD_1.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getComponentType\\(\\); //mutGenLimit 0"));
		Property propSLLVariableDeclaration_1 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_1, mcneSLLVD_1);
        
		List<Pattern> mceSLLVD_2 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_2 = new LinkedList<Pattern>();
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getConstructors\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getDeclaredAnnotations\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getDeclaredClasses\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getDeclaredConstructors\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getDeclaredFields\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getDeclaredMethods\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getDeclaringClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getEnclosingClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getEnclosingConstructor\\(\\); //mutGenLimit 0"));
		mceSLLVD_2.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getEnclosingMethod\\(\\); //mutGenLimit 0"));
		Property propSLLVariableDeclaration_2 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_2, mcneSLLVD_2);
		
		List<Pattern> mceSLLVD_3 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_3 = new LinkedList<Pattern>();
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getEnumConstants\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getFields\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getGenericInterfaces\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getGenericSuperclass\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getInterfaces\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getMethods\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getModifiers\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getName\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getPackage\\(\\); //mutGenLimit 0"));
		mceSLLVD_3.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getProtectionDomain\\(\\); //mutGenLimit 0"));
		Property propSLLVariableDeclaration_3 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_3, mcneSLLVD_3);
		
		List<Pattern> mceSLLVD_4 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_4 = new LinkedList<Pattern>();
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getSigners\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getSimpleName\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getSuperclass\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.getTypeParameters\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isAnnotation\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isAnonymousClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isArray\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isEnum\\(\\); //mutGenLimit 0"));
		mceSLLVD_4.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isInterface\\(\\); //mutGenLimit 0"));
		Property propSLLVariableDeclaration_4 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_4, mcneSLLVD_4);
		
		List<Pattern> mceSLLVD_5 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_5 = new LinkedList<Pattern>();
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isLocalClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isMemberClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isPrimitive\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.isSynthetic\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.newInstance\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.getClass\\(\\)\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = super\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLVD_5.add(Pattern.compile("Object test = this\\.header\\.getClass\\(\\); //mutGenLimit 0"));
		mcneSLLVD_5.add(Pattern.compile("Object test = this\\.header\\.clone\\(\\); //mutGenLimit 0"));
		Property propSLLVariableDeclaration_5 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_5, mcneSLLVD_5);
		
		List<Pattern> mceSLLVD_6 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_6 = new LinkedList<Pattern>();
		mceSLLVD_6.add(Pattern.compile("Object test = this\\.header\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = this\\.header\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = this\\.header\\.value; //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = this\\.header; //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = super\\.toString\\(\\).CASE_INSENSITIVE_ORDER; //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = super\\.toString\\(\\).getBytes\\(\\); //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = super\\.toString\\(\\).getClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = super\\.toString\\(\\).hashCode\\(\\); //mutGenLimit 0"));
		mceSLLVD_6.add(Pattern.compile("Object test = super\\.toString\\(\\).intern\\(\\); //mutGenLimit 0"));
		Property propSLLVariableDeclaration_6 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_6, mcneSLLVD_6);
		
		List<Pattern> mceSLLVD_7 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_7 = new LinkedList<Pattern>();
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\).isEmpty\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\).length\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\).toCharArray\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\).toLowerCase\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\).toString\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\).toUpperCase\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\).trim\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = super\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLVD_7.add(Pattern.compile("Object test = value_param; //mutGenLimit 0"));
		Property propSLLVariableDeclaration_7 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_7, mcneSLLVD_7);

		List<Pattern> mceSLLVD_8 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_8 = new LinkedList<Pattern>();
//		mceSLLVD_8.add(Pattern.compile("Object test = header\\.hashCode\\(\\); //mutGenLimit 0"));
//		mceSLLVD_8.add(Pattern.compile("Object test = header\\.next; //mutGenLimit 0"));
//		mceSLLVD_8.add(Pattern.compile("Object test = header\\.toString\\(\\); //mutGenLimit 0"));
//		mceSLLVD_8.add(Pattern.compile("Object test = header\\.value; //mutGenLimit 0"));
//		mceSLLVD_8.add(Pattern.compile("Object test = header\\.clone\\(\\); //mutGenLimit 0"));
//		mceSLLVD_8.add(Pattern.compile("Object test = header\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_8.add(Pattern.compile("Object test = value_param\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLVD_8.add(Pattern.compile("Object test = value_param\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLVD_8.add(Pattern.compile("Object test = value_param\\.toString\\(\\); //mutGenLimit 0"));
		Property propSLLVariableDeclaration_8 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_8, mcneSLLVD_8);
		
		List<Pattern> mceSLLVD_9 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLVD_9 = new LinkedList<Pattern>();
		mcneSLLVD_9.add(Pattern.compile("Object test = test\\.hashCode\\(\\); //mutGenLimit 0"));
		mcneSLLVD_9.add(Pattern.compile("Object test = test\\.getClass\\(\\); //mutGenLimit 0"));
		mcneSLLVD_9.add(Pattern.compile("Object test = test\\.toString\\(\\); //mutGenLimit 0"));
		mcneSLLVD_9.add(Pattern.compile("Object test = test; //mutGenLimit 0"));
		Property propSLLVariableDeclaration_9 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListVariableDeclaration", "contains", -1, -1, mceSLLVD_9, mcneSLLVD_9);
		
		
		/*
		 *  MUTANTES NO INCLUIDOS EN LOS TEST
		 */
		/*
		 * ORIGINAL
		 * return header;
		 */
		List<Pattern> mceSLLR_0 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_0 = new LinkedList<Pattern>();
		mceSLLR_0.add(Pattern.compile("return null; //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return current; //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return this\\.header\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return this\\.header\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return this\\.header\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return this\\.header\\.next; //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return this\\.header\\.value; //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return super\\.clone\\(\\)\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return super\\.clone\\(\\)\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLR_0.add(Pattern.compile("return this\\.header; //mutGenLimit 0"));
		mcneSLLR_0.add(Pattern.compile("return this\\.header\\.clone\\(\\); //mutGenLimit 0"));
		mcneSLLR_0.add(Pattern.compile("return header; //mutGenLimit 0"));
		mcneSLLR_0.add(Pattern.compile(".+//mutGenLimit [^0]+"));
		mcneSLLR_0.add(Pattern.compile("return this\\.(current|value)(\\..+)?; //mutGenLimit"));
		Property propSLLReturn_0 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", 79 /*original: 83*/, 74 /*original: 78*/, mceSLLR_0, mcneSLLR_0);
		
		List<Pattern> mceSLLR_1 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_1 = new LinkedList<Pattern>();
		mceSLLR_1.add(Pattern.compile("return super\\.clone\\(\\)\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.clone\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.desiredAssertionStatus\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.getAnnotations\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.getCanonicalName\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.getClassLoader\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.getClasses\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.getComponentType\\(\\); //mutGenLimit 0"));
		mceSLLR_1.add(Pattern.compile("return super\\.getClass\\(\\)\\.getConstructors\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_1 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_1, mcneSLLR_1);
		
		List<Pattern> mceSLLR_2 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_2 = new LinkedList<Pattern>();
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getDeclaredAnnotations\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getDeclaredClasses\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getDeclaredConstructors\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getDeclaredFields\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getDeclaredMethods\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getDeclaringClass\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getEnclosingClass\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getEnclosingConstructor\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getEnclosingMethod\\(\\); //mutGenLimit 0"));
		mceSLLR_2.add(Pattern.compile("return super\\.getClass\\(\\)\\.getEnumConstants\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_2 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_2, mcneSLLR_2);
		
		List<Pattern> mceSLLR_3 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_3 = new LinkedList<Pattern>();
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getFields\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getGenericSuperclass\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getInterfaces\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getMethods\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getModifiers\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getName\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getPackage\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getProtectionDomain\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getSigners\\(\\); //mutGenLimit 0"));
		mceSLLR_3.add(Pattern.compile("return super\\.getClass\\(\\)\\.getSimpleName\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_3 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_3, mcneSLLR_3);
		
		List<Pattern> mceSLLR_4 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_4 = new LinkedList<Pattern>();
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.getSuperclass\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.getTypeParameters\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.isAnnotation\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.isAnonymousClass\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.isArray\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.isEnum\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.isInterface\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.isLocalClass\\(\\); //mutGenLimit 0"));
		mceSLLR_4.add(Pattern.compile("return super\\.getClass\\(\\)\\.isMemberClass\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_4 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_4, mcneSLLR_4);
		
		List<Pattern> mceSLLR_5 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_5 = new LinkedList<Pattern>();
		mceSLLR_5.add(Pattern.compile("return super\\.getClass\\(\\)\\.isPrimitive\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return super\\.getClass\\(\\)\\.isSynthetic\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return super\\.getClass\\(\\)\\.newInstance\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return super\\.getClass\\(\\)\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return super\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return this\\.getHeader\\(\\)\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return this\\.getHeader\\(\\)\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return this\\.getHeader\\(\\)\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return this\\.getHeader\\(\\); //mutGenLimit 0"));
		mceSLLR_5.add(Pattern.compile("return super\\.hashCode\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_5 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_5, mcneSLLR_5);
		
		List<Pattern> mceSLLR_6 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_6 = new LinkedList<Pattern>(); 
		mceSLLR_6.add(Pattern.compile("return this\\.header\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return this\\.header\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return this\\.header\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return this\\.header\\.next; //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return this\\.header\\.value; //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return super\\.toString\\(\\)\\.CASE_INSENSITIVE_ORDER; //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return super\\.toString\\(\\)\\.getBytes\\(\\); //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return super\\.toString\\(\\)\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_6.add(Pattern.compile("return super\\.toString\\(\\)\\.hashCode\\(\\); //mutGenLimit 0"));
		mcneSLLR_6.add(Pattern.compile("return this\\.header\\.clone\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_6 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_6, mcneSLLR_6);
		
		List<Pattern> mceSLLR_7 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_7 = new LinkedList<Pattern>();
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.intern\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.isEmpty\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.length\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.toCharArray\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.toLowerCase\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.toUpperCase\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\)\\.trim\\(\\); //mutGenLimit 0"));
		mceSLLR_7.add(Pattern.compile("return super\\.toString\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_7 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_7, mcneSLLR_7);
		
		List<Pattern> mceSLLR_8 = new LinkedList<Pattern>();
		List<Pattern> mcneSLLR_8 = new LinkedList<Pattern>();
		mceSLLR_8.add(Pattern.compile("return current\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLR_8.add(Pattern.compile("return current\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLR_8.add(Pattern.compile("return current\\.hashCode\\(\\); //mutGenLimit 0"));
		mceSLLR_8.add(Pattern.compile("return current\\.value; //mutGenLimit 0"));
		mceSLLR_8.add(Pattern.compile("return current\\.next; //mutGenLimit 0"));
		mcneSLLR_8.add(Pattern.compile("return current\\.clone\\(\\); //mutGenLimit 0"));
		Property propSLLReturn_8 = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListReturn", "getHeader", -1, -1, mceSLLR_8, mcneSLLR_8);
		
		/*
		 * ORIGINAL
		 * current = null;
		 */
		List<Pattern> mceSLLBENR_right = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBENR_right = new LinkedList<Pattern>();
		mceSLLBENR_right.add(Pattern.compile("current = current; //mutGenLimit 0"));
		mceSLLBENR_right.add(Pattern.compile("current = this\\.header; //mutGenLimit 0"));
		mcneSLLBENR_right.add(Pattern.compile("current = header; //mutGenLimit 0"));
		mcneSLLBENR_right.add(Pattern.compile(".+//mutGenLimit [^0]+")); //el + creo que no va
		Property propSLLBExpreVarNull_right = new Property(MutationOperator.PRVOR, "list/SinglyLinkedListBinaryExpressionVarNull", "contains", 2, 1, mceSLLBENR_right, mcneSLLBENR_right);
		
		List<Pattern> mceSLLBENR_left = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBENR_left = new LinkedList<Pattern>();
		mceSLLBENR_left.add(Pattern.compile("current\\.next = null; //mutGenLimit 0"));
		mceSLLBENR_left.add(Pattern.compile("current\\.value = null; //mutGenLimit 0"));
		mceSLLBENR_left.add(Pattern.compile("this\\.header\\.value = null; //mutGenLimit 0"));
		mceSLLBENR_left.add(Pattern.compile("this\\.header\\.next = null; //mutGenLimit 0"));
		mceSLLBENR_left.add(Pattern.compile("this\\.header = null; //mutGenLimit 0"));
		mceSLLBENR_left.add(Pattern.compile("value_param = null; //mutGenLimit 0"));
		mceSLLBENR_left.add(Pattern.compile("super\\.toString\\(\\).CASE_INSENSITIVE_ORDER = null; //mutGenLimit 0"));
		
		mcneSLLBENR_left.add(Pattern.compile("test = null; //mutGenLimit 0"));
		mcneSLLBENR_left.add(Pattern.compile("^([\\s ])*header = null; //mutGenLimit 0"));
		mcneSLLBENR_left.add(Pattern.compile("^([\\s ])*header\\.value = null; //mutGenLimit 0"));
		mcneSLLBENR_left.add(Pattern.compile("^([\\s ])*header\\.next = null; //mutGenLimit 0"));
		
		Property propSLLBExpreVarNull_left = new Property(MutationOperator.PRVOL, "list/SinglyLinkedListBinaryExpressionVarNull", "contains", 7, 0, mceSLLBENR_left, mcneSLLBENR_left);
		
		/*
		 *  MUTANTES NO INCLUIDOS EN LOS TEST
	        test = header; //mutGenLimit 0
		 */
		/*
		 * ORIGINAL
		 * current.next = header;
		 */
		List<Pattern> mceSLLBEFieldVar_right = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBEFieldVar_right = new LinkedList<Pattern>();
		mceSLLBEFieldVar_right.add(Pattern.compile("current\\.next = this\\.header; //mutGenLimit 0"));
		mceSLLBEFieldVar_right.add(Pattern.compile("current\\.next = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLBEFieldVar_right.add(Pattern.compile("current\\.next = current; //mutGenLimit 0"));
		mceSLLBEFieldVar_right.add(Pattern.compile("current\\.next = null; //mutGenLimit 0"));
		mceSLLBEFieldVar_right.add(Pattern.compile("current\\.next = current\\.next; //mutGenLimit 0"));
		mcneSLLBEFieldVar_right.add(Pattern.compile("current\\.next = header\\.next; //mutGenLimit 0"));
		mcneSLLBEFieldVar_right.add(Pattern.compile("current\\.next = header; //mutGenLimit 0"));
		mcneSLLBEFieldVar_right.add(Pattern.compile("current\\.next = test; //mutGenLimit 0"));
		mcneSLLBEFieldVar_right.add(Pattern.compile("current\\.next = result; //mutGenLimit 0"));
		mcneSLLBEFieldVar_right.add(Pattern.compile("current\\.next = value_param; //mutGenLimit 0"));
		mcneSLLBEFieldVar_right.add(Pattern.compile("current\\.next = equalVal; //mutGenLimit 0"));
		mcneSLLBEFieldVar_right.add(Pattern.compile(".+//mutGenLimit [^0]+"));
		Property propSLLBEFieldVar_right = new Property(MutationOperator.PRVOR, "list/SinglyLinkedListBinaryExpressionFieldVar", "contains", 5, 5, mceSLLBEFieldVar_right, mcneSLLBEFieldVar_right);
		
		List<Pattern> mceSLLBEFieldVar_left = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBEFieldVar_left = new LinkedList<Pattern>();
		mceSLLBEFieldVar_left.add(Pattern.compile("current = header; //mutGenLimit 0"));
		mceSLLBEFieldVar_left.add(Pattern.compile("current\\.value = header; //mutGenLimit 0"));
		mceSLLBEFieldVar_left.add(Pattern.compile("current\\.next\\.next = header; //mutGenLimit 0"));
		mceSLLBEFieldVar_left.add(Pattern.compile("current\\.next\\.value = header; //mutGenLimit 0"));
		mceSLLBEFieldVar_left.add(Pattern.compile("this\\.header = header; //mutGenLimit 0"));
		mceSLLBEFieldVar_left.add(Pattern.compile("this\\.header\\.next = header; //mutGenLimit 0"));
		mceSLLBEFieldVar_left.add(Pattern.compile("this\\.header\\.next\\.next = header; //mutGenLimit 0"));
		mceSLLBEFieldVar_left.add(Pattern.compile("value_param = header; //mutGenLimit 0"));
		mcneSLLBEFieldVar_left.add(Pattern.compile("^([\\s ])*header = header; //mutGenLimit 0"));
		mcneSLLBEFieldVar_left.add(Pattern.compile("^([\\s ])*header\\.next = header; //mutGenLimit 0"));
		mcneSLLBEFieldVar_left.add(Pattern.compile("^([\\s ])*header\\.next\\.next = header; //mutGenLimit 0"));
		Property propSLLBEFieldVar_left = new Property(MutationOperator.PRVOL, "list/SinglyLinkedListBinaryExpressionFieldVar", "contains", 8, 8, mceSLLBEFieldVar_left, mcneSLLBEFieldVar_left);
		
		
		/* MUTANTES NO INCLUIDOS EN LOS TESTS
		 */
		/*
		 * ORIGINAL
		 *  current = current.next;
		 */
		List<Pattern> mceSLLBEVarFieldWhile_right = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBEVarFieldWhile_right = new LinkedList<Pattern>();
		mceSLLBEVarFieldWhile_right.add(Pattern.compile("current = current; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_right.add(Pattern.compile("current = current\\.next\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_right.add(Pattern.compile("current = this\\.header; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_right.add(Pattern.compile("current = this\\.header\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_right.add(Pattern.compile("current = this\\.header\\.next\\.next; //mutGenLimit 0"));
		
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = header; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = header\\.next; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = header\\.next\\.next; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = null; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = test; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = value_param; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = equalVal; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile("current = .+\\.value; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_right.add(Pattern.compile(".+//mutGenLimit [^0]+"));
		Property propSLLBEVarFieldWhile_right = new Property(MutationOperator.PRVOR, "list/SinglyLinkedListBinaryExpressionVarFieldWhile", "contains", 5, 5, mceSLLBEVarFieldWhile_right, mcneSLLBEVarFieldWhile_right);
		
		
		List<Pattern> mceSLLBEVarFieldWhile_left = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBEVarFieldWhile_left = new LinkedList<Pattern>();
		mceSLLBEVarFieldWhile_left.add(Pattern.compile("current\\.next = current\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_left.add(Pattern.compile("current\\.value = current\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_left.add(Pattern.compile("this\\.header = current\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_left.add(Pattern.compile("this\\.header\\.next = current\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_left.add(Pattern.compile("this\\.header\\.value = current\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_left.add(Pattern.compile("test = current\\.next; //mutGenLimit 0"));
		mceSLLBEVarFieldWhile_left.add(Pattern.compile("value_param = current\\.next; //mutGenLimit 0"));
		
		mcneSLLBEVarFieldWhile_left.add(Pattern.compile("^([\\s ])*header = current\\.next; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_left.add(Pattern.compile("^([\\s ])*header\\.next = current\\.next; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_left.add(Pattern.compile("^([\\s ])*header\\.value = current\\.next; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_left.add(Pattern.compile("equalVal = current\\.next; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_left.add(Pattern.compile("result = current\\.next; //mutGenLimit 0"));
		mcneSLLBEVarFieldWhile_left.add(Pattern.compile("null = current\\.next; //mutGenLimit 0"));
		Property propSLLBEVarFieldWhile_left = new Property(MutationOperator.PRVOL, "list/SinglyLinkedListBinaryExpressionVarFieldWhile", "contains", 7, 7, mceSLLBEVarFieldWhile_left, mcneSLLBEVarFieldWhile_left);
		
		/*
		 * ORIGINAL
		 * current.value = i; //mutGenLimit 1 
		 */
		List<Pattern> mceSLLBEFieldVarFor_right = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBEFieldVarFor_right = new LinkedList<Pattern>();
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = j; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = null; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = current; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = result; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = this\\.header; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = value_param; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = super\\.toString\\(\\); //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = super\\.getClass\\(\\); //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = super\\.clone\\(\\); //mutGenLimit 0"));
		mceSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = super\\.hashCode\\(\\); //mutGenLimit 0"));
		
		
		mcneSLLBEFieldVarFor_right.add(Pattern.compile(".+//mutGenLimit [^0]+"));
		mcneSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = test; //mutGenLimit 0"));
		mcneSLLBEFieldVarFor_right.add(Pattern.compile("current\\.value = header; //mutGenLimit 0"));
		Property propSLLBEFieldVarFor_right = new Property(MutationOperator.PRVOR, "list/SinglyLinkedListBinaryExpressionFieldVarFor", "contains", 10, 8, mceSLLBEFieldVarFor_right, mcneSLLBEFieldVarFor_right);
		
		/*
		 * ORIGINAL
		 * current.value = i; //mutGenLimit 1 
		 */
		List<Pattern> mceSLLBEFieldVarFor_left = new LinkedList<Pattern>();
		List<Pattern> mcneSLLBEFieldVarFor_left = new LinkedList<Pattern>();
		mceSLLBEFieldVarFor_left.add(Pattern.compile("this\\.header\\.value = i; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_left.add(Pattern.compile("current\\.next\\.value = i; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_left.add(Pattern.compile("this\\.header\\.next\\.value = i; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_left.add(Pattern.compile("value_param = i; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_left.add(Pattern.compile("i = i; //mutGenLimit 0"));
		mceSLLBEFieldVarFor_left.add(Pattern.compile("j = i; //mutGenLimit 0"));
		
		mcneSLLBEFieldVarFor_left.add(Pattern.compile("test = i; //mutGenLimit 0"));
		mcneSLLBEFieldVarFor_left.add(Pattern.compile("^([\\s ])*header\\.value = i; //mutGenLimit 0"));
		mcneSLLBEFieldVarFor_left.add(Pattern.compile("^([\\s ])*header\\.next\\.value = i; //mutGenLimit 0"));
		Property propSLLBEFieldVarFor_left = new Property(MutationOperator.PRVOL, "list/SinglyLinkedListBinaryExpressionFieldVarFor", "contains", 6, 6, mceSLLBEFieldVarFor_left, mcneSLLBEFieldVarFor_left);
		
		
		//LOS SIGUIENTES SON TESTS PARA COMPROBAR QUE NO SE MUTA FUERA DEL METODO BAJO CONSIDERACION
		
		List<Pattern> mceOMUCL = new LinkedList<Pattern>();
		List<Pattern> mcneOMUCL = new LinkedList<Pattern>();
		Property propOMUCL = new Property(MutationOperator.PRVOL, "list/SinglyLinkedListOutsideMUC", "getHeader", 0, 0, mceOMUCL, mcneOMUCL);
		
		List<Pattern> mceOMUCR = new LinkedList<Pattern>();
		List<Pattern> mcneOMUCR = new LinkedList<Pattern>();
		Property propOMUCR = new Property(MutationOperator.PRVOR, "list/SinglyLinkedListOutsideMUC", "getHeader", 0, 0, mceOMUCR, mcneOMUCR);
		
		List<Pattern> mceOMUCU = new LinkedList<Pattern>();
		List<Pattern> mcneOMUCU = new LinkedList<Pattern>();
		Property propOMUCU = new Property(MutationOperator.PRVOU, "list/SinglyLinkedListOutsideMUC", "getHeader", 0, 0, mceOMUCU, mcneOMUCU);
		
		
		
//		//current.value = aninteger; //mutGenLimit 1
//		List<Pattern> mceSLLBEFieldVarSwitch = new LinkedList<Pattern>();
//		List<Pattern> mcneSLLBEFieldVarSwitch = new LinkedList<Pattern>();
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("header\\.value = aninteger; //mutGenLimit 0"));
//		//mceSLLBEFieldVarSwitch.add(Pattern.compile("value_param = aninteger; //mutGenLimit 0"));
//		//mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.next\\.value = aninteger; //mutGenLimit 0"));
//		//mceSLLBEFieldVarSwitch.add(Pattern.compile("header\\.next\\.value = aninteger; //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = current; //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = header; //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = value_param; //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = null; //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = aninteger\\.getClass\\(\\); //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = aninteger\\.toString\\(\\); //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = toString\\(\\); //mutGenLimit 0"));
//		mceSLLBEFieldVarSwitch.add(Pattern.compile("current\\.value = getClass\\(\\); //mutGenLimit 0"));
//		mcneSLLBEFieldVarSwitch.add(Pattern.compile(".+//mutGenLimit [^0]+"));
//		Property propSLLBEFieldVarSwitch = new Property(MutationOperator.PRVO, "list/SinglyLinkedListBinaryExpressionFieldVarSwitch", "contains", 16, 12, mceSLLBEFieldVarSwitch, mcneSLLBEFieldVarSwitch);
//		
//		//current.value = value_param; //mutGenLimit 1
//		List<Pattern> mceSLLBEFieldVarTry = new LinkedList<Pattern>();
//		List<Pattern> mcneSLLBEFieldVarTry = new LinkedList<Pattern>();
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = tryVar; //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = current; //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = header; //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = test; //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = null; //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = value_param\\.toString\\(\\); //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = value_param\\.getClass\\(\\); //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = toString\\(\\); //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("current\\.value = getClass\\(\\); //mutGenLimit 0"));
//		mceSLLBEFieldVarTry.add(Pattern.compile("header\\.value = value_param; //mutGenLimit 0"));
//		//mceSLLBEFieldVarTry.add(Pattern.compile("current\\.next\\.value = value_param; //mutGenLimit 0"));
//		//mceSLLBEFieldVarTry.add(Pattern.compile("header\\.next\\.value = value_param; //mutGenLimit 0"));
//		mcneSLLBEFieldVarTry.add(Pattern.compile(".+//mutGenLimit [^0]+"));
//		Property propSLLBEFieldVarTry = new Property(MutationOperator.PRVO, "list/SinglyLinkedListBinaryExpressionFieldVarTry", "contains", 11, 12, mceSLLBEFieldVarTry, mcneSLLBEFieldVarTry);
		
		
		List<MutantInfo> mfSLLBEVarField_right;
		List<MutantInfo> mfSLLBEVarField_left;
		List<MutantInfo> mfSLLVariableDeclaration;
		List<MutantInfo> mfSLLReturn;
		List<MutantInfo> mfpropSLLBExpreVarNull_right;
		List<MutantInfo> mfpropSLLBExpreVarNull_left;
		List<MutantInfo> mfSLLBEFieldVar_right;
		List<MutantInfo> mfSLLBEFieldVar_left;
		List<MutantInfo> mfSLLBEVarFieldWhile_right;
		List<MutantInfo> mfSLLBEVarFieldWhile_left;
		List<MutantInfo> mfSLLBEFieldVarFor_right;
		List<MutantInfo> mfSLLBEFieldVarFor_left;
//		List<String> mfSLLBEFieldVarSwitch;
//		List<String> mfSLLBEFieldVarTry;
		List<MutantInfo> mfOMUCL;
		List<MutantInfo> mfOMUCR;
		List<MutantInfo> mfOMUCU;
		
		Configuration.add(PRVO.ALLOW_FINAL_MEMBERS, Boolean.TRUE);
		Configuration.add(MutantCodeWriter.USE_SIMPLE_CLASS_NAMES, Boolean.FALSE);
		Configuration.add(MutantCodeWriter.KEEP_ORIGINAL_TYPE_NAMES, Boolean.FALSE);
		List<Pattern> prohibitedClassMethods = new LinkedList<>();
		prohibitedClassMethods.add(Pattern.compile("java\\.lang\\.Class\\#getAnnotatedInterfaces"));
		prohibitedClassMethods.add(Pattern.compile("java\\.lang\\.Class\\#getTypeName"));
		prohibitedClassMethods.add(Pattern.compile("java\\.lang\\.Class\\#getAnnotatedSuperclass"));
		prohibitedClassMethods.add(Pattern.compile("java\\.lang\\.Class\\#toGenericString"));
		Configuration.add(Configuration.PROHIBITED_METHODS, prohibitedClassMethods);
		
		mfSLLBEVarField_right = TestingTools.generateMutants(propSLLBEVarField_right);
		
		mfSLLBEVarField_left = TestingTools.generateMutants(propSLLBEVarField_left);
		
		mfSLLVariableDeclaration = TestingTools.generateMutants(propSLLVariableDeclaration_0);
		
		mfSLLReturn = TestingTools.generateMutants(propSLLReturn_0);
		
		mfpropSLLBExpreVarNull_right = TestingTools.generateMutants(propSLLBExpreVarNull_right);
		
		mfpropSLLBExpreVarNull_left = TestingTools.generateMutants(propSLLBExpreVarNull_left);
		
		mfSLLBEFieldVar_right = TestingTools.generateMutants(propSLLBEFieldVar_right);
		
		mfSLLBEFieldVar_left = TestingTools.generateMutants(propSLLBEFieldVar_left);
		
		mfSLLBEVarFieldWhile_right = TestingTools.generateMutants(propSLLBEVarFieldWhile_right);
	
		mfSLLBEVarFieldWhile_left = TestingTools.generateMutants(propSLLBEVarFieldWhile_left);
		
		mfSLLBEFieldVarFor_right = TestingTools.generateMutants(propSLLBEFieldVarFor_right);
		
		mfSLLBEFieldVarFor_left = TestingTools.generateMutants(propSLLBEFieldVarFor_left);
		
		mfOMUCL = TestingTools.generateMutants(propOMUCL);
		
		mfOMUCR = TestingTools.generateMutants(propOMUCR);
		
		mfOMUCU = TestingTools.generateMutants(propOMUCU);
		
		Configuration.removeArgument(PRVO.ALLOW_FINAL_MEMBERS);
		Configuration.removeArgument(MutantCodeWriter.USE_SIMPLE_CLASS_NAMES);
		Configuration.removeArgument(MutantCodeWriter.KEEP_ORIGINAL_TYPE_NAMES);
		Configuration.removeArgument(Configuration.PROHIBITED_METHODS);
		
		return Arrays.asList(new Object[][] {
				{propSLLBEVarField_right, mfSLLBEVarField_right},			//0
				{propSLLBEVarField_left, mfSLLBEVarField_left},				//1
				{propSLLVariableDeclaration_0, mfSLLVariableDeclaration},	//2
				{propSLLVariableDeclaration_1, mfSLLVariableDeclaration},	//3
				{propSLLVariableDeclaration_2, mfSLLVariableDeclaration},	//4
				{propSLLVariableDeclaration_3, mfSLLVariableDeclaration},	//5
				{propSLLVariableDeclaration_4, mfSLLVariableDeclaration},	//6
				{propSLLVariableDeclaration_5, mfSLLVariableDeclaration},	//7
				{propSLLVariableDeclaration_6, mfSLLVariableDeclaration},	//8
				{propSLLVariableDeclaration_7, mfSLLVariableDeclaration},	//9
				{propSLLVariableDeclaration_8, mfSLLVariableDeclaration},	//10
				{propSLLVariableDeclaration_9, mfSLLVariableDeclaration},	//11
				{propSLLReturn_0, mfSLLReturn},								//12
				{propSLLReturn_1, mfSLLReturn},								//13
				{propSLLReturn_2, mfSLLReturn},								//14
				{propSLLReturn_3, mfSLLReturn},								//15
				{propSLLReturn_4, mfSLLReturn},								//16
				{propSLLReturn_5, mfSLLReturn},								//17
				{propSLLReturn_6, mfSLLReturn},								//18
				{propSLLReturn_7, mfSLLReturn},								//19
				{propSLLReturn_8, mfSLLReturn},								//20
				{propSLLBExpreVarNull_right, mfpropSLLBExpreVarNull_right},	//21
				{propSLLBExpreVarNull_left, mfpropSLLBExpreVarNull_left},	//22
				{propSLLBEFieldVar_right, mfSLLBEFieldVar_right},			//23
				{propSLLBEFieldVar_left, mfSLLBEFieldVar_left},				//24
				{propSLLBEVarFieldWhile_right, mfSLLBEVarFieldWhile_right},	//25
				{propSLLBEVarFieldWhile_left, mfSLLBEVarFieldWhile_left},	//26
				{propSLLBEFieldVarFor_right, mfSLLBEFieldVarFor_right},		//27
				{propSLLBEFieldVarFor_left, mfSLLBEFieldVarFor_left},		//28
				//{propSLLBEFieldVarSwitch},
				//{propSLLBEFieldVarTry}
				{propOMUCL, mfOMUCL},
				{propOMUCR, mfOMUCR},
				{propOMUCU, mfOMUCU},
		});
	}

	
	@Test
	public void testThatMutantsCompile() {
		assertTrue(TestingTools.testThatMutantsCompile(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testCorrectNumberOfMutants() {
		assertTrue(TestingTools.testCorrectNumberOfMutants(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testCorrectMutantsGenerated() {
		assertTrue(TestingTools.testExpectedMutantsFound(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testMutantsNotExpected() {
		assertTrue(TestingTools.testUnexpectedMutantsNotFound(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testMutantsMD5hash() {
		assertTrue(TestingTools.testMD5Hash(this.mutantsInfo));
	}

}
