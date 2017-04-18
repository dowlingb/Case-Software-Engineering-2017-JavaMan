import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


import static javax.swing.GroupLayout.Alignment.*;

public class userInterface extends JFrame {
	
	private final static String newline = "\n";
	
    private JMenuBar menuBar;
    private JMenu optionMenu;
    private JMenuItem help;
    private JMenuItem update;
    
    private JLabel accLabel = new JLabel("Access Class:");;
    private JTextField accText = new JTextField(20);
    private JButton goButton1 = new JButton("go");

    


    private JTextArea textArea = new JTextArea(5, 20);
    
    public userInterface() {

    	buildMenuBar();
    	
        textArea.setEditable(false);
        //JScrollPane scrollPane = new JScrollPane(textArea);

        goButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          	  accessClass();
            }
         });
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
//        layout.setHorizontalGroup(layout.createSequentialGroup()
//                .addComponent(accLabel)
//                .addGroup(layout.createParallelGroup(LEADING)
//                    .addComponent(accText))
//                .addGroup(layout.createParallelGroup(LEADING)
//                    .addComponent(goButton1))
//           );
        
//        layout.setHorizontalGroup(layout.createSequentialGroup()
//                
//                .addGroup(layout.createParallelGroup(LEADING)
//                	.addComponent(accLabel))
//                .addGroup(layout.createParallelGroup(LEADING)
//                    .addComponent(accText))
//                .addGroup(layout.createParallelGroup(LEADING)
//                	.addComponent(goButton1))
//                .addComponent(textArea)
//                    
//           );
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(LEADING)
        		.addComponent(textArea)
        		.addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(LEADING)
                        		.addComponent(accLabel))
                        .addGroup(layout.createParallelGroup(LEADING)
                        		.addComponent(accText))
                        .addGroup(layout.createParallelGroup(LEADING)
                        		.addComponent(goButton1))))
//                .addGroup(layout.createParallelGroup(LEADING)
//                	.addComponent(accLabel))
//                .addGroup(layout.createParallelGroup(LEADING)
//                    .addComponent(accText))
//                .addGroup(layout.createParallelGroup(LEADING)
//                	.addComponent(goButton1))
                
                    
           );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                	.addComponent(accLabel)
                    .addComponent(accText)
                    .addComponent(goButton1))
                .addGroup(layout.createParallelGroup(LEADING)
                	.addComponent(textArea))

            );

        
        setTitle("UI");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
   
//    public void help(){
//    	textArea.append("Enter command to update using \"update\" or access a class using \"access <class path>\" where class path is formatted as java.lang.String");
//    }
    
    private void accessClass(){
    	System.out.println(accText.getText());
    	new JavaMan().accessClass(accText.getText(),this);
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
			
		}
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
