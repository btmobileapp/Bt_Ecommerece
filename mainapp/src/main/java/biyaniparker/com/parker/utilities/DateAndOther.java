package biyaniparker.com.parker.utilities;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DateAndOther
{
	/*

	1.  To get current millisecond               getCurrentMillisec()
	2.  Get String Day from millisecod           getStringDayfromMillisecond(long milisecond)
    3.  Get Current Date in string               public static  String currentDate()
    4   Get forword day                          static  long getForwordDay(long Millisecond,int days)
    5.                                          public static long getMillisecond(String dateInString)


	 */




	Context context;
	Calendar cal;
	public DateAndOther()
	{
		cal=Calendar.getInstance();
	}
	public DateAndOther(Context context)
	{
		this.context=context;
		cal=Calendar.getInstance();
	}
	public    long getCurrentMillisec()
	{
		Time today = new Time(Time.getCurrentTimezone());
		Calendar calendar = Calendar.getInstance();
		return calendar.getTimeInMillis();
	}
public static String getStringDayfromMillisecond(long milisecond)
	{
	Date currentDate = new Date(milisecond);
	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	String da=df.format(currentDate);
	return da;
}
	public static  String currentDate()
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();

		int d=today.monthDay;
		int m=today.month+1;
		int y=today.year;
		if(m<10)
			return d+"-0"+m+"-"+y;
		else
			return d+"-"+m+"-"+y;
	}

	public static  String getHMSFrommillisecond(long millis)
	{
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		return  hms;
	}
	public static  int currentYear()
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();


		return today.year;

	}

	public static  String currentMonthYearID()
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int d=today.monthDay;
		int m=today.month+1;
		int y=today.year;
		if(m<10)
		{
			return "0"+m+"-"+y;
		}
		else
		{
			return +m+"-"+y;
		}
	}

	public static  int getCurrentMonthInt()
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int d=today.monthDay;
		int m=today.month+1;
		int y=today.year;
		return m;
	}
	public static  int getCurrentYearInt()
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int d=today.monthDay;
		int m=today.month+1;
		int y=today.year;
		return y;
	}

	public static long convertToYMD(String date)
	{
		//26/06/2015
		String[] list=date.split("/");
    	/*
    	int year=Integer.parseInt(list[2]);
    	int month=Integer.parseInt(list[1]);
    	int day=Integer.parseInt(list[0]);
    	*/

		return Long.parseLong(list[2]+list[1]+list[0]);
	}

	public static long convertToLongDate(String ddmmyyyy)
	{
		//26/06/2015
		//String[] list=date.split("-");
    	/*
    	int year=Integer.parseInt(list[2]);
    	int month=Integer.parseInt(list[1]);
    	int day=Integer.parseInt(list[0]);
    	*/
		long longDate=0;

		try {
			//String dateString = "30/09/2014";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date = sdf.parse(ddmmyyyy);

			 longDate = date.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}


		return longDate;		//Long.parseLong(list[2]+list[1]+list[0]);
	}


	public static String convertToDMY(long date)
	{
		String sdate=date+"";
		String year=sdate.substring(0, 3);
		String month=sdate.substring(4, 5);
		String day=sdate.substring(6, 7);
		return  day+"/"+month+"/"+year;
	}

	public static String getMonth(int month)
	{
		if(month==1)
			return "Jan";
		if(month==2)
			return "Feb";
		if(month==3)
			return "Mar";
		if(month==4)
			return "Apr";
		if(month==5)
			return "May";
		if(month==6)
			return "Jun";
		if(month==7)
			return "Jul";
		if(month==8)
			return "Aug";
		if(month==9)
			return "Sep";
		if(month==10)
			return "Oct";
		if(month==11)
			return "Nov";
		if(month==12)
			return "Dec";
		return "";
	}


	public static String getMonth(long milisecond)
	{

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date(milisecond));

		return getMonth(cal.get(Calendar.MONTH)+1);

	}


	public static long getLastDate(int month, int year)
	{
		Calendar calendar = Calendar.getInstance();
		// passing month-1 because 0-->jan, 1-->feb... 11-->dec
		calendar.set(year, month - 1, 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		return calendar.getTimeInMillis();

		// Date date = calendar.getTime();
		// DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/YYYY");
		// return DATE_FORMAT.format(date);
	}
	public static  long getFirstDate(int month, int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		return calendar.getTimeInMillis();
	}
	public static  long getForwordDay(long Millisecond,int days)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date(Millisecond));
		cal.add(Calendar.DAY_OF_YEAR, days);
		return cal.getTimeInMillis();
	}
	public static String getMonthFull(int month)
	{
		// TODO Auto-generated method stub
		if(month==1)
			return "January";
		if(month==2)
			return "February";
		if(month==3)
			return "March";
		if(month==4)
			return "April";
		if(month==5)
			return "May";
		if(month==6)
			return "Jun";
		if(month==7)
			return "July";
		if(month==8)
			return "August";
		if(month==9)
			return "September";
		if(month==10)
			return "October";
		if(month==11)
			return "November";
		if(month==12)
			return "December";
		return "";


	}
	public static long getMillisecond(String dateInString)
	{
		try
		{
			//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			//
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			dateInString=dateInString.replace("-", ".");
			dateInString=dateInString+" 0:0:1";
			//String dateInString = "22-01-2015 10:20:56";
			Date date = sdf.parse(dateInString.trim());

			System.out.println(dateInString.trim());
			System.out.println("Date - Time in milliseconds : " + date.getTime());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			System.out.println("Calender - Time in milliseconds : " + calendar.getTimeInMillis());

			return  calendar.getTimeInMillis();
		}
		catch(Exception e)
		{
			Log.d(e.toString(), e.getMessage());
		}
		return 0;
	}


	public static  int getDayDifference(long start,long end)
	{
		long diff = end - start;
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);
		int diffInDays = (int) ((end - start) / (1000 * 60 * 60 * 24));

		if (diffInDays > 1)
			return diffInDays+1;
		if(diffHours > 23)
			return 1+1;
		return 1;
	}


}
