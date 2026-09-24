import java.util.*;

public class task7zad1 {
    static int n;
    static int[][] matrix;
    static String[] names;
    static boolean[] banned;
    static boolean[] visited;
    static List<Integer> path = new ArrayList<>();

    static boolean dfs(int cur, int end) {
        if (banned[cur] || visited[cur]) return false;
        visited[cur] = true;
        path.add(cur);

        if (cur == end) return true;

        for (int i = 0; i < n; i++) {
            if (matrix[cur][i] == 1 && dfs(i, end)) return true;
        }

        path.remove(path.size() - 1);
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введите вершины через пробел: ");
        names = sc.nextLine().trim().split("\\s+");
        n = names.length;

        matrix = new int[n][n];
        System.out.println("Введите матрицу смежности (" + n + " строк по " + n + " чисел):");
        for (int i = 0; i < n; i++) {
            System.out.print("Из " + names[i] + ": ");
            String[] row = sc.nextLine().trim().split("\\s+");
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Integer.parseInt(row[j]);
            }
        }

        System.out.print("Город A: ");
        String a = sc.nextLine().trim();
        System.out.print("Город Б: ");
        String b = sc.nextLine().trim();

        System.out.print("Запрещённые вершины через пробел (Нажмите Enter если их нет): ");
        String line = sc.nextLine().trim();
        banned = new boolean[n];
        if (!line.isEmpty()) {
            for (String s : line.split("\\s+")) {
                for (int i = 0; i < n; i++) {
                    if (names[i].equals(s)) banned[i] = true;
                }
            }
        }

        int start = -1, end = -1;
        for (int i = 0; i < n; i++) {
            if (names[i].equals(a)) start = i;
            if (names[i].equals(b)) end = i;
        }

        visited = new boolean[n];
        if (start == -1 || end == -1 || !dfs(start, end)) {
            System.out.println("Путь не найден");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                sb.append(names[path.get(i)]);
                if (i < path.size() - 1) sb.append(" -> ");
            }
            System.out.println("Путь: " + sb);
        }

        sc.close();
    }
}