import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public abstract class AbstractView extends JFrame {

	protected static Logic logic;
	
	public AbstractView(Logic logic){
		AbstractView.logic = logic;
		logic.addView(this);
	}
	
	public Logic getModel() {
		return logic;
	}
	
	
	
}
