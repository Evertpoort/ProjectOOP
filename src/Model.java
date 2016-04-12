
import java.util.Random;
import java.awt.event.*;

class Model   {
    private CarQueue entranceCarQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private View simulatorView;
  
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private int tickPause = 100;

    private int ZeroToTwoHours = 0;
    private int TwoToFourHours = 0;
    private int FourOrMoreHours = 0;
    private int revenue;

      
    private static final double AdHocProb = 0.6;
    private static final double ParkingPassProb = 0.3;
    private static final double ReservevationProb = 0.1;
    
    private boolean simRunning = false;
    
    int weekDayArrivals= 50; // average number of arriving cars per hour
    int weekendArrivals = 90; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 10; // number of cars that can pay per minute
    int exitSpeed = 9; // number of cars that can leave per minute
    
    public Model() {
        entranceCarQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        simulatorView = new View(3, 6, 30, this); 
        

    }

    /**
     * Implementation of thread override.
     * @author Sam Kroon
     */
    
    public int getRevenue()
    {
		return revenue;
    	
    }
    
    public void start()
	{
    	simRunning = true;
        runsim(1440);
	}
	
	public void pause()
	{
        simRunning = false;
	}
	
	public void step()
	{
		for(int i = 0; i<1; i++) {
            tick();
		}
		System.out.println(getRevenue());

	}
	
	public void display()
	{
        System.out.println("0-2 hours: " + ZeroToTwoHours + ". 2-4 hours: " + TwoToFourHours + ". 4+  hours: " + FourOrMoreHours +".");                    

	}
	
	public void quit()
	{
		System.exit(0);
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
        Random r = new Random();
        
        for (int i = 0; i < numberOfCarsPerMinute; i++) {
            if(r.nextDouble() <= AdHocProb) {
                Car car = new AdHocCar();
                entranceCarQueue.addCar(car);
            }
            
            else if(r.nextDouble() <= ParkingPassProb) {
                Car car = new ParkingPass();
                entranceCarQueue.addCar(car);
            }
                else if(r.nextDouble() <= ReservevationProb) {
                Car car = new ReservationCar();
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
                
                
                if (car.getMinutesLeft() < 121)
                {
                	ZeroToTwoHours++;
                }
                
                if (car.getMinutesLeft() > 120 && car.getMinutesLeft() < 241)
                {
                	TwoToFourHours++;
                }
                
                if (car.getMinutesLeft() > 240)
                {
                	FourOrMoreHours++;
                }
                
                
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
            	int hours = car.getMinutesLeft()/60*2;
            	revenue =+ hours;	
            }
            
            if (car instanceof ReservationCar){
            	simulatorView.removeCarAt(car.getLocation());
            	exitCarQueue.addCar(car);
            	int hours = car.getMinutesLeft()/60*3;
            	revenue =+ hours;
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
            int hours = car.getMinutesLeft()/60*3;
        	revenue =+ hours;
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