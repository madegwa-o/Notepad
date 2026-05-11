import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class GradeCalculator extends JFrame implements ActionListener {

    // Components
    JTextField nameField, scoreField, resultField;
    JTextArea logArea;
    JComboBox<String> subjectBox;
    JCheckBox bonusCheck;
    JRadioButton termRB, examRB;
    JButton calcButton, clearButton;
    JSlider passMark;
    JLabel passMarkLabel;
    JMenuBar menuBar;

    GradeCalculator() {
        setTitle("Grade Calculator");
        setSize(450, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // === MENU (JMenuBar, JMenu, JMenuItem) ===
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem clearLog = new JMenuItem("Clear Log");
        JMenuItem exitItem = new JMenuItem("Exit");
        clearLog.addActionListener(e -> logArea.setText(""));
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(clearLog);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // === TOP PANEL (JPanel, JLabel, JTextField, JComboBox) ===
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));

        topPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Subject:"));
        subjectBox = new JComboBox<>(new String[]{"Math", "Science", "English", "History"});
        topPanel.add(subjectBox);

        topPanel.add(new JLabel("Score (0–100):"));
        scoreField = new JTextField();
        topPanel.add(scoreField);

        topPanel.add(new JLabel("Result:"));
        resultField = new JTextField();
        resultField.setEditable(false);
        topPanel.add(resultField);

        add(topPanel, BorderLayout.NORTH);

        // === MIDDLE PANEL (JCheckBox, JRadioButton, ButtonGroup, JSlider) ===
        JPanel midPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        midPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        bonusCheck = new JCheckBox("Apply 5-point bonus");
        midPanel.add(bonusCheck);

        termRB = new JRadioButton("Term Exam", true);
        examRB = new JRadioButton("Final Exam");
        ButtonGroup bg = new ButtonGroup();
        bg.add(termRB); bg.add(examRB);
        midPanel.add(termRB);
        midPanel.add(examRB);

        passMarkLabel = new JLabel("Pass Mark: 50");
        passMark = new JSlider(0, 100, 50);
        passMark.setMajorTickSpacing(25);
        passMark.setPaintTicks(true);
        passMark.setPaintLabels(true);
        passMark.addChangeListener(e ->
                passMarkLabel.setText("Pass Mark: " + passMark.getValue()));
        midPanel.add(passMarkLabel);
        midPanel.add(passMark);

        add(midPanel, BorderLayout.CENTER);

        // === BUTTON PANEL ===
        JPanel btnPanel = new JPanel(new FlowLayout());
        calcButton = new JButton("Calculate");
        clearButton = new JButton("Clear");
        calcButton.addActionListener(this);
        clearButton.addActionListener(this);
        btnPanel.add(calcButton);
        btnPanel.add(clearButton);

        // === LOG AREA (JTextArea inside JScrollPane) ===
        logArea = new JTextArea(6, 30);
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Log"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        bottomPanel.add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            nameField.setText("");
            scoreField.setText("");
            resultField.setText("");
            bonusCheck.setSelected(false);
            termRB.setSelected(true);
            passMark.setValue(50);
            return;
        }

        // Calculate
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new Exception("Enter a student name.");

            double score = Double.parseDouble(scoreField.getText().trim());
            if (bonusCheck.isSelected()) score += 5;
            score = Math.min(score, 100);

            String type = termRB.isSelected() ? "Term" : "Final";
            String subject = (String) subjectBox.getSelectedItem();
            int pass = passMark.getValue();

            String grade;
            if      (score >= 80) grade = "A";
            else if (score >= 70) grade = "B";
            else if (score >= 60) grade = "C";
            else if (score >= pass) grade = "D";
            else                    grade = "F";

            String result = grade + (score >= pass ? " (Pass)" : " (Fail)");
            resultField.setText(result);

            logArea.append(String.format("[%s] %s | %s | %.0f → %s%n",
                    type, name, subject, score, result));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric score.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new GradeCalculator();
    }
}