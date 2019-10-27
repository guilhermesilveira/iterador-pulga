package br.usp.iterador.gui;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.ExecuteButton;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ModelDoubleField;
import br.usp.iterador.gui.util.ModelField;
import br.usp.iterador.gui.util.ModelIntField;
import br.usp.iterador.gui.util.ModelTextField;
import br.usp.iterador.io.SimpleProperties;
import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.jxpath.JXPathResolver;
import br.usp.iterador.jxpath.Member;
import br.usp.iterador.jxpath.ReflectionResolver;
import br.usp.iterador.model.Application;

public class PanelBuilder {

	private static final Logger LOG = Logger.getLogger(PanelBuilder.class);

	private final GuiFactory guiFactory;

	private final JXPathResolver resolver;

	private final ReflectionResolver genericResolver;

	public PanelBuilder(Application application, GuiFactory factory,
			ReflectionResolver refResolver) {
		this.resolver = new JXPathResolver(application);
		this.guiFactory = factory;
		this.genericResolver = refResolver;
	}

	@SuppressWarnings("unchecked")
	public JPanel build(SimpleProperties p, List<DataModel> models)
			throws JXPathException {

		String title = p.getString("title");

		String forEach = p.getString("forEach");
		List<Object> objModels = (List<Object>) resolver.resolve(forEach).get();

		String[] edit = p.getStringArray("edit");
		String[] onUpdate = p.getStringArray("onUpdate");
		String[] rightSide = p.getStringArray("rightSide");

		JPanel panel = guiFactory.createBorderedPanel(title);
		panel.setLayout(new GridBagLayout());
		LayoutPosition position = new LayoutPosition(panel);
		position.setPlace(1, 0);

		for (Object obj : objModels) {
			models.add(addRow(edit, obj, rightSide, position));
			position.nextLine();
		}

		addStatusBar(p, position);

		if (p.getBoolean("showHeader", true)) {
			position.setPlace(0, 0);
			position.setWidth(1);
			if (objModels.size() != 0) {
				addHeader(objModels.get(0), edit, position);
			}
		}

		/*
		 * for (String update : onUpdate) { ActionListener listener =
		 * guiFactory.getLogicListener(update); DataModel m = new DataModel(new
		 * Object()); m.addActionListener(listener); models.add(m); }
		 */

		return panel;
	}

	private void addStatusBar(SimpleProperties p, LayoutPosition position) {
		String[] statusBar = p.getStringArray("statusBar");
		if (statusBar.length != 0) {
			position.setWidth(position.getCols());
			position.add(getStatus(statusBar), 0.1, 0.1);
			position.setWidth(1);
		}
	}

	private JPanel getStatus(String[] statusBar) {
		JPanel panel = new JPanel(new GridLayout(1, statusBar.length));
		for (String status : statusBar) {
			try {
				panel.add(getExecuteButton(status));
			} catch (ClassNotFoundException e) {
				LOG.error("Unable to create button " + status, e);
			}
		}
		return panel;
	}

	private void addHeader(Object obj, String[] edit, LayoutPosition position) {
		LOG.debug("Adding header");
		for (String column : edit) {
			position.add(guiFactory.getLabel(column), 0.1, 0.1);// getColumnWidth(obj,
			// column), 1);
			position.nextColumn();
		}
		position.nextLine();
	}

	private DataModel addRow(String[] edit, Object obj, String[] rightSide,
			LayoutPosition position) {
		DataModel model = new DataModel(obj);
		for (String column : edit) {
			try {
				Member member = genericResolver.getGenericMember(
						obj.getClass(), column);
				ModelField modelField = getModelForType(member);
				model.addModel(modelField);
				position.add(modelField.getComponent(), modelField.getWidth(),
						modelField.getHeight());
				position.nextColumn();
			} catch (NoSuchFieldException e) {
				LOG.error("Unable to find a field for editing", e);
			}
		}
		for (String call : rightSide) {
			try {
				JButton button = getExecuteButton(call, obj);
				position.add(button, 0.1, 0.1);
				position.nextColumn();
			} catch (ClassNotFoundException e) {
				LOG.error("Unable to create button " + call, e);
			}
		}
		return model;
	}

	@SuppressWarnings("unchecked")
	private JButton getExecuteButton(String call, Object... obj)
			throws ClassNotFoundException {
		int pos = call.lastIndexOf(".");
		Class type = Class.forName(call.substring(0, pos));
		String method = call.substring(pos + 1);
		return GuiFactory.decorate(new ExecuteButton(method, genericResolver
				.instantiateIOC(type), method, obj));
	}

	private ModelField getModelForType(Member member) {
		if (member.isAnnotationPresent(ModelType.class)) {
			ModelType an = (ModelType) member.getAnnotation(ModelType.class);
			Class<? extends ModelField> modelFieldType = an.component();
			ModelField model = genericResolver.instantiate(modelFieldType,
					member.getName());
			model.setSize(an.width(), an.height());
			return model;
		}
		Class<?> type = member.getType();
		if (type.equals(String.class)) {
			return new ModelTextField(member.getName());
		}
		if (type.equals(int.class)) {
			return new ModelIntField(member.getName());
		}
		if (type.equals(double.class)) {
			return new ModelDoubleField(member.getName());
		}
		throw new RuntimeException("No model type found");
	}

}
