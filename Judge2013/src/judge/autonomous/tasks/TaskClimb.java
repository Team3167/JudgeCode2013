/*******************************************************************************
* File:  TaskAutonomous.java
* Date:  3/1/2012
* Auth:  K. Loux
* Desc:  Task for doing *something* in autonomous mode.
*******************************************************************************/

// Declare our package
package judge.autonomous.tasks;

// Local imports
import judge.RobotConfiguration;
import judge.drive.Leadscrew;

/**
 *
 * @author kloux
 */
public class TaskClimb extends TaskBase
{
	private final Leadscrew leadscrew;

	private final double reachDistance = 3.0;// [in]
	private final double pullDistance = 40.0;// [in]

	// States
	private static final byte stateReach = 0;
	private static final byte statePulling = 1;

	private static byte level = 0;

	public TaskClimb(Leadscrew leadscrew)// TODO:  Add hooks here if beneficial
	{
		super("Auto-climbing!      ");

		this.leadscrew = leadscrew;

		SetNextState(stateReach);
	}

	protected void EnterState()
	{
		switch (GetState())
		{
		case stateReach:
			leadscrew.SetPosition(reachDistance);
			break;
		case statePulling:
			leadscrew.SetPosition(pullDistance);
			break;
		}
	}

	protected void ProcessState()
	{
		switch (GetState())
		{
		case stateReach:
			if (leadscrew.AtPosition())
			{
				SetNextState(statePulling);
				level++;
			}
			break;
		case statePulling:
			if (leadscrew.AtPosition() && level != 3)
				SetNextState(stateReach);
			break;
		}

		leadscrew.Update();
	}

	/**
	 * Determines if we've finished with the bridge
	 * @return
	 */
	protected boolean IsComplete()
	{
		if (GetState() == statePulling && level == 3 && leadscrew.AtPosition())
			return true;

		return false;
	}

	/**
	 * Nothing required for this task
	 */
    protected void OnRemoveTask()
	{
	}

	/**
	 * Gets the name of the current state, processed for LCD output
	 *
	 * @return state name
	 */
	public String GetStateName()
	{
		switch (GetState())
		{
			case stateReach:
				return "Reaching for tower   ";

			case statePulling:
				return "Pulling robot up     ";

			default:
				return "Unknown state        ";
		}
	}

	/**
	 * Checks to see if we should allow the driver to control the robot.
	 *
	 * @return
	 */
	protected boolean OkToDrive()
	{
		return true;
	}
}
