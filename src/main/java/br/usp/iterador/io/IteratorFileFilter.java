package br.usp.iterador.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import br.usp.iterador.i18n.Messages;

public class IteratorFileFilter extends FileFilter {

	static final String EXTENSION = ".itr"; //$NON-NLS-1$

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().endsWith(EXTENSION);
	}

	@Override
	public String getDescription() {
		return Messages.getString("file.iterator.file.description"); //$NON-NLS-1$
	}

}
