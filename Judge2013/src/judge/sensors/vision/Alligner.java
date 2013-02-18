package judge.sensors.vision;

import edu.wpi.first.wpilibj.camera.AxisCamera;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Ryan Young and John Breslin
 */
public class Alligner
{
  /*
   * When completed, will move the servo to allign its self with the vision
   * targets then will move the robot so that it is alligned.
   *
   *
   */

	private double pi = Math.PI;
	private double freq = .250;
	private double omega = ((2.0 * pi)) * freq;
	private double time = 0.0;

	private Filter cmdFilter = new Filter(1.5, 1.0, 50.0);


  public void align(ServoController servo)
  {

    BarTracker tracker = new BarTracker();
    //int i = 1;
    //boolean done = false;
   // breslin is awesome
   // while (!done)
   // {/
      try
      {
        VisionTarget[] targets = tracker.getTarget();
       /* VisionTarget target1 = targets[i - 1];
        VisionTarget target2 = targets[i];*/
        double width = AxisCamera.getInstance().getResolution().width;
		double targetPosition = width / 2.0;
		double actualPosition = 0.0;// Put position measuring function here
		double kp = 1.0;
		double servoCommand;
        System.out.println(targets.length);
		if (targets == null || targets[0] == null)
		{
			// Make camera oscillate at 0.5 Hz
			servoCommand = oscillate();
			System.out.println("Found 0");
		}
		else if (targets.length == 1)
		{
			// Center the camera on the target
			System.out.println(targets[0].getRawXPosition());
			actualPosition = targets[0].getRawXPosition();
			System.out.println("Found 1");
			servoCommand = ((actualPosition - targetPosition) / width + 0.5) * kp;

		}
		else  // Found both targets
		{
			// Center the camera between the two
			actualPosition = xMidpoint(targets[0], targets[1]);
			System.out.println("Found 2");
			servoCommand = ((actualPosition - targetPosition) / width + 0.5) * kp;

		}
        System.out.println(servoCommand);
		servo.setCommand(cmdFilter.Apply(servoCommand));// RYAN make this work

        /*if (target1 != null && target2 != null)
        {
          System.out.println("Target " + (i - 1) + ": " + target1.toString());
          System.out.println("Target " + i + ": " + target2.toString());
          double posX = xMidpoint(target1, target2);
          if ((posX > -.01 && posX < .01))
          {
            System.out.println("Target is in the Center!!!");
            System.out.println("Servo moved: "+ servo.getAngleMoved());
            if(i == targets.length)
            {
              done = true;
            }
          }
          else if (posX < 0)
          {
            servo.incrementLeft();
          }
          else if (posX > 0)
          {
            servo.incrementRight();
          }

        }
        else
        {
          System.out.println("No Target Found");
        }*/
        //i ++;
      }
      catch (Exception e)
      {
		e.printStackTrace();
      }


    //}

  }

  private double xMidpoint(VisionTarget t1, VisionTarget t2)
  {
    double x1 = t1.getRawXPosition();
    double x2 = t2.getRawXPosition();
    double midpoint = (x1 + x2)/2;
    return midpoint;
  }

  private void alignOne(VisionTarget target)
  {

  }

  public double oscillate()
	{
		double x = omega * time;
		double sinOfX = Math.sin(x);
		double move = .5 * (sinOfX + 1);
		time += 0.02;
		return move;

	}
}
