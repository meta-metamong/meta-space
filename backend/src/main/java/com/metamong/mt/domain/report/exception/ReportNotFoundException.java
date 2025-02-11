package com.metamong.mt.domain.report.exception;

import java.util.NoSuchElementException;

public class ReportNotFoundException extends NoSuchElementException{
    public ReportNotFoundException(String message, Throwable e) {
        super(message, e);
    }
    
    public ReportNotFoundException(String message) {
        super(message);
    }
    
    public ReportNotFoundException(Throwable e) {
        super(e);
    }
    
    public ReportNotFoundException() {
        super();
    }
}
