package fdtmc;

public class States {
    
	private State initial;
	private State success;
	private State error;
	
    
    public State getInitial() {
		return initial;
	}
	public void setInitial(State initial) {
		this.initial = initial;
	}
	public State getSuccess() {
		return success;
	}
	public void setSuccess(State success) {
		this.success = success;
	}
	public State getError() {
		return error;
	}
	public void setError(State error) {
		this.error = error;
	}
    
    
 
}
