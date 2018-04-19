public class Main {

	public static void main(String[] args) throws Exception {
		FireMessage f = new FireMessage("MY TITLE", "TEST MESSAGE");
	    String fireBaseToken="dXivmF0Km4c:APA91bEcFZwAvklXRZmMuzIcBCRnNaKbLKHtHEuW_lKsEThRA-eeeDTqs1Spydpx10Qk-q4iFl54iC5bYeqEAadkBAffhHVIDACGCOQx2zGIPgEDYpYPps9eGE--YWhMFmMPaUrIpb1Q";
	    f.sendToToken(fireBaseToken);
	}
}
