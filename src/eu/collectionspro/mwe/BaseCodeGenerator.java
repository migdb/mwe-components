/**
 * Eclipse Public License - v 1.0
 * 
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THE
 * ECLIPSE PUBLIC LICENSE ("AGREEMENT"). ANY USE, REPRODUCTION
 * OR DISTRIBUTION OF THE PROGRAM CONSTITUTES RECIPIENT’S ACCEPTANCE
 * OF THIS AGREEMENT.
 * 
 * Full License text is provided in file LICENSE or can be found
 * here: http://www.eclipse.org/org/documents/epl-v10.html
 */
package eu.collectionspro.mwe;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/*
 * Default implementation of code generator. Provides common API for specific subclasses.
 */
public abstract class BaseCodeGenerator implements ICodeGenerator {

	@Override
	public void generateCode(EObject model, File rootFolder, Map<String, Object> arguments) {
		this.rootFolder = rootFolder;
		this.arguments = arguments != null ? arguments : new HashMap<String, Object>();

		// Delegates to particular subclass (thru abstract operation)
		doGenerate(model);
	}

	public Object getArgument(String key) {
		return arguments.get(key);
	}

	public File generateFile(String fileName, CharSequence content) {
		try {
			File file = new File(rootFolder, fileName);
			FileOutputStream stream = new FileOutputStream(file);

			System.out.println("generating file '" + file.getAbsolutePath() + "'");

			stream.write(content.toString().getBytes(getFileEncoding()));
			return file;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Can be overridden by subclass to change encoding
	protected String getFileEncoding() {
		return "UTF-8";
	}

	protected File rootFolder;

	protected Map<String, Object> arguments;

	abstract public void doGenerate(EObject model);
}