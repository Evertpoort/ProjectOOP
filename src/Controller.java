import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller extends AbstractController implements ActionListener  {
	
	
	private static final long serialVersionUID = 1L;
	//private JButton startButton;
	//private JButton stepButton;
	//private JButton pauseButton;
	//private JButton displayButton;
	//private JButton quitButton;
	private ActionEvent event;



	public Controller(Logic logic)
	{	

		super(logic);
		
		//Container contentPane = getContentPane();
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(1, 0));
        
        JPanel flow = new JPanel();
        flow.add(toolbar);
        
        JButton startButton = new JButton("Start");
        JButton stepButton = new JButton("Step one minute");
        JButton pauseButton = new JButton("Pause");
        JButton displayButton = new JButton("Display");
        JButton quitButton = new JButton("Quit");

        startButton.addActionListener(this);
        stepButton.addActionListener(this);
        pauseButton.addActionListener(this);
        displayButton.addActionListener(this);
        quitButton.addActionListener(this);

        toolbar.add(startButton);
        toolbar.add(stepButton);     
        toolbar.add(pauseButton);        
        toolbar.add(displayButton);             
        toolbar.add(quitButton);
                  
        //contentPane.add(flow, BorderLayout.NORTH);                
        //pack();
        setVisible(true);	
	}
	

    public void setActionEvent(ActionEvent e) {
        event = e;
    }
    
    public ActionEvent getActionEvent() {
        return event;
    }
    
    public void actionPerformed(ActionEvent e) {
        setActionEvent(e);
        
        Thread newThread = new Thread() {
            public void run() {
            	
                ActionEvent event = getActionEvent();                
                String command = event.getActionCommand();
                
                if(command == "Step one minute") {
            		for(int i = 0; i<1; i++) {
                        //tick();
            		}                
            	}
                
                if(command == "Pause") {
                    //simRunning = false;
                }
                               
                if (command == "Start") {
                	//simRunning = true;
                    //runsim(1440);                
                }
                 
                if (command == "Display") {
                    //System.out.println("0-2 hours: " + ZeroToTwoHours + ". 2-4 hours: " + TwoToFourHours + ". 4+  hours: " + FourOrMoreHours +".");                    
                }
                                   
                if (command == "Quit") {
            		//System.exit(0);
                }                
            }          
        };        
        newThread.start();    
    }


}
