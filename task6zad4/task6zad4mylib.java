import java.util.*;

class MyHashMap {
    private static class Node {
        String key;
        int value;
        Node next;
        Node(String k, int v) { key = k; value = v; }
    }

    private Node[] table = new Node[16];
    private int size = 0;

    private int idx(String key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public void put(String key, int value) {
        int i = idx(key);
        for (Node n = table[i]; n != null; n = n.next) {
            if (n.key.equals(key)) { n.value = value; return; }
        }
        Node node = new Node(key, value);
        node.next = table[i];
        table[i] = node;
        size++;
    }

    public int get(String key) {
        for (Node n = table[idx(key)]; n != null; n = n.next)
            if (n.key.equals(key)) return n.value;
        return 0;
    }

    public boolean containsKey(String key) {
        for (Node n = table[idx(key)]; n != null; n = n.next)
            if (n.key.equals(key)) return true;
        return false;
    }

    public int size() { return size; }

    public List<String[]> entries() {
        List<String[]> list = new ArrayList<>();
        for (Node head : table)
            for (Node n = head; n != null; n = n.next)
                list.add(new String[]{n.key, String.valueOf(n.value)});
        return list;
    }
}

public class task6zad4mylib {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст (Enter дважды для окончания):");

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) break;
            sb.append(line).append(" ");
        }
        scanner.close();

        String text = sb.toString().toLowerCase();
        String[] segments = text.split("[^a-zA-Zа-яА-ЯёЁ\\s]+");

        MyHashMap map = new MyHashMap();

        for (String segment : segments) {
            String[] words = segment.trim().split("\\s+");
            for (int i = 0; i < words.length - 1; i++) {
                if (words[i].isEmpty() || words[i + 1].isEmpty()) continue;
                String bigram = words[i] + " " + words[i + 1];
                int count = map.containsKey(bigram) ? map.get(bigram) : 0;
                map.put(bigram, count + 1);
            }
        }

        int max = 0;
        for (String[] e : map.entries()) {
            int v = Integer.parseInt(e[1]);
            if (v > max) max = v;
        }

        System.out.println("\n=== Наиболее частые словосочетания ===");
        for (String[] e : map.entries())
            if (Integer.parseInt(e[1]) == max)
                System.out.println("\"" + e[0] + "\" -> " + e[1] + " раз");
    }
}