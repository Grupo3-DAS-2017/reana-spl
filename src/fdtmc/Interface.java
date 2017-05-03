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
        this.states.setInitial(initial);
        this.states.setSuccess(success);
        this.states.setError(error);
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
        return states.getInitial();
    }

    public State getSuccess() {
        return states.getSuccess();
    }

    public State getError() {
        return states.getError();
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
            return states.getInitial().equals(other.states.getInitial())
                    && states.getSuccess().equals(other.states.getSuccess())
                    && states.getError().equals(other.states.getError())
                    && successTransition.equals(other.successTransition)
                    && errorTransition.equals(other.errorTransition);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return states.getInitial().hashCode()
                + states.getSuccess().hashCode()
                + states.getError().hashCode()
                + successTransition.hashCode()
                + errorTransition.hashCode();
    }

}
