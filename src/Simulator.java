import java.awt.*;
import javax.swing.*;


public class Simulator {
	
	
	private JFrame screen;
	private FieldView fieldView;
	private Controller controller;
	private Logic logic;
	//private CarParkView parkView;
    
    public Simulator()
    {
    	logic = new Logic();
    	fieldView = new FieldView(logic, 3, 6, 30); 
        controller = new Controller(logic);    	
        
        screen=new JFrame("The Conway game of Life");
		screen.setSize(540, 285);
		screen.setResizable(false);
		screen.setLayout(null);
		screen.getContentPane().add(fieldView);		        
		screen.setVisible(true);
		
		screen.pack();
        
        //contentPane.add(population, BorderLayout.SOUTH);
        
        
    }
}
