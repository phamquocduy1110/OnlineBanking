package tu.thesis.onlinebanking.pk.Class;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateTime
{
    public static final long TicksPerSecond = 1000L;
    public static final long TicksPerMinute = 60000L;
    public static final long TicksPerHour = 3600000L;
    public static final long TicksPerDay = 86400000L;
    private Date date;
    private static DateTime _instance;
    private static TimeZone zeroTimeZone = new SimpleTimeZone(0, "13256");
    private Calendar cal;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private TimeZone timeZone;

    private static DateTime getInst()
    {
        if (_instance == null)
            _instance = new DateTime();

        return _instance;
    }

    private DateTime()
    {

        this.cal = Calendar.getInstance(Locale.US);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.dateFormat.setLenient(false);
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
        this.timeFormat.setLenient(false);
        this.date = new Date(-4354885899415191552L);
        this.timeZone = TimeZone.getDefault();
    }

    public static long getNow()
    {
        return System.currentTimeMillis();
    }

    public static String Date(long Ticks)
    {
        DateTime d = getInst();
        d.date.setTime(Ticks);
        return d.dateFormat.format(d.date);
    }

    public static String Time(long Ticks)
    {
        DateTime d = getInst();
        d.date.setTime(Ticks);
        return d.timeFormat.format(d.date);
    }

    public static String getTimeFormat()
    {
        return getInst().timeFormat.toPattern(); }

    public static void setTimeFormat(String Pattern) {
        getInst().timeFormat.applyPattern(Pattern);
    }

    public static String getDateFormat()
    {
        return getInst().dateFormat.toPattern(); }

    public static void setDateFormat(String Pattern) {
        getInst().dateFormat.applyPattern(Pattern);
    }

    public static long DateParse(String Date)
            throws ParseException
    {
        return getInst().dateFormat.parse(Date).getTime();
    }

    public static String getDeviceDefaultDateFormat()
    {
        SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateInstance();
        return sdf.toPattern();
    }

    public static String getDeviceDefaultTimeFormat()
    {
        SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getTimeInstance();
        return sdf.toPattern();
    }

    public static long TimeParse(String Time)
            throws ParseException
    {
        SimpleDateFormat tf = getInst().timeFormat;
        tf.setTimeZone(zeroTimeZone);
        long time = tf.parse(Time).getTime();
        tf.setTimeZone(getInst().timeZone);
        long offsetInMinutes = Math.round(getTimeZoneOffset() * 60.0D);
        long dayStartInUserTimeZone = System.currentTimeMillis() + offsetInMinutes * 60000L;
        dayStartInUserTimeZone -= dayStartInUserTimeZone % 86400000L;
        dayStartInUserTimeZone -= offsetInMinutes * 60000L;
        return (dayStartInUserTimeZone + time % 86400000L);
    }

    public static long DateTimeParse(String Date, String Time)
            throws ParseException
    {
        SimpleDateFormat df = getInst().dateFormat;
        SimpleDateFormat tf = getInst().timeFormat;
        df.setTimeZone(zeroTimeZone);
        tf.setTimeZone(zeroTimeZone);
        try
        {
            long l1;
            long dd = DateParse(Date);
            long tt = tf.parse(Time).getTime();
            long total = dd + tt;

            int endShift = (int)(GetTimeZoneOffsetAt(total) * 3600000.0D);
            total -= endShift;
            int startShift = (int)(GetTimeZoneOffsetAt(total) * 3600000.0D);
            total += endShift - startShift;
            return total;
        } finally {
            tf.setTimeZone(getInst().timeZone);
            df.setTimeZone(getInst().timeZone);
        }
    }

    public static void SetTimeZone(int OffsetHours)
    {
        getInst().timeZone = new SimpleTimeZone(OffsetHours * 3600 * 1000, "");
        getInst().cal.setTimeZone(getInst().timeZone);
        getInst().dateFormat.setTimeZone(getInst().timeZone);
        getInst().timeFormat.setTimeZone(getInst().timeZone);
    }

    public static double getTimeZoneOffset()
    {
        return (getInst().timeZone.getOffset(System.currentTimeMillis()) / 3600000.0D);
    }

    public static double GetTimeZoneOffsetAt(long Date)
    {
        double d = getInst().timeZone.getOffset(Date) / 3600000.0D;
        return d;
    }

    public static int GetYear(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return getInst().cal.get(1);
    }

    public static int GetMonth(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return (getInst().cal.get(2) + 1);
    }

    public static int GetDayOfMonth(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return getInst().cal.get(5);
    }

    public static int GetDayOfYear(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return getInst().cal.get(6);
    }

    public static int GetDayOfWeek(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return getInst().cal.get(7);

    }

    public static int GetHour(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return getInst().cal.get(11);
    }

    public static int GetSecond(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return getInst().cal.get(13);
    }

    public static int GetMinute(long Ticks)
    {
        getInst().cal.setTimeInMillis(Ticks);
        return getInst().cal.get(12);
    }

    public static long Add(long Ticks, int Years, int Months, int Days)
    {
        Calendar c = getInst().cal;
        c.setTimeInMillis(Ticks);
        c.add(1, Years);
        c.add(2, Months);
        c.add(6, Days);
        return c.getTimeInMillis();
    }
}