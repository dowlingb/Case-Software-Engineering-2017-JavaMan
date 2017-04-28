import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
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
    

    
    public userInterface() {

    	jm = new JavaMan();
    	jm.setUI(this);
    	//jm.init();
    	
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
    	jm.accessClass(accText.getText());
    }
    
    private void access(){
    	jm.access(acText.getText());
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
			textArea.append("To find documentation for a class: Type the name of the class "
					+"with its full path name (eg java.lang.String) into the Access Class "
					+"textbox and click the “Go” button to the right of it.  The "
					+ "corresponding man page file will be printed to the text output "
					+ "field below. \n\nTo find documentation for a method/constructor: Type "
					+ "the name of the method with its full path name "
					+ "(eg java.lang.String.charAt) into the Access textbox and click the "
					+ "“Go” button to the right of it.  The corresponding man page file "
					+ "will be printed to the text output field below. \n\nTo update the "
					+ "documentation database: Javaman is configured to automatically "
					+ "update itself when it is run if its log indicates enough time has "
					+ "passed since its last update.  However, a manual update can also be "
					+ "initiated by selecting “Update” from the “Options” dropdown menu. "
					+ "This may cause the program to stall for a time as a full update can "
					+ "take over a minute to complete. \n\nTo view help: The user can view a "
					+ "short help blurb for how to use Javaman by selecting the “Help” "
					+ "option from the “Options” dropdown menu.  The help blurb is "
					+ "printed to the text output field below.");
		}
	}
	
	private class updateListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			jm.update();
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
