package max.nlp.util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

	
	  public static String getDurationBreakdown(long millis)
	    {
	        if(millis < 0)
	        {
	            throw new IllegalArgumentException("Duration must be greater than zero!");
	        }

	        long days = TimeUnit.MILLISECONDS.toDays(millis);
	        millis -= TimeUnit.DAYS.toMillis(days);
	        long hours = TimeUnit.MILLISECONDS.toHours(millis);
	        millis -= TimeUnit.HOURS.toMillis(hours);
	        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
	        millis -= TimeUnit.MINUTES.toMillis(minutes);
	        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

	        StringBuilder sb = new StringBuilder(64);
	        sb.append(days);
	        sb.append(" Days ");
	        sb.append(hours);
	        sb.append(" Hours ");
	        sb.append(minutes);
	        sb.append(" Minutes ");
	        sb.append(seconds);
	        sb.append(" Seconds");

	        return(sb.toString());
	    }
}
