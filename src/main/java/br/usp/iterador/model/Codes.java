package br.usp.iterador.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Guilherme Silveira
 */
public class Codes {

	/** codes */
	private List<PieceOfCode> codes = new ArrayList<PieceOfCode>();

	public List<PieceOfCode> getCodes() {
		return codes;
	}

	public void add(PieceOfCode code) {
		this.codes.add(code);
		update();
	}

	public void update() {
		Collections.sort(codes);
	}

	/**
	 * @param codes
	 *            The codes to set.
	 */
	public void setCodes(List<PieceOfCode> codes) {
		this.codes = codes;
	}

}
