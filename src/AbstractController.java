import javax.swing.*;

public abstract class AbstractController extends JPanel {
	protected Logic logic;
	
	public AbstractController(Logic logic) {
		this.logic=logic;
	}
}
