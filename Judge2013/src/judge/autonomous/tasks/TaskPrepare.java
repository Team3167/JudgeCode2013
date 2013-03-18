/*******************************************************************************
* File:  TaskBalance.java
* Date:  1/11/2012
* Auth:  K. Loux
* Desc:  Task for balancing the teetering bridges.
*******************************************************************************/

// Declare our package
package judge.autonomous.tasks;

// Local imports
import judge.drive.Leadscrew;
import judge.drive.Boom;
import judge.drive.Hook;

/**
 *
 * @author kloux
 */
public class TaskPrepare extends TaskBase
{
	// Fields
	private final Leadscrew leadscrew;
	private final Boom boom;
	private final Hook leftHook, rightHook;

	/**
	 * Constructor for balancing task
	 */
	public TaskPrepare(Leadscrew leadscrew, Boom boom, Hook leftHook, Hook rightHook)
	{
		super("Preparing to climb   ");

		this.leadscrew = leadscrew;
		this.boom = boom;
		this.leftHook = leftHook;
		this.rightHook = rightHook;

		// Tell leadscrew to home
		// Tell boom to extend
		//boom.Extend();
		//leftHook.Release();
		//rightHook.Release();
	}

	protected void ProcessState()
	{
		// Nothing here
	}

	protected boolean IsComplete()
	{
		return boom.IsExtended() && leadscrew.IsHomed();// TODO:  Any sensors for hook extension?
	}

	/**
	 * Nothing required
	 */
    protected void OnRemoveTask()
	{
	}

	public String GetStateName()
	{
		return "Preparing...         ";
	}

	protected boolean OkToDrive()
	{
		return true;
	}
}
