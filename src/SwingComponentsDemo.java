import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

/**
 * SwingComponentsDemo - A comprehensive Java Swing demo
 * showcasing all major Swing components organized into tabs.
 */
public class SwingComponentsDemo extends JFrame {

    // ─── Color Palette ───────────────────────────────────────────
    private static final Color BG_DARK      = new Color(18, 18, 30);
    private static final Color BG_PANEL     = new Color(28, 28, 45);
    private static final Color BG_CARD      = new Color(38, 38, 58);
    private static final Color ACCENT       = new Color(99, 179, 237);
    private static final Color ACCENT2      = new Color(159, 122, 234);
    private static final Color SUCCESS      = new Color(72, 199, 142);
    private static final Color WARNING      = new Color(255, 184, 0);
    private static final Color TEXT_PRIMARY = new Color(230, 230, 255);
    private static final Color TEXT_MUTED   = new Color(140, 140, 170);
    private static final Color BORDER       = new Color(60, 60, 90);

    public SwingComponentsDemo() {
        super("⚡ Java Swing Components Showcase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // Dark background for the frame
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // Header
        add(buildHeader(), BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabs = buildTabbedPane();
        add(tabs, BorderLayout.CENTER);

        // Status bar
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ─── Header ──────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PANEL);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("  ⚡ Java Swing Components Demo", SwingConstants.LEFT);
        title.setFont(new Font("Monospaced", Font.BOLD, 20));
        title.setForeground(ACCENT);

        JLabel sub = new JLabel("All Swing components in one place  ", SwingConstants.RIGHT);
        sub.setFont(new Font("Monospaced", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);

        header.add(title, BorderLayout.WEST);
        header.add(sub, BorderLayout.EAST);
        return header;
    }

    // ─── Status Bar ──────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        bar.setBackground(BG_PANEL);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JLabel status = new JLabel("● Ready  |  Java Swing Demo  |  All components loaded");
        status.setFont(new Font("Monospaced", Font.PLAIN, 11));
        status.setForeground(SUCCESS);
        bar.add(status);
        return bar;
    }

    // ─── Tabbed Pane ─────────────────────────────────────────────
    private JTabbedPane buildTabbedPane() {
        JTabbedPane tp = new JTabbedPane();
        tp.setBackground(BG_DARK);
        tp.setForeground(TEXT_PRIMARY);
        tp.setFont(new Font("Monospaced", Font.BOLD, 12));

        tp.addTab("🖱 Buttons",      buildButtonsTab());
        tp.addTab("📝 Text Fields",  buildTextFieldsTab());
        tp.addTab("✅ Selections",   buildSelectionsTab());
        tp.addTab("📋 Lists & Tables", buildListsTablesTab());
        tp.addTab("🌲 Tree & Scroll", buildTreeScrollTab());
        tp.addTab("📐 Layouts",      buildLayoutsTab());
        tp.addTab("🗂 Menu & Dialog", buildMenuDialogTab());
        tp.addTab("🎚 Sliders",      buildSlidersTab());

        return tp;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 1 – JButton  / JLabel
    // ════════════════════════════════════════════════════════════
    private JPanel buildButtonsTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(1, 2, 12, 0));

        // ── JButton section ──
        JPanel btnCard = card("JButton — Labeled Buttons");
        btnCard.setLayout(new BoxLayout(btnCard, BoxLayout.Y_AXIS));

        JButton plainBtn   = styledButton("Plain Button",   ACCENT);
        JButton greenBtn   = styledButton("Success Button", SUCCESS);
        JButton warnBtn    = styledButton("Warning Button", WARNING);
        JButton disabledBtn = styledButton("Disabled Button", TEXT_MUTED);
        disabledBtn.setEnabled(false);

        JLabel feedbackLabel = new JLabel("Click a button…");
        feedbackLabel.setFont(new Font("Monospaced", Font.ITALIC, 13));
        feedbackLabel.setForeground(ACCENT2);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ActionListener feedBack = e -> {
            JButton src = (JButton) e.getSource();
            feedbackLabel.setText("✔ Clicked: " + src.getText());
        };
        plainBtn.addActionListener(feedBack);
        greenBtn.addActionListener(feedBack);
        warnBtn.addActionListener(feedBack);

        for (JButton b : new JButton[]{plainBtn, greenBtn, warnBtn, disabledBtn}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnCard.add(Box.createVerticalStrut(8));
            btnCard.add(b);
        }
        btnCard.add(Box.createVerticalStrut(16));
        btnCard.add(feedbackLabel);
        btnCard.add(Box.createVerticalGlue());

        // ── JLabel section ──
        JPanel lblCard = card("JLabel — Text & Alignment");
        lblCard.setLayout(new BoxLayout(lblCard, BoxLayout.Y_AXIS));

        JLabel[] labels = {
                new JLabel("← Left aligned",   SwingConstants.LEFT),
                new JLabel("↔ Center aligned",  SwingConstants.CENTER),
                new JLabel("→ Right aligned",   SwingConstants.RIGHT),
                new JLabel("★ Bold & Colored"),
                new JLabel("Monospaced font"),
        };
        Color[] lColors = {TEXT_PRIMARY, ACCENT, SUCCESS, WARNING, ACCENT2};
        Font[]  lFonts  = {
                new Font("Dialog", Font.PLAIN, 14),
                new Font("Dialog", Font.PLAIN, 14),
                new Font("Dialog", Font.PLAIN, 14),
                new Font("Dialog", Font.BOLD,  16),
                new Font("Monospaced", Font.PLAIN, 13),
        };
        for (int i = 0; i < labels.length; i++) {
            labels[i].setForeground(lColors[i]);
            labels[i].setFont(lFonts[i]);
            labels[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            lblCard.add(Box.createVerticalStrut(10));
            lblCard.add(labels[i]);
        }
        lblCard.add(Box.createVerticalGlue());

        grid.add(btnCard);
        grid.add(lblCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 2 – JTextField / JTextArea / JPasswordField
    // ════════════════════════════════════════════════════════════
    private JPanel buildTextFieldsTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(1, 2, 12, 0));

        // ── JTextField ──
        JPanel tfCard = card("JTextField & JPasswordField");
        tfCard.setLayout(new BoxLayout(tfCard, BoxLayout.Y_AXIS));

        JLabel userLbl   = muted("Username:");
        JTextField userFld = styledTextField("Enter username…");

        JLabel passLbl   = muted("Password:");
        JPasswordField passFld = new JPasswordField(20);
        styleTextField(passFld);

        JLabel num1Lbl = muted("Number A:");
        JTextField num1 = styledTextField("e.g. 42");

        JLabel num2Lbl = muted("Number B:");
        JTextField num2 = styledTextField("e.g. 8");

        JButton addBtn = styledButton("Add ＋", ACCENT);
        JButton subBtn = styledButton("Sub −", ACCENT2);

        JLabel resultLbl = new JLabel("Result: —");
        resultLbl.setFont(new Font("Monospaced", Font.BOLD, 14));
        resultLbl.setForeground(SUCCESS);

        ActionListener calc = e -> {
            try {
                int a = Integer.parseInt(num1.getText().trim());
                int b = Integer.parseInt(num2.getText().trim());
                int r = e.getSource() == addBtn ? a + b : a - b;
                String op = e.getSource() == addBtn ? "+" : "−";
                resultLbl.setText("Result: " + a + " " + op + " " + b + " = " + r);
            } catch (NumberFormatException ex) {
                resultLbl.setText("⚠ Enter valid integers");
                resultLbl.setForeground(WARNING);
            }
        };
        addBtn.addActionListener(calc);
        subBtn.addActionListener(calc);

        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        subBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btnRow = darkPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnRow.add(addBtn);
        btnRow.add(subBtn);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Component c : new Component[]{
                userLbl, userFld, passLbl, passFld,
                num1Lbl, num1, num2Lbl, num2, btnRow, resultLbl
        }) {
            if (c instanceof JComponent) ((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
            tfCard.add(Box.createVerticalStrut(6));
            tfCard.add(c);
        }
        tfCard.add(Box.createVerticalGlue());

        // ── JTextArea ──
        JPanel taCard = card("JTextArea — Multi-line Input");
        taCard.setLayout(new BoxLayout(taCard, BoxLayout.Y_AXIS));

        JTextArea area = new JTextArea(8, 20);
        area.setBackground(BG_DARK);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(ACCENT);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createLineBorder(BORDER));
        area.setText("Type something here…\nMultiple lines supported.");

        JScrollPane areaScroll = new JScrollPane(area);
        areaScroll.setPreferredSize(new Dimension(300, 160));
        areaScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaScroll.setBorder(BorderFactory.createLineBorder(BORDER));

        JLabel wordLbl = muted("Words: 0   Characters: 0");
        wordLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton countBtn = styledButton("Count Words", SUCCESS);
        countBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        countBtn.addActionListener(e -> {
            String txt = area.getText().trim();
            int words = txt.isEmpty() ? 0 : txt.split("\\s+").length;
            wordLbl.setText("Words: " + words + "   Characters: " + txt.length());
        });

        JButton clearBtn = styledButton("Clear", WARNING);
        clearBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearBtn.addActionListener(e -> { area.setText(""); wordLbl.setText("Words: 0   Characters: 0"); });

        JPanel taButtons = darkPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        taButtons.add(countBtn);
        taButtons.add(clearBtn);
        taButtons.setAlignmentX(Component.LEFT_ALIGNMENT);

        taCard.add(Box.createVerticalStrut(4));
        taCard.add(areaScroll);
        taCard.add(Box.createVerticalStrut(8));
        taCard.add(taButtons);
        taCard.add(Box.createVerticalStrut(8));
        taCard.add(wordLbl);
        taCard.add(Box.createVerticalGlue());

        grid.add(tfCard);
        grid.add(taCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 3 – JCheckBox / JRadioButton / JComboBox
    // ════════════════════════════════════════════════════════════
    private JPanel buildSelectionsTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(1, 3, 12, 0));

        // ── JCheckBox ──
        JPanel cbCard = card("JCheckBox — Food Order");
        cbCard.setLayout(new BoxLayout(cbCard, BoxLayout.Y_AXIS));

        JLabel cbTitle = new JLabel("🍕 Food Ordering System");
        cbTitle.setForeground(ACCENT);
        cbTitle.setFont(new Font("Dialog", Font.BOLD, 14));
        cbTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox pizza  = styledCheckBox("Pizza @ KES 100");
        JCheckBox burger = styledCheckBox("Burger @ KES 30");
        JCheckBox tea    = styledCheckBox("Tea @ KES 10");
        JCheckBox fries  = styledCheckBox("Fries @ KES 50");

        JLabel totalLbl = new JLabel("Total: KES 0");
        totalLbl.setForeground(SUCCESS);
        totalLbl.setFont(new Font("Monospaced", Font.BOLD, 14));
        totalLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton orderBtn = styledButton("Place Order 🛒", ACCENT);
        orderBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        orderBtn.addActionListener(e -> {
            int total = 0;
            StringBuilder msg = new StringBuilder("Order Summary:\n");
            if (pizza.isSelected())  { total += 100; msg.append("  • Pizza:  100\n"); }
            if (burger.isSelected()) { total +=  30; msg.append("  • Burger:  30\n"); }
            if (tea.isSelected())    { total +=  10; msg.append("  • Tea:     10\n"); }
            if (fries.isSelected())  { total +=  50; msg.append("  • Fries:   50\n"); }
            if (total == 0) { JOptionPane.showMessageDialog(this, "Select at least one item!"); return; }
            msg.append("─────────────────\n  Total:  KES ").append(total);
            totalLbl.setText("Total: KES " + total);
            JOptionPane.showMessageDialog(this, msg.toString(), "Order Placed ✔", JOptionPane.INFORMATION_MESSAGE);
        });

        for (Component c : new Component[]{cbTitle, pizza, burger, tea, fries}) {
            cbCard.add(Box.createVerticalStrut(8));
            cbCard.add(c);
        }
        cbCard.add(Box.createVerticalStrut(12));
        cbCard.add(totalLbl);
        cbCard.add(Box.createVerticalStrut(8));
        cbCard.add(orderBtn);
        cbCard.add(Box.createVerticalGlue());

        // ── JRadioButton ──
        JPanel rbCard = card("JRadioButton — Single Select");
        rbCard.setLayout(new BoxLayout(rbCard, BoxLayout.Y_AXIS));

        JLabel q1 = muted("Q1. Your gender:");
        q1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JRadioButton male   = styledRadio("Male");
        JRadioButton female = styledRadio("Female");
        JRadioButton other  = styledRadio("Other");
        ButtonGroup genderGrp = new ButtonGroup();
        genderGrp.add(male); genderGrp.add(female); genderGrp.add(other);

        JLabel q2 = muted("Q2. Preferred IDE:");
        q2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JRadioButton vsc     = styledRadio("VS Code");
        JRadioButton intellij = styledRadio("IntelliJ");
        JRadioButton eclipse = styledRadio("Eclipse");
        ButtonGroup ideGrp = new ButtonGroup();
        ideGrp.add(vsc); ideGrp.add(intellij); ideGrp.add(eclipse);

        JLabel rbResult = new JLabel("Selection: —");
        rbResult.setForeground(ACCENT2);
        rbResult.setFont(new Font("Monospaced", Font.ITALIC, 12));
        rbResult.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton checkBtn = styledButton("Check Answers", ACCENT2);
        checkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkBtn.addActionListener(e -> {
            String g = male.isSelected() ? "Male" : female.isSelected() ? "Female" : other.isSelected() ? "Other" : "N/A";
            String ide = vsc.isSelected() ? "VS Code" : intellij.isSelected() ? "IntelliJ" : eclipse.isSelected() ? "Eclipse" : "N/A";
            rbResult.setText("<html>Gender: <b>" + g + "</b>  |  IDE: <b>" + ide + "</b></html>");
        });

        for (Component c : new Component[]{q1, male, female, other, q2, vsc, intellij, eclipse}) {
            rbCard.add(Box.createVerticalStrut(6));
            rbCard.add(c);
        }
        rbCard.add(Box.createVerticalStrut(10));
        rbCard.add(checkBtn);
        rbCard.add(Box.createVerticalStrut(8));
        rbCard.add(rbResult);
        rbCard.add(Box.createVerticalGlue());

        // ── JComboBox ──
        JPanel cmbCard = card("JComboBox — Drop-down Lists");
        cmbCard.setLayout(new BoxLayout(cmbCard, BoxLayout.Y_AXIS));

        JLabel langLbl = muted("Programming Language:");
        langLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] langs = {"Java", "Python", "C++", "JavaScript", "C#", "Kotlin", "Rust"};
        JComboBox<String> langBox = styledComboBox(langs);
        langBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel countryLbl = muted("Country:");
        countryLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] countries = {"Kenya", "Uganda", "Tanzania", "Nigeria", "South Africa", "Ethiopia"};
        JComboBox<String> countryBox = styledComboBox(countries);
        countryBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cmbResult = new JLabel("—");
        cmbResult.setForeground(SUCCESS);
        cmbResult.setFont(new Font("Monospaced", Font.BOLD, 13));
        cmbResult.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton showBtn = styledButton("Show Selection", SUCCESS);
        showBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        showBtn.addActionListener(e -> {
            cmbResult.setText("<html>Lang: <b>" + langBox.getSelectedItem()
                    + "</b><br>Country: <b>" + countryBox.getSelectedItem() + "</b></html>");
        });

        for (Component c : new Component[]{langLbl, langBox, countryLbl, countryBox}) {
            cmbCard.add(Box.createVerticalStrut(8));
            cmbCard.add(c);
        }
        cmbCard.add(Box.createVerticalStrut(12));
        cmbCard.add(showBtn);
        cmbCard.add(Box.createVerticalStrut(8));
        cmbCard.add(cmbResult);
        cmbCard.add(Box.createVerticalGlue());

        grid.add(cbCard);
        grid.add(rbCard);
        grid.add(cmbCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 4 – JList / JTable
    // ════════════════════════════════════════════════════════════
    private JPanel buildListsTablesTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(1, 2, 12, 0));

        // ── JList ──
        JPanel listCard = card("JList — Selectable List");
        listCard.setLayout(new BorderLayout(0, 8));

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String s : new String[]{"Java", "Python", "C++", "JavaScript", "Go", "Rust", "Kotlin", "Swift"})
            model.addElement(s);

        JList<String> list = new JList<>(model);
        list.setBackground(BG_DARK);
        list.setForeground(TEXT_PRIMARY);
        list.setSelectionBackground(ACCENT);
        list.setSelectionForeground(Color.WHITE);
        list.setFont(new Font("Monospaced", Font.PLAIN, 13));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane listScroll = new JScrollPane(list);
        listScroll.getViewport().setBackground(BG_DARK);
        listScroll.setBorder(BorderFactory.createLineBorder(BORDER));

        JLabel listResult = new JLabel("Selected: —");
        listResult.setForeground(ACCENT2);
        listResult.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton showSelBtn = styledButton("Show Selection", ACCENT);
        showSelBtn.addActionListener(e -> {
            java.util.List<String> sel = list.getSelectedValuesList();
            listResult.setText(sel.isEmpty() ? "Selected: —" : "Selected: " + sel);
        });

        JPanel listSouth = darkPanel(new BorderLayout(0, 6));
        listSouth.add(showSelBtn, BorderLayout.NORTH);
        listSouth.add(listResult, BorderLayout.SOUTH);

        listCard.add(listScroll, BorderLayout.CENTER);
        listCard.add(listSouth, BorderLayout.SOUTH);

        // ── JTable ──
        JPanel tableCard = card("JTable — Tabular Data");
        tableCard.setLayout(new BorderLayout(0, 8));

        String[] cols = {"ID", "Name", "Language", "Salary"};
        Object[][] data = {
                {"001", "Alice",   "Java",   "120,000"},
                {"002", "Bob",     "Python", " 98,000"},
                {"003", "Carol",   "Go",     "115,000"},
                {"004", "Dave",    "Rust",   "130,000"},
                {"005", "Eve",     "C++",    "105,000"},
                {"006", "Frank",   "Kotlin", "122,000"},
        };

        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setBackground(BG_DARK);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER);
        table.getTableHeader().setBackground(BG_PANEL);
        table.getTableHeader().setForeground(ACCENT);
        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 12));
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(22);
        table.setCellSelectionEnabled(true);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        tableScroll.getViewport().setBackground(BG_DARK);

        JLabel tableResult = new JLabel("Click a cell to select");
        tableResult.setForeground(TEXT_MUTED);
        tableResult.setFont(new Font("Monospaced", Font.ITALIC, 12));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row >= 0 && col >= 0) {
                    tableResult.setText("Selected: " + cols[col] + " → " + table.getValueAt(row, col));
                    tableResult.setForeground(SUCCESS);
                }
            }
        });

        tableCard.add(tableScroll, BorderLayout.CENTER);
        tableCard.add(tableResult, BorderLayout.SOUTH);

        grid.add(listCard);
        grid.add(tableCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 5 – JTree / JScrollBar / JLayeredPane
    // ════════════════════════════════════════════════════════════
    private JPanel buildTreeScrollTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(1, 3, 12, 0));

        // ── JTree ──
        JPanel treeCard = card("JTree — Hierarchical Data");
        treeCard.setLayout(new BorderLayout(0, 8));

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Java Swing");

        DefaultMutableTreeNode containers = new DefaultMutableTreeNode("Containers");
        containers.add(new DefaultMutableTreeNode("JFrame"));
        containers.add(new DefaultMutableTreeNode("JPanel"));
        containers.add(new DefaultMutableTreeNode("JDialog"));

        DefaultMutableTreeNode controls = new DefaultMutableTreeNode("Controls");
        controls.add(new DefaultMutableTreeNode("JButton"));
        controls.add(new DefaultMutableTreeNode("JLabel"));
        controls.add(new DefaultMutableTreeNode("JTextField"));
        controls.add(new DefaultMutableTreeNode("JTextArea"));

        DefaultMutableTreeNode selects = new DefaultMutableTreeNode("Selection");
        selects.add(new DefaultMutableTreeNode("JCheckBox"));
        selects.add(new DefaultMutableTreeNode("JRadioButton"));
        selects.add(new DefaultMutableTreeNode("JComboBox"));
        selects.add(new DefaultMutableTreeNode("JList"));

        rootNode.add(containers);
        rootNode.add(controls);
        rootNode.add(selects);

        JTree tree = new JTree(rootNode);
        tree.setBackground(BG_DARK);
        tree.setForeground(TEXT_PRIMARY);
        tree.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tree.expandRow(0);

        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        treeScroll.getViewport().setBackground(BG_DARK);

        JLabel treeResult = new JLabel("Click a node…");
        treeResult.setForeground(ACCENT2);
        treeResult.setFont(new Font("Monospaced", Font.ITALIC, 12));

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null) treeResult.setText("Selected: " + node.getUserObject());
        });

        treeCard.add(treeScroll, BorderLayout.CENTER);
        treeCard.add(treeResult, BorderLayout.SOUTH);

        // ── JScrollBar ──
        JPanel sbCard = card("JScrollBar — Value Picker");
        sbCard.setLayout(new BoxLayout(sbCard, BoxLayout.Y_AXIS));

        JLabel sbTitle = muted("Vertical Scrollbar:");
        sbTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollBar vBar = new JScrollBar(JScrollBar.VERTICAL, 50, 1, 0, 101);
        vBar.setBackground(BG_DARK);
        vBar.setForeground(ACCENT);
        vBar.setPreferredSize(new Dimension(24, 200));
        vBar.setMaximumSize(new Dimension(24, 200));
        vBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel vVal = new JLabel("Value: 50");
        vVal.setForeground(ACCENT);
        vVal.setFont(new Font("Monospaced", Font.BOLD, 16));
        vVal.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hTitle = muted("Horizontal Scrollbar:");
        hTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollBar hBar = new JScrollBar(JScrollBar.HORIZONTAL, 50, 1, 0, 101);
        hBar.setBackground(BG_DARK);
        hBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        hBar.setMaximumSize(new Dimension(300, 24));

        JLabel hVal = new JLabel("Value: 50");
        hVal.setForeground(ACCENT2);
        hVal.setFont(new Font("Monospaced", Font.BOLD, 16));
        hVal.setAlignmentX(Component.CENTER_ALIGNMENT);

        vBar.addAdjustmentListener(e -> vVal.setText("Value: " + vBar.getValue()));
        hBar.addAdjustmentListener(e -> hVal.setText("Value: " + hBar.getValue()));

        for (Component c : new Component[]{sbTitle, Box.createVerticalStrut(8),
                vBar, Box.createVerticalStrut(6), vVal,
                Box.createVerticalStrut(16), hTitle, Box.createVerticalStrut(4),
                hBar, hVal}) {
            sbCard.add(c);
        }
        sbCard.add(Box.createVerticalGlue());

        // ── JLayeredPane ──
        JPanel lpCard = card("JLayeredPane — Depth Layers");
        lpCard.setLayout(new BorderLayout());

        JLayeredPane lp = new JLayeredPane();
        lp.setPreferredSize(new Dimension(200, 220));
        lp.setBackground(BG_DARK);
        lp.setOpaque(true);

        JButton bBottom = new JButton("Layer 1");
        bBottom.setBounds(20, 60, 120, 60);
        bBottom.setBackground(new Color(99, 0, 160));
        bBottom.setForeground(Color.WHITE);
        bBottom.setFont(new Font("Monospaced", Font.BOLD, 12));

        JButton bMiddle = new JButton("Layer 2");
        bMiddle.setBounds(55, 90, 120, 60);
        bMiddle.setBackground(new Color(0, 120, 200));
        bMiddle.setForeground(Color.WHITE);
        bMiddle.setFont(new Font("Monospaced", Font.BOLD, 12));

        JButton bTop = new JButton("Layer 3");
        bTop.setBounds(90, 120, 120, 60);
        bTop.setBackground(new Color(0, 180, 100));
        bTop.setForeground(Color.WHITE);
        bTop.setFont(new Font("Monospaced", Font.BOLD, 12));

        lp.add(bBottom, Integer.valueOf(1));
        lp.add(bMiddle, Integer.valueOf(2));
        lp.add(bTop,    Integer.valueOf(3));

        JLabel lpDesc = new JLabel("Buttons stacked in depth layers");
        lpDesc.setForeground(TEXT_MUTED);
        lpDesc.setFont(new Font("Monospaced", Font.ITALIC, 11));

        lpCard.add(lp, BorderLayout.CENTER);
        lpCard.add(lpDesc, BorderLayout.SOUTH);

        grid.add(treeCard);
        grid.add(sbCard);
        grid.add(lpCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 6 – Layout Managers
    // ════════════════════════════════════════════════════════════
    private JPanel buildLayoutsTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(1, 3, 12, 0));

        // ── BorderLayout ──
        JPanel blCard = card("BorderLayout");
        blCard.setLayout(new BorderLayout(4, 4));

        blCard.add(colorBtn("NORTH",  new Color(220, 80, 80)),  BorderLayout.NORTH);
        blCard.add(colorBtn("SOUTH",  new Color(80, 180, 80)),  BorderLayout.SOUTH);
        blCard.add(colorBtn("EAST",   new Color(80, 120, 220)), BorderLayout.EAST);
        blCard.add(colorBtn("WEST",   new Color(200, 130, 40)), BorderLayout.WEST);
        blCard.add(colorBtn("CENTER", new Color(120, 80, 200)), BorderLayout.CENTER);

        // ── GridLayout ──
        JPanel glCard = card("GridLayout (3×3)");
        glCard.setLayout(new BorderLayout());

        JPanel gridInner = darkPanel(new GridLayout(3, 3, 4, 4));
        Color[] gc = {
                new Color(220,80,80),  new Color(80,200,80),  new Color(80,80,220),
                new Color(200,160,0),  new Color(0,180,180),  new Color(180,0,180),
                new Color(60,140,220), new Color(220,100,60), new Color(80,200,140),
        };
        for (int i = 0; i < 9; i++) {
            JLabel cell = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            cell.setBackground(gc[i]);
            cell.setForeground(Color.WHITE);
            cell.setFont(new Font("Monospaced", Font.BOLD, 18));
            cell.setOpaque(true);
            gridInner.add(cell);
        }
        glCard.add(gridInner, BorderLayout.CENTER);

        // ── CardLayout ──
        JPanel clCard = card("CardLayout");
        clCard.setLayout(new BorderLayout(0, 8));

        CardLayout cardLayout = new CardLayout();
        JPanel cardContainer = darkPanel(cardLayout);
        cardContainer.setPreferredSize(new Dimension(0, 180));

        String[] cardNames = {"Card A", "Card B", "Card C", "Card D"};
        Color[]  cardColors = {new Color(60,30,100), new Color(20,70,60), new Color(70,30,30), new Color(30,50,90)};
        for (int i = 0; i < cardNames.length; i++) {
            JPanel c = darkPanel(new GridBagLayout());
            c.setBackground(cardColors[i]);
            JLabel lbl = new JLabel(cardNames[i]);
            lbl.setFont(new Font("Monospaced", Font.BOLD, 22));
            lbl.setForeground(Color.WHITE);
            c.add(lbl);
            cardContainer.add(c, cardNames[i]);
        }

        JPanel clButtons = darkPanel(new FlowLayout());
        JButton prev = styledButton("← Prev", ACCENT2);
        JButton next = styledButton("Next →", ACCENT);
        prev.addActionListener(e -> cardLayout.previous(cardContainer));
        next.addActionListener(e -> cardLayout.next(cardContainer));
        clButtons.add(prev);
        clButtons.add(next);

        clCard.add(cardContainer, BorderLayout.CENTER);
        clCard.add(clButtons, BorderLayout.SOUTH);

        grid.add(blCard);
        grid.add(glCard);
        grid.add(clCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 7 – JMenu / JPopupMenu / JDialog / JTabbedPane
    // ════════════════════════════════════════════════════════════
    private JPanel buildMenuDialogTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(2, 2, 12, 12));

        // ── JMenu / JMenuBar ──
        JPanel menuCard = card("JMenuBar + JMenu + JMenuItem");
        menuCard.setLayout(new BorderLayout(0, 8));

        JMenuBar mb = new JMenuBar();
        mb.setBackground(BG_DARK);
        mb.setBorder(BorderFactory.createLineBorder(BORDER));

        JMenu fileMenu = styledMenu("File");
        fileMenu.add(styledMenuItem("New"));
        fileMenu.add(styledMenuItem("Open"));
        fileMenu.add(styledMenuItem("Save"));
        fileMenu.addSeparator();
        fileMenu.add(styledMenuItem("Exit"));

        JMenu editMenu = styledMenu("Edit");
        JMenu subFormat = styledMenu("Format ▶");
        subFormat.add(styledMenuItem("Bold"));
        subFormat.add(styledMenuItem("Italic"));
        editMenu.add(styledMenuItem("Cut"));
        editMenu.add(styledMenuItem("Copy"));
        editMenu.add(styledMenuItem("Paste"));
        editMenu.add(subFormat);

        JMenu helpMenu = styledMenu("Help");
        helpMenu.add(styledMenuItem("About"));

        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(helpMenu);

        JLabel menuNote = muted("↑ Click the menus above to explore");
        menuNote.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuCard.add(mb, BorderLayout.NORTH);
        menuCard.add(menuNote, BorderLayout.CENTER);

        // ── JPopupMenu ──
        JPanel popupCard = card("JPopupMenu — Right-Click Menu");
        popupCard.setLayout(new BorderLayout());

        JLabel popupArea = new JLabel("Right-click anywhere in this box", SwingConstants.CENTER);
        popupArea.setFont(new Font("Monospaced", Font.ITALIC, 13));
        popupArea.setForeground(TEXT_MUTED);
        popupArea.setOpaque(true);
        popupArea.setBackground(BG_DARK);
        popupArea.setBorder(BorderFactory.createLineBorder(BORDER));

        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(BG_PANEL);

        JLabel popResult = new JLabel("Action: —");
        popResult.setFont(new Font("Monospaced", Font.BOLD, 13));
        popResult.setForeground(ACCENT);

        for (String item : new String[]{"Cut", "Copy", "Paste", "Select All"}) {
            JMenuItem mi = styledMenuItem(item);
            mi.addActionListener(e -> popResult.setText("Action: " + e.getActionCommand()));
            popup.add(mi);
        }

        popupArea.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) popup.show(popupArea, e.getX(), e.getY());
            }
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) popup.show(popupArea, e.getX(), e.getY());
            }
        });

        popupCard.add(popupArea, BorderLayout.CENTER);
        popupCard.add(popResult, BorderLayout.SOUTH);

        // ── JDialog ──
        JPanel dialogCard = card("JDialog — Modal Windows");
        dialogCard.setLayout(new BoxLayout(dialogCard, BoxLayout.Y_AXIS));

        JButton[] dBtns = {
                styledButton("Show Info Dialog",    ACCENT),
                styledButton("Show Warning Dialog", WARNING),
                styledButton("Show Input Dialog",   ACCENT2),
                styledButton("Show Custom Dialog",  SUCCESS),
        };

        JLabel dialogResult = new JLabel("Result: —");
        dialogResult.setForeground(SUCCESS);
        dialogResult.setFont(new Font("Monospaced", Font.ITALIC, 12));
        dialogResult.setAlignmentX(Component.LEFT_ALIGNMENT);

        dBtns[0].addActionListener(e -> JOptionPane.showMessageDialog(this,
                "This is an information dialog.\nUsed to inform the user.", "Info",
                JOptionPane.INFORMATION_MESSAGE));
        dBtns[1].addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Warning! Something needs attention.", "Warning",
                JOptionPane.WARNING_MESSAGE));
        dBtns[2].addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter your name:");
            if (val != null) dialogResult.setText("Input: " + val);
        });
        dBtns[3].addActionListener(e -> {
            JDialog dlg = new JDialog(this, "Custom Dialog", true);
            dlg.setSize(320, 200);
            dlg.setLocationRelativeTo(this);
            dlg.getContentPane().setBackground(BG_PANEL);
            dlg.setLayout(new BorderLayout(10, 10));
            JLabel msg = new JLabel("This is a custom JDialog!", SwingConstants.CENTER);
            msg.setForeground(ACCENT);
            msg.setFont(new Font("Monospaced", Font.BOLD, 14));
            JButton ok = styledButton("OK", SUCCESS);
            ok.addActionListener(x -> { dialogResult.setText("Custom dialog closed."); dlg.dispose(); });
            dlg.add(msg, BorderLayout.CENTER);
            dlg.add(ok, BorderLayout.SOUTH);
            dlg.setVisible(true);
        });

        for (JButton b : dBtns) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            dialogCard.add(Box.createVerticalStrut(8));
            dialogCard.add(b);
        }
        dialogCard.add(Box.createVerticalStrut(12));
        dialogCard.add(dialogResult);
        dialogCard.add(Box.createVerticalGlue());

        // ── JTabbedPane ──
        JPanel tpCard = card("JTabbedPane — Tabbed Groups");
        tpCard.setLayout(new BorderLayout());

        JTabbedPane innerTabs = new JTabbedPane();
        innerTabs.setBackground(BG_DARK);
        innerTabs.setForeground(TEXT_PRIMARY);
        innerTabs.setFont(new Font("Monospaced", Font.PLAIN, 11));

        String[][] tabContent = {
                {"Main", "This is the main tab.\nPrimary content goes here."},
                {"Settings", "Configure settings here.\nAdjust preferences."},
                {"Help", "Need help?\nConsult the documentation."},
        };
        for (String[] tc : tabContent) {
            JTextArea ta = new JTextArea(tc[1]);
            ta.setEditable(false);
            ta.setBackground(BG_CARD);
            ta.setForeground(TEXT_PRIMARY);
            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            innerTabs.addTab(tc[0], ta);
        }

        tpCard.add(innerTabs, BorderLayout.CENTER);

        grid.add(menuCard);
        grid.add(popupCard);
        grid.add(dialogCard);
        grid.add(tpCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  TAB 8 – JSlider / JScrollPane
    // ════════════════════════════════════════════════════════════
    private JPanel buildSlidersTab() {
        JPanel root = darkPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel grid = darkPanel(new GridLayout(1, 2, 12, 0));

        // ── JSlider ──
        JPanel sliderCard = card("JSlider — Value Range Input");
        sliderCard.setLayout(new BoxLayout(sliderCard, BoxLayout.Y_AXIS));

        // Volume slider
        JLabel volLbl = new JLabel("🔊 Volume: 50");
        volLbl.setForeground(ACCENT);
        volLbl.setFont(new Font("Monospaced", Font.BOLD, 14));
        volLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider volSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        styleSlider(volSlider);
        volSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        volSlider.addChangeListener(e -> volLbl.setText("🔊 Volume: " + volSlider.getValue()));

        // Brightness slider
        JLabel brightLbl = new JLabel("☀ Brightness: 50");
        brightLbl.setForeground(WARNING);
        brightLbl.setFont(new Font("Monospaced", Font.BOLD, 14));
        brightLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider brightSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        styleSlider(brightSlider);
        brightSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        brightSlider.addChangeListener(e -> brightLbl.setText("☀ Brightness: " + brightSlider.getValue()));

        // Rating slider (ticks)
        JLabel ratingLbl = new JLabel("★ Rating: 3");
        ratingLbl.setForeground(SUCCESS);
        ratingLbl.setFont(new Font("Monospaced", Font.BOLD, 14));
        ratingLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider ratingSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
        ratingSlider.setBackground(BG_CARD);
        ratingSlider.setForeground(TEXT_PRIMARY);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for (int i = 1; i <= 5; i++) {
            JLabel l = new JLabel("★".repeat(i));
            l.setForeground(WARNING);
            l.setFont(new Font("Dialog", Font.PLAIN, 10));
            labels.put(i, l);
        }
        ratingSlider.setLabelTable(labels);
        ratingSlider.addChangeListener(e -> ratingLbl.setText("★ Rating: " + ratingSlider.getValue()));

        // Vertical slider
        JLabel vSliderLbl = new JLabel("↕ Level: 50");
        vSliderLbl.setForeground(ACCENT2);
        vSliderLbl.setFont(new Font("Monospaced", Font.BOLD, 14));
        vSliderLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider vSlider = new JSlider(JSlider.VERTICAL, 0, 100, 50);
        styleSlider(vSlider);
        vSlider.addChangeListener(e -> vSliderLbl.setText("↕ Level: " + vSlider.getValue()));

        JPanel sliderBottom = darkPanel(new BorderLayout());
        sliderBottom.add(vSlider, BorderLayout.WEST);
        sliderBottom.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Component c : new Component[]{
                volLbl, volSlider, Box.createVerticalStrut(12),
                brightLbl, brightSlider, Box.createVerticalStrut(12),
                ratingLbl, ratingSlider, Box.createVerticalStrut(12),
                vSliderLbl, sliderBottom
        }) {
            sliderCard.add(c);
        }
        sliderCard.add(Box.createVerticalGlue());

        // ── JScrollPane (with JTextArea content) ──
        JPanel spCard = card("JScrollPane — Scrollable Content");
        spCard.setLayout(new BorderLayout(0, 8));

        JTextArea scrollContent = new JTextArea();
        scrollContent.setEditable(false);
        scrollContent.setBackground(BG_DARK);
        scrollContent.setForeground(TEXT_PRIMARY);
        scrollContent.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollContent.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        StringBuilder sb = new StringBuilder();
        sb.append("=== Java Swing Component Reference ===\n\n");
        String[][] info = {
                {"JFrame",        "Main application window container"},
                {"JPanel",        "Lightweight container for grouping components"},
                {"JLabel",        "Displays read-only text or image"},
                {"JButton",       "Clickable button with ActionListener support"},
                {"JTextField",    "Single-line text input field"},
                {"JPasswordField","Single-line password (masked) input"},
                {"JTextArea",     "Multi-line text input/display area"},
                {"JCheckBox",     "Toggle on/off checkbox control"},
                {"JRadioButton",  "Single-select button (use with ButtonGroup)"},
                {"JComboBox",     "Drop-down selection list"},
                {"JList",         "Scrollable list of selectable items"},
                {"JTable",        "Tabular data display with row/column model"},
                {"JTree",         "Hierarchical tree data display"},
                {"JScrollBar",    "Horizontal or vertical scrollbar"},
                {"JScrollPane",   "Adds scrollbars to any component"},
                {"JSlider",       "Numeric value selector with track"},
                {"JMenuBar",      "Top-level menu bar container"},
                {"JMenu",         "Drop-down menu in a menu bar"},
                {"JMenuItem",     "Clickable item inside a JMenu"},
                {"JPopupMenu",    "Context (right-click) menu"},
                {"JDialog",       "Modal or modeless dialog window"},
                {"JTabbedPane",   "Tab-based panel switcher"},
                {"JLayeredPane",  "Depth-layered component container"},
                {"BorderLayout",  "5-region layout: N/S/E/W/CENTER"},
                {"GridLayout",    "Equal-cell grid arrangement"},
                {"CardLayout",    "Single-card-at-a-time panel switcher"},
                {"FlowLayout",    "Left-to-right, wrapping component flow"},
                {"BoxLayout",     "Single-axis (X or Y) linear layout"},
                {"ScrollPaneLayout","Layout used by JScrollPane internally"},
        };
        for (String[] row : info) {
            sb.append(String.format("  %-20s  %s%n", row[0], row[1]));
        }
        sb.append("\n=== End of Reference ===");
        scrollContent.setText(sb.toString());

        JScrollPane sp = new JScrollPane(scrollContent);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        sp.getViewport().setBackground(BG_DARK);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JLabel spNote = muted("↕ Scroll to explore all 29 components");

        spCard.add(sp, BorderLayout.CENTER);
        spCard.add(spNote, BorderLayout.SOUTH);

        grid.add(sliderCard);
        grid.add(spCard);
        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    // ════════════════════════════════════════════════════════════
    //  Helper / Factory Methods
    // ════════════════════════════════════════════════════════════
    private JPanel darkPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(BG_DARK);
        return p;
    }

    private JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(4, 8, 8, 8),
                        title,
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Monospaced", Font.BOLD, 12),
                        ACCENT
                )
        ));
        return p;
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Monospaced", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    private JButton colorBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Monospaced", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    private JTextField styledTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        styleTextField(tf);
        return tf;
    }

    private void styleTextField(JTextField tf) {
        tf.setBackground(BG_DARK);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT);
        tf.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    }

    private JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_MUTED);
        l.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return l;
    }

    private JCheckBox styledCheckBox(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(new Font("Monospaced", Font.PLAIN, 13));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return cb;
    }

    private JRadioButton styledRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBackground(BG_CARD);
        rb.setForeground(TEXT_PRIMARY);
        rb.setFont(new Font("Monospaced", Font.PLAIN, 13));
        rb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return rb;
    }

    @SuppressWarnings("unchecked")
    private <T> JComboBox<T> styledComboBox(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setBackground(BG_DARK);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(new Font("Monospaced", Font.PLAIN, 13));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return cb;
    }

    private JMenu styledMenu(String text) {
        JMenu m = new JMenu(text);
        m.setForeground(TEXT_PRIMARY);
        m.setFont(new Font("Monospaced", Font.PLAIN, 12));
        m.getPopupMenu().setBackground(BG_PANEL);
        return m;
    }

    private JMenuItem styledMenuItem(String text) {
        JMenuItem mi = new JMenuItem(text);
        mi.setBackground(BG_PANEL);
        mi.setForeground(TEXT_PRIMARY);
        mi.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return mi;
    }

    private void styleSlider(JSlider s) {
        s.setBackground(BG_CARD);
        s.setForeground(TEXT_PRIMARY);
        s.setMajorTickSpacing(25);
        s.setMinorTickSpacing(5);
        s.setPaintTicks(true);
        s.setPaintLabels(true);
    }

    // ─── Entry Point ─────────────────────────────────────────────
    public static void main(String[] args) {
        // Use system look-and-feel as base, then override colors
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            SwingComponentsDemo demo = new SwingComponentsDemo();
            demo.setVisible(true);
        });
    }
}