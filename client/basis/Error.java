package client.basis;

public class Error {
	private int errorCode;	
	private String errorDescription;

	public Error(int errorCode, String errorDescription) {
		this.errorCode = errorCode;		
		this.errorDescription = errorDescription;
	}

	public int errorCode() {
		return this.errorCode;
	}	

	public String errorDescription() {
		return this.errorDescription;
	}

	public String toString() {
		return "ERROR " + this.errorCode + ": " + this.errorDescription;
	}
}