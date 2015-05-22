package facade.exceptions;

public class RollbackFailureException extends Exception {
	private static final long serialVersionUID = -6313061291320659252L;
	public RollbackFailureException(String message, Throwable cause) {
        super(message, cause);
    }
    public RollbackFailureException(String message) {
        super(message);
    }
}
