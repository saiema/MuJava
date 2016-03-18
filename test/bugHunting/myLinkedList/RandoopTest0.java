package bugHunting.myLinkedList;
import junit.framework.*;

public class RandoopTest0 extends TestCase {

  public static boolean debug = false;

  public void test1() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test1");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var3 = var0.set(10, (java.lang.Object)(short)(-1));
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }

  }

  public void test2() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test2");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var2 = var0.pop();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);

  }

  public void test3() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test3");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var2 = var0.get(1);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }

  }

  public void test4() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test4");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var1 = var0.removeFirst();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }

  }

  public void test5() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test5");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var5 = var0.getFirst();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));

  }

  public void test6() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test6");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.util.ListIterator var6 = var0.listIterator((-1));
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);

  }

  public void test7() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test7");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var11 = var0.set((-1), (java.lang.Object)(byte)1);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);

  }

  public void test8() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test8");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    var5.clear();
    boolean var11 = var5.add((java.lang.Object)0L);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);

  }

  public void test9() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test9");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    boolean var6 = var0.offerLast((java.lang.Object)1L);
    java.util.ListIterator var7 = var0.listIterator();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    java.lang.Object var9 = var8.peek();
    java.lang.Object var10 = var8.peekFirst();
    boolean var11 = var0.containsAll((java.util.Collection)var8);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var13 = var0.get(100);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);

  }

  public void test10() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test10");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    java.lang.Object var5 = var0.peekLast();
    java.lang.String var6 = var0.toString();
    boolean var8 = var0.removeLastOccurrence((java.lang.Object)false);
    java.lang.Object var9 = var0.element();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var5 + "' != '" + '4'+ "'", var5.equals('4'));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var6 + "' != '" + "[4]"+ "'", var6.equals("[4]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var9 + "' != '" + '4'+ "'", var9.equals('4'));

  }

  public void test11() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test11");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    java.lang.Object var9 = var0.peekLast();
    java.lang.Object[] var10 = var0.toArray();
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var10);

  }

  public void test12() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test12");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    java.lang.Object var7 = var2.poll();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var8 = var2.removeLast();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);

  }

  public void test13() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test13");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var5 = var0.remove();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));

  }

  public void test14() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test14");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    boolean var6 = var0.offerLast((java.lang.Object)1L);
    java.util.ListIterator var7 = var0.listIterator();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    java.lang.Object var9 = var8.peek();
    java.lang.Object var10 = var8.peekFirst();
    boolean var11 = var0.containsAll((java.util.Collection)var8);
    var8.clear();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);

  }

  public void test15() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test15");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.pollLast();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var3 = var0.pop();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);

  }

  public void test16() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test16");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.pollLast();
    myLinkedList.MyLinkedList var4 = new myLinkedList.MyLinkedList();
    boolean var6 = var4.equals((java.lang.Object)0.0d);
    java.util.ListIterator var7 = var4.listIterator();
    java.lang.String var8 = var4.toString();
    myLinkedList.MyLinkedList var9 = new myLinkedList.MyLinkedList();
    java.lang.Object var10 = var9.peek();
    java.lang.Object var11 = var9.peekFirst();
    boolean var12 = var4.offerFirst((java.lang.Object)var9);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      boolean var13 = var0.addAll((-1), (java.util.Collection)var4);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var8 + "' != '" + "[]"+ "'", var8.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var12 == true);

  }

  public void test17() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test17");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    java.lang.Object var5 = var0.peekLast();
    java.lang.String var6 = var0.toString();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    java.lang.Object var9 = var7.peekFirst();
    boolean var11 = var7.add((java.lang.Object)'4');
    java.lang.Object var12 = var7.peekLast();
    java.lang.String var13 = var7.toString();
    boolean var15 = var7.removeLastOccurrence((java.lang.Object)false);
    boolean var16 = var0.retainAll((java.util.Collection)var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var5 + "' != '" + '4'+ "'", var5.equals('4'));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var6 + "' != '" + "[4]"+ "'", var6.equals("[4]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var12 + "' != '" + '4'+ "'", var12.equals('4'));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var13 + "' != '" + "[4]"+ "'", var13.equals("[4]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == false);

  }

  public void test18() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test18");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.offerLast((java.lang.Object)100.0f);
    java.lang.Object var5 = var0.pollLast();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var5 + "' != '" + 100.0f+ "'", var5.equals(100.0f));

  }

  public void test19() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test19");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    boolean var7 = var0.containsAll((java.util.Collection)var5);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.util.ListIterator var9 = var0.listIterator(10);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var7 == true);

  }

  public void test20() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test20");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.pollLast();
    myLinkedList.MyLinkedList var3 = new myLinkedList.MyLinkedList();
    boolean var5 = var3.equals((java.lang.Object)0.0d);
    java.util.ListIterator var6 = var3.listIterator();
    boolean var7 = var0.offer((java.lang.Object)var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var7 == true);

  }

  public void test21() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test21");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    java.util.Spliterator var3 = var0.spliterator();
    java.lang.Object var4 = var0.peekLast();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var5 = var0.getLast();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var4);

  }

  public void test22() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test22");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    java.lang.Object var9 = var7.peekFirst();
    boolean var11 = var7.add((java.lang.Object)'4');
    boolean var13 = var7.offerLast((java.lang.Object)1L);
    java.util.ListIterator var14 = var7.listIterator();
    int var15 = var2.lastIndexOf((java.lang.Object)var7);
    myLinkedList.MyLinkedList var16 = new myLinkedList.MyLinkedList();
    boolean var18 = var16.equals((java.lang.Object)0.0d);
    java.util.ListIterator var19 = var16.listIterator();
    java.lang.String var20 = var16.toString();
    myLinkedList.MyLinkedList var21 = new myLinkedList.MyLinkedList();
    java.lang.Object var22 = var21.peek();
    java.lang.Object var23 = var21.peekFirst();
    boolean var24 = var16.offerFirst((java.lang.Object)var21);
    int var25 = var7.lastIndexOf((java.lang.Object)var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var15 == (-1));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var20 + "' != '" + "[]"+ "'", var20.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var22);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var23);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var24 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var25 == (-1));

  }

  public void test23() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test23");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    java.util.Spliterator var3 = var0.spliterator();
    java.lang.Object var4 = var0.peekLast();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var5 = var0.element();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var4);

  }

  public void test24() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test24");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    boolean var6 = var0.offerLast((java.lang.Object)1L);
    java.util.ListIterator var7 = var0.listIterator();
    java.lang.Object var8 = var0.removeFirst();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var8 + "' != '" + '4'+ "'", var8.equals('4'));

  }

  public void test25() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test25");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    java.lang.Object var5 = var0.peekLast();
    java.lang.String var6 = var0.toString();
    boolean var8 = var0.removeLastOccurrence((java.lang.Object)false);
    java.lang.Object var9 = var0.poll();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var5 + "' != '" + '4'+ "'", var5.equals('4'));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var6 + "' != '" + "[4]"+ "'", var6.equals("[4]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var9 + "' != '" + '4'+ "'", var9.equals('4'));

  }

  public void test26() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test26");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var6 = new myLinkedList.MyLinkedList();
    boolean var8 = var6.equals((java.lang.Object)0.0d);
    java.util.ListIterator var9 = var6.listIterator();
    java.lang.String var10 = var6.toString();
    myLinkedList.MyLinkedList var11 = new myLinkedList.MyLinkedList();
    java.lang.Object var12 = var11.peek();
    java.lang.Object var13 = var11.peekFirst();
    boolean var14 = var6.offerFirst((java.lang.Object)var11);
    java.util.ListIterator var16 = var11.listIterator(0);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var17 = var0.set(100, (java.lang.Object)var16);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var10 + "' != '" + "[]"+ "'", var10.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var12);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var16);

  }

  public void test27() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test27");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    boolean var10 = var7.contains((java.lang.Object)1.0d);
    boolean var11 = var5.removeAll((java.util.Collection)var7);
    myLinkedList.MyLinkedList var12 = new myLinkedList.MyLinkedList();
    java.util.Iterator var13 = var12.iterator();
    java.util.Iterator var14 = var12.descendingIterator();
    java.util.Spliterator var15 = var12.spliterator();
    boolean var16 = var7.equals((java.lang.Object)var12);
    myLinkedList.MyLinkedList var17 = new myLinkedList.MyLinkedList();
    java.lang.Object var18 = var17.peek();
    java.lang.Object var19 = var17.peekFirst();
    boolean var21 = var17.add((java.lang.Object)'4');
    boolean var23 = var17.offerLast((java.lang.Object)1L);
    java.util.ListIterator var24 = var17.listIterator();
    java.lang.Object[] var25 = new java.lang.Object[] { var24};
    java.lang.Object[] var26 = var7.toArray(var25);
    java.lang.Object[] var27 = var0.toArray(var26);
    boolean var29 = var0.removeFirstOccurrence((java.lang.Object)100);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var26);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var29 == false);

  }

  public void test28() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test28");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    java.util.Spliterator var3 = var0.spliterator();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var4 = var0.removeLast();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);

  }

  public void test29() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test29");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.pollLast();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var3 = var0.remove();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);

  }

  public void test30() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test30");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    boolean var10 = var7.contains((java.lang.Object)1.0d);
    boolean var11 = var5.removeAll((java.util.Collection)var7);
    myLinkedList.MyLinkedList var12 = new myLinkedList.MyLinkedList();
    java.util.Iterator var13 = var12.iterator();
    java.util.Iterator var14 = var12.descendingIterator();
    java.util.Spliterator var15 = var12.spliterator();
    boolean var16 = var7.equals((java.lang.Object)var12);
    myLinkedList.MyLinkedList var17 = new myLinkedList.MyLinkedList();
    java.lang.Object var18 = var17.peek();
    java.lang.Object var19 = var17.peekFirst();
    boolean var21 = var17.add((java.lang.Object)'4');
    boolean var23 = var17.offerLast((java.lang.Object)1L);
    java.util.ListIterator var24 = var17.listIterator();
    java.lang.Object[] var25 = new java.lang.Object[] { var24};
    java.lang.Object[] var26 = var7.toArray(var25);
    java.lang.Object[] var27 = var0.toArray(var26);
    myLinkedList.MyLinkedList var29 = new myLinkedList.MyLinkedList();
    java.lang.Object var30 = var29.peek();
    myLinkedList.MyLinkedList var31 = new myLinkedList.MyLinkedList();
    java.lang.Object var32 = var31.peek();
    boolean var34 = var31.contains((java.lang.Object)1.0d);
    boolean var35 = var29.removeAll((java.util.Collection)var31);
    java.lang.Object var36 = var31.poll();
    boolean var37 = var0.addAll(0, (java.util.Collection)var31);
    myLinkedList.MyLinkedList var39 = new myLinkedList.MyLinkedList();
    java.util.Iterator var40 = var39.iterator();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      boolean var41 = var0.addAll(100, (java.util.Collection)var39);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var26);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var30);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var32);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var34 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var35 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var36);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var37 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var40);

  }

  public void test31() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test31");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    boolean var10 = var7.contains((java.lang.Object)1.0d);
    boolean var11 = var5.removeAll((java.util.Collection)var7);
    myLinkedList.MyLinkedList var12 = new myLinkedList.MyLinkedList();
    java.util.Iterator var13 = var12.iterator();
    java.util.Iterator var14 = var12.descendingIterator();
    java.util.Spliterator var15 = var12.spliterator();
    boolean var16 = var7.equals((java.lang.Object)var12);
    myLinkedList.MyLinkedList var17 = new myLinkedList.MyLinkedList();
    java.lang.Object var18 = var17.peek();
    java.lang.Object var19 = var17.peekFirst();
    boolean var21 = var17.add((java.lang.Object)'4');
    boolean var23 = var17.offerLast((java.lang.Object)1L);
    java.util.ListIterator var24 = var17.listIterator();
    java.lang.Object[] var25 = new java.lang.Object[] { var24};
    java.lang.Object[] var26 = var7.toArray(var25);
    java.lang.Object[] var27 = var0.toArray(var26);
    myLinkedList.MyLinkedList var29 = new myLinkedList.MyLinkedList();
    java.lang.Object var30 = var29.peek();
    myLinkedList.MyLinkedList var31 = new myLinkedList.MyLinkedList();
    java.lang.Object var32 = var31.peek();
    boolean var34 = var31.contains((java.lang.Object)1.0d);
    boolean var35 = var29.removeAll((java.util.Collection)var31);
    java.lang.Object var36 = var31.poll();
    boolean var37 = var0.addAll(0, (java.util.Collection)var31);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var38 = var31.removeFirst();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var26);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var30);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var32);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var34 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var35 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var36);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var37 == false);

  }

  public void test32() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test32");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    java.util.ListIterator var10 = var5.listIterator(0);
    boolean var12 = var5.removeFirstOccurrence((java.lang.Object)true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var12 == false);

  }

  public void test33() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test33");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var5 = var0.removeLast();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));

  }

  public void test34() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test34");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    boolean var4 = var0.contains((java.lang.Object)false);
    java.lang.Object[] var5 = var0.toArray();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var6 = var0.removeLast();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var5);

  }

  public void test35() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test35");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    boolean var7 = var0.containsAll((java.util.Collection)var5);
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    java.lang.Object var9 = var8.peek();
    java.lang.Object var10 = var8.peekFirst();
    boolean var12 = var8.add((java.lang.Object)'4');
    boolean var14 = var8.offerLast((java.lang.Object)1L);
    java.util.ListIterator var15 = var8.listIterator();
    myLinkedList.MyLinkedList var16 = new myLinkedList.MyLinkedList();
    java.lang.Object var17 = var16.peek();
    java.lang.Object var18 = var16.peekFirst();
    boolean var19 = var8.containsAll((java.util.Collection)var16);
    boolean var20 = var5.containsAll((java.util.Collection)var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var12 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var17);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var19 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var20 == false);

  }

  public void test36() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test36");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    java.lang.Object var9 = var0.peekLast();
    var0.clear();
    myLinkedList.MyLinkedList var11 = new myLinkedList.MyLinkedList();
    java.lang.Object var12 = var11.peek();
    java.lang.Object var13 = var11.peekFirst();
    boolean var15 = var11.add((java.lang.Object)'4');
    boolean var17 = var11.add((java.lang.Object)1.0d);
    java.lang.Object var18 = var11.removeLast();
    boolean var19 = var0.removeAll((java.util.Collection)var11);
    java.lang.Object var20 = var11.element();
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var12);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var15 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var17 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var18 + "' != '" + 1.0d+ "'", var18.equals(1.0d));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var19 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var20 + "' != '" + '4'+ "'", var20.equals('4'));

  }

  public void test37() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test37");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    boolean var3 = var0.contains((java.lang.Object)1.0d);
    int var4 = var0.size();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == 0);

  }

  public void test38() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test38");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    boolean var3 = var0.contains((java.lang.Object)1.0d);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var4 = var0.removeFirst();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var3 == false);

  }

  public void test39() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test39");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    boolean var6 = var0.add((java.lang.Object)1.0d);
    java.lang.Object var7 = var0.removeLast();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    java.lang.Object var9 = var8.peek();
    java.lang.Object var10 = var8.peekFirst();
    boolean var12 = var8.add((java.lang.Object)'4');
    boolean var14 = var8.offerLast((java.lang.Object)1L);
    myLinkedList.MyLinkedList var15 = new myLinkedList.MyLinkedList();
    java.lang.Object var16 = var15.peek();
    myLinkedList.MyLinkedList var17 = new myLinkedList.MyLinkedList();
    java.lang.Object var18 = var17.peek();
    boolean var20 = var17.contains((java.lang.Object)1.0d);
    boolean var21 = var15.removeAll((java.util.Collection)var17);
    myLinkedList.MyLinkedList var22 = new myLinkedList.MyLinkedList();
    java.util.Iterator var23 = var22.iterator();
    java.util.Iterator var24 = var22.descendingIterator();
    java.util.Spliterator var25 = var22.spliterator();
    boolean var26 = var17.equals((java.lang.Object)var22);
    myLinkedList.MyLinkedList var27 = new myLinkedList.MyLinkedList();
    java.lang.Object var28 = var27.peek();
    java.lang.Object var29 = var27.peekFirst();
    boolean var31 = var27.add((java.lang.Object)'4');
    boolean var33 = var27.offerLast((java.lang.Object)1L);
    java.util.ListIterator var34 = var27.listIterator();
    java.lang.Object[] var35 = new java.lang.Object[] { var34};
    java.lang.Object[] var36 = var17.toArray(var35);
    java.lang.Object[] var37 = var8.toArray(var35);
    myLinkedList.MyLinkedList var38 = new myLinkedList.MyLinkedList();
    boolean var40 = var38.equals((java.lang.Object)0.0d);
    boolean var42 = var38.contains((java.lang.Object)false);
    java.lang.Object[] var43 = var38.toArray();
    java.lang.Object[] var44 = var8.toArray(var43);
    myLinkedList.MyLinkedList var45 = new myLinkedList.MyLinkedList();
    boolean var47 = var45.equals((java.lang.Object)0.0d);
    java.util.ListIterator var48 = var45.listIterator();
    java.lang.String var49 = var45.toString();
    boolean var50 = var8.addAll((java.util.Collection)var45);
    boolean var51 = var0.removeAll((java.util.Collection)var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var7 + "' != '" + 1.0d+ "'", var7.equals(1.0d));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var12 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var16);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var23);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var26 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var28);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var29);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var31 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var33 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var34);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var35);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var36);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var37);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var40 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var42 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var43);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var44);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var47 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var48);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var49 + "' != '" + "[]"+ "'", var49.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var50 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var51 == true);

  }

  public void test40() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test40");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    boolean var10 = var7.contains((java.lang.Object)1.0d);
    boolean var11 = var5.removeAll((java.util.Collection)var7);
    myLinkedList.MyLinkedList var12 = new myLinkedList.MyLinkedList();
    java.util.Iterator var13 = var12.iterator();
    java.util.Iterator var14 = var12.descendingIterator();
    java.util.Spliterator var15 = var12.spliterator();
    boolean var16 = var7.equals((java.lang.Object)var12);
    boolean var17 = var0.equals((java.lang.Object)var7);
    myLinkedList.MyLinkedList var18 = new myLinkedList.MyLinkedList();
    boolean var20 = var18.equals((java.lang.Object)0.0d);
    java.util.ListIterator var21 = var18.listIterator();
    java.lang.String var22 = var18.toString();
    myLinkedList.MyLinkedList var23 = new myLinkedList.MyLinkedList();
    java.lang.Object var24 = var23.peek();
    java.lang.Object var25 = var23.peekFirst();
    boolean var26 = var18.offerFirst((java.lang.Object)var23);
    boolean var27 = var7.add((java.lang.Object)var26);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var17 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var21);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var22 + "' != '" + "[]"+ "'", var22.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var26 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var27 == true);

  }

  public void test41() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test41");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    java.lang.Object[] var5 = var0.toArray();
    java.util.Spliterator var6 = var0.spliterator();
    java.util.ListIterator var7 = var0.listIterator();
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var5);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);

  }

  public void test42() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test42");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    java.lang.Object var9 = var7.peekFirst();
    boolean var11 = var7.add((java.lang.Object)'4');
    boolean var13 = var7.offerLast((java.lang.Object)1L);
    java.util.ListIterator var14 = var7.listIterator();
    int var15 = var2.lastIndexOf((java.lang.Object)var7);
    java.lang.Object var16 = var7.removeFirst();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var15 == (-1));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var16 + "' != '" + '4'+ "'", var16.equals('4'));

  }

  public void test43() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test43");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    java.lang.Object var7 = var2.poll();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var8 = var2.removeFirst();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);

  }

  public void test44() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test44");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    java.lang.Object var7 = var2.poll();
    var2.addFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var10 = new myLinkedList.MyLinkedList();
    java.lang.Object var11 = var10.peek();
    java.lang.Object var12 = var10.peekFirst();
    boolean var14 = var10.add((java.lang.Object)'4');
    java.lang.Object var15 = var10.peekLast();
    java.lang.String var16 = var10.toString();
    boolean var18 = var10.offer((java.lang.Object)false);
    boolean var19 = var2.equals((java.lang.Object)false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var12);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var15 + "' != '" + '4'+ "'", var15.equals('4'));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var16 + "' != '" + "[4]"+ "'", var16.equals("[4]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var18 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var19 == false);

  }

  public void test45() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test45");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    java.lang.Object var3 = var0.peekLast();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    boolean var7 = var5.equals((java.lang.Object)0.0d);
    java.util.ListIterator var8 = var5.listIterator();
    java.lang.String var9 = var5.toString();
    myLinkedList.MyLinkedList var10 = new myLinkedList.MyLinkedList();
    java.lang.Object var11 = var10.peek();
    java.lang.Object var12 = var10.peekFirst();
    boolean var13 = var5.offerFirst((java.lang.Object)var10);
    java.lang.Object var14 = var5.peekLast();
    java.lang.Object var15 = var5.peekLast();
    myLinkedList.MyLinkedList var16 = new myLinkedList.MyLinkedList();
    boolean var18 = var16.equals((java.lang.Object)0.0d);
    java.util.ListIterator var19 = var16.listIterator();
    java.lang.String var20 = var16.toString();
    java.lang.Object[] var21 = var16.toArray();
    java.util.Spliterator var22 = var16.spliterator();
    boolean var23 = var5.retainAll((java.util.Collection)var16);
    myLinkedList.MyLinkedList var24 = new myLinkedList.MyLinkedList();
    java.lang.Object var25 = var24.peek();
    myLinkedList.MyLinkedList var26 = new myLinkedList.MyLinkedList();
    java.lang.Object var27 = var26.peek();
    boolean var29 = var26.contains((java.lang.Object)1.0d);
    boolean var30 = var24.removeAll((java.util.Collection)var26);
    myLinkedList.MyLinkedList var31 = new myLinkedList.MyLinkedList();
    java.util.Iterator var32 = var31.iterator();
    java.util.Iterator var33 = var31.descendingIterator();
    java.util.Spliterator var34 = var31.spliterator();
    boolean var35 = var26.equals((java.lang.Object)var31);
    boolean var36 = var5.containsAll((java.util.Collection)var31);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      boolean var37 = var0.addAll((-1), (java.util.Collection)var5);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var9 + "' != '" + "[]"+ "'", var9.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var12);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var20 + "' != '" + "[]"+ "'", var20.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var21);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var22);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var29 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var30 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var32);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var33);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var34);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var35 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var36 == true);

  }

  public void test46() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test46");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    var5.clear();
    myLinkedList.MyLinkedList var10 = new myLinkedList.MyLinkedList();
    java.lang.Object var11 = var10.peek();
    java.lang.Object var12 = var10.peekFirst();
    boolean var14 = var10.add((java.lang.Object)'4');
    boolean var16 = var10.offerLast((java.lang.Object)1L);
    boolean var17 = var5.addAll((java.util.Collection)var10);
    myLinkedList.MyLinkedList var18 = new myLinkedList.MyLinkedList();
    boolean var20 = var18.equals((java.lang.Object)0.0d);
    java.util.ListIterator var21 = var18.listIterator();
    java.lang.String var22 = var18.toString();
    myLinkedList.MyLinkedList var23 = new myLinkedList.MyLinkedList();
    java.lang.Object var24 = var23.peek();
    java.lang.Object var25 = var23.peekFirst();
    boolean var26 = var18.offerFirst((java.lang.Object)var23);
    boolean var27 = var10.retainAll((java.util.Collection)var23);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var12);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var17 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var21);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var22 + "' != '" + "[]"+ "'", var22.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var26 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var27 == true);

  }

  public void test47() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test47");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    boolean var6 = var0.offerLast((java.lang.Object)1L);
    java.util.ListIterator var7 = var0.listIterator();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    java.lang.Object var9 = var8.peek();
    java.lang.Object var10 = var8.peekFirst();
    boolean var11 = var0.containsAll((java.util.Collection)var8);
    java.lang.Object var12 = var8.peek();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var12);

  }

  public void test48() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test48");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    boolean var4 = var0.contains((java.lang.Object)false);
    java.lang.Object[] var5 = var0.toArray();
    myLinkedList.MyLinkedList var6 = new myLinkedList.MyLinkedList();
    java.lang.Object var7 = var6.peek();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    java.lang.Object var9 = var8.peek();
    boolean var11 = var8.contains((java.lang.Object)1.0d);
    boolean var12 = var6.removeAll((java.util.Collection)var8);
    java.util.Iterator var13 = var6.iterator();
    java.util.stream.Stream var14 = var6.stream();
    var0.push((java.lang.Object)var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var5);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);

  }

  public void test49() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test49");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    boolean var4 = var0.offer((java.lang.Object)10.0d);
    java.util.ListIterator var6 = var0.listIterator(0);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var6);

  }

  public void test50() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test50");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    java.lang.Object var9 = var0.peekLast();
    java.lang.Object var10 = var0.peekLast();
    myLinkedList.MyLinkedList var11 = new myLinkedList.MyLinkedList();
    boolean var13 = var11.equals((java.lang.Object)0.0d);
    java.util.ListIterator var14 = var11.listIterator();
    java.lang.String var15 = var11.toString();
    java.lang.Object[] var16 = var11.toArray();
    java.util.Spliterator var17 = var11.spliterator();
    boolean var18 = var0.retainAll((java.util.Collection)var11);
    myLinkedList.MyLinkedList var19 = new myLinkedList.MyLinkedList();
    java.lang.Object var20 = var19.peek();
    myLinkedList.MyLinkedList var21 = new myLinkedList.MyLinkedList();
    java.lang.Object var22 = var21.peek();
    boolean var24 = var21.contains((java.lang.Object)1.0d);
    boolean var25 = var19.removeAll((java.util.Collection)var21);
    myLinkedList.MyLinkedList var26 = new myLinkedList.MyLinkedList();
    java.util.Iterator var27 = var26.iterator();
    java.util.Iterator var28 = var26.descendingIterator();
    java.util.Spliterator var29 = var26.spliterator();
    boolean var30 = var21.equals((java.lang.Object)var26);
    boolean var31 = var0.containsAll((java.util.Collection)var26);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var32 = var0.removeFirst();
      fail("Expected exception of type java.util.NoSuchElementException");
    } catch (java.util.NoSuchElementException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var15 + "' != '" + "[]"+ "'", var15.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var16);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var17);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var18 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var20);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var22);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var25 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var28);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var29);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var30 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var31 == true);

  }

  public void test51() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test51");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var10 = var5.remove(100);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);

  }

  public void test52() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test52");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    java.util.Iterator var7 = var0.iterator();
    java.lang.Object var8 = var0.peek();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);

  }

  public void test53() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test53");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    java.lang.Object[] var5 = var0.toArray();
    java.util.Spliterator var6 = var0.spliterator();
    java.util.stream.Stream var7 = var0.stream();
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var5);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);

  }

  public void test54() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test54");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    java.lang.Object var3 = var0.peekLast();
    java.util.stream.Stream var4 = var0.parallelStream();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var4);

  }

  public void test55() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test55");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    var5.clear();
    boolean var11 = var5.add((java.lang.Object)(-1.0d));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);

  }

  public void test56() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test56");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var9 = var5.add((java.lang.Object)'4');
    boolean var11 = var5.add((java.lang.Object)1.0d);
    java.lang.Object var12 = var5.removeLast();
    boolean var13 = var0.equals(var12);
    myLinkedList.MyLinkedList var14 = new myLinkedList.MyLinkedList();
    java.lang.Object var15 = var14.peek();
    java.lang.Object var16 = var14.peekFirst();
    boolean var18 = var14.add((java.lang.Object)'4');
    boolean var20 = var14.offerLast((java.lang.Object)1L);
    myLinkedList.MyLinkedList var21 = new myLinkedList.MyLinkedList();
    java.lang.Object var22 = var21.peek();
    myLinkedList.MyLinkedList var23 = new myLinkedList.MyLinkedList();
    java.lang.Object var24 = var23.peek();
    boolean var26 = var23.contains((java.lang.Object)1.0d);
    boolean var27 = var21.removeAll((java.util.Collection)var23);
    myLinkedList.MyLinkedList var28 = new myLinkedList.MyLinkedList();
    java.util.Iterator var29 = var28.iterator();
    java.util.Iterator var30 = var28.descendingIterator();
    java.util.Spliterator var31 = var28.spliterator();
    boolean var32 = var23.equals((java.lang.Object)var28);
    myLinkedList.MyLinkedList var33 = new myLinkedList.MyLinkedList();
    java.lang.Object var34 = var33.peek();
    java.lang.Object var35 = var33.peekFirst();
    boolean var37 = var33.add((java.lang.Object)'4');
    boolean var39 = var33.offerLast((java.lang.Object)1L);
    java.util.ListIterator var40 = var33.listIterator();
    java.lang.Object[] var41 = new java.lang.Object[] { var40};
    java.lang.Object[] var42 = var23.toArray(var41);
    java.lang.Object[] var43 = var14.toArray(var41);
    myLinkedList.MyLinkedList var44 = new myLinkedList.MyLinkedList();
    boolean var46 = var44.equals((java.lang.Object)0.0d);
    boolean var48 = var44.contains((java.lang.Object)false);
    java.lang.Object[] var49 = var44.toArray();
    java.lang.Object[] var50 = var14.toArray(var49);
    int var51 = var0.lastIndexOf((java.lang.Object)var49);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var9 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var12 + "' != '" + 1.0d+ "'", var12.equals(1.0d));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var16);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var18 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var20 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var22);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var26 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var27 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var29);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var30);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var31);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var32 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var34);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var35);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var37 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var39 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var40);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var41);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var42);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var43);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var46 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var48 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var49);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var50);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var51 == (-1));

  }

  public void test57() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test57");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    boolean var9 = var7.equals((java.lang.Object)0.0d);
    java.util.ListIterator var10 = var7.listIterator();
    int var12 = var7.lastIndexOf((java.lang.Object)(byte)100);
    java.util.Iterator var13 = var7.iterator();
    java.lang.Object var14 = var7.clone();
    boolean var15 = var2.insertar(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var12 == (-1));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var15 == true);

  }

  public void test58() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test58");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    boolean var10 = var7.contains((java.lang.Object)1.0d);
    boolean var11 = var5.removeAll((java.util.Collection)var7);
    myLinkedList.MyLinkedList var12 = new myLinkedList.MyLinkedList();
    java.util.Iterator var13 = var12.iterator();
    java.util.Iterator var14 = var12.descendingIterator();
    java.util.Spliterator var15 = var12.spliterator();
    boolean var16 = var7.equals((java.lang.Object)var12);
    myLinkedList.MyLinkedList var17 = new myLinkedList.MyLinkedList();
    java.lang.Object var18 = var17.peek();
    java.lang.Object var19 = var17.peekFirst();
    boolean var21 = var17.add((java.lang.Object)'4');
    boolean var23 = var17.offerLast((java.lang.Object)1L);
    java.util.ListIterator var24 = var17.listIterator();
    java.lang.Object[] var25 = new java.lang.Object[] { var24};
    java.lang.Object[] var26 = var7.toArray(var25);
    java.lang.Object[] var27 = var0.toArray(var26);
    myLinkedList.MyLinkedList var29 = new myLinkedList.MyLinkedList();
    java.lang.Object var30 = var29.peek();
    myLinkedList.MyLinkedList var31 = new myLinkedList.MyLinkedList();
    java.lang.Object var32 = var31.peek();
    boolean var34 = var31.contains((java.lang.Object)1.0d);
    boolean var35 = var29.removeAll((java.util.Collection)var31);
    java.lang.Object var36 = var31.poll();
    boolean var37 = var0.addAll(0, (java.util.Collection)var31);
    java.lang.Object var38 = var0.getFirst();
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var26);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var30);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var32);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var34 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var35 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var36);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var37 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var38 + "' != '" + (-1L)+ "'", var38.equals((-1L)));

  }

  public void test59() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test59");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    java.util.Iterator var7 = var0.iterator();
    boolean var9 = var0.offerLast((java.lang.Object)(short)100);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var9 == true);

  }

  public void test60() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test60");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    myLinkedList.MyLinkedList var2 = new myLinkedList.MyLinkedList();
    java.lang.Object var3 = var2.peek();
    boolean var5 = var2.contains((java.lang.Object)1.0d);
    boolean var6 = var0.removeAll((java.util.Collection)var2);
    java.lang.Object var7 = var2.poll();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    boolean var10 = var8.equals((java.lang.Object)0.0d);
    java.util.ListIterator var11 = var8.listIterator();
    int var13 = var8.lastIndexOf((java.lang.Object)(byte)100);
    java.util.Iterator var14 = var8.iterator();
    java.lang.Object var15 = var8.clone();
    boolean var16 = var2.offerFirst(var15);
    java.util.Iterator var17 = var2.iterator();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == (-1));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var17);

  }

  public void test61() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test61");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    java.lang.Object var5 = var0.peekLast();
    java.lang.String var6 = var0.toString();
    boolean var8 = var0.removeLastOccurrence((java.lang.Object)false);
    myLinkedList.MyLinkedList var9 = new myLinkedList.MyLinkedList();
    java.util.Iterator var10 = var9.iterator();
    java.util.Iterator var11 = var9.descendingIterator();
    boolean var13 = var9.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var14 = new myLinkedList.MyLinkedList();
    java.lang.Object var15 = var14.peek();
    myLinkedList.MyLinkedList var16 = new myLinkedList.MyLinkedList();
    java.lang.Object var17 = var16.peek();
    boolean var19 = var16.contains((java.lang.Object)1.0d);
    boolean var20 = var14.removeAll((java.util.Collection)var16);
    myLinkedList.MyLinkedList var21 = new myLinkedList.MyLinkedList();
    java.util.Iterator var22 = var21.iterator();
    java.util.Iterator var23 = var21.descendingIterator();
    java.util.Spliterator var24 = var21.spliterator();
    boolean var25 = var16.equals((java.lang.Object)var21);
    myLinkedList.MyLinkedList var26 = new myLinkedList.MyLinkedList();
    java.lang.Object var27 = var26.peek();
    java.lang.Object var28 = var26.peekFirst();
    boolean var30 = var26.add((java.lang.Object)'4');
    boolean var32 = var26.offerLast((java.lang.Object)1L);
    java.util.ListIterator var33 = var26.listIterator();
    java.lang.Object[] var34 = new java.lang.Object[] { var33};
    java.lang.Object[] var35 = var16.toArray(var34);
    java.lang.Object[] var36 = var9.toArray(var35);
    java.lang.Object[] var37 = var0.toArray(var36);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var5 + "' != '" + '4'+ "'", var5.equals('4'));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var6 + "' != '" + "[4]"+ "'", var6.equals("[4]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var17);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var19 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var22);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var23);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var25 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var28);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var30 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var32 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var33);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var34);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var35);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var36);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var37);

  }

  public void test62() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test62");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    boolean var6 = var0.add((java.lang.Object)1.0d);
    java.lang.Object var7 = var0.getLast();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    boolean var10 = var8.equals((java.lang.Object)0.0d);
    java.util.ListIterator var11 = var8.listIterator();
    java.lang.String var12 = var8.toString();
    java.lang.Object[] var13 = var8.toArray();
    java.util.Spliterator var14 = var8.spliterator();
    boolean var15 = var0.removeFirstOccurrence((java.lang.Object)var8);
    java.lang.Object var16 = var8.clone();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var7 + "' != '" + 1.0d+ "'", var7.equals(1.0d));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var12 + "' != '" + "[]"+ "'", var12.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var16);

  }

  public void test63() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test63");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    var0.clear();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.util.ListIterator var3 = var0.listIterator(10);
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }

  }

  public void test64() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test64");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    java.lang.Object var9 = var0.peekLast();
    java.lang.Object var10 = var0.peekLast();
    myLinkedList.MyLinkedList var11 = new myLinkedList.MyLinkedList();
    boolean var13 = var11.equals((java.lang.Object)0.0d);
    java.util.ListIterator var14 = var11.listIterator();
    java.lang.String var15 = var11.toString();
    java.lang.Object[] var16 = var11.toArray();
    java.util.Spliterator var17 = var11.spliterator();
    boolean var18 = var0.retainAll((java.util.Collection)var11);
    myLinkedList.MyLinkedList var19 = new myLinkedList.MyLinkedList();
    java.lang.Object var20 = var19.peek();
    myLinkedList.MyLinkedList var21 = new myLinkedList.MyLinkedList();
    java.lang.Object var22 = var21.peek();
    boolean var24 = var21.contains((java.lang.Object)1.0d);
    boolean var25 = var19.removeAll((java.util.Collection)var21);
    var19.clear();
    boolean var27 = var11.add((java.lang.Object)var19);
    myLinkedList.MyLinkedList var28 = new myLinkedList.MyLinkedList();
    java.lang.Object var29 = var28.peek();
    java.lang.Object var30 = var28.peekFirst();
    boolean var32 = var28.add((java.lang.Object)'4');
    java.lang.Object var33 = var28.peekLast();
    java.lang.String var34 = var28.toString();
    boolean var36 = var28.removeLastOccurrence((java.lang.Object)false);
    myLinkedList.MyLinkedList var37 = new myLinkedList.MyLinkedList();
    java.lang.Object var38 = var37.peek();
    java.lang.Object var39 = var37.peekFirst();
    boolean var41 = var37.add((java.lang.Object)'4');
    boolean var43 = var37.offerLast((java.lang.Object)1L);
    java.util.ListIterator var44 = var37.listIterator();
    myLinkedList.MyLinkedList var45 = new myLinkedList.MyLinkedList();
    java.lang.Object var46 = var45.peek();
    java.lang.Object var47 = var45.peekFirst();
    boolean var48 = var37.containsAll((java.util.Collection)var45);
    var28.addFirst((java.lang.Object)var37);
    var11.push((java.lang.Object)var28);
    java.lang.Object var51 = var28.poll();
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var15 + "' != '" + "[]"+ "'", var15.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var16);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var17);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var18 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var20);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var22);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var25 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var27 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var29);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var30);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var32 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var33 + "' != '" + '4'+ "'", var33.equals('4'));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var34 + "' != '" + "[4]"+ "'", var34.equals("[4]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var36 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var38);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var39);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var41 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var43 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var44);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var46);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var47);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var48 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var51);

  }

  public void test65() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test65");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    java.lang.Object var7 = var5.peekFirst();
    boolean var8 = var0.offerFirst((java.lang.Object)var5);
    java.lang.Object var9 = var0.peekLast();
    java.lang.Object var10 = var0.peekLast();
    myLinkedList.MyLinkedList var11 = new myLinkedList.MyLinkedList();
    boolean var13 = var11.equals((java.lang.Object)0.0d);
    java.util.ListIterator var14 = var11.listIterator();
    java.lang.String var15 = var11.toString();
    java.lang.Object[] var16 = var11.toArray();
    java.util.Spliterator var17 = var11.spliterator();
    boolean var18 = var0.retainAll((java.util.Collection)var11);
    java.util.Iterator var19 = var11.descendingIterator();
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var7);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var9);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var10);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var15 + "' != '" + "[]"+ "'", var15.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var16);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var17);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var18 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var19);

  }

  public void test66() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test66");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    boolean var2 = var0.equals((java.lang.Object)0.0d);
    java.util.ListIterator var3 = var0.listIterator();
    java.lang.String var4 = var0.toString();
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    boolean var10 = var7.contains((java.lang.Object)1.0d);
    boolean var11 = var5.removeAll((java.util.Collection)var7);
    myLinkedList.MyLinkedList var12 = new myLinkedList.MyLinkedList();
    java.util.Iterator var13 = var12.iterator();
    java.util.Iterator var14 = var12.descendingIterator();
    java.util.Spliterator var15 = var12.spliterator();
    boolean var16 = var7.equals((java.lang.Object)var12);
    boolean var17 = var0.equals((java.lang.Object)var7);
    myLinkedList.MyLinkedList var18 = new myLinkedList.MyLinkedList();
    java.lang.Object var19 = var18.peek();
    java.lang.Object var20 = var18.peekFirst();
    boolean var22 = var18.add((java.lang.Object)'4');
    boolean var24 = var18.offerLast((java.lang.Object)1L);
    java.util.ListIterator var25 = var18.listIterator();
    myLinkedList.MyLinkedList var26 = new myLinkedList.MyLinkedList();
    java.lang.Object var27 = var26.peek();
    java.lang.Object var28 = var26.peekFirst();
    boolean var29 = var18.containsAll((java.util.Collection)var26);
    boolean var30 = var0.removeAll((java.util.Collection)var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var3);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var4 + "' != '" + "[]"+ "'", var4.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var17 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var20);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var22 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var24 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var28);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var29 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var30 == false);

  }

  public void test67() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test67");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList((java.util.Collection)var0);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);

  }

  public void test68() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test68");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    boolean var6 = var0.add((java.lang.Object)1.0d);
    java.lang.Object var7 = var0.getLast();
    myLinkedList.MyLinkedList var8 = new myLinkedList.MyLinkedList();
    boolean var10 = var8.equals((java.lang.Object)0.0d);
    java.util.ListIterator var11 = var8.listIterator();
    java.lang.String var12 = var8.toString();
    java.lang.Object[] var13 = var8.toArray();
    java.util.Spliterator var14 = var8.spliterator();
    boolean var15 = var0.removeFirstOccurrence((java.lang.Object)var8);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      java.lang.Object var17 = var0.remove((-1));
      fail("Expected exception of type java.lang.IndexOutOfBoundsException");
    } catch (java.lang.IndexOutOfBoundsException e) {
      // Expected exception.
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var7 + "' != '" + 1.0d+ "'", var7.equals(1.0d));
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var11);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var12 + "' != '" + "[]"+ "'", var12.equals("[]"));
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var15 == false);

  }

  public void test69() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test69");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.lang.Object var1 = var0.peek();
    java.lang.Object var2 = var0.peekFirst();
    boolean var4 = var0.add((java.lang.Object)'4');
    java.lang.Object var5 = var0.element();
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue("'" + var5 + "' != '" + '4'+ "'", var5.equals('4'));

  }

  public void test70() throws Throwable {

    if (debug) System.out.printf("%nRandoopTest0.test70");


    myLinkedList.MyLinkedList var0 = new myLinkedList.MyLinkedList();
    java.util.Iterator var1 = var0.iterator();
    java.util.Iterator var2 = var0.descendingIterator();
    boolean var4 = var0.offerFirst((java.lang.Object)(-1L));
    myLinkedList.MyLinkedList var5 = new myLinkedList.MyLinkedList();
    java.lang.Object var6 = var5.peek();
    myLinkedList.MyLinkedList var7 = new myLinkedList.MyLinkedList();
    java.lang.Object var8 = var7.peek();
    boolean var10 = var7.contains((java.lang.Object)1.0d);
    boolean var11 = var5.removeAll((java.util.Collection)var7);
    myLinkedList.MyLinkedList var12 = new myLinkedList.MyLinkedList();
    java.util.Iterator var13 = var12.iterator();
    java.util.Iterator var14 = var12.descendingIterator();
    java.util.Spliterator var15 = var12.spliterator();
    boolean var16 = var7.equals((java.lang.Object)var12);
    myLinkedList.MyLinkedList var17 = new myLinkedList.MyLinkedList();
    java.lang.Object var18 = var17.peek();
    java.lang.Object var19 = var17.peekFirst();
    boolean var21 = var17.add((java.lang.Object)'4');
    boolean var23 = var17.offerLast((java.lang.Object)1L);
    java.util.ListIterator var24 = var17.listIterator();
    java.lang.Object[] var25 = new java.lang.Object[] { var24};
    java.lang.Object[] var26 = var7.toArray(var25);
    java.lang.Object[] var27 = var0.toArray(var26);
    myLinkedList.MyLinkedList var29 = new myLinkedList.MyLinkedList();
    java.lang.Object var30 = var29.peek();
    myLinkedList.MyLinkedList var31 = new myLinkedList.MyLinkedList();
    java.lang.Object var32 = var31.peek();
    boolean var34 = var31.contains((java.lang.Object)1.0d);
    boolean var35 = var29.removeAll((java.util.Collection)var31);
    java.lang.Object var36 = var31.poll();
    boolean var37 = var0.addAll(0, (java.util.Collection)var31);
    myLinkedList.MyLinkedList var38 = new myLinkedList.MyLinkedList();
    java.lang.Object var39 = var38.peek();
    java.lang.Object var40 = var38.pollLast();
    int var41 = var31.indexOf((java.lang.Object)var38);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var1);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var2);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var6);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var8);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var13);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var14);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var15);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var18);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var19);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var24);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var25);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var26);
    
    // Regression assertion (captures the current behavior of the code)
    assertNotNull(var27);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var30);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var32);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var34 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var35 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var36);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var37 == false);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var39);
    
    // Regression assertion (captures the current behavior of the code)
    assertNull(var40);
    
    // Regression assertion (captures the current behavior of the code)
    assertTrue(var41 == (-1));

  }

}
