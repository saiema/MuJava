package bugHunting.myLinkedList;


public  class Entry<E> {
     E element;
     Entry<E> next;
     Entry<E> previous;

    Entry(E element, Entry<E> next, Entry<E> previous) {
        this.element = element;
        this.next = next;
        this.previous = previous;
    }
}
