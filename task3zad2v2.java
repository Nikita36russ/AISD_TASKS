import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

public class task3zad2v2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Queue<Integer> q1 = new LinkedList<>();
        Queue<Integer> q2 = new LinkedList<>();

        System.out.println("Введите первую очередь (через пробел):");
        for (String s : scanner.nextLine().trim().split("\\s+"))
            q1.add(Integer.parseInt(s));

        System.out.println("Введите вторую очередь (через пробел):");
        for (String s : scanner.nextLine().trim().split("\\s+"))
            q2.add(Integer.parseInt(s));

        scanner.close();

        System.out.println("\n-------------------------------------");
        System.out.println("Было в 1 очереди: " + q1);
        System.out.println("Было во 2 очереди: " + q2);

        int size1 = q1.size();
        int size2 = q2.size();

        for (int i = 0; i < size1; i++) {
            int val = q1.poll();
            q1.add(val);
            q2.add(val);
        }
        for (int i = 0; i < size2; i++) {
            int val = q2.poll();
            q2.add(val);
            q1.add(val);
        }

        System.out.println("Стало в 1 очереди: " + q1);
        System.out.println("Стало во 2 очереди: " + q2);
    }
}