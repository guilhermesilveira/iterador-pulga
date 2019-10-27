package br.usp.iterador.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.usp.iterador.gui.util.CloseButton;
import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ModelIntField;
import br.usp.iterador.gui.util.ModelTextArea;
import br.usp.iterador.gui.util.PulgaLabel;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Codes;
import br.usp.iterador.model.PieceOfCode;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * A piece of code editor.
 * 
 * @author Guilherme Silveira
 */
public class PieceOfCodeEditor extends PulgaFrame {

	private static final long serialVersionUID = -599691051490806053L;

	private PieceOfCode code;

	public PieceOfCodeEditor(WindowManager windows, final ShowCodeFrame parent,
			PieceOfCode c, GuiFactory guiFactory, final Application app) {
		super(windows, "pieces_of_code", "pieces_of_code");
		setSize(600, 200);
		this.code = c;
		dialogPane = new JPanel();
		contentPane = new JPanel();
		modelIntField1 = new ModelIntField("level");
		modelIntField2 = new ModelIntField("iterations");
		scrollPane1 = new JScrollPane();
		modelTextArea1 = new ModelTextArea("code");
		buttonBar = new JPanel();
		okButton = new DataModel(code);
		cancelButton = guiFactory.createCloseButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		Container contentPane2 = getContentPane();
		contentPane2.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);

			dialogPane
					.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
						public void propertyChange(
								java.beans.PropertyChangeEvent e) {
							if ("border".equals(e.getPropertyName()))
								throw new RuntimeException();
						}
					});

			dialogPane.setLayout(new BorderLayout());

			// ======== contentPane ========
			{
				contentPane.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC }, new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC }));

				// ---- pulgaLabel1 ----
				contentPane.add(new PulgaLabel("level"), cc.xy(1, 1));

				// ---- modelIntField1 ----
				contentPane.add(modelIntField1.getComponent(), cc.xy(3, 1));

				contentPane.add(new PulgaLabel("iterations"), cc.xy(1, 3));

				// ---- modelIntField2 ----
				contentPane.add(modelIntField2.getComponent(), cc.xy(3, 3));

				// ---- pulgaLabel3 ----
				contentPane.add(new PulgaLabel("value"), cc.xy(1, 5));

				// ======== scrollPane1 ========
				{
					scrollPane1.setMaximumSize(new Dimension(2147483647,
							2147483647));
					scrollPane1.setMinimumSize(new Dimension(200, 20));
					scrollPane1.setViewportView(modelTextArea1.getComponent());
				}
				contentPane.add(scrollPane1, cc.xy(3, 5));
			}
			dialogPane.add(contentPane, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC, FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC }, RowSpec
						.decodeSpecs("pref")));

				DataModel model = new DataModel(code);
				model.addModel(this.modelIntField1, this.modelIntField2,
						this.modelTextArea1);

				// ---- okButton ----
				okButton.addModel(this.modelIntField1, this.modelIntField2,
						this.modelTextArea1);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Codes codes = app.getCodes();
						codes.update();
						parent.updateData();
					}
				});

				// ---- cancelButton ----
				buttonBar.add(cancelButton, cc.xy(6, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane2.add(dialogPane, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Guilherme Silveira
	private JPanel dialogPane;

	private JPanel contentPane;

	private ModelIntField modelIntField1, modelIntField2;

	private JScrollPane scrollPane1;

	private ModelTextArea modelTextArea1;

	private JPanel buttonBar;

	private DataModel okButton;

	private CloseButton cancelButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
