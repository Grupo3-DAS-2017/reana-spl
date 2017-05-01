package fdtmc;

public class States {
    
	public State initial;
    public State success;
    public State error;
	
    
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
