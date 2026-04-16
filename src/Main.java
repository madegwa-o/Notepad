import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Notepad implements ActionListener {

    JFrame frame;
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, viewMenu,zoom;
    JMenuItem open, save, saveAs,exit, cut, copy, paste,zoomIn,zoomOut,statusBar,wordwrap;
    JTextArea textAr;



    Notepad(){
        menuBar = new JMenuBar();

        //Todo: menu bar
        menuBar.setBounds(5,5,400,20);

        //Todo: File Menu

        /*
            open
            save
            save As
            exit
         */
        fileMenu = new JMenu("File");

        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save As");
        exit = new JMenuItem("Exit");


        fileMenu.add(open);
        fileMenu.add(saveAs);
        fileMenu.add(save);
        fileMenu.add(exit);


        editMenu = new JMenu("Edit");
        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);


        viewMenu = new JMenu("View");
        zoom = new JMenu("Zoom");
        zoomIn = new JMenuItem("Zoom In");
        zoomOut = new JMenuItem("Zoom Out");
        statusBar=new JMenuItem("Status Bar");
        wordwrap=new JMenuItem("Word Wrap");



        zoom.add(zoomIn);
        zoom.add(zoomOut);
        viewMenu.add(zoom);
        viewMenu.add(statusBar);
        viewMenu.add(wordwrap);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        textAr = new JTextArea("Najua ulifikiria siwezi!");
        textAr.setBounds(0,25,400,400);


        frame = new JFrame();
        frame.add(menuBar);
        frame.add(textAr);
        frame.setLayout(null);
        frame.setBounds(50,50, 400,400);
        frame.setVisible(true);




    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == copy) {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        }
    }
}

class Main {

    public static void main(String[] args) {
        new Notepad();
    }
}