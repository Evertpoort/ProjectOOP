import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Simulator implements ActionListener {
    private CarQueue entranceCarQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private SimulatorView simulatorView;
    private ActionEvent event;
  
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private int tickPause = 100;
    
    private boolean simRunning = false;

    int weekDayArrivals= 50; // average number of arriving cars per hour
    int weekendArrivals = 90; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 10; // number of cars that can pay per minute
    int exitSpeed = 9; // number of cars that can leave per minute

    public static void main(String[] args){
        new Simulator();        
    }
    
    public Simulator() {
        entranceCarQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        simulatorView = new SimulatorView(3, 6, 30, this);        
    }
    
    /**
     * Implementation of thread override.
     * @author Sam Kroon
     */
    
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
                        tick();
                    }
                }
                
                if (command == "Start") {
                    simRunning = true;
                    runsim(10000);
                }
                
                if (command == "Pause") {
                    simRunning = false;
                }
                
                if (command == "Quit") {
                    System.exit(0);
                }                
            }          
        };        
        newThread.start();    
    }
    
    
    public void runsim(int steps) {          
    		for (int i = 0; i < steps; i++) {
    			if(simRunning == true){
    				tick();
    			}
    			else{
    				break;
    			}    			
    		}	
    	}
        	
    public void tick() {
        // Advance the time by one minute.    	
    		minute++;
    		while (minute > 59) {
    			minute -= 60;
    			hour++;
    		}
    		while (hour > 23) {
	            hour -= 24;
	            day++;
	        }
	        while (day > 6) {
	            day -= 7;
	        }
    	    	
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDayArrivals
                : weekendArrivals;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.1;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        int numberOfCarsPerMinute = (int)Math.round(numberOfCarsPerHour / 60);

        // Add the cars to the back of the queue.
        for (int i = 0; i < numberOfCarsPerMinute; i++) {
        	Random r = new Random();
        	int Low = 0;
        	int High = 11;
        	int Result = r.nextInt(High-Low) + Low;
        	
        	if (Result > 8){
        		Car car = new ParkingPass();
        		entranceCarQueue.addCar(car);
        		}
        	
        	if (Result < 3 ){
        		Car car = new ReservationCar();
        		entranceCarQueue.addCar(car);
        	}
        	
        	else {
        		Car car = new AdHocCar();
        		entranceCarQueue.addCar(car);
        	}
        }

        // Remove car from the front of the queue and assign to a parking space.
        for (int i = 0; i < enterSpeed; i++) {
            Car car = entranceCarQueue.removeCar();
            if (car == null) {
                break;
            }
            // Find a space for this car.
            Location freeLocation = simulatorView.getFirstFreeLocation();
            if (freeLocation != null) {
                simulatorView.setCarAt(freeLocation, car);
                int stayMinutes = (int) (15 + random.nextFloat() * 10 * 60);
                car.setMinutesLeft(stayMinutes);
            }
        }

        // Perform car park tick.
        simulatorView.tick();

        // Add leaving cars to the exit queue.
        while (true) {
            Car car = simulatorView.getFirstLeavingCar();
            if (car == null) {
                break;
            }
            if (car instanceof ParkingPass){
            	simulatorView.removeCarAt(car.getLocation());
            	exitCarQueue.addCar(car);
            }
            
            if (car instanceof ReservationCar){
            	simulatorView.removeCarAt(car.getLocation());
            	exitCarQueue.addCar(car);
            }
            
            if (car instanceof AdHocCar){
            car.setIsPaying(true);
            paymentCarQueue.addCar(car);
            }
            
            
        }

        // Let cars pay.
        for (int i = 0; i < paymentSpeed; i++) {
            Car car = paymentCarQueue.removeCar();
            if (car == null) {
                break;
            }
            // TODO Handle payment.
            simulatorView.removeCarAt(car.getLocation());
            exitCarQueue.addCar(car);
        }

        // Let cars leave.
        for (int i = 0; i < exitSpeed; i++) {
            Car car = exitCarQueue.removeCar();
            if (car == null) {
                break;
            }
            // Bye!
        }

        // Update the car park view.
        simulatorView.updateView();

        // Pause.
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}