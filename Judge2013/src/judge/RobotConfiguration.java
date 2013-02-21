/*******************************************************************************
* File:  RobotConfiguration.java
* Date:  2/19/2013
* Auth:  K. Loux
* Desc:  Constants for the 2013 robot.
*******************************************************************************/

// Declare our package
package judge;

/**
 * Class for storing robot configuration
 * Mostly wiring - defines all connections, slots, channels, etc.
 *
 * @author kloux
 */
public class RobotConfiguration
{
    // Module slot numbers
    public static final int digitalSideCarModule = 2;

	public static final double frequency = 50.0;
	public static final double timeStep = 1.0 / frequency;

	/***************************************************************************
	 *                              Outputs
	 **************************************************************************/
	// PWM Outputs
	public static final int leftMotorChannel = 1;
	public static final int rightMotorChannel = 2;
	public static final int leadscrewMotor1Channel = 3;
	public static final int leadscrewMotor2Channel = 4;
	public static final int boomMotorChannel = 5;
	public static final int leftHookChannel = 6;
	public static final int rightHookChannel = 7;

	/***************************************************************************
	 *								 Inputs
	 **************************************************************************/
    // Digital sensors
	public static final int leadscrewEncoderAChannel = 1;
	public static final int leadscrewEncoderBChannel = 2;
	public static final int leadscrewHomeSwitchChannel = 3;
	public static final int boomSwitchChannel = 4;

	/***************************************************************************
	 *                   Hardware Information and Constants
	 **************************************************************************/
	public static final int usrEncoderPulsesPerRevolution = 360;// [-]
	public static final double usrEncoderAnglePerPulse =
			usrEncoderPulsesPerRevolution / 360.0;// [deg]

	//public static final double cimpleBoxGearRatio = 4.67;// [-]
	public static final double leadscrewPulleyRatio = 18.0 / 20.0;// [-]
	public static final int leadscrewEncoderFactor = 1;
	public static final double leadscrewPitch = 10.0;// [rev/in]
	public static final double leadscrewRatio = 1.0 /
			(usrEncoderPulsesPerRevolution * leadscrewEncoderFactor *
			leadscrewPulleyRatio * leadscrewPitch);// [in/encoder count]
}
