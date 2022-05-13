package exceptions;

@SuppressWarnings("serial")
abstract class GameActionException extends Exception {

	public GameActionException() {
		super();
	}

	public GameActionException(String s) {
		super(s);
	}

}
