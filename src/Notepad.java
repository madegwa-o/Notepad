import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class Notepad implements ActionListener {

    private static final Color APP_BG = new Color(16, 20, 28);
    private static final Color HEADER_BG = new Color(24, 29, 39);
    private static final Color EDITOR_BG = new Color(10, 14, 23);
    private static final Color PANEL_BG = new Color(30, 36, 50);
    private static final Color PANEL_CARD_BG = new Color(39, 47, 64);
    private static final Color ACCENT = new Color(68, 138, 255);
    private static final Color TEXT_PRIMARY = new Color(229, 233, 240);
    private static final Color TEXT_MUTED = new Color(150, 159, 179);

    private final JFrame frame;
    private final JTextArea textArea;
    private final JTextArea lineNumbers;
    private final JLabel documentTitleLabel;
    private final JLabel lineCountValue;
    private final JLabel wordCountValue;
    private final JLabel charCountValue;
    private final JLabel charNoSpaceCountValue;
    private final JLabel statusLabel;
    private JMenuItem statusBarMenuItem;
    private JMenuItem wordWrapMenuItem;
    private final JScrollPane scrollPane;
    private final JPanel statusBar;

    private File currentFile;
    private float currentFontSize = 15f;
    private boolean statusBarVisible = true;
    private boolean wordWrapEnabled = false;

    Notepad() {
        frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 560));
        frame.setSize(1120, 700);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);

        JPanel header = createHeader();
        root.add(header, BorderLayout.NORTH);

        textArea = createEditorArea();
        lineNumbers = createLineNumberArea();
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(EDITOR_BG);
        scrollPane.setRowHeaderView(lineNumbers);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBorder(new EmptyBorder(10, 10, 10, 6));
        editorPanel.setBackground(APP_BG);
        editorPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel rightPanel = createStatisticsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPanel, rightPanel);
        splitPane.setDividerLocation(760);
        splitPane.setResizeWeight(0.77);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerSize(3);
        splitPane.setBackground(APP_BG);
        splitPane.setContinuousLayout(true);

        root.add(splitPane, BorderLayout.CENTER);

        statusLabel = new JLabel("Ln 1, Col 1");
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(HEADER_BG);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, PANEL_BG));
        statusBar.add(statusLabel, BorderLayout.WEST);

        root.add(statusBar, BorderLayout.SOUTH);
        frame.setContentPane(root);

        documentTitleLabel = new JLabel("New Document");
        documentTitleLabel.setForeground(TEXT_PRIMARY);
        documentTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.add(documentTitleLabel);

        lineCountValue = findLabel(rightPanel, "lineCountValue");
        wordCountValue = findLabel(rightPanel, "wordCountValue");
        charCountValue = findLabel(rightPanel, "charCountValue");
        charNoSpaceCountValue = findLabel(rightPanel, "charNoSpaceCountValue");

        textArea.getDocument().addDocumentListener((SimpleDocumentListener) e -> refreshDocumentStats());
        textArea.addCaretListener(this::handleCaretUpdate);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitApp();
            }
        });

        refreshDocumentStats();
        updateLineNumbers();
        frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(HEADER_BG);
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        JMenu fileMenu = new JMenu("Open");
        fileMenu.setForeground(TEXT_PRIMARY);

        JMenuItem open = menuItem("Open", KeyEvent.VK_O, ActionEvent.CTRL_MASK);
        JMenuItem save = menuItem("Save", KeyEvent.VK_S, ActionEvent.CTRL_MASK);
        JMenuItem saveAs = menuItem("Save As...", KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);
        JMenuItem exit = menuItem("Exit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK);

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setForeground(TEXT_PRIMARY);
        JMenuItem cut = menuItem("Cut", KeyEvent.VK_X, ActionEvent.CTRL_MASK);
        JMenuItem copy = menuItem("Copy", KeyEvent.VK_C, ActionEvent.CTRL_MASK);
        JMenuItem paste = menuItem("Paste", KeyEvent.VK_V, ActionEvent.CTRL_MASK);
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setForeground(TEXT_PRIMARY);

        JMenu zoomMenu = new JMenu("Zoom");
        JMenuItem zoomIn = menuItem("Zoom In", KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK);
        JMenuItem zoomOut = menuItem("Zoom Out", KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK);
        zoomMenu.add(zoomIn);
        zoomMenu.add(zoomOut);

        statusBarMenuItem = menuItem("Status Bar ✓", -1, 0);
        wordWrapMenuItem = menuItem("Word Wrap", -1, 0);

        viewMenu.add(zoomMenu);
        viewMenu.add(statusBarMenuItem);
        viewMenu.add(wordWrapMenuItem);

        JMenuItem[] allItems = {open, save, saveAs, exit, cut, copy, paste, zoomIn, zoomOut, statusBarMenuItem, wordWrapMenuItem};
        for (JMenuItem item : allItems) {
            item.addActionListener(this);
        }

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(10, 14, 10, 14));

        JButton openButton = new JButton("Open ▾");
        openButton.setFocusPainted(false);
        openButton.setBackground(new Color(37, 44, 58));
        openButton.setForeground(TEXT_PRIMARY);
        openButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        openButton.addActionListener(e -> openFile());

        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftGroup.setOpaque(false);
        leftGroup.add(openButton);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titlePanel.setOpaque(false);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);
        controls.add(circleButton("i"));
        controls.add(circleButton("⋮"));

        header.add(leftGroup, BorderLayout.WEST);
        header.add(titlePanel, BorderLayout.CENTER);
        header.add(controls, BorderLayout.EAST);
        return header;
    }

    private JButton circleButton(String label) {
        JButton button = new JButton(label);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(new Color(48, 56, 74));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return button;
    }

    private JTextArea createEditorArea() {
        JTextArea area = new JTextArea();
        area.setBackground(EDITOR_BG);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TEXT_PRIMARY);
        area.setSelectionColor(new Color(68, 138, 255, 140));
        area.setFont(new Font("Monospaced", Font.PLAIN, (int) currentFontSize));
        area.setBorder(new EmptyBorder(14, 12, 14, 12));
        area.setLineWrap(false);
        area.setWrapStyleWord(false);
        return area;
    }

    private JTextArea createLineNumberArea() {
        JTextArea area = new JTextArea("1\n");
        area.setEditable(false);
        area.setFocusable(false);
        area.setBackground(new Color(15, 20, 32));
        area.setForeground(TEXT_MUTED);
        area.setFont(new Font("Monospaced", Font.PLAIN, (int) currentFontSize));
        area.setBorder(new EmptyBorder(14, 12, 14, 8));
        return area;
    }

    private JPanel createStatisticsPanel() {
        JPanel side = new JPanel();
        side.setBackground(APP_BG);
        side.setBorder(new EmptyBorder(10, 6, 10, 10));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        JLabel sectionTitle = new JLabel("Statistics");
        sectionTitle.setForeground(TEXT_PRIMARY);
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        sectionTitle.setBorder(new EmptyBorder(0, 2, 14, 0));

        JPanel card = new JPanel();
        card.setLayout(new GridLayout(4, 1, 0, 8));
        card.setBackground(PANEL_BG);
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        card.add(statRow("Lines", "0", "lineCountValue"));
        card.add(statRow("Words", "0", "wordCountValue"));
        card.add(statRow("All Characters", "0", "charCountValue"));
        card.add(statRow("Characters, No Spaces", "0", "charNoSpaceCountValue"));

        side.add(sectionTitle);
        side.add(card);
        side.add(Box.createVerticalGlue());
        return side;
    }

    private JPanel statRow(String label, String value, String name) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(PANEL_CARD_BG);
        row.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel title = new JLabel(label);
        title.setForeground(TEXT_MUTED);
        title.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel amount = new JLabel(value);
        amount.setName(name);
        amount.setForeground(TEXT_PRIMARY);
        amount.setFont(new Font("SansSerif", Font.BOLD, 24));

        row.add(title, BorderLayout.NORTH);
        row.add(amount, BorderLayout.CENTER);
        return row;
    }

    private JLabel findLabel(Container parent, String name) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JLabel && name.equals(c.getName())) {
                return (JLabel) c;
            }
            if (c instanceof Container) {
                JLabel nested = findLabel((Container) c, name);
                if (nested != null) {
                    return nested;
                }
            }
        }
        return null;
    }

    private JMenuItem menuItem(String text, int keyCode, int modifiers) {
        JMenuItem item = new JMenuItem(text);
        if (keyCode >= 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        }
        return item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = ((JMenuItem) e.getSource()).getText().replace(" ✓", "");
        switch (cmd) {
            case "Open" -> openFile();
            case "Save" -> saveFile();
            case "Save As..." -> saveFileAs();
            case "Exit" -> exitApp();
            case "Cut" -> textArea.cut();
            case "Copy" -> textArea.copy();
            case "Paste" -> textArea.paste();
            case "Zoom In" -> adjustFontSize(1.5f);
            case "Zoom Out" -> adjustFontSize(-1.5f);
            case "Status Bar" -> toggleStatusBar();
            case "Word Wrap" -> toggleWordWrap();
        }
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            try {
                String content = Files.readString(currentFile.toPath(), StandardCharsets.UTF_8);
                textArea.setText(content);
                textArea.setCaretPosition(0);
                documentTitleLabel.setText(currentFile.getName());
                frame.setTitle(currentFile.getName() + " - Text Editor");
            } catch (IOException ex) {
                showError("Could not open file", ex);
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
            return;
        }
        writeToFile(currentFile);
    }

    private void saveFileAs() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            writeToFile(currentFile);
        }
    }

    private void writeToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(textArea.getText());
            documentTitleLabel.setText(file.getName());
            frame.setTitle(file.getName() + " - Text Editor");
        } catch (IOException ex) {
            showError("Could not save file", ex);
        }
    }

    private void toggleStatusBar() {
        statusBarVisible = !statusBarVisible;
        statusBar.setVisible(statusBarVisible);
        statusBarMenuItem.setText(statusBarVisible ? "Status Bar ✓" : "Status Bar");
    }

    private void toggleWordWrap() {
        wordWrapEnabled = !wordWrapEnabled;
        textArea.setLineWrap(wordWrapEnabled);
        textArea.setWrapStyleWord(wordWrapEnabled);
        scrollPane.setHorizontalScrollBarPolicy(wordWrapEnabled
                ? JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                : JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        wordWrapMenuItem.setText(wordWrapEnabled ? "Word Wrap ✓" : "Word Wrap");
    }

    private void adjustFontSize(float delta) {
        currentFontSize = Math.max(10f, Math.min(42f, currentFontSize + delta));
        Font editorFont = new Font("Monospaced", Font.PLAIN, (int) currentFontSize);
        textArea.setFont(editorFont);
        lineNumbers.setFont(editorFont);
    }

    private void handleCaretUpdate(CaretEvent event) {
        updateCaretPositionLabel();
    }

    private void refreshDocumentStats() {
        String text = textArea.getText();
        int lines = textArea.getLineCount();
        int words = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
        int chars = text.length();
        int charsNoSpaces = text.replaceAll("\\s", "").length();

        lineCountValue.setText(String.valueOf(lines));
        wordCountValue.setText(String.valueOf(words));
        charCountValue.setText(String.valueOf(chars));
        charNoSpaceCountValue.setText(String.valueOf(charsNoSpaces));

        updateLineNumbers();
        updateCaretPositionLabel();
    }

    private void updateLineNumbers() {
        int lineCount = Math.max(1, textArea.getLineCount());
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            sb.append(i).append('\n');
        }
        lineNumbers.setText(sb.toString());
    }

    private void updateCaretPositionLabel() {
        try {
            int pos = textArea.getCaretPosition();
            int line = textArea.getLineOfOffset(pos);
            int col = pos - textArea.getLineStartOffset(line);
            statusLabel.setText(String.format("Ln %d, Col %d", line + 1, col + 1));
        } catch (Exception ignored) {
            statusLabel.setText("Ln 1, Col 1");
        }
    }

    private void exitApp() {
        int choice = JOptionPane.showConfirmDialog(frame,
                "Close Text Editor?",
                "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose();
            System.exit(0);
        }
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(frame,
                title + ":\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @FunctionalInterface
    interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
        void update(javax.swing.event.DocumentEvent e);

        @Override
        default void insertUpdate(javax.swing.event.DocumentEvent e) {
            update(e);
        }

        @Override
        default void removeUpdate(javax.swing.event.DocumentEvent e) {
            update(e);
        }

        @Override
        default void changedUpdate(javax.swing.event.DocumentEvent e) {
            update(e);
        }
    }
}
