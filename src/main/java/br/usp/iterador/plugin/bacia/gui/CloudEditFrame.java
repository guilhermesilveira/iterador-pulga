package br.usp.iterador.plugin.bacia.gui;

import br.usp.iterador.gui.util.ModelCheckbox;
import br.usp.iterador.gui.util.ModelColorField;
import br.usp.iterador.gui.util.ModelTextField;
import br.usp.iterador.plugin.bacia.model.Cloud;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Edits a single cloud.
 * 
 * @author Guilherme Silveira
 */
public class CloudEditFrame {

	public CloudEditFrame(Cloud at, WindowManager windows) {

		EditionFrameBuilder builder = new EditionFrameBuilder(windows,"cloud_editor",
				"cloud_editor", at, 300, 150, 3);

		builder.register("name", new ModelTextField("name"));
		builder.register("color", new ModelColorField("color"));
		builder.register("reverse", new ModelCheckbox("reverse"));

		builder.init();

	}

}
