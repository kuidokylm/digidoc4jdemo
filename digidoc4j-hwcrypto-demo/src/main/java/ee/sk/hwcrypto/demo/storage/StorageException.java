package ee.sk.hwcrypto.demo.storage;

public class StorageException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7527522033587932635L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
}
}
