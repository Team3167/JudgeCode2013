package edu.wpi.first.wpilibj.templates;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ryan Young
 */
public class Alligner
{


	public static void Allign(boolean flag)
	{

	  BarTracker tracker = new BarTracker();

	  while(flag)
	  {
		try
		{
	      VisionTarget[] targets = tracker.getTarget();
		  for(int i = 0; i < targets.length; i ++)
		  {
			VisionTarget target = targets[i];
		    if(target != null)
		    {
			  System.out.println("Target " + i + ": "+ target.toString());
			  if((target.getXPosition() > -.01 && target.getXPosition() < .01))// && (target.getYPosition() > -.01 && target.getYPosition() < .01))
			  {
			    System.out.println("Target is in the Center!!!");
			    flag = false;
			  }
		    }
		    else
		    {
			  System.out.println("No Target Found");
		    }
		  }
		}
		catch(Exception e)
		{

		}


	  }

    }
}
