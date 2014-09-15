package mujava.api;

/**
 * The type of the mutation operators available for muJava++
 * these types are
 * <p>
 * <li>MethodLevel         : mutates a statement inside a method declaration</li>
 * <li>ClassLevel          : mutates member declarations inside a class</li>
 * <li>ClassAndMethodLevel : can generate MethodLevel and ClassLevel mutations</li>
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 1.0
 */
public enum MutantType {
	MethodLevel,
	ClassLevel,
	ClassAndMethodLevel
}
