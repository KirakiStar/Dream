package helpers;

public class ScalarValues {
	private static int x10 = (int)(10 * Settings.getCurrentScale());
	private static int x20 = x10 * 2;
	private static int x16 = (int)(16 * Settings.getCurrentScale());
	private static int x32 = x16 * 2;
	private static int x64 = x32 * 2;
	private static int x128 = x64 * 2;
	
	public static void setScalars() {
		x10 = (int)(10 * Settings.getCurrentScale());
		x20 = getX10() * 2;
		x16 = (int)(16 * Settings.getCurrentScale());
		x32 = getX16() * 2;
		x64 = getX32() * 2;
		x128 = getX64() * 2;
	}
	
	public static int getX10() { return x10; }
	public static int getX20() { return x20; }
	public static int getX16() { return x16; }
	public static int getX32() { return x32; }
	public static int getX64() { return x64; }
	public static int getX128() { return x128; }
}
