import java.util.Scanner;

class MyQueue {
    private class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    public void add(int data) {
        Node newNode = new Node(data);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public int size() { return size; }

    public void print() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}

public class task3zad2v1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MyQueue q1 = new MyQueue();
        MyQueue q2 = new MyQueue();

        System.out.println("Введите первую очередь (через пробел):");
        for (String s : scanner.nextLine().trim().split("\\s+"))
            q1.add(Integer.parseInt(s));

        System.out.println("Введите вторую очередь (через пробел):");
        for (String s : scanner.nextLine().trim().split("\\s+"))
            q2.add(Integer.parseInt(s));

        scanner.close();
        
        System.out.println("\n--------------------------");
        System.out.print("Было в 1 очереди: "); q1.print();
        System.out.print("Было во 2 очереди: "); q2.print();

        System.out.print("Стало в 1 очереди: "); q1.print();
        System.out.print("Стало во 2 очереди: "); q2.print();
    }
}