package com.ffa.FFA_flight_booking_system.generators;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/*
*   Original code by https://github.com/hendisantika
*   Source: https://github.com/hendisantika/spring-boot-custom-ID-sample
*   Description: Used this project to create custom Flight ID's and custom Reservation ID's
*/

public class PrefixCodeGenerator extends SequenceStyleGenerator {

    public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
    public static final String VALUE_PREFIX_DEFAULT = "";
    public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
    public static final String NUMBER_FORMAT_DEFAULT = "%d";
    public static final String VALUE_SUFFIX_PARAMETER = "valueSuffix";
    public static final String VALUE_SUFFIX_DEFAULT = "";
    public static final int INCREMENT_PARAM_DEFAULT = 50;
    private String valuePrefix;
    private String numberFormat;
    private String valueSuffix;

    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) throws HibernateException {
        return valuePrefix + String.format(numberFormat, super.generate(session, object)) + valueSuffix;
    }

    @Override
    public void configure(Type type, Properties params,
                          ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(LongType.INSTANCE, params, serviceRegistry);
        valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER,
                params, VALUE_PREFIX_DEFAULT);
        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER,
                params, NUMBER_FORMAT_DEFAULT);
        valueSuffix = ConfigurationHelper.getString(VALUE_SUFFIX_PARAMETER,
                params, VALUE_PREFIX_DEFAULT);
    }
}
