package fdtmc;

/**
 * Represents an abstracted away FDTMC fragment.
 *
 * @author thiago
 */
public class Interface {
    private String abstractedId;
    private States states;
    private Transition successTransition;
    private Transition errorTransition;

    public Interface(String abstractedId, State initial, State success, State error, Transition successTransition, Transition errorTransition) {
        this.states = new States();
    	this.abstractedId = abstractedId;
        this.states.initial = initial;
        this.states.success = success;
        this.states.error = error;
        this.successTransition = successTransition;
        this.errorTransition = errorTransition;
    }

    public States getStates() {
		return states;
	}

	public void setStates(States states) {
		this.states = states;
	}

	public State getInitial() {
        return states.initial;
    }

    public State getSuccess() {
        return states.success;
    }

    public State getError() {
        return states.error;
    }

    public Transition getSuccessTransition() {
        return successTransition;
    }

    public Transition getErrorTransition() {
        return errorTransition;
    }

    public String getAbstractedId() {
        return abstractedId;
    }

    /**
     * Interfaces are compared for equality disregarding the abstracted id.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Interface) {
            Interface other = (Interface) obj;
            return states.initial.equals(other.states.initial)
                    && states.success.equals(other.states.success)
                    && states.error.equals(other.states.error)
                    && successTransition.equals(other.successTransition)
                    && errorTransition.equals(other.errorTransition);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return states.initial.hashCode()
                + states.success.hashCode()
                + states.error.hashCode()
                + successTransition.hashCode()
                + errorTransition.hashCode();
    }

}
