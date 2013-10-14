package eu.collectionspro.mwe;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 *  This component is used to compare two models. The models differs if they don't conform to the 
 *  same metamodel or the content of models is different.
 * @author woxie
 *
 */
public class ModelComparator extends AbstractWorkflowComponent implements IModelComparator{

	/**
	 * Uri of result file
	 */
	protected String resultUri;

	/**
	 * Uri of tested file
	 */
	protected String testUri;

	/**
	 * Test description that is showed after test success/failure.
	 */
	protected String description;
	
	/**
	 * Indicates whether test fails or not
	 */
	protected boolean success;
	
	/**
	 *  Sets URI of expected result file 
	 * @param uri
	 */
	@Override
	public void setResultURI(final String uri){
		this.resultUri = uri;
	}
	
	/**
	 *  Sets URI of test file 
	 * @param uri
	 */
	@Override
	public void setTestedURI(final String uri){
		this.testUri = uri;
	}
	
	/**
	 * Sets message that is describing model(test) content.
	 * @param message
	 */
	@Override
	public void setTestDescription(final String description){
		this.description = description;
	}

	/**
	 * Checks whether the component is used in desired way - eg. if the values of the parameters 
	 * are from the allowed range (set).
	 * 
	 * @param issues - the set of errors that will be printed on standard output
	 */
	@Override
	public void checkConfiguration(Issues issues) {
		if(resultUri == null){
			issues.addError("Result uri is empty");
			if(!new File(resultUri).canRead()){
				issues.addError("File with uri: " + resultUri + " can't be read.");
			}
		}
		if(testUri == null){
			issues.addError("Result uri is empty");
			if(!new File(testUri).canRead()){
				issues.addError("File with uri: " + testUri + " can't be read.");
			}
		}
		
	}

	/**
	 * Compares 2 models in the context.
	 * @param ctxs
	 * @param monitor
	 * @param issues
	 */
	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
			// Creates the resourceSet where we'll load the models
			final ResourceSet resourceSet = new ResourceSetImpl();
			// Register additionnal packages here. For UML2 for instance :
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					UMLResource.Factory.INSTANCE);
			resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

			try {
				// Loads the two models passed as arguments
				final EObject model1 = ModelUtils.load(new File(testUri), resourceSet);
				final EObject model2 = ModelUtils.load(new File(resultUri), resourceSet);

				// Creates the match then the diff model for those two models
				final MatchModel match = MatchService.doMatch(model1, model2, Collections
						.<String, Object> emptyMap());
				// Makes diff between models
				final DiffModel diff = DiffService.doDiff(match, false);
				EList<DiffElement> diffList = diff.getDifferences();
				
				// Prints the results
				if(diff.getDifferences().size() > 0 ){
					for(DiffElement diffElement : diff.getDifferences()){
						System.out.println(diffElement);
					}
					System.out.println("ModelComparator: " + description + " fails");
					success = false;
				} else {
					System.out.println("ModelComparator: " + description + " OK");
					success = true;
				}
			} catch (IOException e) {
				// shouldn't be thrown
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	/**
	 *  Get the result test value
	 * @return boolean value
	 */
	@Override
	public boolean getSuccess() {
		return success;
	}
}
