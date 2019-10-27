package br.usp.iterador.model;

/**
 * Intermediate value.
 * 
 * @author Guilherme Silveira
 */
public class Intermediate {

	private String type, name, value;

	public Intermediate(String type, String nome, String valor) {
		this.type = type;
		this.name = nome;
		this.value = valor;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setName(String nome) {
		this.name = nome;
	}

	/**
	 * @return Returns the valor.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param valor
	 *            The valor to set.
	 */
	public void setValue(String valor) {
		this.value = valor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}