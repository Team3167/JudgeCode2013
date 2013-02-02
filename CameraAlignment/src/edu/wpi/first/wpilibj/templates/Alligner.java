package edu.wpi.first.wpilibj.templates;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Ryan Young
 */
public class Alligner
{
  /*
   * When completed, will move the servo to allign its self with the vision
   * targets then will move the robot so that it is alligned.
   *
   *
   */
  private boolean done = false;

  public void allign(ServoController servo)
  {

    BarTracker tracker = new BarTracker();
    int i = 0;
    while (true)
    {
      try
      {
        VisionTarget[] targets = tracker.getTarget();
        VisionTarget target = targets[0];
        if (target != null)
        {
          System.out.println("Target " + 0 + ": " + target.toString());
          if ((target.getXPosition() > -.01 && target.getXPosition() < .01))
          {
            System.out.println("Target is in the Center!!!");
            System.out.println("Servo moved: "+ servo.getAngleMoved());
            if(i == 0)
            {
              done = true;
            }
            i ++;

          }
          else if (target.getXPosition() < 0)
          {
            servo.incrementLeft();
          }
          else if (target.getXPosition() > 0)
          {
            servo.incrementRight();
          }
        }
        else
        {
          System.out.println("No Target Found");
        }
      }
      catch (Exception e)
      {
      }


    }

  }

  public void notDone()
  {
	done = false;
  }
}
