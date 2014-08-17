package eu.collectionspro.mwe;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EAttribute;
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
public class SlotComparator extends BaseEObjectComparator{	
	/**
	 * Checks whether the component is used in desired way - eg. if the values of the parameters 
	 * are from the allowed range (set).
	 * 
	 * @param issues - the set of errors that will be printed on standard output
	 */
	@Override
	public void checkConfiguration(Issues issues) {
		if(resultUri == null){
			issues.addError("Result slot name is empty");
			if(!new File(resultUri).canRead()){
				issues.addError("Slot: " + resultUri + " can't be loaded.");
			}
		}
		if(testUri == null){
			issues.addError("Test slot name is empty");
			if(!new File(testUri).canRead()){
				issues.addError("Slot: " + testUri + " can't be loaded.");
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	protected EObject loadModel(WorkflowContext ctx, ProgressMonitor monitor, Issues issues, String location){
		Object slotContent = ctx.get(location);
		if(slotContent == null || !(slotContent instanceof List<?>)){
			issues.addError("Input slot '" + location
					+ "' is empty or contains iappropriate object!");
			return null;
		}
		List<EObject> list = (List<EObject>)(slotContent);
		if(list.size() != 1){
			issues.addError("Input slot '" + location
					+ "' doesn't contain one root element!");
			return null;
		}
		return list.get(0);
	}
}
