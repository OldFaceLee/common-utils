package com.ai.commonUtils.dateTimeUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


import com.ai.commonUtils.httpclientUtils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;



public class DateTimeUtils {
	private static Logger logger = Logger.getLogger(DateTimeUtils.class);
	/*private DateTimeUtils(){}

	private static DateTimeUtils dateTimeUtils = null;
	public static DateTimeUtils getInstance(){
		if (dateTimeUtils == null) {
			dateTimeUtils = new DateTimeUtils();
			logger.info("单例模式创建DateTimeUtils对象");
		}
		return dateTimeUtils;
	}*/

	/**
	 * 获取一天中第一秒
	 *
	 * @param date
	 * @return
	 */
	public static String getTheDayMin(Date date) {
		if (date == null) {
			return null;
		}
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDateTime min = LocalDateTime.of(localDate, LocalTime.MIN);
		return min.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 获取一天最后一秒
	 *
	 * @param date
	 * @return
	 */
	public static String getTheDayMax(Date date) {
		if (date == null) {
			return null;
		}
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDateTime max = LocalDateTime.of(localDate, LocalTime.MAX);
		return max.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}




	/**
	 * 获取两个时间差多少天,与getDayDiffrent 方法一直
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long getDiffrentDays(Date startTime, Date endTime) {
		long day = 0;
		long nd = 1000 * 24 * 60 * 60;
		long diff;
		try {
			//获得两个时间的毫秒时间差异
			diff = endTime.getTime() - startTime.getTime();
			// 计算差多少天
			day = diff / nd;
			logger.info("结束时间"+endTime+"减去开始时间"+startTime+"获得的时间差为"+day+"天");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}
	
	
	/**
	 * 两个时间日期相减获取的天数，天数时间差，单位为天
	 * @return
	 * @throws ParseException
	 */
	public static int getDiffrentDays(String sdfPattern,String startDatePattern,String endDatePattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
		long dateA = 0;
		try {
			dateA = sdf.parse(startDatePattern).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long dateB = 0;
		try {
			dateB = sdf.parse(endDatePattern).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("结束时间"+endDatePattern+"减去开始时间"+startDatePattern+"=" +(int) ((dateB - dateA)/(24*3600*1000))+"天");
		return (int) ((dateA - dateB)/(24*3600*1000));
	}

	/**
	 * 获取date2  比  date1多出来的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) {
			/**同一年*/
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
					/**闰年*/
					timeDistance += 366;
				} else {
					/**不是闰年*/
					timeDistance += 365;
				}
			}
			return timeDistance + (day2 - day1);
		} else {
			/**不同年*/
			return day2 - day1;
		}
	}
	
	/**
	 * 获取当前时间，传入SimpleDateFormat的pattern即可
	 * @return
	 */
	public static String getCurrentDateTime(SimpleDateFormat sdf){
	    Date currentdate = new Date(System.currentTimeMillis()); 
	    String dateTime = sdf.format(currentdate);
	    logger.info("当前的时间为: "+currentdate);
	    return dateTime;
	}
	/**
	 * 判断月份是否为基数月，返回true为基数月，否则均为偶数月
	 * @param month
	 * @return
	 */
	public static boolean isOddMonth(Integer month){
		boolean day31 = true;
		if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
			logger.info("月份参数month="+month+"为31天");
			return day31;
		}
		return false;
	}
	
	/**
	 * http://lanfly.vicp.io/api/holiday/info/用了这个免费的api,
	 * 该方法中传入的pattern必须为： yyyy-MM-dd，会自动判断日期是否是工作日还是非工作日， 如果返回true，那么是非工作日（即周六周日或者法定节假日）；如果返回false则为工作日
	 * @return
	 * @throws Exception
	 */
	public static boolean isDateNonWorkDay(String dateTimePatten_yyyy_MM_dd) {
		boolean nonworkday = false;
		String apiURL = "http://timor.tech/api/holiday/info/";
		Map<String,String> map = new HashMap();
		JSONObject jo = null;
		try {
			jo = HttpClientUtil.getInstance().get(apiURL+dateTimePatten_yyyy_MM_dd, map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date bdate = null;
		try {
			bdate = format1.parse(dateTimePatten_yyyy_MM_dd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
        cal.setTime(bdate); 
        
        int weekOfDay = cal.get(Calendar.DAY_OF_WEEK);
        String details = "true, 存在节假日或者周末, "+"节假日详情：" +jo.get("holiday")+", 周末详情：["+dateTimePatten_yyyy_MM_dd+"]";
        String nonworkDetails = "False,非节假日或者非周末-即为工作日["+dateTimePatten_yyyy_MM_dd+"]" ;

		if((!jo.get("holiday").equals(null)) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
			
			switch(weekOfDay-1){
			case 0:
				nonworkday = true;
				logger.info(details+" 星期日");
				break;
			case 1:
				nonworkday = true;
				logger.info(details+" 星期一");
				break;
			case 2:
				nonworkday = true;
				logger.info(details+" 星期二");
				break;
			case 3:
				nonworkday = true;
				logger.info(details+" 星期三");
				break;
			case 4:
				nonworkday = true;
				logger.info(details+" 星期四");
				break;
			case 5:
				nonworkday = true;
				logger.info(details+" 星期五");
				break;
			case 6:
				nonworkday = true;
				logger.info(details+" 星期六");
			}
		}else{
			switch(weekOfDay-1){
			case 0:
				logger.info(nonworkDetails+" 星期日");
				break;
			case 1:
				logger.info(nonworkDetails+" 星期一");
				break;
			case 2:
				logger.info(nonworkDetails+" 星期二");
				break;
			case 3:
				logger.info(nonworkDetails+" 星期三");
				break;
			case 4:
				logger.info(nonworkDetails+" 星期四");
				break;
			case 5:
				logger.info(nonworkDetails+" 星期五");
				break;
			case 6:
				logger.info(nonworkDetails+" 星期六");
			}
		}
		return nonworkday;
	}

	/**
	 * 获取 date 向前的时间， 返回时间类型
	 * @param date
	 * @param CalendarClazz
	 * @param CalendarParam
	 * @return
	 */
	public static Date getDateForwardXCalendarParam(Date date,int CalendarClazz,int CalendarParam){
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(CalendarClazz, -(int)CalendarParam);//这里就是月份的相加
		logger.info("获取向时间" + date + "向前推" + "的时间为"+cl.getTime());
		return cl.getTime();//获取到相加后的时间
	}

	/**
	 * 获取 date 向后的时间， 返回时间类型
	 * @param date
	 * @param CalendarClazz
	 * @param CalendarParam
	 * @return
	 */
	public static Date getDateBackwardXCalendarParam(Date date,int CalendarClazz,int CalendarParam){
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(CalendarClazz, +(int)CalendarParam);//这里就是月份的相加
		logger.info("获取向时间" + date + "向后推的时间为" + cl.getTime());
		return cl.getTime();//获取到相加后的时间
	}

	
	/**
	 * @Description 在已知的Date下，向前推算x秒，进行时间的计算返回
	 * CalendarClazz  要传入Calendar.SENCONDES 之类的
	 * @return
	 */
	public static String getDateTimeForwardXCalendarParam(String sdfPattern, Date date, int CalendarClazz,int CalendarParam){
		SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
		String knownTime = sdf.format(date);
		Calendar c = new GregorianCalendar();
		c.setTime(date);//设置参数时间
		c.add(CalendarClazz,-(int)(CalendarParam));//把日期往后增加SECOND 秒.整数往后推,负数往前移动
		date=c.getTime(); //这个时间就是日期往后推一天的结果
		String str = sdf.format(date);
		logger.info("已经参数时间"+knownTime+"向前推算"+CalendarClazz+"的时间："+sdf.format(c.getTime()));
		return str;
	}

    /**
     * CalendarClazz  要传入Calendar.SENCONDES 之类的  时间向后推算X
     * @param sdfPattern
     * @param date
     * @param CalendarClazz
     * @param CalendarParam
     * @return
     */
	public static String getDateTimeBackWardXCalendarParam(String sdfPattern, Date date,int CalendarClazz,int CalendarParam){
		SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
		String knownTime = sdf.format(date);
		Calendar c = new GregorianCalendar();
		c.setTime(date);//设置参数时间
		c.add(CalendarClazz,+(int)(CalendarParam));//把日期往后增加SECOND 秒.整数往后推,负数往前移动
		date=c.getTime(); //这个时间就是日期往后推一天的结果
		String str = sdf.format(date);
		logger.info("已经参数时间"+knownTime+"向后推算"+CalendarClazz+"的时间："+sdf.format(c.getTime()));
		return str;
	}

	/**
	 * 对比两个时间戳的大小
	 * @param startTime
	 * @param endTime
	 * @return
	 *
	 */
	public static boolean compare2TimeStr(String sdfPattern, String startTime, String endTime) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
		return sdf.parse(startTime).getTime() < sdf.parse(endTime).getTime();

	}



	/**
	 * 将时间戳 转化 为  smf 格式的 date
	 * @param dateTime
	 * @param sdfPattern
	 * @return
	 * @throws Exception
	 */
	public static String timeStrToDate(String sdfPattern,String dateTime){

//		Assert.hasText(dateStamp, "dateStampParam str is required");
//		Assert.hasText(sdfPattern, "sdfPatternParam is required");  //Assert.haxText 是 spring的断言用法
		SimpleDateFormat df = new SimpleDateFormat(sdfPattern);
		String dateStr = df.format(new Date(Long.parseLong(dateTime)));
		return dateStr;

	}

	/**
	 * string 转化为date 类型
	 * @param
	 * @param dateTimeStr
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String sdfPattern,String dateTimeStr) {
//		Assert.hasText(sdfPattern,"sdfPatternParam is required");
//		Assert.hasText(dateTimeStr,"dateTimeStrParam is required");
		SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
		Date date = null;
		try {
			date = sdf.parse(dateTimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date(date.getTime());
	}

	/**
	 * 将时间 转换 为 时间戳，  dateTimePatten 可以赋值为 sdf.format(new Date()))
	 * @param
	 * @param dateTimePatten
	 * @return
	 * @throws ParseException
	 */
	public static long dateToStamp(String sdfPattern, String dateTimePatten) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
        Date date = null;
        Timestamp ts = null;
        try {
            date = sdf.parse(dateTimePatten);
            ts = new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ts.getTime();
    }

    /**
     * 传入Date格式 转成 long的时间戳
     * @param date
     * @return
     */
    public static long dateToStamp(Date date) {
        Timestamp ts = null;
        ts = new Timestamp(date.getTime());
        return ts.getTime();
    }


	/**
	 * 传入时间， 获取时间对应的string类型对象
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String dateToString(Date time, String pattern) {
//		Assert.notNull(time, "time is required");
//		Assert.hasText(pattern, "pattern is required");
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(time);
	}

	/**
	 * 获取月份
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		return month + 1;
	}

	/**
	 * 这个方法的sdfPattern为yyyyMM, 在月份净增+1，  递增一个月的意思
	 * @param dateStr
	 * @return
	 */
	public static String incrementMonth(String dateStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		Calendar c1 = Calendar.getInstance();
		try {
			c1.setTime(simpleDateFormat.parse(dateStr));
			c1.add(Calendar.MONTH, 1);
		} catch (ParseException e) {
		}
		return simpleDateFormat.format(c1.getTime());
	}

	/**
	 * 递增一个月 返回date
	 * @param date
	 * @return
	 */
	public static Date incrementMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}

    /**
     * date递减一年
     *
     * @param date
     * @return
     */
    public static Date decrementYear(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime();
    }

	/**
	 * 根据指定日期获取第几季度
	 *
	 * @param date
	 * @return
	 */
	public static String getQuarter(Date date) {
		if (date == null) {
			return null;
		}
		return getYear(date) + "Q" + getSeason(date);
	}

	/**
	 * 取得日期：年
	 *
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		return year;
	}

	public static String getYear(String sdfPattern, Timestamp timestamp) {
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat(sdfPattern);
		try {
			//方法一
			tsStr = sdf.format(timestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	public static String getPreviousYear(String currentYear) {
		String tsStr = "";
		try {
			int i = Integer.parseInt(currentYear);
			return String.valueOf(i - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	public static String getNextYear(String currentYear) {
		String tsStr = "";
		try {
			int i = Integer.parseInt(currentYear);
			return String.valueOf(i + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}



	/**
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 *
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {

		int season = 0;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				season = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				season = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				season = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				season = 4;
				break;
			default:
				break;
		}
		return season;
	}

    /**
     * 获取某年第一天日期
     *
     * @param date
     * @return
     */
    public static  Date getYearFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, getYear(date));
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param date
     * @return
     */
    public static Date getYearLastDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, getYear(date));
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }
    /**
     * 返回指定日期的季的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 获取指定日期所在月的最后一天日期
     *
     * @param date
     * @return
     */
    private static   Date getLastDayOfDateMonth(Date date) {
        Date lastDayOfMonth = getLastDayOfMonth(getYear(date), getMonth(date));
        return lastDayOfMonth;
    }

	/**
	 * 获取当季的第一天
	 * @param localDate
	 * @return
	 */
	public static LocalDate getFirstDayOfQuarter(LocalDate localDate) {
		return localDate.with(IsoFields.DAY_OF_QUARTER, 1L);
	}
    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    private static Date getFirstDayOfMonth(Integer year, Integer month) {
        if(!(month==1 || month==2 ||month==3 ||month==4 ||month==5 ||month==6 ||month==7 || month==8 || month==9 ||
                month==10 || month==11 || month==12)){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定日期所在月的第一天日期
     *
     * @param date
     * @return
     */
    private static Date getFirstDayOfDateMonth(Date date) {
        Date firstDayOfMonth = getFirstDayOfMonth(getYear(date), getMonth(date));
        return firstDayOfMonth;
    }

    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month;
        if(!(quarter<=4 && quarter>=1)){
            return null;
        }
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    private static Date getLastDayOfMonth(Integer year, Integer month) {
//        Assert.notNull(year,"Year must not null");
        if(!(month==1 || month==2 ||month==3 ||month==4 ||month==5 ||month==6 ||month==7 || month==8 || month==9 ||
                month==10 || month==11 || month==12)){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }






}
