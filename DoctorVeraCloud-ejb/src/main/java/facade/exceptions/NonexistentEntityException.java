package facade.exceptions;

public class NonexistentEntityException extends Exception {
	private static final long serialVersionUID = 8887815390820548302L;
	public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public NonexistentEntityException(String message) {
        super(message);
    }
}
