package com.jnulocker.events.quartz;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventSchedulerConstants {

    public static final String EVENT_JOB_GROUP = "EVENT_JOB_GROUP";
    public static final String EVENT_TRIGGER_GROUP = "EVENT_TRIGGER_GROUP";
    public static final String EVENT_ID = "eventId";
    public static final String ASIA_SEOUL = "Asia/Seoul";

    public static final String PUBLISH_JOB_NAME = "EVENT_PUBLISH_JOB-";
    public static final String PUBLISH_TRIGGER_NAME = "EVENT_PUBLISH_TRIGGER-";

    public static final String UNPUBLISH_JOB_NAME = "EVENT_UNPUBLISH_JOB-";
    public static final String UNPUBLISH_TRIGGER_NAME = "EVENT_UNPUBLISH_TRIGGER-";

    public static final String OPEN_JOB_NAME = "EVENT_OPEN_JOB-";
    public static final String OPEN_TRIGGER_NAME = "EVENT_OPEN_TRIGGER-";

    public static final String CLOSE_JOB_NAME = "EVENT_CLOSE_JOB-";
    public static final String CLOSE_TRIGGER_NAME = "EVENT_CLOSE_TRIGGER-";

    public static final Long TWO_HOURS = 2L;
    public static final Long ONE_DAY = 1L;
}
