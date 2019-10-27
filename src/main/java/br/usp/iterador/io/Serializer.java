package br.usp.iterador.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.usp.iterador.Pulga;
import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.logic.DefaultController;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Information;
import br.usp.iterador.model.Intermediate;
import br.usp.iterador.model.Parameter;
import br.usp.iterador.model.Scale;
import br.usp.iterador.plugin.DefaultPluginManager;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.PluginActivationException;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.PluginSerializer;
import br.usp.iterador.util.Pair;
import br.usp.iterador.util.ReflectionException;
import br.usp.iterador.util.ReflectionUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Responsible for serializing the data.
 *
 * @author Guilherme Silveira
 */
public class Serializer {

	private static final String ROOT_NAME = "pulga-file";

	private static final String COMPATIBILITY_VERSION = "5";

	private static Logger LOG = Logger.getLogger(Serializer.class);

	private final HashMap<String, CompatibleVersionMaker> COMPATIBLE_VERSIONS = new HashMap<String, CompatibleVersionMaker>();

	private final PluginSerializer oldPluginSerializer;

	private final Pulga oldpulga;

	public Serializer(PluginSerializer serializer, Pulga pulga) {
		this.oldPluginSerializer = serializer;
		this.oldpulga = pulga;
		try {
			InputStream is = null;
			Scanner sc = new Scanner(is = Serializer.class
					.getResourceAsStream("/info/compatible_versions.txt"));
			sc.useDelimiter("\\n");
			while (sc.hasNext()) {
				String[] data = sc.next().split("\\s+");
				String version = data[1];
				CompatibleVersionMaker cvm = (CompatibleVersionMaker) ReflectionUtil
						.newInstance(data[2]);
				COMPATIBLE_VERSIONS.put(version, cvm);
			}
			sc.close();
			is.close();
		} catch (IOException e) {
			LOG.error("Unable to deal with compatible versions file", e);
		} catch (ReflectionException e) {
			LOG.error("Unable to deal with compatible versions file", e);
		}
	}

	public void save(File file) throws PulgaIOException {

		try {

			LOG.debug("saving to " + file.getAbsolutePath());
			XStream stream = new XStream(new DomDriver());
			FileWriter fw;
			BufferedWriter bw;
			ObjectOutputStream oos = stream.createObjectOutputStream(
					bw = new BufferedWriter(fw = new FileWriter(file)),
					ROOT_NAME);

			addBaseAliases(stream);

			oos.writeUTF("Compatibility Version");
			oos.writeUTF(COMPATIBILITY_VERSION);
			oos.writeObject(oldpulga.getApplication());

			// writes all plugin data
			oos.writeObject(oldPluginSerializer.getSerializedData());

			oos.close();
			bw.close();
			fw.close();

		} catch (Exception e) {
			throw new PulgaIOException("Unable to save", e);
		}

	}

	private static void addBaseAliases(XStream stream) {
		stream.alias("application", Application.class);
		stream.alias("intermediate", Intermediate.class);
		stream.alias("parameter", Parameter.class);
		stream.alias("scale", Scale.class);
		stream.alias("pair", Pair.class);
		stream.alias("information", Information.class);
	}

	/**
	 * @param file
	 * @return
	 * @throws PulgaIOException
	 *             some error has ocurred
	 */
	@SuppressWarnings("unchecked")
	public Pulga load(File file) throws PulgaIOException {

		try {

			XStream stream = new XStream(new DomDriver());
			FileReader fr;
			BufferedReader br;
			ObjectInputStream ois = stream
					.createObjectInputStream(br = new BufferedReader(
							fr = new FileReader(file)));

			addBaseAliases(stream);

			ois.readUTF();
			String version = ois.readUTF();
			if (!COMPATIBLE_VERSIONS.containsKey(version)) {
				throw new RuntimeException("Incompatible file version");
			}

			CompatibleVersionMaker compatibleVersion = COMPATIBLE_VERSIONS
					.get(version);

			if (compatibleVersion.shouldUpdateFile()) {

				ois.close();
				compatibleVersion.updateFile(file);
				stream = new XStream(new DomDriver());
				ois = stream.createObjectInputStream(br = new BufferedReader(
						fr = new FileReader(file)));

				addBaseAliases(stream);

				ois.readUTF();
				version = ois.readUTF();
				if (!COMPATIBLE_VERSIONS.containsKey(version)) {
					throw new RuntimeException("Incompatible file version");
				}
				compatibleVersion = COMPATIBLE_VERSIONS.get(version);
			}

			Pulga newPulga = executeLoad(stream, ois);

			ois.close();
			br.close();
			fr.close();

			COMPATIBLE_VERSIONS.get(version).execute(newPulga.getApplication());

			return newPulga;

		} catch (Exception e) {
			throw new PulgaIOException("Unable to load", e);
		}

	}

	@SuppressWarnings("unchecked")
	private Pulga executeLoad(XStream stream, ObjectInputStream ois)
			throws IOException, ClassNotFoundException,
			PluginActivationException {

		// set application data
		Application newApp = (Application) ois.readObject();
		Pulga newPulga = new Pulga(new DefaultController(), newApp);
		PluginManager newManager = newPulga.getController().find(
				DefaultPluginManager.class);

		newManager.deactivateAll();

		// fetchs each activated plugin
		List<Pair<Class<? extends Plugin>, List<Object>>> fullData = (List<Pair<Class<? extends Plugin>, List<Object>>>) ois
				.readObject();
		for (Pair<Class<? extends Plugin>, List<Object>> pair : fullData) {
			Class<? extends Plugin> clazz = pair.getFirst();
			Object data = pair.getSecond();
			newManager.activatePlugin(clazz, data);
		}
		newPulga.getController().find(IteratorFrame.class).reorganize();

		// resizes the window
		newPulga.getController().executeAction("basic/updateSize");

		return newPulga;

	}

}
