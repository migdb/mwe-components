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

import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

public class CommandLineExec extends AbstractWorkflowComponent {

	protected String command;

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
		JavaSystemCaller.Exec.execute(command);
	}

	@Override
	public void checkConfiguration(Issues issues) {
		if (command == null)
			issues.addError("Command not set");
	}
}
