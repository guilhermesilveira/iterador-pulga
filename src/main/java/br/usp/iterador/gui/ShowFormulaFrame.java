package br.usp.iterador.gui;

import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.MultiPaneCreator;
import br.usp.iterador.io.SimpleProperties;
import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Intermediate;
import br.usp.iterador.model.Parameter;
import br.usp.iterador.model.Variable;
import br.usp.iterador.plugin.PanelManager;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Main panel.
 * 
 * @author Guilherme Silveira
 */
public class ShowFormulaFrame implements PulgaAction {

	private static final Logger logger = Logger
			.getLogger(ShowFormulaFrame.class);

	private PanelManager panelManager;

	private Application application;

	private WindowManager windows;

	private PanelBuilder panelBuilder;

	public ShowFormulaFrame(Application application, PanelManager panelManager,
			PanelBuilder panelBuilder, WindowManager windows) {
		this.application = application;
		this.panelManager = panelManager;
		this.panelBuilder = panelBuilder;
		this.windows = windows;
	}

	private JScrollPane createContentPane() {
		panelManager.refresh();
		List<DataModel> models = panelManager.getModels();
		List<Component> panels = panelManager.getPanels();
		panels.add(0, getDynamicPanel("/iteration.frame", models));
		panels.add(1, getDynamicPanel("/parameters.frame", models));
		panels.add(2, getDynamicPanel("/intermediate.frame", models));
		ReloadListener reloadListener = new ReloadListener(models);
		for (DataModel model : models) {
			model.addActionListener(reloadListener);
		}
		return new MultiPaneCreator().createPane(panels);
	}

	private JPanel getDynamicPanel(String configFile, List<DataModel> models) {
		SimpleProperties p = new SimpleProperties(configFile);
		try {
			return panelBuilder.build(p, models);
		} catch (JXPathException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void newParameter() {
		application.getParameters().add(
				new Parameter("name", "0.0", "-1.0", "+1.0"));
		execute();
	}

	public void execute() {
		logger.debug("executing show formula frame");
		PulgaFrame frame = windows.showFrame("formula",
				"formula_parameter_and_starting_conditions", new JPanel(), 300,
				600);
		frame.setContentPane(createContentPane());
		frame.validate();
	}

	public void remove(Parameter parameter) {
		application.getParameters().remove(parameter);
		execute();
	}

	public void removeIntermediate(Intermediate intermediate) {
		application.getIntermediateExpressions().remove(intermediate);
		execute();
	}

	public void newIntermediate() {
		String r = "" + ((char) (Math.random() * 26 + 'a'))
				+ ((char) (Math.random() * 26 + 'a'))
				+ ((char) (Math.random() * 26 + 'a'));
		Intermediate intermediate = new Intermediate("double", r, "0.0");
		application.getIntermediateExpressions().add(intermediate);
		execute();
		editIntermediate(intermediate);
	}

	public void editIntermediate(Intermediate intermediate) {
		ValueEditDialog dialog = new ValueEditDialog();
		dialog.setName(intermediate.getName());
		dialog.setValue(intermediate.getValue());
		dialog.setVisible(true);
		if (dialog.getSelectedButton() == ValueEditDialog.BUTTON_OK) {
			intermediate.setName(dialog.getName());
			intermediate.setValue(dialog.getValue());
		}
	}

	public void newVariable() {
		application.getVariables().add(
				new Variable(0.0, "x" + (application.getDimension() + 1)));
		execute();
	}

	public void removeVariable(Variable variable) {
		application.getVariables().remove(variable);
		execute();
	}

}
