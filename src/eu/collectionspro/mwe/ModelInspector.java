package eu.collectionspro.mwe;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class ModelInspector extends BaseModelWalker {

	String reprText(EObject obj, String name) {
		if (name == null)
			name = "";
		else name = name + " ";
		return String.format("%s%s %s[0x%08x]", obj.eIsProxy() ? "proxy-to ":"",
					obj.eClass().getName(), name, obj.hashCode());
	}

	String repr(EObject obj) {
		if (obj == null)
			return "<null>";

		Method m;
		try {
			m = obj.getClass().getMethod("getQualifiedName");
			return reprText(obj, (String) m.invoke(obj));
		} catch (Exception e) {
			//do nothing, try method getName instead of getQualifiedName
		}
		try {
			m = obj.getClass().getMethod("getName");
			return reprText(obj, (String) m.invoke(obj));
		} catch (Exception e) {
			return reprText(obj, null);
		}
	}
	
	protected int indent = 0;
	
	@Override
	protected void traverseElement(EObject element) {
		for (int i = 0; i < indent; i++)
			System.out.print("	");
		System.out.println(repr(element) + " in " + repr(element.eContainer()));
		for (EObject eObj : element.eContents()) {
			indent++;
			traverseElement(eObj);
			indent--;
		}
	}

	@Override
	public void onRootElement(EObject element) {
		indent = 0;
		super.onRootElement(element);
	}

	@Override
	public void onRootResource(Resource res) {
		indent = 1;
		super.onRootResource(res);
	}
}
