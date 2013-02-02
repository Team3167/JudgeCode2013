/*
 * This code should find and displays the location of the small piece of reflective
 * tape on the piece of cardboard only on half of the image though.
 *
 *
 *@author Ryan Young
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author Ryan Young
 */
public class VisionTarget_1
{

  private static final double FRC_PARTICLE_TO_IMAGE_PERCENT = .0001;
  private static final double FRC_SIZE_FACTOR = 3.0;
  private static final double FRC_MAX_IMAGE_SEPARATION = 20.0;
  private static final double FRC_ALIGNMENT_SCALE = 3.0;
  final ParticleAnalysisReport firstParticle;
  //final ParticleAnalysisReport secondParticle;

  VisionTarget_1(ParticleAnalysisReport firstParticle)
  {
    this.firstParticle = firstParticle;
  }

  /*
   * public static class Position {
   *
   * public final int value; protected static final int above_val = 0; protected
   * static final int below_val = 1; protected static final int right_val = 2;
   * protected static final int left_val = 3; public static final Position above
   * = new Position(above_val); public static final Position bellow = new
   * Position(below_val); public static final Position right = new
   * Position(right_val); public static final Position left = new
   * Position(left_val);
   *
   * private Position(int value) { this.value = value; }
	}
   */
  public static class Threshold
  {

    int plane1Low;
    int plane1High;
    int plane2Low;
    int plane2High;
    int plane3Low;
    int plane3High;

    public Threshold(int plane1Low, int plane1High,
                     int plane2Low, int plane2High,
                     int plane3Low, int plane3High)
    {
      this.plane1Low = plane1Low;
      this.plane1High = plane1High;
      this.plane2Low = plane2Low;
      this.plane2High = plane2High;
      this.plane3Low = plane3Low;
      this.plane3High = plane3High;
    }
  }

  private static boolean aligned(int center1, int center2, int dimension1, int dimension2)
  {
    double averageWidth = (dimension1 + dimension2) / 2.0;
    //scale down width
    averageWidth *= FRC_ALIGNMENT_SCALE;
    int centerDiff = Math.abs(center1 - center2);
    if (centerDiff < averageWidth)
    {
      return true;
    }
    //dimensions (widths or heights) should be nearly the same. If they are
    //different, most likely there is glare or incorrect color specification
    return false;
  }

  private static boolean adjacent(int value1, int value2)
  {
    if (Math.abs(value1 - value2) <= FRC_MAX_IMAGE_SEPARATION)
    {
      return true;
    }
    return false;
  }

  private static boolean sizesRelative(double area1, double area2)
  {
    if ((area2 < (area1 * FRC_SIZE_FACTOR)) && (area1 < (area2 * FRC_SIZE_FACTOR)))
    {
      return true;
    }
    return false;
  }

  /**
   * Search for a target in the given image of the two color ranges given and
   * positioned according to the given position.
   *
   * @param image The image to find the target within.
   * @param position The position of the two colors realtive to eachother.
   * @param firstThreshold The first color range.
   * @param secondThreshold The second color range.
   * @return A Target object containing information about the target or null if
   * no target was found.
   */
  public static VisionTarget findFirstTarget(ColorImage image,
                                             Threshold firstThreshold) throws NIVisionException
  {
    BinaryImage firstColor = image.thresholdHSL(
            firstThreshold.plane1Low, firstThreshold.plane1High,
            firstThreshold.plane2Low, firstThreshold.plane2High,
            firstThreshold.plane3Low, firstThreshold.plane3High);
    //BinaryImage secondColor = image.thresholdHSL(
    //firstThreshold.plane1Low, firstThreshold.plane1High,
    //firstThreshold.plane2Low, firstThreshold.plane2High,
    //firstThreshold.plane3Low, firstThreshold.plane3High);

    int numberOfParticles = firstColor.getNumberParticles();
    int halfTheParticles = numberOfParticles / 2;

    ParticleAnalysisReport[] firstColorHits = new ParticleAnalysisReport[halfTheParticles];

    for (int i = 0; i < halfTheParticles; i++)
    {
      firstColorHits[i] = firstColor.getParticleAnalysisReport(i);
    }

    //ParticleAnalysisReport[] firstColorHits = firstColor.getOrderedParticleAnalysisReports(3);
    //ParticleAnalysisReport[] secondColorHits = secondColor.getOrderedParticleAnalysisReports(3);
    firstColor.free();
    //secondColor.free();

    for (int i = 0; i < firstColorHits.length; i++)
    {
      ParticleAnalysisReport firstTrackReport = firstColorHits[i];
      //ParticleAnalysisReport secondTrackReport = secondColorHits[i];
      if (firstTrackReport.particleToImagePercent < FRC_PARTICLE_TO_IMAGE_PERCENT)
      {
        break;
      }

      VisionTarget target = new VisionTarget(firstTrackReport);

      //firstTrackReport.center_mass_y >= -.1 && firstTrackReport.center_mass_y <= .1  possible alternate filter
      if ((target.getSize()) >= 1.0)
      {
        // add in the SizesRelative call if needed -
        // so far it does not seem necessary
        return target;
      }

    }

    return null;
  }

  public static VisionTarget findSecondTarget(ColorImage image,
                                              Threshold firstThreshold) throws NIVisionException
  {

    BinaryImage secondColor = image.thresholdHSL(
            firstThreshold.plane1Low, firstThreshold.plane1High,
            firstThreshold.plane2Low, firstThreshold.plane2High,
            firstThreshold.plane3Low, firstThreshold.plane3High);

    int numberOfParticles = secondColor.getNumberParticles();
    int halfTheParticles = numberOfParticles / 2;
    int theOtherHalf = numberOfParticles - halfTheParticles;

    ParticleAnalysisReport[] secondColorHits = new ParticleAnalysisReport[theOtherHalf];
    secondColor.free();

    for (int i = 0; i < secondColorHits.length; i++)
    {
      ParticleAnalysisReport secondTrackReport = secondColorHits[i];
      if (secondTrackReport.particleToImagePercent < FRC_PARTICLE_TO_IMAGE_PERCENT)
      {
        break;
      }

      VisionTarget target = new VisionTarget(secondTrackReport);


      if ((target.getSize()) >= 1.0)
      {
        return target;
      }

    }

    return null;
  }

  //public double getX1Position
  public double getXPosition()
  {
    return (firstParticle.center_mass_x_normalized * firstParticle.particleToImagePercent) / getSize();
  }

  //public double getX2Position()
  public double getYPosition()  //getY1() and getY2()
  {
    return (firstParticle.center_mass_y_normalized * firstParticle.particleToImagePercent) / getSize();
  }

  public double getSize() //get
  {
    return firstParticle.particleToImagePercent;
  }

  public String toString()
  {
    return "Target at ( " + getXPosition() + " , " + getYPosition() + " ) of size " + getSize();
  }
}
