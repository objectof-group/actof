package net.objectof.actof.common.controller.config;



import java.io.File;




public class Env
{

	public enum OS {
		WINDOWS,
		MAC,
		UNIX,
		OTHER
	}
	

	public static boolean isWindows()
	{

		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);

	}


	public static boolean isMac()
	{

		String os = System.getProperty("os.name").toLowerCase();
		// Mac
		return (os.indexOf("mac") >= 0);

	}


	public static boolean isUnix()
	{

		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

	}

	
	public static Env.OS getOS()
	{
		if (isWindows()) return OS.WINDOWS;
		if (isMac()) return OS.MAC;
		if (isUnix()) return OS.UNIX;
		return OS.OTHER;
	}
	
	
	public static File appDataDirectory(String appname)
	{
		switch (getOS())
		{
			case WINDOWS: return new File(System.getenv("APPDATA") + "\\" + appname);
			case MAC: return new File(System.getProperty("user.home") + "/Library/Application Support/" + appname);
			
			case OTHER:
			case UNIX:
			default:
				return new File(System.getProperty("user.home") + "/.config/" + appname);
		}

	}
	
	

}
