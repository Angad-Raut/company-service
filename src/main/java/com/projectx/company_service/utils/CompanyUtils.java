package com.projectx.company_service.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CompanyUtils {
    public static final String COMPANY_DETAILS_NOT_EXISTS="Company details not present!!";
    public static final String COMPANY_DETAILS_ALREADY_EXISTS="Company details already exists!!";
    public static final String DASH="-";
    public static final String SALARY_TYPE="SALARY";
    public static final String FORM_16_TYPE="FORM_16";
    public static final String OFFER_LETTER_TYPE="OFFER_LETTER";
    public static final String EXPERIENCE_LETTER_TYPE="EXPERIENCE_LETTER";
    public static final String SERVICE_LETTER_TYPE="SERVICE_LETTER";
    public static final String APPOINTMENT_LETTER_TYPE="APPOINTMENT_LETTER";
    public static final String OTHER_TYPE="OTHER";
    public static final String ISO_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String VIEW_DATE_FORMAT="dd MMMM yyyy";
    public static final String toExpenseDate(Date expenseDate) {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        return format.format(expenseDate);
    }
    public static Date getISODate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(date);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate));
    }
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
