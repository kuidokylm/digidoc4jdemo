package ee.sk.hwcrypto.demo.storage;

public class StorageFileNotFoundException extends StorageException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2828994800372598606L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
