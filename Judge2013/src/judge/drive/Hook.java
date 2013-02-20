/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package judge.drive;

import edu.wpi.first.wpilibj.Servo;

/**
 *
 * @author kloux
 */
public class Hook
{
	private final Servo servo;

	private double holdCmd = 0.0;
	private double releaseCmd = 1.0;

	private final boolean left;
	private boolean release = false;

	public Hook(int dsSlot, int servoChannel, boolean left)
	{
		servo = new Servo(dsSlot, servoChannel);
		this.left = left;
	}

	public void Update()
	{
		if ((release && !left) || (!release && left))
		{
			servo.set(releaseCmd);
		}
		else
		{
			servo.set(holdCmd);
		}
	}

	public void Release()
	{
		release = true;
	}
}
