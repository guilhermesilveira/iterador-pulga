package br.usp.iterador.prefs;

public class Pref {
	
	private static Preferences io = new Preferences("io");

	public Preferences getIo() {
		return io;
	}

}
