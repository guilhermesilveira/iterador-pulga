package br.usp.iterador.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import br.usp.iterador.Pulga;
import br.usp.iterador.gui.menu.MenuLoader;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.logic.Component;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.PluginMenuFrame;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;
import br.usp.iterador.plugin.tool.ToolManager;

/**
 * Main frame.
 *
 * @author Guilherme Silveira
 */
@Component(autoCreate = true)
public class SimpleIteratorFrame extends PulgaFrame implements PluginMenuFrame,
		IteratorFrame {

	private static final long serialVersionUID = -3893695185401970378L;

	private static final Logger LOG = Logger
			.getLogger(SimpleIteratorFrame.class);

	private final JMenuBar menuBar;

	private final SimpleIteratorCanvas canvas;

	private JToolBar toolBar;

	private final ToolBarBuilder toolBarBuilder;

	public SimpleIteratorFrame(WindowManager windows, final Pulga pulga,
			ToolManager toolManager, PluginManager pluginManager,
			ToolBarBuilder toolBarBuilder, MenuLoader menuLoader) {

		super(windows, "iterator", "Iterador.title");
		canvas = new SimpleIteratorCanvas(new IteratorCanvas(pulga
				.getApplication().getImage()), pulga.getApplication(),
				toolManager);

		this.toolBarBuilder = toolBarBuilder;

		LOG.debug("Creating the main menu bar");
		this.menuBar = menuLoader.load();
		this.toolBar = toolBarBuilder.buildToolBar();
		this.getContentPane().add(new JScrollPane(canvas));
		this.setJMenuBar(menuBar);

		LOG.debug("Initializing the IteratorFrame");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setName("janela");
		this.setSize(800, 600);
		this.add(this.toolBar, BorderLayout.PAGE_START);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				pulga.getController().executeAction("exit");
			}
		});
		LOG.debug("Initialized IteratorFrame");
		setVisible(true);
		setSize(640, 480);
		setVisible(true);

	}

	public void updateMenu() {
		this.setJMenuBar(menuBar);
		this.repaint();
	}

	public int getMenuHeight() {
		return this.menuBar.getHeight();
	}

	public void updateDrawing(PluginManager manager)
			throws CompileTimeException {
		this.canvas.update(manager);
	}

	public void clear() {
		this.canvas.clear();
	}

	public SimpleIteratorCanvas getCanvas() {
		return this.canvas;
	}

	public void reorganize() {
		this.remove(this.toolBar);
		this.toolBar = toolBarBuilder.buildToolBar();
		this.add(this.toolBar, BorderLayout.PAGE_START);
		this.setContentPane(this.getContentPane());
	}

}