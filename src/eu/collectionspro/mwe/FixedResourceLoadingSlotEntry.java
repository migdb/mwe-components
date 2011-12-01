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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.mwe.ResourceLoadingSlotEntry;

public class FixedResourceLoadingSlotEntry extends ResourceLoadingSlotEntry {

	private Set<Pattern> fixedUris = new HashSet<Pattern>();

	@Override
	public void addUri(String uri) {
		this.fixedUris.add(Pattern.compile(uri));
	}

	@Override
	protected boolean isMatch(Resource resource) {
		if (fixedUris.isEmpty())
			return true;
		for (Pattern uriPattern : fixedUris) {
			if (uriPattern.matcher(resource.getURI().toString()).find())
				return true;
		}
		return false;
	}

}
