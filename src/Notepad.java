import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

class Notepad implements ActionListener {

    JFrame frame;
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, viewMenu, zoom;
    JMenuItem open, save, saveAs, exit, cut, copy, paste, zoomIn, zoomOut, statusBar, wordwrap;
    JTextArea textAr;
    JScrollPane scrollPane;
    JLabel statusLabel;
    JPanel statusPanel;

    // State tracking
    File currentFile = null;
    float currentFontSize = 14f;
    boolean statusBarVisible = true;
    boolean wordWrapEnabled = false;

    Notepad() {
        // ── Menu Bar ──────────────────────────────────────────────
        menuBar = new JMenuBar();

        // File menu
        fileMenu = new JMenu("File");
        open    = new JMenuItem("Open");
        save    = new JMenuItem("Save");
        saveAs  = new JMenuItem("Save As");
        exit    = new JMenuItem("Exit");
        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        exit.addActionListener(this);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        // Edit menu
        editMenu = new JMenu("Edit");
        cut   = new JMenuItem("Cut");
        copy  = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        // View menu
        viewMenu  = new JMenu("View");
        zoom      = new JMenu("Zoom");
        zoomIn    = new JMenuItem("Zoom In");
        zoomOut   = new JMenuItem("Zoom Out");
        statusBar = new JMenuItem("Status Bar");
        wordwrap  = new JMenuItem("Word Wrap");
        zoomIn.addActionListener(this);
        zoomOut.addActionListener(this);
        statusBar.addActionListener(this);
        wordwrap.addActionListener(this);
        zoom.add(zoomIn);
        zoom.add(zoomOut);
        viewMenu.add(zoom);
        viewMenu.add(statusBar);
        viewMenu.add(wordwrap);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        // ── Text Area ─────────────────────────────────────────────
        textAr = new JTextArea();
        textAr.setFont(new Font("Monospaced", Font.PLAIN, (int) currentFontSize));
        textAr.setLineWrap(false);
        textAr.setWrapStyleWord(false);
        textAr.setText("Najua ulifikiria siwezi!");

        scrollPane = new JScrollPane(textAr);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // ── Status Bar ────────────────────────────────────────────
        statusLabel = new JLabel(" Ln 1, Col 1");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));

        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // Update status bar on caret move
        textAr.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateStatusBar();
            }
        });

        // ── Frame ─────────────────────────────────────────────────
        frame = new JFrame("Notepad");
        frame.setJMenuBar(menuBar);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);
        frame.setSize(700, 500);
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == copy)  { textAr.copy();  return; }
        if (src == paste) { textAr.paste(); return; }
        if (src == cut)   { textAr.cut();   return; }

        if (src == open)   { openFile();   return; }
        if (src == save)   { saveFile();   return; }
        if (src == saveAs) { saveFileAs(); return; }
        if (src == exit)   { exitApp();    return; }

        if (src == zoomIn)  { adjustFontSize(2f);  return; }
        if (src == zoomOut) { adjustFontSize(-2f); return; }

        if (src == statusBar) { toggleStatusBar(); return; }
        if (src == wordwrap)  { toggleWordWrap();  return; }
    }


    private void openFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            currentFile = fc.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(currentFile.toPath()));
                textAr.setText(content);
                textAr.setCaretPosition(0);
                frame.setTitle(currentFile.getName() + " - Notepad");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Could not open file:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            writeFile(currentFile);
        }
    }

    private void saveFileAs() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            currentFile = fc.getSelectedFile();
            writeFile(currentFile);
        }
    }

    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textAr.getText());
            frame.setTitle(file.getName() + " - Notepad");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Could not save file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitApp() {
        int choice = JOptionPane.showConfirmDialog(frame,
                "Do you want to exit Notepad?",
                "Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose();
            System.exit(0);
        }
    }


    private void adjustFontSize(float delta) {
        currentFontSize = Math.max(6f, Math.min(72f, currentFontSize + delta));
        textAr.setFont(textAr.getFont().deriveFont(currentFontSize));
    }

    private void toggleStatusBar() {
        statusBarVisible = !statusBarVisible;
        statusPanel.setVisible(statusBarVisible);
        frame.revalidate();
    }

    private void toggleWordWrap() {
        wordWrapEnabled = !wordWrapEnabled;
        textAr.setLineWrap(wordWrapEnabled);
        textAr.setWrapStyleWord(wordWrapEnabled);
        // Hide horizontal scrollbar when wrapping
        scrollPane.setHorizontalScrollBarPolicy(
                wordWrapEnabled
                        ? JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                        : JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        wordwrap.setText(wordWrapEnabled ? "Word Wrap ✓" : "Word Wrap");
    }

    private void updateStatusBar() {
        try {
            int caretPos = textAr.getCaretPosition();
            int line = textAr.getLineOfOffset(caretPos);
            int col  = caretPos - textAr.getLineStartOffset(line);
            statusLabel.setText(String.format("  Ln %d, Col %d", line + 1, col + 1));
        } catch (Exception ignored) {}
    }

    // ── Entry Point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Notepad::new);
    }
}