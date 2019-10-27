package br.usp.iterador.task;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ProgressBar;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Window for long tasks.
 * 
 * @author Guilherme Silveira
 */
public class LongTaskManager {

	private final ProgressBar bar = new ProgressBar(0, 10000);

	private PulgaFrame progress;

	private Thread repaintThread;

	private final LongTask task;

	private Thread thread;

	private final SimpleIteratorFrame iteratorFrame;

	private final GuiFactory gui = new GuiFactory();

	private final WindowManager windows;

	public PulgaFrame createFrame() {
		progress = new PulgaFrame(windows, "progress", "progress") {

			private static final long serialVersionUID = -3921640894961499355L;

			@Override
			public void dispose() {
				super.dispose();
				LongTaskManager.this.dispose();
			}
		};
		progress.setSize(300, 90);
		progress.setContentPane(getJContentPane());
		progress.setTitle(this.task.getName());
		return progress;
	}

	public LongTaskManager(WindowManager windows, LongTask task,
			SimpleIteratorFrame iteratorFrame) {
		this.task = task;
		this.iteratorFrame = iteratorFrame;
		this.thread = new Thread(this.task);
		this.windows = windows;
		this.progress = windows.showFrame(task.getName(), this, "createFrame");
	}

	/**
	 * Changes the bar value and ask for repaint
	 * 
	 * @param value
	 *            new value
	 */
	private void changeValue(int value) {
		this.bar.setValue(value);
		this.progress.repaint();
		this.iteratorFrame.repaint();
	}

	/**
	 * Closes the window and stops the frame
	 * 
	 * @see java.awt.Window#dispose()
	 */
	public void dispose() {
		stop();
	}

	private JButton getCloseButton() {
		return gui.getButton("stop_and_close", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progress.dispose();
			}
		});
	}

	private JPanel getJContentPane() {
		JPanel jContentPane = new JPanel(new BorderLayout());
		jContentPane.add(bar.getExpectedTime());
		jContentPane.add(getStatusPanel(), BorderLayout.SOUTH);
		return jContentPane;
	}

	private JPanel getStatusPanel() {
		return gui.getPanel(this.bar.getPercentageBar(), getStopButton(),
				getCloseButton());
	}

	private JButton getStopButton() {
		return gui.getButton("stop", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
	}

	/**
	 * Starts the task
	 * 
	 */
	public void start() {
		this.progress.setVisible(true);
		this.thread.start();
		this.repaintThread = new Thread(new RepaintThread(this));
		this.repaintThread.start();
	}

	/**
	 * Stops the thread no matter what happens
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void stop() {
		updatePercentage();
		if (this.thread.isAlive()) {
			this.thread.stop();
		}
		if (this.repaintThread.isAlive()) {
			this.repaintThread.stop();
		}
	}

	/**
	 * Updates the current percentage
	 * 
	 * @param percentageComplete
	 */
	public void updatePercentage() {
		changeValue((int) (this.task.getPercentageComplete() * 100));
	}

	public boolean isTaskAlive() {
		return this.thread != null && this.thread.isAlive();
	}

}
