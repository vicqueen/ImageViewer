package ImageViewer;

public class ImageInfoBuilder {
	
	public ImageInfo BuildImage(String line)
	{
		String[] parameters = line.split(";");
		
		for (int i = 0; i < parameters.length; i++)
			parameters[i] = parameters[i].trim();
		
		try
		{	
			return new ImageInfo(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4]);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
