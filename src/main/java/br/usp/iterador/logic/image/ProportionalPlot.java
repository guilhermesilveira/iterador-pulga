package br.usp.iterador.logic.image;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.model.Scale;

/**
 * Sets the proportional plot.
 * 
 * @author Guilherme Silveira
 */
public class ProportionalPlot implements PulgaAction {

	private Application data;

	public ProportionalPlot(Application data) {
		this.data = data;
	}

	/**
	 * Makes the plot size proportional to the y and x scale by fixing the image
	 * width
	 */
	public void execute(Controller controller) {
		update();
		controller.executeAction("basic/updateSize");
	}

	void update() {
		ImageInfo info = data.getImage();
		Scale x = data.getXScale(), y = data.getYScale();
		info.setHeight((int) (info.getWidth() * (y.getMax() - y.getMin()) / (x
				.getMax() - x.getMin())));
	}

}
