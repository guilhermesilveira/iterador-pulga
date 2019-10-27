package br.usp.iterador.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.util.DataModel;

public class ReloadListener implements ActionListener {
	
	private static final Logger log = Logger.getLogger(ReloadListener.class);

	private List<DataModel> models;

	public ReloadListener(List<DataModel> models) {
		this.models = models;
	}

	public void actionPerformed(ActionEvent e) {
		log.debug("reloading all data models");
		for (DataModel model : models) {
			model.reloadAllValues(e.getSource());
		}
	}

}
