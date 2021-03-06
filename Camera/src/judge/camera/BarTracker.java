/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package judge.camera;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 *
 * @author Ryan Young
 */
public class BarTracker
{

  VisionTarget.Threshold firstColor = new VisionTarget.Threshold(0, 240, 140, 255, 100, 230);
  AxisCamera camera = AxisCamera.getInstance();

  public BarTracker()
  {
  }

  public void setFirstColorThresholds(int lowerHue, int upperHue,
                                      int lowerSaturation, int upperSaturation,
                                      int lowerLuminance, int upperLuminance)
  {
    firstColor = new VisionTarget.Threshold(lowerHue, upperHue,
                                            lowerSaturation, upperSaturation,
                                            lowerLuminance, upperLuminance);
  }

  public VisionTarget[] getTarget() throws NIVisionException, AxisCameraException
  {
    while (true)
    {
      ColorImage image = camera.getImage();
      try
      {
        VisionTarget[] targets = VisionTarget.findTargets(image, firstColor);
        return targets;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (image != null)
        {
          image.free();
        }
        image = null;
      }
      Timer.delay(.001);
    }
    // While loop ends when an array of targets is found and returned. 

  }
}
