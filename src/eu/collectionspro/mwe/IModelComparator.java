package eu.collectionspro.mwe;

import org.eclipse.emf.mwe.core.issues.Issues;

/** 
 * @author woxie
 *
 */
public interface IModelComparator {

	/**
	 *  Sets URI of expected result file 
	 * @param uri
	 */
	public abstract void setResultURI(final String uri);

	/**
	 * Sets URI of tested file
	 * @param uri
	 */
	public abstract void setTestedURI(final String uri);

	/**
	 * Sets message that is describing model(test) content.
	 * @param message
	 */
	public abstract void setTestDescription(final String description);

	/**
	 * Checks whether the component is used in desired way - eg. if the values of the parameters 
	 * are from the allowed range (set).
	 * 
	 * @param issues - the set of errors that will be printed on standard output
	 */
	public abstract void checkConfiguration(Issues issues);

	/**
	 *  Get the result test value
	 * @return boolean value
	 */
	public boolean getSuccess();
}