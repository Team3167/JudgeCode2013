/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
// This package
package judge;

// WPI imports
import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Jaguar;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;

// Standard Java imports
import java.util.Timer;
import java.util.TimerTask;

// Judge imports
import judge.drive.*;
import judge.autonomous.tasks.*;
import judge.util.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation.
 *
 * The package for this class was changed by modifying the manifest file in the
 * resource directory.
 *
 * @author K. Loux
 */
public class Team3167Robot extends IterativeRobot
{
  // Fields

  private TaskManager taskManager;
  // HID objects
  private Joystick stick;
  private Jaguar driveMotor1;
  private Jaguar driveMotor2;
  private RobotDrive drive;
  private JoystickButton button3;
  private JoystickButton button4;
  private JoystickButton button5;
  private JoystickButton button6;
  private Leadscrew leadscrew;
  private Boom boom;
  private Hook leftHook;
  private Hook rightHook;
  //private AxisCamera camera = AxisCamera.getInstance();
  // For displaying information on the driver's station message window
  private final DriverStationLCD msg = DriverStationLCD.getInstance();
  private Timer timer;
  private float matchTime;
  private DSPlotDataBuffer dsBuffer;

  // Robot initialization method =============================================
  /**
   * Robot initialization method, called once when the cRIO is first powered up.
   */
  public void robotInit()
  {
    driveMotor1 = new Jaguar(RobotConfiguration.digitalSideCarModule,
                             RobotConfiguration.leftMotorChannel);
    driveMotor2 = new Jaguar(RobotConfiguration.digitalSideCarModule,
                             RobotConfiguration.rightMotorChannel);
    stick = new Joystick(1);
    drive = new RobotDrive(driveMotor1, driveMotor2);

    button3 = new JoystickButton(stick, 3);
    button4 = new JoystickButton(stick, 4);
    button5 = new JoystickButton(stick, 5);
    button6 = new JoystickButton(stick, 6);

    leadscrew = new Leadscrew(RobotConfiguration.digitalSideCarModule,
                              RobotConfiguration.leadscrewMotor1Channel,
                              RobotConfiguration.leadscrewMotor2Channel,
                              RobotConfiguration.leadscrewEncoderAChannel,
                              RobotConfiguration.leadscrewEncoderBChannel,
                              RobotConfiguration.leadscrewHomeSwitchChannel);

    boom = new Boom(RobotConfiguration.digitalSideCarModule,
                    RobotConfiguration.boomMotorChannel, 6);// FIXME: put real channel in
    leftHook = new Hook(RobotConfiguration.digitalSideCarModule,
                        RobotConfiguration.leftHookChannel, true);
    rightHook = new Hook(RobotConfiguration.digitalSideCarModule,
                         RobotConfiguration.rightHookChannel, false);

    // Create the joystick
    stick = new Joystick(0);

    // Create the buffer for sending data to the dashboard
    dsBuffer = new DSPlotDataBuffer();


    // Create the task manager object
    taskManager = new TaskManager();

    // Create a thread for displaying the LCD data to keep it from slowing
    // down the critical operation of the robot
    timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        UpdateLCD();
      }
    }, 0, 500);// 0 - no delay (start right now), 500 - period (in msec)

    // Print to the console to tell us that we're ready to rock and roll
    // Useful when debugging and we don't want to wait longer than necessary
    // to make sure the robot is ready
    System.out.println("Robot is ready to rock and roll!");
  }

  // Autonomous mode methods =================================================
  /**
   * Initialization method for autonomous mode. Called once when mode is first
   * set to autonomous.
   */
  public void autonomousInit()
  {
    // Clear out any tasks from a previous practice/test
    taskManager.ClearAllTasks();
    taskManager.AddTask(new TaskPrepare(leadscrew, boom, leftHook, rightHook));
    taskManager.AddTask(new TaskClimb(leadscrew));
  }

  /**
   * Method called as fast as possible while in autonomous mode. Empty method.
   */
  public void autonomousContinuous()
  {
    // Nothing here
  }

  /**
   * Method called at 50 Hz while in autonomous mode. Performs tasks, updates
   * the elevator and prints messages to the dashboard.
   */
  public void autonomousPeriodic()
  {
    taskManager.DoCurrentTask();
  }

  // Teleoperated mode methods ===============================================
  /**
   * Initialization method for teleoperated mode. Called once when mode is first
   * set to teleoperated.
   */
  public void teleopInit()
  {
  }

  /**
   * Method called as fast as possible while in teleoperated mode. Handles
   * button presses only.
   */
  public void teleopContinuous()
  {
  }

  /**
   * Method called at 50 Hz while in teleoperated mode. Drives robot according
   * to joystick inputs, performs tasks, and prints messages to the dashboard.
   */
  public void teleopPeriodic()
  {
    // Move the 'bot according to the joystick position, but only if we have
    // no tasks to do
    if (taskManager.OkToDrive())
    {
      drive.arcadeDrive(stick); //Didn't we want to step down the drive so that we
                                // don't wheelie. 
    }

    if (leadscrew.IsHomed())
    {
      if (button3.IsPressed())
      {
        leadscrew.GoUp();
      }
      else if (button5.IsPressed())
      {
        leadscrew.GoDown();
      }
      else
      {
        leadscrew.Park();
      }
    }
    leadscrew.Update();
    System.out.println("Position: " + leadscrew.GetPosition());

    if (button4.IsPressed())
    {
      boom.Extend();
    }
    else if (button6.IsPressed())
    {
      boom.Retract();
    }
  }

  // Disabled mode methods ===================================================
  /**
   * Initialization method for disabled mode. Called once every time the mode
   * changes from running (either autonomous or teleoperated) to disabled.
   */
  public void disabledInit()
  {
    // We should probably clear any left-over tasks, so we don't have
    // competing logic inputs
    taskManager.ClearAllTasks();
    leadscrew.Reset();
  }

  /**
   * Method called as fast as possible while the robot is disabled. Handles
   * button presses for switching autonomous profiles.
   */
  public void disabledContinuous()
  {
  }

  /**
   * Method called at 50 Hz while the robot is disabled.
   */
  public void disabledPeriodic()
  {
  }

  // Private methods =========================================================
  /**
   * Updates the text being displayed on the Driver's Station's LCD area. Calls
   * different methods for generating the text depending on the robot's current
   * state of operation.
   */
  private synchronized void UpdateLCD()
  {
    // It is possible to be autonomous and disabled, even though we treat
    // these as three states, so we add checks here to make it work as
    // expected
    if (isAutonomous() && !isDisabled())
    {
      UpdateAutonomousLCD();
    }
    else if (isOperatorControl() && !isDisabled())
    {
      UpdateTeleoperatedLCD();
    }
    else// Disabled
    {
      UpdateDisabledLCD();
    }

    // Send the changes to the driver's station
    msg.updateLCD();
  }

  /**
   * Generates the LCD text to be displayed during autonomous mode. All text is
   * 21 characters per line in order to ensure erasure of previously displayed
   * text.
   */
  private void UpdateAutonomousLCD()
  {
    // Show the name of the currently selected autonomous profile
    msg.println(DriverStationLCD.Line.kMain6, 1, "     AUTONOMOUS      ");
    msg.println(DriverStationLCD.Line.kUser2, 1,
                //GetAutonomousProfileName(autonomousProfile));
                "                     ");
    msg.println(DriverStationLCD.Line.kUser3, 1, "                     ");
    msg.println(DriverStationLCD.Line.kUser4, 1, "TASK:                ");
    msg.println(DriverStationLCD.Line.kUser5, 1,
                taskManager.GetCurrentTaskName());
    msg.println(DriverStationLCD.Line.kUser6, 1,
                taskManager.GetCurrentTaskState());
  }

  /**
   * Generates the LCD text to be displayed during teleoperated mode. All text
   * is 21 characters per line in order to ensure erasure of previously
   * displayed text.
   */
  private void UpdateTeleoperatedLCD()
  {
    msg.println(DriverStationLCD.Line.kMain6, 1, "    TELEOPERATED     ");
  }

  /**
   * Generates the LCD text to be displayed during disabled mode. All text is 21
   * characters per line in order to ensure erasure of previously displayed
   * text.
   */
  private void UpdateDisabledLCD()
  {
    // Show the name of the currently selected autonomous profile
    msg.println(DriverStationLCD.Line.kMain6, 1, "      DISABLED       ");
    msg.println(DriverStationLCD.Line.kUser2, 1, "                     ");
    msg.println(DriverStationLCD.Line.kUser3, 1, "AUTONOMOUS PROFILE:  ");
    msg.println(DriverStationLCD.Line.kUser4, 1,
                //GetAutonomousProfileName(autonomousProfile));
                "                     ");
    msg.println(DriverStationLCD.Line.kUser5, 1, "                     ");
    msg.println(DriverStationLCD.Line.kUser6, 1, "                     ");
  }
}