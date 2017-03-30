import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class startingPoint extends JFrame {

	private JPanel wholePanel;
	
	public startingPoint(){
	      
	      setPreferredSize(new Dimension(2000,1000));
	      //setMaximumSize(new Dimension(800,800));
	      // Specify what happens when the close when the button is clicked.
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      
	      
	      //Set the wholePanel, which is the content panel. 
	      wholePanel= new JPanel();
	      wholePanel.setMaximumSize(new Dimension(400,400));
	      setContentPane(wholePanel);
	      wholePanel.setBackground(Color.WHITE);
	      wholePanel.setLayout(new BoxLayout(wholePanel, BoxLayout.Y_AXIS));
	      //wholePanel.setLayout(BorderLayout.CENTER);
	      

	      //Menu bar
//	      JMenuBar bar = new JMenuBar();
//	      JMenu menu = new JMenu("File");
//	      bar.add(menu);
//	      menu.add(new JMenuItem("Close"));
//	      menu.add(new JSeparator()); // SEPARATOR
//	      menu.add(new JMenuItem("Exit"));
//	      wholePanel.add(bar);
	      
	      //Set up the table.
//	      Object rowData[][] = { { "Row1-Column1", "Row1-Column2", "Row1-Column3" },
//	    	        { "Row2-Column1", "Row2-Column2", "Row2-Column3" } };
//	      
//	      Object columnNames[] = { "Column One", "Column Two", "Column Three" };
//	      JTable table = new JTable(rowData, columnNames);
//	      JTable table = new JTable(10,3);
//	      table.setRowHeight(30);
//	      table.set
	      
	      JLabel title = new JLabel("JAVAMAN USER INTERFACE");
	      wholePanel.add(title);
	      
	      DefaultTableModel model = new DefaultTableModel(); 
	      JTable table = new JTable(model); 
	      table.setRowHeight(30);
	      
	      // Create a couple of columns 
	      model.addColumn("Col1"); 
	      model.addColumn("Col2"); 
	      model.addColumn("Col3");

	      // Append a row 
	      model.addRow(new Object[]{"PACKAGE","DESCRIPTION","ABOUT"});
	      model.addRow(new Object[]{"java.applet", "Provides the classes necessary to create an applet and the classes an applet uses to communicate with its applet context.",""});
	      model.addRow(new Object[]{"java.awt.dnd","Drag and Drop is a direct manipulation gesture found in many Graphical User Interface systems that provides a mechanism to transfer information between two entities logically associated with presentation elements in the GUI.",""});
	      model.addRow(new Object[]{"java.awt","Contains all of the classes for creating user interfaces and for painting graphics and images.",""});
	      model.addRow(new Object[]{"java.awt.color","Provides classes for color spaces.",""});
	      model.addRow(new Object[]{"java.awt.datatransfer","Provides interfaces and classes for transferring data between and within applications.",""});
	      model.addRow(new Object[]{"","",""});
	      model.addRow(new Object[]{"","",""});
	      model.addRow(new Object[]{"","",""});
	      wholePanel.add(table);
	      
	      //Add button 
	      JButton addButton = new JButton("Add");

	      
	      
	      final TextField addFirstName = new TextField();
	      addFirstName.setText("Package");
	        //addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
	      final TextField addLastName = new TextField();
	      //addLastName.setMaxWidth(lastNameCol.getPrefWidth());
	      addLastName.setText("Description");
	      final TextField addEmail = new TextField();
	      //addEmail.setMaxWidth(emailCol.getPrefWidth());
	      addEmail.setText("About");
	      
	      addButton.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  model.addRow(new Object[]{addFirstName.getText(),addLastName.getText(),addEmail.getText()});
	          }
	       });
	      
	      wholePanel.add(addButton);
	      wholePanel.add(addFirstName);
	      wholePanel.add(addLastName);
	      wholePanel.add(addEmail);

	      
	      //Update button 
	      JButton updateButton = new JButton("Update");
	      wholePanel.add(updateButton);
	      

	      
	      // Pack and display the window.
	      pack();
	      setVisible(true);
	  	}
	
	public static void main(String[] args){
		startingPoint sp = new startingPoint();
	}  
	
}
