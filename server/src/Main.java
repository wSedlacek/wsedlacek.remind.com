public class Main {

	public static void main(String[] args) throws Exception {
		FireMessage f = new FireMessage("MY TITLE", "TEST MESSAGE");
	    String fireBaseToken="eGjiTwXrCLo:APA91bFOuMxz8RdvbL83RsLY-446BJAbPgFllLPNtTkCvjBk8yg8qOGVfvMpvU7F37R-nIUWkhuAX7qpYLsQn64qZvjI6TT0wLQbsgHz4Y9DbOG3J2niTgEZDGY4vgg3OQA9LP5TtM3A";
	    f.sendToToken(fireBaseToken);
	}
}
