package zin.sub.bo;

import zin.z.string.ZinRegEx;

public class SubtitleTime {

	public static final String 		HOUR_MIN_SEC_SEPARATOR			= Constant.HOUR_MIN_SEC_SEPARATOR;
	public static final String 		SEC_MILLI_SEPARATOR				= Constant.SEC_MILLI_SEPARATOR;
	public static final short		MAX_POSSIBLE_MILLI_SEC_VALUE	= Constant.MAX_POSSIBLE_MILLI_SEC_VALUE;
	public static final short		MAX_POSSIBLE_MILLI_SEC_VALUE_SRT= Constant.MAX_POSSIBLE_MILLI_SEC_VALUE_SRT;
	public static final String 		FROM_TO_SEPARATOR				= Constant.FROM_TO_SEPARATOR;
	public static final short		MAX_POSSIBLE_MIN_SEC_VALUE		= 59;
	
	public final short hour;
	public final short min;
	public final short sec;
	public final int millSec;

	public SubtitleTime(String subtitleTimeStr) {
		String [] hourMinSecMilliArr = subtitleTimeStr.split(ZinRegEx.literal(HOUR_MIN_SEC_SEPARATOR).or(ZinRegEx.literal(SEC_MILLI_SEPARATOR))
				.or(ZinRegEx.literal(FROM_TO_SEPARATOR)).getRegEx());
		if(hourMinSecMilliArr.length != 4)
			throw new RuntimeException("String "+subtitleTimeStr+" is not in h:mm:ss:ms format");
		this.hour = Short.valueOf(hourMinSecMilliArr[0]);
		this.min = Short.valueOf(hourMinSecMilliArr[1]);
		this.sec = Short.valueOf(hourMinSecMilliArr[2]);
		this.millSec = Short.valueOf(hourMinSecMilliArr[3]);
	}
	
	private SubtitleTime(short hour, short min, short sec, short millSec) {
		this.hour = hour;
		this.min = min;
		this.sec = sec;
		this.millSec = millSec;
	}
	
	private SubtitleTime(short hour, short min, short sec, int millSec) {
		this.hour = hour;
		this.min = min;
		this.sec = sec;
		this.millSec = millSec;
	}

	public String getStringValue() {
		return hour < 0 ? "" : hour + HOUR_MIN_SEC_SEPARATOR + zeroPrefixedTime(min) + HOUR_MIN_SEC_SEPARATOR + zeroPrefixedTime(sec) 
		+ SEC_MILLI_SEPARATOR + zeroPrefixedTime( (short) millSec);
	}
	
	public String getSrtValue() {
		return hour < 0 ? "" : zeroPrefixedTime(hour) + HOUR_MIN_SEC_SEPARATOR + zeroPrefixedTime(min) + HOUR_MIN_SEC_SEPARATOR + zeroPrefixedTime(sec) 
		+ FROM_TO_SEPARATOR + zeroPrefixedTimeSrt(millSec);
	}
	
	private String zeroPrefixedTime(short minOrSec) {
		return (minOrSec / 10 == 0) ? "0" + minOrSec : ""+minOrSec;
	}
	
	private String zeroPrefixedTimeSrt(int minOrSec) {
		int noOfDigits = (minOrSec/10 == 0 ? 1 : (minOrSec/100 == 0 ? 2 : 3 ));
		if(noOfDigits == 1)
			return "00" + minOrSec;
		else if(noOfDigits == 2)
			return "0" + minOrSec;
		else
			return minOrSec+"";
	}

	public short operate(Operation operation, short num1, short num2) {
		if ( operation == Operation.ADD)
			return (short) (num1 + num2);
		else if ( operation == Operation.SUBTRACT)
			return (short) (num1 - num2);
		else
			throw new RuntimeException("Write logic for the operation "+operation);
	}

	public int operate(Operation operation, int num1, int num2) {
		if ( operation == Operation.ADD)
			return (num1 + num2);
		else if ( operation == Operation.SUBTRACT)
			return (num1 - num2);
		else
			throw new RuntimeException("Write logic for the operation "+operation);
	}

	public SubtitleTime operate(Operation operation, SubtitleTime with) {
		short opMill = operate(operation, (short) this.millSec , (short) with.millSec);
		short addToSec = 0, addToMin = 0, addToHour = 0;
		if (opMill > MAX_POSSIBLE_MILLI_SEC_VALUE) {
			opMill = (short) (opMill % (MAX_POSSIBLE_MILLI_SEC_VALUE + 1 ));
			addToSec++;
		}
		else if(opMill < 0) {
			opMill = (short) (opMill + MAX_POSSIBLE_MILLI_SEC_VALUE + 1);
			addToSec--;
		}
		short opSec = (short) ( operate(operation, this.sec, with.sec) + addToSec );
		if (opSec > MAX_POSSIBLE_MIN_SEC_VALUE) {
			opSec = (short) (opSec % (MAX_POSSIBLE_MIN_SEC_VALUE + 1));
			addToMin++;
		}
		else if ( opSec < 0) {
			opSec = (short) (opSec + MAX_POSSIBLE_MIN_SEC_VALUE + 1);
			addToMin--;
		}
		short opMin = (short) ( operate(operation, this.min, with.min) + addToMin );
		if(opMin > MAX_POSSIBLE_MIN_SEC_VALUE) {
			opMin = (short) (opMin % (MAX_POSSIBLE_MIN_SEC_VALUE + 1));
			addToHour++;
		}
		else if ( opMin < 0) {
			opMin = (short) (opMin + MAX_POSSIBLE_MIN_SEC_VALUE + 1);
			addToHour--;
		}
		short opHour = (short) (operate(operation, this.hour, with.hour) + addToHour );
		if(opHour > 99)
			throw new RuntimeException("Developers haven't considered the possiblity of hour value "+opHour+" being more than 99");
		return new SubtitleTime(opHour, opMin, opSec, opMill);
	}

	public SubtitleTime operateSrt(Operation operation, SubtitleTime with) {
		int opMill = operate(operation, this.millSec , with.millSec);
		short addToSec = 0, addToMin = 0, addToHour = 0;
		if (opMill > MAX_POSSIBLE_MILLI_SEC_VALUE_SRT) {
			opMill = (short) (opMill % (MAX_POSSIBLE_MILLI_SEC_VALUE_SRT + 1 ));
			addToSec++;
		}
		else if(opMill < 0) {
			opMill = (short) (opMill + MAX_POSSIBLE_MILLI_SEC_VALUE_SRT + 1);
			addToSec--;
		}
		short opSec = (short) ( operate(operation, this.sec, with.sec) + addToSec );
		if (opSec > MAX_POSSIBLE_MIN_SEC_VALUE) {
			opSec = (short) (opSec % (MAX_POSSIBLE_MIN_SEC_VALUE + 1));
			addToMin++;
		}
		else if ( opSec < 0) {
			opSec = (short) (opSec + MAX_POSSIBLE_MIN_SEC_VALUE + 1);
			addToMin--;
		}
		short opMin = (short) ( operate(operation, this.min, with.min) + addToMin );
		if(opMin > MAX_POSSIBLE_MIN_SEC_VALUE) {
			opMin = (short) (opMin % (MAX_POSSIBLE_MIN_SEC_VALUE + 1));
			addToHour++;
		}
		else if ( opMin < 0) {
			opMin = (short) (opMin + MAX_POSSIBLE_MIN_SEC_VALUE + 1);
			addToHour--;
		}
		short opHour = (short) (operate(operation, this.hour, with.hour) + addToHour );
		if(opHour > 99)
			throw new RuntimeException("Developers haven't considered the possiblity of hour value "+opHour+" being more than 99");
		return new SubtitleTime(opHour, opMin, opSec, opMill);
	}

	public SubtitleTime subtract (String hhMMSSMs) {
		return operate(Operation.SUBTRACT, new SubtitleTime(hhMMSSMs));
	}

	public SubtitleTime subtractSrt (String hhMMSSMs) {
		return operateSrt(Operation.SUBTRACT, new SubtitleTime(hhMMSSMs));
	}
	
	private static enum Operation {
		ADD, SUBTRACT;
	}
}