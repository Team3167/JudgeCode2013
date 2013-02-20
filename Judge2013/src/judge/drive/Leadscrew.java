/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package judge.drive;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;

import judge.RobotConfiguration;

/**
 *
 * @author User
 */
public class Leadscrew
{
	private Jaguar motor1;
	private Jaguar motor2;

	private Encoder encoder;

	private DigitalInput homeSwitch;

	private static final byte driveOffOfSwitch = 0;
	private static final byte driveOnToSwitch = 1;
	private static final byte homedToSwitch = 2;
	private static final byte ready = 3;
	private byte state, nextState;

	private double command;

	public Leadscrew(int dsSlot, int motor1Chan, int motor2Chan, int encAChan,
			int encBChan, int homeSwitchChan)
	{
		motor1 = new Jaguar(dsSlot, motor1Chan);
		motor2 = new Jaguar(dsSlot, motor2Chan);

		homeSwitch = new DigitalInput(dsSlot, homeSwitchChan);

		encoder = new Encoder(dsSlot, encAChan, dsSlot, encBChan);
		encoder.setDistancePerPulse(RobotConfiguration.leadscrewRatio);
		encoder.setReverseDirection(false);

		Reset();
	}

	public boolean IsHomed()
	{
		return state == ready;
	}

	// FIXME:  Temporary method - should be replaced by GoToPosition()
	public void GoUp()
	{
		command = 1.0;
	}

	// FIXME:  Temporary method - should be replaced by GoToPosition()
	public void GoDown()
	{
		command = -1.0;
	}

	// FIXME:  Temporary method - should be replaced by GoToPosition()
	public void Park()
	{
		command = 0.0;
	}

	public void SetPosition(double position)
	{
		// FIXME:  Implement
	}

	public boolean AtPosition()
	{
		// FIXME:  Implement
		return false;
	}

	public void Update()
	{
		state = nextState;

		switch (state)
		{
		case driveOffOfSwitch:
			SetMotors(-1.0);

			if (!homeSwitch.get())
				nextState = driveOnToSwitch;
			break;

		case driveOnToSwitch:
			SetMotors(1.0);

			if (homeSwitch.get())
				nextState = homedToSwitch;
			break;

		case homedToSwitch:
			SetMotors(0.0);
			encoder.reset();
			encoder.start();
			nextState = ready;
			break;

		case ready:
			SetMotors(command);
			break;

		default:

		}
	}

	public final void Reset()
	{
		if (homeSwitch.get())
		{
			nextState = driveOffOfSwitch;
		}
		else
		{
			nextState = driveOnToSwitch;
		}
	}

	private void SetMotors(double cmd)
	{
		motor1.set(-cmd);
		motor2.set(-cmd);
	}

	public double GetPosition()
	{
		if (encoder.getStopped())
		{
			return 0.0;
		}

		return encoder.getDistance();
	}
}
