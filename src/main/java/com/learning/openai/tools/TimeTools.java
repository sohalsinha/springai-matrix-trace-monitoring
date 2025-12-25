package com.learning.openai.tools;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeTools {

    private static final Logger logger = LoggerFactory.getLogger(TimeTools.class);

    @Tool(name = "getCurrentLocalTime" ,description = "Get the Current time in the user's time zone")
    String getCurrentLocalTime() {
        logger.info("Returning local time from time tool");
        return LocalDateTime.now().toString();
    }

    @Tool(name = "getCurrentTime" ,description = "Get the Current time in the specified time zone")
    public static String getCurrentTime(@ToolParam(
            description = "Value representing the time zone" ) String timeZone) {
        logger.info("Returning local time from time tool");
        return LocalDateTime.now(ZoneId.of(timeZone)).toString();
    }
}
