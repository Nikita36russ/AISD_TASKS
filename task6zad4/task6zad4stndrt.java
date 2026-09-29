import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class task6zad4stndrt extends JFrame {
    private JTextArea outputArea;

    public task6zad4stndrt() {
        setTitle("Задача 6 - стандарт");
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

        StringBuilder result = new StringBuilder();
        result.append("=== Наиболее частые словосочетания ===\n\n");

        if (max == 0) {
            result.append("Словосочетания не найдены.\n");
        } else {
            for (Map.Entry<String, Integer> e : map.entrySet()) {
                if (e.getValue() == max) {
                    result.append("\"").append(e.getKey()).append("\" -> ").append(e.getValue()).append(" раз\n");
                }
            }
        }

        outputArea.setText(result.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new task6zad4stndrt().setVisible(true));
    }
}