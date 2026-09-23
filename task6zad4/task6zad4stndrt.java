
import java.util.*;

public class task6zad4stndrt {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст (нажмите Enter дважды):");

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) break;
            sb.append(line).append(" ");
        }
        scanner.close();

        String text = sb.toString().toLowerCase();
        String[] segments = text.split("[^a-zA-Zа-яА-ЯёЁ\\s]+");

        Map<String, Integer> map = new HashMap<>();

        for (String segment : segments) {
            String[] words = segment.trim().split("\\s+");
            for (int i = 0; i < words.length - 1; i++) {
                if (words[i].isEmpty() || words[i + 1].isEmpty()) continue;
                String bigram = words[i] + " " + words[i + 1];
                map.put(bigram, map.getOrDefault(bigram, 0) + 1);
            }
        }

        int max = 0;
        for (int v : map.values()) if (v > max) max = v;

        System.out.println("\n---- Наиболее частые словосочетания ----");
        for (Map.Entry<String, Integer> e : map.entrySet())
            if (e.getValue() == max)
                System.out.println("\"" + e.getKey() + "\" " + e.getValue() + " раз");
    }
}