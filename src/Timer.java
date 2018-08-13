
public class Timer {
	long start;
	long stop;
	public final static String[] units = { "μs", "ms", "s", "ks", "Ms" };


	public void start() {
		start = System.nanoTime();
	}


	public void stop() {
		stop = System.nanoTime();
	}


	public long getTime() {
		return stop - start;
	}


	public String getFormattedTime() {
		long time = getTime();
		int unit = (int)((Math.log10(time) - 9 - 2) / 3);
		if(unit > 2)
			unit = 2;
		else if(unit < -2)
			unit = -2;

		return (time / Math.pow(10, unit * 3 + 9)) + units[unit + 2]; 
	}
}