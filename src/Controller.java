import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller extends JFrame implements ActionListener {
	
	
	private Simulator simulator;
    private ActionEvent event;


	public Controller(Simulator simulator)
	{		
		
		this.simulator=simulator;

		Container contentPane = getContentPane();
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
                  
        contentPane.add(flow, BorderLayout.NORTH);                
        pack();
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
                    simulator.step();                    
                }
                
                if(command == "Pause") {
                    simulator.pause();                    
                }
                               
                if (command == "Start") {
                    simulator.start();                    
                }
                 
                if (command == "Display") {
                    simulator.display();                    
                }
                                   
                if (command == "Quit") {
                    simulator.quit();                                        
                }                
            }          
        };        
        newThread.start();    
    }
}
