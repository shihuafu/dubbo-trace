package com.fushihua.web;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: DateEditor 
 * @Description: 时间转换 
 * @author fushihua
 * @date 2018年4月26日 上午10:44:11
 */
public class WebDateEditor extends PropertyEditorSupport {

	/** 日期类型 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 时间类型 */
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 时间标记，若包含该字符串则为时间 */
	private static final String TIME_FLAG = ":";

    private DateFormat dateFormat;
    private boolean allowEmpty = true;

    public WebDateEditor() {
    }

    public WebDateEditor(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public WebDateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
    }

    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && StringUtils.isBlank(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            try {
                if(this.dateFormat != null) {
                	setValue(this.dateFormat.parse(text));                	
                } else {
                    if(text.contains(TIME_FLAG)) {
                        setValue(new SimpleDateFormat(TIME_FORMAT).parse(text));
                    } else {
                        setValue(new SimpleDateFormat(DATE_FORMAT).parse(text));
                    }
                }
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        DateFormat dateFormat = this.dateFormat;
        if(dateFormat == null) {
            dateFormat = new SimpleDateFormat(TIME_FORMAT);
        }
        return (value != null ? dateFormat.format(value) : "");
    }
}

