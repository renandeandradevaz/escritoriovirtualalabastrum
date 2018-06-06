package br.com.alabastrum.escritoriovirtual.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class UtilReflection {

	@SuppressWarnings("rawtypes")
	public Iterable<Class> obterClassesDoPacote(String nomePacote) throws ClassNotFoundException, IOException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = nomePacote.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<Class> classes = new ArrayList<Class>();
		for (File diretorios : dirs) {
			classes.addAll(encontrarClasses(diretorios, nomePacote));
		}

		return classes;
	}

	@SuppressWarnings("rawtypes")
	private List<Class> encontrarClasses(File diretorio, String nomePacote) throws ClassNotFoundException {

		List<Class> classes = new ArrayList<Class>();
		if (!diretorio.exists()) {
			return classes;
		}
		File[] files = diretorio.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(encontrarClasses(file, nomePacote + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(nomePacote + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public String obterPacotePrincipal() {

		return this.getClass().getName().split("\\.")[0];
	}

	public static void nullifyStrings(Object o) {

		Class<?> superClass = o.getClass().getSuperclass();

		if (superClass != null) {

			Field[] declaredFields = superClass.getDeclaredFields();

			settaNull(o, declaredFields);

		}

		settaNull(o, o.getClass().getDeclaredFields());

	}

	private static void settaNull(Object o, Field[] declaredFields) {

		for (Field f : declaredFields) {

			f.setAccessible(true);

			try {
				if (f.getType().equals(String.class)) {

					String value = (String) f.get(o);

					if (value != null && value.trim().isEmpty()) {

						f.set(o, null);
					}
				}
			} catch (Exception e) {

				throw new RuntimeException(e);
			}
		}
	}
}
