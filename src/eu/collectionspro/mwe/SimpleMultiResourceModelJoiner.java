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

import java.util.ArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

public class SimpleMultiResourceModelJoiner extends ResourceSlotProcessor {

	protected boolean found = false;
	protected ArrayList<EObject> modelElements = new ArrayList<EObject>();

	@Override
	protected void onStart(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
		found = false;
	}

	@Override
	protected void onResource(Resource resource, WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		modelElements.addAll(resource.getContents());
		found = true;
	}

	@Override
	protected void onFinish(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
		if (!found) {
			issues.addError("No resource found in the slot");
			return;
		}
		ctx.set(modelSlot, modelElements);
	}
}
