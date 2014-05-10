package eu.collectionspro.mwe;

import java.util.Collections;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

public abstract class BaseEObjectComparator extends AbstractWorkflowComponent implements IBaseEObjectComparator{
	/**
	 * Test description that is showed after test success/failure.
	 */
	protected String description;
	
	/**
	 * Indicates whether test fails or not
	 */
	protected boolean success;
	
	/**
	 * name of expected result slot
	 */
	protected String resultUri;

	/**
	 * name of tested slot
	 */
	protected String testUri;

	@Override
	public void setResultURI(String uri) {
		this.resultUri = uri;
	}

	@Override
	public void setTestedURI(String uri) {
		this.testUri = uri;
	}

	/**
	 * Sets message that is describing model(test) content.
	 * @param message
	 */
	public void setTestDescription(final String description){
		this.description = description;
	}

	/**
	 *  Get the result test value
	 * @return boolean value
	 */
	public boolean getSuccess(){
		return success;
	}
	
	protected abstract EObject loadModel(WorkflowContext ctx, ProgressMonitor monitor, Issues issues, String location);
	/**
	 * Compares 2 models in the context.
	 * @param ctxs
	 * @param monitor
	 * @param issues
	 */
	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
			try {
				// Loads the two models passed as arguments
				final EObject model1 = loadModel(ctx, monitor, issues, this.testUri);
				final EObject model2 = loadModel(ctx, monitor, issues, this.resultUri);

				// Creates the match then the diff model for those two models
				final MatchModel match = MatchService.doMatch(model1, model2, Collections
						.<String, Object> emptyMap());
				// Makes diff between models
				final DiffModel diff = DiffService.doDiff(match, false);
				EList<DiffElement> diffList = diff.getDifferences();
				
				// Prints the results
				if(diffList.size() > 0 ){
					for(DiffElement diffElement : diffList){
						if(diffElement instanceof AttributeChange){
							AttributeChange attChange = (AttributeChange) diffElement;
							EAttribute changedAtribute = attChange.getAttribute();
							System.out.println("Atribute " + changedAtribute.getName() + 
									" in " + attChange.getLeftElement().eClass().getName() + 
									" has changed from " + 
									 attChange.getLeftElement().eGet(changedAtribute) +
									" to " + attChange.getRightElement().eGet(changedAtribute));
						}else{
							System.out.println(diffElement);
						}
					}
					System.out.println("ModelComparator: " + description + " fails");
					success = false;
				} else {
					System.out.println("ModelComparator: " + description + " OK");
					success = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

}
