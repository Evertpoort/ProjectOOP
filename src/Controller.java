import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller extends JFrame implements ActionListener {
	
	private Model model;
    private ActionEvent event;
    private String test = "1";
    public JTable table;

	public Controller(Model model)
	{				
		this.model=model;

		Container contentPane = getContentPane();
		
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(1, 0));
        
        JPanel flow = new JPanel();
        flow.add(toolbar);

        JButton startButton = new JButton("Start");
        JButton stepButton = new JButton("Step one minute");
        JButton pauseButton = new JButton("Pause");
        JButton quitButton = new JButton("Quit");
        JLabel revenueLabel = new JLabel("Label");
        
		revenueLabel.setText(String.valueOf(test));

        startButton.addActionListener(this);
        stepButton.addActionListener(this);
        pauseButton.addActionListener(this);
        quitButton.addActionListener(this);

        toolbar.add(startButton);
        toolbar.add(stepButton);     
        toolbar.add(pauseButton);        
        toolbar.add(quitButton);
        toolbar.add(revenueLabel);
                  
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
                	model.step();                    
                }
                
                if(command == "Pause") {
                	model.pause();                    
                }
                               
                if (command == "Start") {
                	model.start();                    
                }
                                                
                if (command == "Quit") {
                	model.quit();                                        
                }                
            }          
        };        
        newThread.start();    
    }
}
