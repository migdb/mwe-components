package eu.collectionspro.mwe;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public interface IModelWalker {

	public void start();
	
	public void onNewSlot(String slotName, Object slotContent);
	public void onRootResource(Resource res);
	public void onRootElement(EObject element);

	public void finish();

}
