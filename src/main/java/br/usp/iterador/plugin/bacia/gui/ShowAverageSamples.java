package br.usp.iterador.plugin.bacia.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.LinkedList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.SimplePanel;
import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.ExecuteButton;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ModelColorField;
import br.usp.iterador.gui.util.ModelDoubleField;
import br.usp.iterador.gui.util.ModelTextField;
import br.usp.iterador.gui.util.PulgaLabel;
import br.usp.iterador.gui.util.UpdateException;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.AveragesInfo;
import br.usp.iterador.plugin.bacia.Basin;
import br.usp.iterador.plugin.bacia.BasinController;
import br.usp.iterador.plugin.bacia.average.AverageCanvas;
import br.usp.iterador.plugin.bacia.average.AverageListModel;
import br.usp.iterador.plugin.bacia.average.AverageThread;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * The average picker form.
 * 
 * @author Guilherme Silveira
 */
public class ShowAverageSamples implements PulgaAction {

	private static final Logger logger = Logger
			.getLogger(ShowAverageSamples.class);

	private PulgaFrame frame;

	private AttractorsInformation attractors;

	private GuiFactory guiFactory = new GuiFactory();

	private JList averageList;

	private AverageThread averageThread;

	private AverageCanvas baciaAverageCanvas = null;

	private LinkedList<Double[]> averages = new LinkedList<Double[]>();

	private Application app;

	private Basin basin;

	private WindowManager windows;

	private BasinController basinController;

	public ShowAverageSamples(Basin basin, WindowManager windows,
			BasinController basinController) {
		this.basin = basin;
		this.windows = windows;
		this.basinController = basinController;
	}

	public void execute(Application app) {
		attractors = new AttractorsInformation(basin, app);
		this.app = app;
		this.frame = windows.showFrame("averagesamples", this, "createFrame");
		clearTemporaryAverages();
	}

	public class SamplesFrame extends PulgaFrame {

		private static final long serialVersionUID = 7196697310954359512L;

		SamplesFrame(String key, String title, Container pane, int width,
				int height) throws HeadlessException {
			super(windows, key, title, pane, width, height);
		}

	}

	public Object createFrame() {

		return new SamplesFrame("averagesamples", "AverageSamplesFrame.Title",
				getMainPane(), 800, 600) {
			private static final long serialVersionUID = 2093262013427934094L;

			public void repaint() {
				super.repaint();
				attractors.getList().repaint();
				attractors.getModel().updateAttractors();
				if (baciaAverageCanvas != null) {
					baciaAverageCanvas.repaint();
				}
			}

			public void dispose() {
				if (averageThread != null && averageThread.isRunning()) {
					averageThread.stop();
				}
				super.dispose();
			}
		};

	}

	/**
	 * Clear the temporary averages
	 */
	public void clearTemporaryAverages() {
		this.averages.clear();
		this.frame.repaint();
	}

	private AverageCanvas getBaciaAverageCanvas() {
		if (baciaAverageCanvas == null) {
			this.baciaAverageCanvas = new AverageCanvas(app, this.averages,
					this.basinController);
		}
		return baciaAverageCanvas;
	}

	private boolean running = false;

	private AverageListModel averageListModel;

	public void stopAndStart() {

		if (running) {
			averageThread.stop();
		} else {
			startAverageThread(app);
		}
		running = !running;

	}

	public void add() {
		basinController.getNewAttractorLogic().execute(basin,
				baciaAverageCanvas);
	}

	public void close() {
		if (averageThread != null) {
			averageThread.stop();
		}
		frame.dispose();
	}

	private JPanel getBottom() {
		JPanel jPanel = new JPanel(new GridLayout(1, 4));
		jPanel.add(new ExecuteButton("basin.clearaverages", this,
				"clearTemporaryAverages"));
		jPanel.add(new ExecuteButton("basin.stopandrestart", this,
				"stopAndStart"));
		jPanel.add(new ExecuteButton("new", this, "add"));
		jPanel.add(new ExecuteButton("CloseButton.caption", this, "close"));
		return jPanel;
	}

	public void edit() {
		new CloudEditFrame(basin.getAttractors().get(getIndex()), windows);
	}

	private int getIndex() {
		return attractors.getList().getSelectedIndex();
	}

	public void remove() {
		basin.getAttractors().remove(getIndex());
		windows.refreshWindows();
	}

	private JPanel getMainPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(getBottom(), SOUTH);
		panel.add(getLeftPane(), WEST);
		panel.add(getBaciaAverageCanvas(), CENTER);
		return panel;
	}

	private JScrollPane getJList() {
		if (this.averageList == null) {
			this.averageListModel = new AverageListModel(this.averages);
			this.averageList = new JList(averageListModel);
		}
		return new JScrollPane(this.averageList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	private JPanel getLeftPane() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getLeftPaneUp());
		panel.add(getScalePanel(), SOUTH);
		return panel;
	}

	private JPanel getLeftPaneUp() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(new SimplePanel("samples_list", BorderLayout.NORTH,
				getJList()));
		panel.add(new SimplePanel("clouds_list", BorderLayout.NORTH,
				new JScrollPane(attractors.getList(),
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(panel);
		mainPanel.add(guiFactory.getPanel(new ExecuteButton("edit", this,
				"edit"), new ExecuteButton("remove", this, "remove")),
				BorderLayout.SOUTH);
		return mainPanel;
	}

	private JPanel getScalePanel() {

		ModelDoubleField xmin = new ModelDoubleField("xScale.min"), xmax = new ModelDoubleField(
				"xScale.max"), ymin = new ModelDoubleField("yScale.min"), ymax = new ModelDoubleField(
				"yScale.max");

		JPanel panel = new JPanel(new GridLayout(5, 2));

		try {
			ModelColorField color = new ModelColorField(basin,
					"sampleAverageColor");
			ModelTextField xAverage = new ModelTextField(
					"averagesInfo.averageFunctions.x");
			ModelTextField yAverage = new ModelTextField(
					"averagesInfo.averageFunctions.y");
			new DataModel(basin, color, xAverage, yAverage);
			panel.add(new PulgaLabel("averagecolor"));
			panel.add(color.getComponent());
			panel.add(new PulgaLabel("average1"));
			panel.add(xAverage.getComponent());
			panel.add(new PulgaLabel("average2"));
			panel.add(yAverage.getComponent());
			panel.add(new PulgaLabel("x"));
			panel
					.add(new SimplePanel(xmin.getComponent(), xmax
							.getComponent()));
			panel.add(new PulgaLabel("y"));
			panel
					.add(new SimplePanel(ymin.getComponent(), ymax
							.getComponent()));
			AveragesInfo scales = basin.getAveragesInfo();
			DataModel model2 = new DataModel(scales, xmin, xmax, ymin, ymax);
			model2.addActionListener(new ScaleKeyListener(this));

		} catch (UpdateException e) {
			logger.error(e.getMessage(), e);
		}
		return panel;

	}

	private void startAverageThread(Application app) {
		this.averageThread = new AverageThread(this, app, basinController,
				basin);
		this.averageThread.init();
	}

	public void askForRepaint() {
		this.frame.repaint();
	}

	public void clearCache() {
		this.averages = new LinkedList<Double[]>();
	}

	public void addAverage(Double[] average) {
		this.averages.add(average);
		this.baciaAverageCanvas.updateAverages(basin);
		this.averageListModel.updateAverages(basin);
	}

}
