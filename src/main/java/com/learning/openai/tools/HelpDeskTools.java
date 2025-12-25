package com.learning.openai.tools;

import com.learning.openai.entity.HelpDeskTicket;
import com.learning.openai.model.TicketRequest;
import com.learning.openai.services.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionException;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {

    private static final Logger logger = LoggerFactory.getLogger(HelpDeskTools.class);

    private final HelpDeskTicketService helpDeskTicketService;

    @Bean
    ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {
            //Set value True if the error as is need to be send to the client default is false
        return new DefaultToolExecutionExceptionProcessor(false);
    }


    //Return Direct to be used only if the response to be sending as is instead of fabricating it with LLM
    @Tool(name ="createTicket", description = "Create a Support Ticket", returnDirect = true)
    String createTicket(@ToolParam(required = true,
                    description = "Details to create a Support Ticket")
                    TicketRequest ticketRequest, ToolContext toolContext){

        /*ToolCallResultConverter do the job in background to convert json into desired objects*/
        String username = (String)toolContext.getContext().get("username");
        logger.info("Creating a Support Ticket for user {}", username);
        HelpDeskTicket savedTicket = helpDeskTicketService.createTicket(ticketRequest, username);
        return "Ticket created successfully with ID :" + savedTicket.getId() ;
    }

    @Tool(description = "Fetch the status of the open tickets based on the given username")
    List<HelpDeskTicket> getTicketStatusByUsername(ToolContext toolContext){
        String  username = (String)toolContext.getContext().get("username");
        //throw new RuntimeException("Not implemented yet");
        return helpDeskTicketService.getHelpDeskTicket(username);
    }


}
