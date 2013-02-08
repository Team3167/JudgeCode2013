/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 *
 * Runs a test to find a the cardboard vision target. Will be expanded to
 * involve controlling one or more servos to center the target.
 *
 * Will also be tested to become more accurate at finding the target in bright
 * light. currently performs fantastic in the dark with just the reflected
 * target for obvious reasons.
 *
 * @author Ryan Young
 */
public class RobotTemplate extends IterativeRobot
{

  private Alligner alligner;
  private ServoController cameraServo;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  public void robotInit()
  {
	  AxisCamera.getInstance().writeResolution(AxisCamera.ResolutionT.k160x120);
    alligner = new Alligner();
    cameraServo = new ServoController(1);     //making up the channel number.
  }

  /**
   * This function is called periodically during autonomous
   */
  public void autonomousPeriodic()
  {
  }

  /**
   * This function is called periodically during operator control
   */
  public void teleopPeriodic()
  {
    alligner.align(cameraServo);

  }
}
