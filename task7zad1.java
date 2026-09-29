import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class task7zad1 extends JFrame {
    private int n;
    private int[][] matrix;
    private String[] names;
    private boolean[] banned;
    private boolean[] visited;
    private List<Integer> path = new ArrayList<>();

    private JTextArea verticesArea;
    private JTextArea matrixArea;
    private JTextField cityAField;
    private JTextField cityBField;
    private JTextField bannedField;
    private JTextArea outputArea;

    public task7zad1() {
        setTitle("Задача 7 - графы");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JPanel leftInputPanel = new JPanel(new BorderLayout(5, 5));
        leftInputPanel.setBorder(BorderFactory.createTitledBorder("Данные графа"));
        
        JPanel verticesPanel = new JPanel(new BorderLayout());
        verticesPanel.add(new JLabel("Вершины (через пробел):"), BorderLayout.NORTH);
        verticesArea = new JTextArea(2, 20);
        verticesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        verticesPanel.add(new JScrollPane(verticesArea), BorderLayout.CENTER);
        
        JPanel matrixPanel = new JPanel(new BorderLayout());
        matrixPanel.add(new JLabel("Матрица смежности (в строчку):"), BorderLayout.NORTH);
        matrixArea = new JTextArea(10, 20);
        matrixArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        matrixPanel.add(new JScrollPane(matrixArea), BorderLayout.CENTER);
        
        leftInputPanel.add(verticesPanel, BorderLayout.NORTH);
        leftInputPanel.add(matrixPanel, BorderLayout.CENTER);

        JPanel rightInputPanel = new JPanel(new GridBagLayout());
        rightInputPanel.setBorder(BorderFactory.createTitledBorder("Параметры поиска"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        rightInputPanel.add(new JLabel("Город А:"), gbc);
        gbc.gridy = 1;
        cityAField = new JTextField(10);
        rightInputPanel.add(cityAField, gbc);

        gbc.gridy = 2;
        rightInputPanel.add(new JLabel("Город Б:"), gbc);
        gbc.gridy = 3;
        cityBField = new JTextField(10);
        rightInputPanel.add(cityBField, gbc);

        gbc.gridy = 4;
        rightInputPanel.add(new JLabel("Запрещённые вершины (через пробел):"), gbc);
        gbc.gridy = 5;
        bannedField = new JTextField(10);
        rightInputPanel.add(bannedField, gbc);

        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rightInputPanel.add(new JPanel(), gbc);

        inputPanel.add(leftInputPanel);
        inputPanel.add(rightInputPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loadButton = new JButton("Загрузить из файла");
        JButton saveButton = new JButton("Сохранить в файл");
        JButton findButton = new JButton("Найти путь");
        findButton.setFont(new Font("Dialog", Font.BOLD, 14));

        loadButton.addActionListener(e -> loadFromFile());
        saveButton.addActionListener(e -> saveToFile());
        findButton.addActionListener(e -> findPathAction());

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(findButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        outputArea.setBorder(BorderFactory.createTitledBorder("Результат"));
        JScrollPane outputScroll = new JScrollPane(outputArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, outputScroll);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(350);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (Scanner sc = new Scanner(fileChooser.getSelectedFile())) {
                if (sc.hasNextLine()) verticesArea.setText(sc.nextLine().trim());
                
                String[] vNames = verticesArea.getText().trim().split("\\s+");
                int size = vNames.length;
                
                StringBuilder matrixSb = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    if (sc.hasNextLine()) {
                        matrixSb.append(sc.nextLine().trim()).append("\n");
                    }
                }
                matrixArea.setText(matrixSb.toString());

                if (sc.hasNextLine()) cityAField.setText(sc.nextLine().trim());
                if (sc.hasNextLine()) cityBField.setText(sc.nextLine().trim());
                if (sc.hasNextLine()) bannedField.setText(sc.nextLine().trim());

                outputArea.setText("Данные успешно загружены из файла.\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка чтения файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(fileChooser.getSelectedFile())) {
                pw.println(verticesArea.getText().trim());
                pw.print(matrixArea.getText());
                pw.println(cityAField.getText().trim());
                pw.println(cityBField.getText().trim());
                pw.println(bannedField.getText().trim());
                outputArea.setText("Данные успешно сохранены в файл.\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void findPathAction() {
        outputArea.setText("");
        try {
            String[] vNames = verticesArea.getText().trim().split("\\s+");
            if (vNames.length == 0 || vNames[0].isEmpty()) {
                throw new IllegalArgumentException("Введите вершины");
            }
            n = vNames.length;
            names = vNames;

            String[] matrixLines = matrixArea.getText().trim().split("\\n");
            if (matrixLines.length != n) {
                throw new IllegalArgumentException("Количество строк матрицы (" + matrixLines.length + ") не совпадает с количеством вершин (" + n + ")!");
            }

            matrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                String[] row = matrixLines[i].trim().split("\\s+");
                if (row.length != n) {
                    throw new IllegalArgumentException("В строке " + (i + 1) + " матрицы должно быть " + n + " чисел!");
                }
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Integer.parseInt(row[j]);
                }
            }

            String a = cityAField.getText().trim();
            String b = cityBField.getText().trim();

            int start = -1, end = -1;
            for (int i = 0; i < n; i++) {
                if (names[i].equals(a)) start = i;
                if (names[i].equals(b)) end = i;
            }

            if (start == -1) throw new IllegalArgumentException("Город А '" + a + "' не найден в списке вершин!");
            if (end == -1) throw new IllegalArgumentException("Город Б '" + b + "' не найден в списке вершин!");

            banned = new boolean[n];
            String bannedLine = bannedField.getText().trim();
            if (!bannedLine.isEmpty()) {
                for (String s : bannedLine.split("\\s+")) {
                    boolean found = false;
                    for (int i = 0; i < n; i++) {
                        if (names[i].equals(s)) {
                            banned[i] = true;
                            found = true;
                        }
                    }
                    if (!found) {
                        outputArea.append("Запрещенная вершина '" + s + "' не найдена в графе.\n");
                    }
                }
            }

            visited = new boolean[n];
            path.clear();

            if (dfs(start, end)) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < path.size(); i++) {
                    sb.append(names[path.get(i)]);
                    if (i < path.size() - 1) sb.append(" -> ");
                }
                outputArea.append("Путь найден:\n" + sb.toString() + "\n");
            } else {
                outputArea.append("Путь не найден.\n");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка формата числа в матрице", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Непредвиденная ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean dfs(int cur, int end) {
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
        SwingUtilities.invokeLater(() -> new task7zad1().setVisible(true));
    }
}