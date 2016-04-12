import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame implements ActionListener {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CarParkView carParkView;
    private Model model;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private Car[][][] cars;
    private ActionEvent event;
    private JLabel revenueLabel;
    private JLabel queueLengthLabel;
    private JLabel amountOfCarsLabel;


    public View(int numberOfFloors, int numberOfRows, int numberOfPlaces, Model model) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
		this.model=model;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        carParkView = new CarParkView(); 
        
        Container contentPane = getContentPane();
        
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(1, 0));
        
        JPanel flow = new JPanel();
        flow.add(toolbar);

        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(flow, BorderLayout.SOUTH);
    
        JButton startButton = new JButton("Start");
        JButton stepButton = new JButton("Step one minute");
        JButton pauseButton = new JButton("Pause");
        JButton displayButton = new JButton("Display");
        JButton quitButton = new JButton("Quit");
        
        revenueLabel = new JLabel("Revenue");
        revenueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		revenueLabel.setText(String.valueOf(model.getRevenue()));
		
		queueLengthLabel = new JLabel("Queue Length");
        queueLengthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		queueLengthLabel.setText(String.valueOf(model.getQueueLength()));
		
		amountOfCarsLabel = new JLabel("Amount of Cars");
		amountOfCarsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		amountOfCarsLabel.setText(String.valueOf(model.getAmountOfCars()));
		

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
        toolbar.add(revenueLabel);
        toolbar.add(queueLengthLabel);
        toolbar.add(amountOfCarsLabel);
        
                  
        //contentPane.add(flow, BorderLayout.NORTH);
     
        
        
        pack();
        setVisible(true);
        updateView();
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
                 
                if (command == "Display") {
                	model.display();                    
                }
                                   
                if (command == "Quit") {
                	model.quit();                                        
                }                
            }          
        };        
        newThread.start();    
    }
    	

    	public void updateView() {
    		carParkView.updateView();
    		
    		revenueLabel.setText("Revenue: €" + String.valueOf(model.getRevenue())+ ".-");
    		queueLengthLabel.setText("Queue Length: " + String.valueOf(model.getQueueLength()));
    		amountOfCarsLabel.setText("Amount of Cars: " + String.valueOf(model.getAmountOfCars()) + "/540");

    		

    	}
    
     	public int getNumberOfFloors() {
            return numberOfFloors;
        }
    
     	public int getNumberOfRows() {
            return numberOfRows;
        }
    
        public int getNumberOfPlaces() {
            return numberOfPlaces;
        }
    
        public Car getCarAt(Location location) {
            if (!locationIsValid(location)) {
                return null;
            }
            return cars[location.getFloor()][location.getRow()][location.getPlace()];
        }
    
        public boolean setCarAt(Location location, Car car) {
            if (!locationIsValid(location)) {
                return false;
            }
            Car oldCar = getCarAt(location);
            if (oldCar == null) {
                cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
                car.setLocation(location);
                return true;
            }
            return false;
        }
    
        public Car removeCarAt(Location location) {
            if (!locationIsValid(location)) {
                return null;
            }
            Car car = getCarAt(location);
            if (car == null) {
                return null;
            }
            cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
            car.setLocation(null);
            return car;
        }
    
        public Location getFirstFreeLocation() {
            for (int floor = 0; floor < getNumberOfFloors(); floor++) {
                for (int row = 0; row < getNumberOfRows(); row++) {
                    for (int place = 0; place < getNumberOfPlaces(); place++) {
                        Location location = new Location(floor, row, place);
                        if (getCarAt(location) == null) {
                            return location;
                        }
                    }
                }
            }
            return null;
        }
    
        public Car getFirstLeavingCar() {
            for (int floor = 0; floor < getNumberOfFloors(); floor++) {
                for (int row = 0; row < getNumberOfRows(); row++) {
                    for (int place = 0; place < getNumberOfPlaces(); place++) {
                        Location location = new Location(floor, row, place);
                        Car car = getCarAt(location);
                        if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                            return car;
                        }
                    }
                }
            }
            return null;
        }
    
        public void tick() {
            for (int floor = 0; floor < getNumberOfFloors(); floor++) {
                for (int row = 0; row < getNumberOfRows(); row++) {
                    for (int place = 0; place < getNumberOfPlaces(); place++) {
                        Location location = new Location(floor, row, place);
                        Car car = getCarAt(location);
                        if (car != null) {
                            car.tick();
                        }
                    }
                }
            }
        }
    
        private boolean locationIsValid(Location location) {
            int floor = location.getFloor();
            int row = location.getRow();
            int place = location.getPlace();
            if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
                return false;
            }
            return true;
        }


        
        
private class CarParkView extends JPanel {
        
	private static final long serialVersionUID = 1L;
	private Dimension size;
    private Image carParkImage;    
    
    /**
     * Constructor for objects of class CarPark
     */
        
    public CarParkView() {
        size = new Dimension(0, 0);
    }
    
    /**
     * Overridden. Tell the GUI manager how big we would like to be.
     */
        
    public Dimension getPreferredSize() {
        return new Dimension(1200, 500);
    }
    
    /**
     * Overridden. The car park view component needs to be redisplayed. Copy the
     * internal image to screen.
     */
        
   public void paintComponent(Graphics g) {
        if (carParkImage == null) {
            return;
        }
    
        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(carParkImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
        }
    }
    
    public void updateView() {
        // Create a new car park image if the size has changed.
    	if (!size.equals(getSize())) {
            size = getSize();
            carParkImage = createImage(size.width, size.height);
            }
            Graphics graphics = carParkImage.getGraphics();
            for(int floor = 0; floor < getNumberOfFloors(); floor++) {
                for(int row = 0; row < getNumberOfRows(); row++) {
                    for(int place = 0; place < getNumberOfPlaces(); place++) {
                        Location location = new Location(floor, row, place);
                        Car car = getCarAt(location);
                        
                        if(car instanceof ParkingPassCar){Color color = car == null ? Color.white : Color.orange;
                        drawPlace(graphics, location, color);}
                        
                        else if (car instanceof ReservationCar){Color color = car == null ? Color.white : Color.blue;
                        drawPlace(graphics, location, color);}
                        
                        else {Color color = car == null ? Color.white : Color.red;
                        drawPlace(graphics, location, color);}                        
                    }
                }
            }
            repaint();
        }
    
        /**
         * Paint a place on this car park view in a given color.
         */
        
    private void drawPlace(Graphics graphics, Location location, Color color) {
        graphics.setColor(color);
        graphics.fillRect(
            location.getFloor() * 260 + (1 + (int)Math.floor(location.getRow() * 0.5)) * 75 + (location.getRow() % 2) * 20,
            60 + location.getPlace() * 10,
            20 - 1,
            10 - 1); // TODO use dynamic size or constants
    	}
 	}
}