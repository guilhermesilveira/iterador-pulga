package br.usp.iterador.gui.util;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressBar {

	private final JLabel expectedTime;

	private final JProgressBar percentageBar;

	private long lastSnapshot;

	private long lastVal;

	public ProgressBar(int min, int max) {
		this.percentageBar = GuiFactory.decorate(new JProgressBar(min, max));
		this.percentageBar.setStringPainted(true);
		this.expectedTime = GuiFactory.decorate(new JLabel("starting...",
				JLabel.CENTER));
	}

	public JProgressBar getPercentageBar() {
		return percentageBar;
	}

	public void setValue(int val) {
		this.percentageBar.setValue(val);
		updateExpectedTime(val);
	}

	private void updateExpectedTime(int val) {

		if (lastSnapshot == 0) {
			lastSnapshot = System.currentTimeMillis();
			lastVal = val;
			return;
		}

		long current = System.currentTimeMillis();
		double delta = (current - lastSnapshot) / 1000.0;
		double expectedSeconds = delta * (10000 - val) / (val - lastVal);
		expectedTime.setText(String
				.format("%.0f seconds left", expectedSeconds));
		lastSnapshot = current;
		lastVal = val;

	}

	public JLabel getExpectedTime() {
		return expectedTime;
	}

}
