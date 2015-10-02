package fdtmc;

public class Transition {

	private String actionName;
	private String probability;
	private State source, target;
	
	public Transition(State source, State target, String actionName, String probability) {
		this.source = source; 
		this.target = target;
		this.actionName = actionName; 
		this.probability = probability;
	}

	public String getActionName() {
		return actionName;
	}

	public String getProbability() {
		return probability;
	}

	public State getSource() {
		return source;
	}

	public State getTarget() {
		return target;
	}
}