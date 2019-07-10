package com.example;

import lombok.Data;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串格式化测试.
 */
public class DateFormatTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateFormatTest.class);

    /**
     * 将原本格式的日期格式化成另一种日期表示形式.
     * 缺陷:原日期格式如果没有一些数据,会报越界.
     */
    @Test
    public void testFormat(){
        //给定一个format格式,格式化一个日期字符串并返回.
        //例如给定一个字符串"2019-09-19 00:00:00" 和format "dd*MM@yyyy" 返回19*09@2019.
        String targetFormat = "dd*MM@yyyy mm-hh/ss";
        String sourceFormat = "yyyy-MM-dd hh:mm:ss";
        String time = "2019-09-19 15:30:25";
        String result = convert(sourceFormat,targetFormat,time);
        LOGGER.info("日期:{}用格式化器:{}格式化的结果:{}",time,targetFormat,result);
    }

    private String convert(String sourceFormat,String targetFormat, String time) {
        return new SourceTime(new FormatInfo(sourceFormat),time).format(targetFormat);
    }

    @Data
    private class FormatInfo{
        private int yearOffset;
        private int yearLength;
        private int monthOffset;
        private int monthLength;
        private int dayOffset;
        private int dayLength;
        private int hourOffset;
        private int hourLength;
        private int minuteOffset;
        private int minuteLength;
        private int secondOffset;
        private int secondLength;
        private int totalLength;

        FormatInfo(String format){
            int yo = format.indexOf('y');
            if(yo!=-1){
                yearOffset = yo;
                yearLength = format.lastIndexOf('y') - yo + 1;
            }
            int mo = format.indexOf('M');
            if(mo!=-1){
                monthOffset = mo;
                monthLength = format.lastIndexOf('M') -mo + 1;
            }
            int dayO = format.indexOf('d');
            if(dayO!=-1){
                dayOffset = dayO;
                dayLength = format.lastIndexOf('d') - dayO + 1;
            }
            int ho = format.indexOf('h');
            if(ho!=-1){
                hourOffset = ho;
                hourLength = format.lastIndexOf('h') - ho + 1;
            }
            int minO = format.indexOf('m');
            if(minO!=-1){
                minuteOffset = minO;
                minuteLength = format.lastIndexOf('m') - minO + 1;
            }
            int so = format.indexOf('s');
            if(so!=-1){
                secondOffset = so;
                secondLength = format.lastIndexOf('s') - so + 1;
            }
            this.totalLength = format.length();
        }
    }

    /**
     * 取决于入参格式.
     */
    private class SourceTime{
        private char[] year;
        private char[] month;
        private char[] day;
        private char[] hour;
        private char[] minute;
        private char[] second;
        private FormatInfo format;

        SourceTime(FormatInfo format,String time){
            this.format = format;
            char[] times = verifyAndGetCharArray(time);
            year = new char[format.getYearLength()];
            month = new char[format.getMonthLength()];
            day = new char[format.getDayLength()];
            hour = new char[format.getHourLength()];
            minute = new char[format.getMinuteLength()];
            second = new char[format.getSecondLength()];
            System.arraycopy(times,format.getYearOffset(),year,0,year.length);
            System.arraycopy(times,format.getMonthOffset(),month,0,month.length);
            System.arraycopy(times,format.getDayOffset(),day,0,day.length);
            System.arraycopy(times,format.getHourOffset(),hour,0,hour.length);
            System.arraycopy(times,format.getMinuteOffset(),minute,0,minute.length);
            System.arraycopy(times,format.getSecondOffset(),second,0,second.length);

        }

        //校验自身的构建参数时间和format是否一致.
        char[] verifyAndGetCharArray(String time){
            //check null,length>=19
            if(null==time||time.length()!=format.getTotalLength()){
                //抛出异常.
                throw new RuntimeException("参数格式错误,需要"+format.getTotalLength()+"字符");
            }
            char[] times = time.toCharArray();
            char c;

            for(int i = format.getYearOffset(); i< format.getYearLength(); i++){
                int d = time.charAt(i) - '0' ;
                if(d>9 | d<0){
                    throw new RuntimeException("参数格式错误,请检查"+time+"第"+(i+1)+"位的年份位是否为0-9数字");
                }
            }
            for(int i = format.getMonthOffset(); i< format.getMonthLength(); i++){
                int d = time.charAt(i) - '0' ;
                if(d>9 | d<0){
                    throw new RuntimeException("参数格式错误,请检查"+time+"第"+(i+1)+"位的月份位是否为0-9数字");
                }
            }
            for(int i = format.getDayOffset(); i< format.getDayLength(); i++){
                int d = time.charAt(i) - '0' ;
                if(d>9 | d<0){
                    throw new RuntimeException("参数格式错误,请检查"+time+"第"+(i+1)+"位的日期位是否为0-9数字");
                }
            }
            for(int i = format.getHourOffset(); i< format.getHourLength(); i++){
                int d = time.charAt(i) - '0' ;
                if(d>9 | d<0){
                    throw new RuntimeException("参数格式错误,请检查"+time+"第"+(i+1)+"位的小时位是否为0-9数字");
                }
            }
            for(int i = format.getMinuteOffset(); i< format.getMinuteLength(); i++){
                int d = time.charAt(i) - '0' ;
                if(d>9 | d<0){
                    throw new RuntimeException("参数格式错误,请检查"+time+"第"+(i+1)+"位的分钟位是否为0-9数字");
                }
            }
            for(int i = format.getSecondOffset(); i< format.getSecondLength(); i++){
                int d = time.charAt(i) - '0' ;
                if(d>9 | d<0){
                    throw new RuntimeException("参数格式错误,请检查"+time+"第"+(i+1)+"位的秒数位是否为0-9数字");
                }
            }
            return times;
        }
        //格式化为另一个format.
        String format(String format){
            FormatInfo info = new FormatInfo(format);
            char[] formatArr = format.toCharArray();
            int dayLength = info.getDayLength();
            int yearLength = info.getYearLength();
            int monthLength = info.getMonthLength();
            int hourLength = info.getHourLength();
            int minuteLength = info.getMinuteLength();
            int secondLength = info.getSecondLength();
            if(yearLength > 0){
                System.arraycopy(year,year.length - yearLength,formatArr,info.getYearOffset(),yearLength);
            }
            if(monthLength > 0){
                System.arraycopy(month,month.length-monthLength,formatArr,info.getMonthOffset(),monthLength);
            }
            if(dayLength > 0){
                System.arraycopy(day,day.length - dayLength,formatArr,info.getDayOffset(),dayLength);
            }
            if(hourLength > 0){
                System.arraycopy(hour,hour.length-hourLength,formatArr,info.getHourOffset(),hourLength);
            }
            if(minuteLength > 0){
                System.arraycopy(minute,minute.length - minuteLength,formatArr,info.getMinuteOffset(),minuteLength);
            }
            if(secondLength > 0){
                System.arraycopy(second,second.length-secondLength,formatArr,info.getSecondOffset(),secondLength);
            }

            return new String(formatArr);
        }
    }
}
