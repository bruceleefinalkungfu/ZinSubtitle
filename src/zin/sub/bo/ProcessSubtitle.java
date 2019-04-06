package zin.sub.bo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ProcessSubtitle {

	String fileName;
	
	Map<String, String> time = new LinkedHashMap<>();

	public ProcessSubtitle(String fileName) {
		super();
		this.fileName = fileName;
	}

	public void mark(String howMuchToSubtract) {
		mark("00:00:00", howMuchToSubtract);
	}

	/**
	 * Mark is used to mark the whole subtitle. It will subtract later on at once
	 * <pre>
	 * values of subtract and their meaning
	 * 13		=> 0:00:12:00
	 * 2m3		=> 0:02:03:00
	 * 1m2s11	=> 0:01:02:11
	 * subtracting an hour is currently not supported, just subtract 30 twice 
	 * </pre>
	 * @param fromTime : 00:00:00 by default
	 * @param howMuchToSubtract : check the comments above 
	 */
	public void mark(String fromTime, String howMuchToSubtract) {
		time.put(fromTime, howMuchToSubtract);
	}
	
	public Set<Map.Entry<String, String>> getEntries() {
		return time.entrySet();
	}
	
}
