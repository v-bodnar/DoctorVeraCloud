package facade.exceptions;

public class PreexistingEntityException extends Exception {
	private static final long serialVersionUID = -8431348345805856824L;
	public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
        super(message);
    }
}
