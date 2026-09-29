import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class task4zad17 extends JFrame {
    private JTextArea inputArea;
    private JTextArea outputArea;

    public task4zad17() {
        setTitle("Задача 4");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Введите текст для анализа:"), BorderLayout.NORTH);
        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("Dialog", Font.PLAIN, 14));
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Результат (от частых к редким):"), BorderLayout.NORTH);
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JButton processButton = new JButton("Выполнить");
        processButton.setFont(new Font("Dialog", Font.BOLD, 14));
        processButton.addActionListener(e -> processText());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(processButton);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, outputPanel);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void processText() {
        String text = inputArea.getText().toLowerCase();
        if (text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите текст.");
            return;
        }

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

        StringBuilder result = new StringBuilder();
        if (list.isEmpty()) {
            result.append("Не найдено ни одной пары букв (биграммы).\n");
        } else {
            for (Map.Entry<String, Integer> entry : list) {
                result.append("Пара '").append(entry.getKey())
                      .append("' встречается ").append(entry.getValue()).append(" раз\n");
            }
        }

        outputArea.setText(result.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new task4zad17().setVisible(true));
    }
}