package judge.camera;

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

  public void allign(ServoController servo)
  {

    BarTracker tracker = new BarTracker();
    int i = 1;
    boolean done = false;

    while (!done)
    {
      try
      {
        VisionTarget[] targets = tracker.getTarget();
        VisionTarget target1 = targets[i - 1];
        VisionTarget target2 = targets[i];
        
        if (target1 != null && target2 != null)
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
        }
        i ++;
      }
      catch (Exception e)
      {
      }


    }

  }
  
  private double xMidpoint(VisionTarget t1, VisionTarget t2)
  {
    double x1 = t1.getXPosition();
    double x2 = t2.getXPosition();
    double midpoint = (x1 + x2)/2;
    return midpoint;
  }
}
