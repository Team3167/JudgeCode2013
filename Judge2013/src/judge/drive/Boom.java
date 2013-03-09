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
	private double speed = 0.5;
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
		if (limit.get() && cmd > 0.0)
			motor.set(0.0);
		else
			motor.set(cmd);
	}

	public void Extend()
	{
		cmd = speed;
	}

	public void Retract()
	{
		cmd = -speed;
	}

	public void Stop()
	{
		cmd = 0.0;
	}

	public boolean IsExtended()
	{
		return limit.get();
	}

	public void SetSpeed(double speed)
	{
		this.speed = speed;
	}
}
