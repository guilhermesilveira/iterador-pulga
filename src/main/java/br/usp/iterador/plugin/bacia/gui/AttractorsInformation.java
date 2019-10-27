package br.usp.iterador.plugin.bacia.gui;

import javax.swing.JList;

import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.Basin;

public class AttractorsInformation {

	private JList attractorsList;

	private AttractorsListModel model;

	public AttractorsInformation(Basin basin, Application app) {
		this.model = new AttractorsListModel(basin);
		this.attractorsList = new JList(this.model);
		this.attractorsList.setBackground(app.getBackgroundColor());
		if (basin.getAttractors().size() != 0) {
			this.attractorsList.setSelectedIndex(0);
		}
	}

	public JList getList() {
		return attractorsList;
	}

	public AttractorsListModel getModel() {
		return model;
	}

}
