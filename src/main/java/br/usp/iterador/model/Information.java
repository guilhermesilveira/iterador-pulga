package br.usp.iterador.model;

/**
 * File information
 * @author Guilherme Silveira
 * @version $Revision$
 */
public class Information {

	private String name = "Unknown";

	private String description = "";

	private String authors = "";

	/**
	 * @return Returns the authors.
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors
	 *            The authors to set.
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
