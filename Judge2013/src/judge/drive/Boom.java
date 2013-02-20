/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package judge.drive;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author kloux
 */
public class Boom
{
	private double cmd = 0;
	private double speed = 1.0;
	private final Jaguar motor;
	private final DigitalInput limit;

	public Boom(int dsSlot, int motorChannel, int limitChannel)
	{
		motor = new Jaguar(dsSlot, motorChannel);

		limit = new DigitalInput(dsSlot, limitChannel);

		cmd = 0.0;
	}

	public void Update()
	{
		motor.set(cmd);

		// TODO:  Add logic to stop extending/retracting when switch engages
		// and remove this line:
		cmd = 0.0;
	}

	public void Extend()
	{
		cmd = speed;
	}

	public void Retract()
	{
		cmd = -speed;
	}

	public boolean IsExtended()
	{
		// FIXME:  Implement
		return false;
	}
}
