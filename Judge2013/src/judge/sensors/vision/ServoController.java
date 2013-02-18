/*
 * Make code to move a servo and keep track of how much it moved.
 */
package judge.sensors.vision;

import edu.wpi.first.wpilibj.Servo;

/**
 *
 * @author Ryan Young
 */
public class ServoController
{

	private Servo myServo;
	private double totalAngleMoved;
	private double currentAngle;

	public ServoController(int channel)
	{
		myServo = new Servo(channel);
		setCurrentAngle();
		totalAngleMoved = 0;
	}

	public ServoController()
	{
	}

	public void setServoToBeControlled(Servo servo)
	{
		myServo = servo;
	}

	public boolean wasSuccessful()
	{
		return (myServo.getAngle() == currentAngle);
	}

	public void incrementRight()
	{
		if (myServo.getAngle() < 1)
		{
			myServo.set(currentAngle + .001);
			totalAngleMoved += .001;
			setCurrentAngle();
		} else
		{
			System.out.println("Can't move any more Right");
		}
	}

	public void incrementLeft()
	{
		if (myServo.getAngle() > 0)
		{
			myServo.set(currentAngle - .001);
			totalAngleMoved -= .001;
		}
		setCurrentAngle();
	}

	public void setCommand(double command)
	{
		myServo.set(command);
	}

	private void setCurrentAngle()
	{
		currentAngle = myServo.getAngle();
	}

	public double getAngleMoved()
	{
		return totalAngleMoved;
	}
}
