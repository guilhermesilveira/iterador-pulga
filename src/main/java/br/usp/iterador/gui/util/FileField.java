package br.usp.iterador.gui.util;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import br.usp.iterador.util.ReflectionUtil;

public class FileField extends JPanel {

	private static final long serialVersionUID = 3690481324586579764L;

	private Object data;

	private JFileChooser chooser;

	private JLabel label;

	public FileField(File defaultFile, Object dataObject, final String field,
			final String suffix) {

		super(new GridLayout(1, 2));

		this.data = dataObject;
		add(this.label = new JLabel(defaultFile.getAbsolutePath()));
		this.chooser = new JFileChooser(defaultFile);
		this.chooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(suffix);
			}

			@Override
			public String getDescription() {
				return suffix;
			}
		});

		add(new ClassicButton("...", new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (chooser.showSaveDialog(FileField.this) == JFileChooser.APPROVE_OPTION) {
					String file = chooser.getSelectedFile().getAbsolutePath();
					if (!file.endsWith(suffix)) {
						file += suffix;
					}
					ReflectionUtil.set(data, field, File.class, new File(file));
					label.setText(file);
				}
			}

		}));

	}
}
