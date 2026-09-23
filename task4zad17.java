import java.util.*;

public class task4zad17 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите текст:");
        String text = scanner.nextLine().toLowerCase();
        scanner.close();

        HashMap<String, Integer> map = new HashMap<>();
        
        String[] words = text.split("[^a-zа-яё]+");

        for (String word : words) {
            if (word.length() < 2) continue;
            
            for (int i = 0; i < word.length() - 1; i++) {
                String bigram = word.substring(i, i + 2);
                map.put(bigram, map.getOrDefault(bigram, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                if (list.get(j).getValue() < list.get(j + 1).getValue()) {
                    Map.Entry<String, Integer> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

        System.out.println("\n=== Результат ===");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println("Пара " + entry.getKey() + " встречается " + entry.getValue() + " раз");
        }
    }
}