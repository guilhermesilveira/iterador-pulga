package br.usp.iterador.io;

import java.io.File;
import java.io.IOException;

import br.usp.iterador.model.Application;

public interface CompatibleVersionMaker {

	public void execute(Application data);

	public boolean shouldUpdateFile();

	public void updateFile(File file) throws IOException;

}
