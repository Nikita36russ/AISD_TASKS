import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MyLib {
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

public class task6zad4mylib extends JFrame {
    private JTextArea outputArea;

    public task6zad4mylib() {
        setTitle("Задача 6 - своя реализация");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JButton loadButton = new JButton("Загрузить файл");
        loadButton.setFont(new Font("Dialog", Font.BOLD, 14));
        loadButton.addActionListener(e -> loadFileAndProcess());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadFileAndProcess() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(file, "UTF-8")) {
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine()).append(" ");
                }
                processText(sb.toString());
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Файл не найден", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка чтения: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void processText(String text) {
        text = text.toLowerCase();
        String[] segments = text.split("[^a-zA-Zа-яА-ЯёЁ\\s]+");

        MyLib map = new MyLib();

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

        StringBuilder result = new StringBuilder();
        result.append("=== Наиболее частые словосочетания ===\n\n");
        
        if (max == 0) {
            result.append("Словосочетания не найдены.\n");
        } else {
            for (String[] e : map.entries()) {
                if (Integer.parseInt(e[1]) == max) {
                    result.append("\"").append(e[0]).append("\" -> ").append(e[1]).append(" раз\n");
                }
            }
        }
        
        outputArea.setText(result.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new task6zad4mylib().setVisible(true));
    }
}