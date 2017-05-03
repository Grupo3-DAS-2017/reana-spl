package paramwrapper;

import java.util.Set;

public class Module {
	
	private String bodyModule = new String();
	
	public void concatenateParam(String params){
		this.bodyModule += "dtmc\n" +
			"\n" + params + "\n";
	}
	
	public void concatenateModuleName(String moduleName){
		this.bodyModule += "module " + moduleName + "\n";
	}
	
	public void concatenateState(String stateVariable, int stateRangeStart, int stateRangeEnd, int initialState){
		this.bodyModule +="	"+stateVariable+ " : ["+stateRangeStart+".."+stateRangeEnd+"] init "+initialState+";" +
				"\n";
	}
	
	public void concatenateCommand(Command command, String variable){
		this.bodyModule += "	"+command.makeString(variable) + "\n";
	}
	
	public void endModule(){
		this.bodyModule += "endmodule\n\n";
	}
	
	public void concatenateLabel(String label){
		this.bodyModule += "label \""+label+"\" = ";
	}
	
	public void concatenateStates(Set<Integer> states, String stateVariable){
	
		int count = 1;
		for (Integer state : states) {
			this.bodyModule += stateVariable+"="+state;
			if (count < states.size()) {
				this.bodyModule += " | ";
			}
			count++;
		}
		this.bodyModule += ";\n";
		
	}
	
	public String getResult(){
		return this.bodyModule;
	}
	
}
