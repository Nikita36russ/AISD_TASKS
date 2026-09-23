import java.util.Scanner;

class Node {
    String data;
    Node next;

    Node(String data) {
        this.data = data;
        this.next = null;
    }
}

class MyLinkedList {
    Node head;

    public void add(String data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public void removeDuplicates() {
        Node current = head;

        while (current != null && current.next != null) {
            if (current.data.equals(current.next.data)) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
    }
}

public class task2zad17 {
    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите слова через пробел (например: a a b b c a a):");
        String input = scanner.nextLine();

        String[] words = input.split(" ");
        for (String word : words) {
            list.add(word);
        }

        System.out.print("Исходный список: ");
        list.printList();

        list.removeDuplicates();

        System.out.print("Список после удаления подряд идущих дубликатов: ");
        list.printList();

        scanner.close();
    }
}