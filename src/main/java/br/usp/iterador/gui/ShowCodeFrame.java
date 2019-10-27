package br.usp.iterador.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.usp.iterador.gui.tablemodel.PieceOfCodeModel;
import br.usp.iterador.gui.util.ExecuteButton;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Codes;
import br.usp.iterador.model.PieceOfCode;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Piece of code frame.
 * 
 * @author Guilherme Silveira
 */
public class ShowCodeFrame implements PulgaAction {

	private JTable table = null;

	private PieceOfCodeModel model;

	private final GuiFactory guiFactory;

	private final Application app;

	private final WindowManager windows;

	public ShowCodeFrame(GuiFactory guiFactory, Application app,
			WindowManager windows) {
		this.guiFactory = guiFactory;
		this.app = app;
		this.windows = windows;
	}

	public void execute() {
		PulgaFrame frame = windows.showFrame("pieces_of_code",
				"pieces_of_code", getJContentPane(windows), 623, 323);
		frame.setVisible(true);
	}

	private JPanel getJContentPane(WindowManager windows) {
		JPanel jContentPane = new JPanel(new BorderLayout());
		jContentPane.add(getButtons(), BorderLayout.SOUTH);
		jContentPane.add(getMiddle(), BorderLayout.CENTER);
		return jContentPane;
	}

	private JPanel getButtons() {
		return guiFactory.getPanel(new ExecuteButton("add", this, "add"),
				new ExecuteButton("edit", this, "edit"), new ExecuteButton(
						"remove", this, "remove"), guiFactory
						.createCloseButton());
	}

	public void remove() {
		int selected = table.getSelectedRow();
		if (selected < 0) {
			return;
		}
		Codes codes = app.getCodes();
		codes.getCodes().remove(selected);
		updateData();
		if (codes.getCodes().size() != 0) {
			table.setRowSelectionInterval(0, 0);
		}
	}

	public void add() {
		Codes codes = app.getCodes();
		codes.add(new PieceOfCode());
		int last = codes.getCodes().size() - 1;
		updateData();
		table.setRowSelectionInterval(last, last);
	}

	public void edit() {
		int selected = table.getSelectedRow();
		if (selected < 0) {
			return;
		}
		Codes codes = app.getCodes();
		PieceOfCode code = codes.getCodes().get(selected);
		PieceOfCodeEditor editor = new PieceOfCodeEditor(windows, this, code,
				guiFactory, app);
		editor.setVisible(true);
		updateData();
	}

	private JScrollPane getMiddle() {
		return guiFactory.createPane(table = new JTable(
				this.model = new PieceOfCodeModel(app)));
	}

	public void updateData() {
		Codes codes = app.getCodes();
		codes.update();
		model.update();
	}
}
