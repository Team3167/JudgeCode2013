/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package judge.drive;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;

import judge.RobotConfiguration;
import judge.util.Button;
import judge.util.BangBangController;

/**
 * Leadscrew class.
 * Represents the robot's leadscrew system, including the drive motors, homing
 * switch and encoder.
 *
 * @author KLoux
 */
public class Leadscrew
{
	private Jaguar motor1;
	private Jaguar motor2;

	private Encoder encoder;

	private Button homeSwitch;

	private static final byte driveOffOfSwitch = 0;
	private static final byte driveOnToSwitch = 1;
	private static final byte homedToSwitch = 2;
	private static final byte ready = 3;
	private byte state, nextState;

	private double command;
	private double tolerance = 0.1;// [in]
	private double reachPosition = 0.0;// [in] w.r.t. "home" position (+ve is hook up)
	private double pullPosition = -40.0;// [in] w.r.t. "home" position (+ve is hook up)

	private BangBangController controller;

	/**
	 * Constructor for leadscrew class.
	 *
	 * @param dsModule			digital sidecar module number
	 * @param motor1Chan		first motor PWM channel
	 * @param motor2Chan		second motor PWM channel
	 * @param encAChan			encoder 'A' channel
	 * @param encBChan			encoder 'B' channel
	 * @param homeSwitchChan	homing switch input channel
	 */
	public Leadscrew(int dsModule, int motor1Chan, int motor2Chan, int encAChan,
			int encBChan, int homeSwitchChan)
	{
		motor1 = new Jaguar(dsModule, motor1Chan);
		motor2 = new Jaguar(dsModule, motor2Chan);

		final int switchDebounceCycles = 3;
		homeSwitch = new Button(dsModule, homeSwitchChan, switchDebounceCycles);

		encoder = new Encoder(dsModule, encAChan, dsModule, encBChan);
		encoder.setDistancePerPulse(RobotConfiguration.leadscrewRatio);
		encoder.setReverseDirection(false);

		controller = new BangBangController(motor1, tolerance, false);

		Reset();
	}

	/**
	 * Checks to see if the homing procedure has successfully been executed.
	 *
	 * @return true if the homing is complete
	 */
	public boolean IsHomed()
	{
		return state == ready;
	}

	/**
	 * Command the hook to the top position.
	 */
	public void GoToReachPosition()
	{
		SetPosition(reachPosition);
	}

	/**
	 * Command the hook to the bottom position.
	 */
	public void GoToPullPosition()
	{
		SetPosition(pullPosition);
	}

	/**
	 * Sets the command equal to the current position.
	 */
	public void Stop()
	{
		SetPosition(GetPosition());
	}

	/**
	 * Sets the position command to the specified (arbitrary) value.
	 *
	 * @param position desired position in inches
	 */
	public void SetPosition(double position)
	{
		command = position;
	}

	/**
	 * Checks to see if current position is close to commanded position.
	 *
	 * @return true if position is within tolerance
	 */
	public boolean AtPosition()
	{
		return Math.abs(GetPosition() - command) < tolerance;
	}

	/**
	 * State manager and command processor.
	 * To be called from robot's periodic method.
	 */
	public void Update()
	{
		state = nextState;
		homeSwitch.Update();

		switch (state)
		{
		case driveOffOfSwitch:
			SetMotors(-1.0);

			if (!homeSwitch.Get())
				nextState = driveOnToSwitch;
			break;

		case driveOnToSwitch:
			SetMotors(1.0);

			if (homeSwitch.Get())
				nextState = homedToSwitch;
			break;

		case homedToSwitch:
			SetMotors(0.0);
			encoder.reset();
			encoder.start();
			Stop();
			nextState = ready;
			break;

		case ready:
			controller.DoControl(command, GetPosition());
			motor2.set(motor1.get());// Make motor2 always match motor1 command
			break;

		default:
		}
	}

	/**
	 * Resets homing (requires homing procedure to be executed again).
	 */
	public final void Reset()
	{
		if (homeSwitch.Get())
		{
			nextState = driveOffOfSwitch;
		}
		else
		{
			nextState = driveOnToSwitch;
		}
	}

	/**
	 * Directly specify motor speed.
	 *
	 * @param cmd	speed command to issue to the jaguars.
	 */
	private void SetMotors(double cmd)
	{
		motor1.set(-cmd);
		motor2.set(-cmd);
	}

	/**
	 * Gets the leadscrew position.
	 * Only returns valid data if IsHomed() returns true.
	 *
	 * @return current leadscrew position in inches as reported by the encoder
	 */
	public double GetPosition()
	{
		return encoder.getDistance();
	}
}
