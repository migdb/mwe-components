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
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.utils.StandaloneSetup;

public class WalkThruModel extends AbstractWorkflowComponent {

	protected List<String> modelSlots = new ArrayList<String>();

	static {
		new StandaloneSetup();
	}

	public void addModelSlot(String modelSlot) {
		this.modelSlots.add(modelSlot);
	}

	protected IModelWalker walker = null;

	public void setWalker(IModelWalker walker) {
		this.walker = walker;
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
		try {
			walker.start();
			for (String modelSlot : modelSlots) {

				Object slotContent = ctx.get(modelSlot);

				walker.onNewSlot(modelSlot, slotContent);

				if (slotContent == null)
					break;

				if (slotContent instanceof Iterable<?>) {
					for (Object obj : (Iterable<?>) slotContent) {
						if (obj instanceof EObject) {
							EObject eObj = (EObject) obj;
							walker.onRootElement(eObj);
						}
						if (obj instanceof Resource) {
							Resource res = (Resource) obj;
							walker.onRootResource(res);
						}
					}
				} else {
					walker.onRootElement((EObject) slotContent);
				}
			}
			walker.finish();
		} catch (Throwable t) {
			t.printStackTrace();
			issues.addError("Exception during execution");
		}
	}

	@Override
	public void checkConfiguration(Issues issues) {
		for (String modelSlot : modelSlots)
			if (modelSlot == null || modelSlot.isEmpty())
				issues.addError("modelSlot not set");
		if (modelSlots.isEmpty())
			issues.addError("no modelSlot specified");
		if (walker == null)
			issues.addError("walker not specified");
	}
}
