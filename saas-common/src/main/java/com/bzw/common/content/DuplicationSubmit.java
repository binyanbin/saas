package com.bzw.common.content;

import com.bzw.common.enums.BusinessType;

@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
public @interface DuplicationSubmit {

    BusinessType businessType() default BusinessType.NULL;
}
