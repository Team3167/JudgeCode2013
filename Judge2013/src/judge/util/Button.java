/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package judge.util;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author User
 */
public class Button
{
	private final DigitalInput input;
	private int counter = 0;
	private final int count;
	
	public Button(int dsSlot, int channel, int count)
	{
		input = new DigitalInput(dsSlot, channel);
		this.count = count;
	}

	public boolean Get()
	{
		return counter >= count;
	}

	public void Update()
	{
		if (input.get() && counter < count)
			counter++;
		else
			counter = 0;
	}
}
