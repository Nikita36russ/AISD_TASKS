import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class task3zad2v2 extends JFrame {
    private Queue<Integer> q1 = new LinkedList<>();
    private Queue<Integer> q2 = new LinkedList<>();
    private JTextArea outputArea;

    public task3zad2v2() {
        setTitle("Задача 3 - стандартная библиотека");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JButton loadButton = new JButton("Загрузить из файла");
        loadButton.addActionListener(e -> loadFile());

        JButton runButton = new JButton("Выполнить");
        runButton.addActionListener(e -> executeAlgorithm());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(runButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (Scanner scanner = new Scanner(fileChooser.getSelectedFile())) {
                q1 = new LinkedList<>(); // Сброс перед загрузкой
                q2 = new LinkedList<>();

                if (scanner.hasNextLine()) {
                    String[] line1 = scanner.nextLine().trim().split("\\s+");
                    for (String s : line1) if (!s.isEmpty()) q1.add(Integer.parseInt(s));
                }
                if (scanner.hasNextLine()) {
                    String[] line2 = scanner.nextLine().trim().split("\\s+");
                    for (String s : line2) if (!s.isEmpty()) q2.add(Integer.parseInt(s));
                }

                outputArea.setText("Данные успешно загружены!\n");
                outputArea.append("Исходная 1 очередь: " + q1 + "\n");
                outputArea.append("Исходная 2 очередь: " + q2 + "\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка чтения файла: " + ex.getMessage());
            }
        }
    }

    private void executeAlgorithm() {
        if (q1.isEmpty() && q2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Сначала загрузите данные из файла.");
            return;
        }

        outputArea.append("\n--- Выполнение алгоритма ---\n");
        int size1 = q1.size();
        int size2 = q2.size();

        for (int i = 0; i < size2; i++) {
            int val = q2.poll();
            q1.add(val);
            q2.add(val);
        }

        for (int i = 0; i < size1; i++) {
            int val = q1.poll();
            q2.add(val);
            q1.add(val);
        }

        outputArea.append("Стало в 1 очереди: " + q1 + "\n");
        outputArea.append("Стало во 2 очереди: " + q2 + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new task3zad2v2().setVisible(true));
    }
}