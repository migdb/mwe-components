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
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

public class ModelIndexer extends BaseModelWalker {

	protected int count = 0;

	protected IdentityHashMap<EClass, Integer> perMetaClass;

	@Override
	public void start() {
		count = 0;
		perMetaClass = new IdentityHashMap<EClass, Integer>();
	};

	@Override
	public void finish() {
		System.out.println("Total objects: " + count);
		List<Map.Entry<EClass, Integer>> list = new ArrayList<Map.Entry<EClass, Integer>>(
				perMetaClass.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<EClass, Integer>>() {
			@Override
			public int compare(Entry<EClass, Integer> arg0, Entry<EClass, Integer> arg1) {
				return arg0.getKey().getName().compareTo(arg1.getKey().getName());
			}
		});
		for (Map.Entry<EClass, Integer> item : list) {
			System.out.printf("	%s: %d\n", item.getKey().getName(), item.getValue());
		}
	};

	@Override
	protected void traverseElement(EObject element) {
		index(element);
		for (EObject eObj : element.eContents())
			traverseElement(eObj);
	}

	void index(EObject element) {
		count++;
		Integer count = perMetaClass.get(element.eClass());
		perMetaClass.put(element.eClass(), count == null ? 1 : ++count);
	}
}
