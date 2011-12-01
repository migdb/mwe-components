package eu.collectionspro.mwe;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public abstract class BaseModelWalker implements IModelWalker {

	@Override
	public void start() {
		
	}
	
	@Override
	public void finish() {
		
	}
	
	@Override
	public void onRootElement(EObject element) {
		traverseElement(element);
	}
	
	abstract protected void traverseElement(EObject element);
	
	@Override
	public void onRootResource(Resource res) {
		System.out.println("--------------------");
		System.out.println("Resource: " + res.getURI().toString());
		for (EObject eObj : res.getContents())
			traverseElement(eObj);
	}

	@Override
	public void onNewSlot(String slotName, Object slotContent) {
		System.out.println("*************************************************************");
		System.out.println("** "+this.getClass().getName()+" for slot: " + slotName);
		if (slotContent == null)
			System.out.println("slotContent: <empty>");
		else
			System.out.println("slotContent: " + slotContent.getClass().getCanonicalName()
					+ " [" + slotContent.hashCode() + "]");
	}
}
