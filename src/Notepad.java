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
    private static final Color HEADER_BUTTON_BG = new Color(37, 44, 58);
    private static final Color TEXT_PRIMARY = new Color(229, 233, 240);
    private static final Color TEXT_MUTED = new Color(150, 159, 179);

    private final JFrame frame;
    private final JTextArea textArea;
    private final JTextArea lineNumbers;
    private final JLabel documentTitleLabel;
    private final JLabel statusLabel;
    private final JScrollPane scrollPane;
    private final JPanel statusBar;

    private JMenuItem statusBarMenuItem;
    private JMenuItem wordWrapMenuItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem exitItem;
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    private JMenuItem zoomInItem;
    private JMenuItem zoomOutItem;

    private File currentFile;
    private float currentFontSize = 15f;
    private boolean statusBarVisible = true;
    private boolean wordWrapEnabled = false;

    Notepad() {
        frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(880, 560));
        frame.setSize(1080, 700);
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
        editorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        editorPanel.setBackground(APP_BG);
        editorPanel.add(scrollPane, BorderLayout.CENTER);

        root.add(editorPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Ln 1, Col 1");
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(HEADER_BG);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(30, 36, 50)));
        statusBar.add(statusLabel, BorderLayout.WEST);
        root.add(statusBar, BorderLayout.SOUTH);

        frame.setContentPane(root);

        documentTitleLabel = new JLabel("New Document");
        documentTitleLabel.setForeground(TEXT_PRIMARY);
        documentTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.add(documentTitleLabel, BorderLayout.CENTER);

        textArea.getDocument().addDocumentListener((SimpleDocumentListener) e -> updateLineNumbers());
        textArea.addCaretListener(this::handleCaretUpdate);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitApp();
            }
        });

        updateLineNumbers();
        frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(HEADER_BG);
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        JMenu fileMenu = new JMenu("Open");
        fileMenu.setForeground(TEXT_PRIMARY);

        openItem = menuItem("Open", KeyEvent.VK_O, ActionEvent.CTRL_MASK);
        saveItem = menuItem("Save", KeyEvent.VK_S, ActionEvent.CTRL_MASK);
        saveAsItem = menuItem("Save As...", KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);
        exitItem = menuItem("Exit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setForeground(TEXT_PRIMARY);
        cutItem = menuItem("Cut", KeyEvent.VK_X, ActionEvent.CTRL_MASK);
        copyItem = menuItem("Copy", KeyEvent.VK_C, ActionEvent.CTRL_MASK);
        pasteItem = menuItem("Paste", KeyEvent.VK_V, ActionEvent.CTRL_MASK);
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setForeground(TEXT_PRIMARY);
        JMenu zoomMenu = new JMenu("Zoom");
        zoomInItem = menuItem("Zoom In", KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK);
        zoomOutItem = menuItem("Zoom Out", KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK);
        zoomMenu.add(zoomInItem);
        zoomMenu.add(zoomOutItem);

        statusBarMenuItem = menuItem("Status Bar ✓", -1, 0);
        wordWrapMenuItem = menuItem("Word Wrap", -1, 0);
        viewMenu.add(zoomMenu);
        viewMenu.add(statusBarMenuItem);
        viewMenu.add(wordWrapMenuItem);

        for (JMenuItem item : new JMenuItem[]{
                openItem, saveItem, saveAsItem, exitItem,
                cutItem, copyItem, pasteItem,
                zoomInItem, zoomOutItem,
                statusBarMenuItem, wordWrapMenuItem
        }) {
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
        styleHeaderButton(openButton);
        openButton.addActionListener(e -> openFile());

        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftGroup.setOpaque(false);
        leftGroup.add(openButton);

        JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightGroup.setOpaque(false);
        rightGroup.add(circleButton("i", null));
        rightGroup.add(circleButton("⋮", this::showHamburgerMenu));

        header.add(leftGroup, BorderLayout.WEST);
        header.add(rightGroup, BorderLayout.EAST);
        return header;
    }

    private void showHamburgerMenu(ActionEvent event) {
        JButton source = (JButton) event.getSource();
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(47, 55, 74));
        menu.setBorder(new EmptyBorder(8, 8, 8, 8));

        menu.add(dropdownItem("New Window", "Ctrl+N", e -> clearDocument()));
        menu.add(dropdownItem("Save", "Ctrl+S", e -> saveFile()));
        menu.add(dropdownItem("Save As...", "Shift+Ctrl+S", e -> saveFileAs()));
        menu.addSeparator();
        menu.add(dropdownItem("Find/Replace...", "Ctrl+F", e -> focusEditor()));
        menu.add(dropdownItem("Print...", "Ctrl+P", e -> focusEditor()));
        menu.add(dropdownItem("Fullscreen", "F11", e -> toggleFullscreen()));
        menu.addSeparator();
        menu.add(dropdownItem("Preferences", "Ctrl+,", e -> showPreferences()));
        menu.add(dropdownItem("Keyboard Shortcuts", "Ctrl+?", e -> showShortcuts()));
        menu.add(dropdownItem("About Text Editor", "", e -> showAbout()));

        menu.show(source, 0, source.getHeight() + 2);
    }

    private JMenuItem dropdownItem(String text, String shortcut, ActionListener action) {
        String padded = shortcut.isEmpty() ? text : text + "    " + shortcut;
        JMenuItem item = new JMenuItem(padded);
        item.setForeground(TEXT_PRIMARY);
        item.setBackground(new Color(47, 55, 74));
        item.setFont(new Font("SansSerif", Font.PLAIN, 15));
        item.setBorder(new EmptyBorder(8, 10, 8, 10));
        item.addActionListener(action);
        return item;
    }

    private void styleHeaderButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setBackground(HEADER_BUTTON_BG);
        button.setForeground(TEXT_PRIMARY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    private JButton circleButton(String label, ActionListener action) {
        JButton button = new JButton(label);
        styleHeaderButton(button);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        if (action != null) {
            button.addActionListener(action);
        }
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

    private JMenuItem menuItem(String text, int keyCode, int modifiers) {
        JMenuItem item = new JMenuItem(text);
        if (keyCode >= 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        }
        return item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == openItem) {
            openFile();
        } else if (source == saveItem) {
            saveFile();
        } else if (source == saveAsItem) {
            saveFileAs();
        } else if (source == exitItem) {
            exitApp();
        } else if (source == cutItem) {
            textArea.cut();
        } else if (source == copyItem) {
            textArea.copy();
        } else if (source == pasteItem) {
            textArea.paste();
        } else if (source == zoomInItem) {
            adjustFontSize(1.5f);
        } else if (source == zoomOutItem) {
            adjustFontSize(-1.5f);
        } else if (source == statusBarMenuItem) {
            toggleStatusBar();
        } else if (source == wordWrapMenuItem) {
            toggleWordWrap();
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

    private void clearDocument() {
        textArea.setText("");
        textArea.setCaretPosition(0);
        currentFile = null;
        documentTitleLabel.setText("New Document");
        frame.setTitle("Text Editor");
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

    private void updateLineNumbers() {
        int lineCount = Math.max(1, textArea.getLineCount());
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            sb.append(i).append('\n');
        }
        lineNumbers.setText(sb.toString());
        updateCaretPositionLabel();
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

    private void focusEditor() {
        textArea.requestFocusInWindow();
    }

    private void toggleFullscreen() {
        frame.dispose();
        frame.setUndecorated(!frame.isUndecorated());
        frame.setVisible(true);
    }

    private void showPreferences() {
        JOptionPane.showMessageDialog(frame, "Preferences panel is not implemented yet.", "Preferences", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showShortcuts() {
        JOptionPane.showMessageDialog(frame,
                "Ctrl+O Open\nCtrl+S Save\nShift+Ctrl+S Save As\nCtrl+F Find/Replace\nF11 Fullscreen",
                "Keyboard Shortcuts",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(frame,
                "Text Editor clone UI built with Java Swing.",
                "About Text Editor",
                JOptionPane.INFORMATION_MESSAGE);
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
