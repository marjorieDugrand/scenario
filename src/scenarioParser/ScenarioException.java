package scenarioParser;

public class ScenarioException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ScenarioException() {
		super();
	}
	
	public ScenarioException(String message) {
		super(message);
	}
	
	public ScenarioException(Exception exception) {
		super(exception);
	}

}
