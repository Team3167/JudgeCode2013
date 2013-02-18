/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package judge.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import judge.util.JoystickButton;
import judge.RobotConfiguration;

/**
 *Used to control the robot's boom and claw for climbing. 
 * 
 * @author Ryan
 */
public class HookControl
{
  // The two motors for the climbing mechanism.
  private Jaguar hookMotor;
  private Jaguar boomMotor;
  
  // The Joystick buttons
  private JoystickButton clawUp;
  private JoystickButton clawDown;
  private JoystickButton boomOut;
  private JoystickButton boomIn;
  
  
  //Variables for the switches 
  private DigitalInput topOfHook;     
  private DigitalInput bottomOfHook;
  private DigitalInput maxBoomOut;
  private DigitalInput maxBoomIn;
  
  /*
   * Initializes the two motors. Will eventually intialize the digitalInputs as 
   * well but for now just the motors. 
   */
  public HookControl(Joystick stick)
  {
    hookMotor = new Jaguar(RobotConfiguration.digitalSideCarModule, RobotConfiguration.clawMotorChannel);
    boomMotor = new Jaguar(RobotConfiguration.digitalSideCarModule, RobotConfiguration.boomMotorChannel);
   
    clawUp = new JoystickButton(stick, 3);
    clawDown = new JoystickButton(stick, 5);
    boomOut = new JoystickButton(stick, 4);
    boomIn = new JoystickButton(stick, 6);
  }
  /*
   * Takes the input from the joystick and controls the hook and boom. 
   * Currently nothing but the driver is controlling the climb so we may 
   * in the future want to put in some code doing the climb autonomously,
   */
  public void climb()
  {
    if(clawUp.HasJustBeenPressed())
    {
      hookMotor.set(1.0);
    }
    else if(clawDown.HasJustBeenPressed())
    {
      hookMotor.set(-1.0);
    }
    else
    {
      hookMotor.set(0.0);
    }
    
    if(boomOut.HasJustBeenPressed())
    {
      boomMotor.set(1.0);
    }
    else if(boomIn.HasJustBeenPressed())
    {
      boomMotor.set(-1.0);
    }
    else
    {
      boomMotor.set(0.0);
    }
    
  }
}
