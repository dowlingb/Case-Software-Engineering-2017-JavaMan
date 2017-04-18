package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import static javax.swing.GroupLayout.Alignment.*;

public class userInterface extends JFrame {
	
	private final static String newline = "\n";
	
	private JavaMan jm;
    private JMenuBar menuBar;
    private JMenu optionMenu;
    private JMenuItem help;
    private JMenuItem update;
    
    private JLabel accLabel;
    private JTextField accText;
    private JButton goButton1;
    
    private JLabel acLabel;
    private JTextField acText;
    private JButton goButton2;
    
    
    private JTextArea textArea;
    private JScrollPane scroller;
    
    //final Color entryBg;
    final Highlighter hilit;
    final Highlighter.HighlightPainter painter;
    final static Color  HILIT_COLOR = Color.LIGHT_GRAY;
    
    public userInterface() {

    	jm = new JavaMan();
    	jm.init();
    	
    	buildMenuBar();
    	
    	accLabel = new JLabel("Access Class:");;
        accText = new JTextField(20);
        goButton1 = new JButton("Go");
        goButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          	  accessClass();
            }
         });
        
        acLabel = new JLabel("Access:");
        acText = new JTextField(20);
        goButton2 = new JButton("Go");
        goButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          	  access();
            }
         });
        
        textArea = new JTextArea(50, 150);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        
        hilit = new DefaultHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
        textArea.setHighlighter(hilit);
        
        scroller = new JScrollPane(textArea);

        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(LEADING)
        				.addGroup(layout.createParallelGroup(LEADING)
        						.addComponent(scroller))
        		.addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(LEADING)
                        		.addComponent(accLabel)
                        		.addComponent(acLabel))
                        .addGroup(layout.createParallelGroup(LEADING)
                        		.addComponent(accText)
                        		.addComponent(acText))
                        .addGroup(layout.createParallelGroup(LEADING)
                        		.addComponent(goButton1)
        						.addComponent(goButton2))))
           );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                	.addComponent(accLabel)
                    .addComponent(accText)
                    .addComponent(goButton1))
                .addGroup(layout.createParallelGroup(LEADING)
                    	.addComponent(acLabel)
                        .addComponent(acText)
                        .addComponent(goButton2))
                .addGroup(layout.createParallelGroup(LEADING)
                	.addComponent(scroller))
            );
        
        setTitle("JAVAMAN USER INTERFACE");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
   
    
    private void accessClass(){
    	jm.accessClass(accText.getText(),this);
    }
    
    private void access(){
    	jm.access(acText.getText(),this);
    }
    
    public void print(String text){
        textArea.append(text + newline);
        accText.selectAll();

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
	
    private void buildMenuBar()
	{
		// Build the file and font menus.
		buildOptionMenu();

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Add the file and font menus to the menu bar.
		menuBar.add(optionMenu);

		// Set the menu bar for this frame.
		setJMenuBar(menuBar);
	}
	
	private void buildOptionMenu()
	{
	    // Create the Help menu item.
		help = new JMenuItem("Help");
		help.addActionListener(new helpListener());
	    update = new JMenuItem("Update");
	    update.addActionListener(new updateListener());
	    
	    optionMenu = new JMenu("Options");
	    // Add the items and some separator bars to the menu.
	    optionMenu.add(help);
	    optionMenu.add(update);
	
	}
	
	private class helpListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			textArea.append("Enter command to update using \"update\" or access a class using \"access <class path>\" where class path is formatted as java.lang.String");
		}
	}
	
	private class updateListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			update();
		}
	}
	
    private void update(){
    	jm.update(this);
    }
	
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                                  "javax.swing.plaf.metal.MetalLookAndFeel");
                                //  "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                                //UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                new userInterface().setVisible(true);
            }
        });
    }
}
