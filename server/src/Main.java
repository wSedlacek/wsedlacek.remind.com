public class Main {

	public static void main(String[] args) throws Exception {
		FireMessage f = new FireMessage("MY TITLE", "TEST MESSAGE");
	    String fireBaseToken="foFGSB9Oyw0:APA91bFwAta2O9HT5QuTFLzzAyMfiJzKEGWh-kLj9yu9RB_Fa7lynROXgCzJO6Dh-e_vtRIq6vgWU58iA7jGb_HPavYQktiXydxU58pX0fxTZy5hCvmISewk-P6T1NdSvHpfdgtkW0Qm";
	    f.sendToToken(fireBaseToken);
	}
}
