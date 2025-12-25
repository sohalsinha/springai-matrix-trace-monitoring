package com.learning.openai.services;

import com.learning.openai.entity.HelpDeskTicket;
import com.learning.openai.model.TicketRequest;
import com.learning.openai.repository.HelpDeskTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

    @Autowired
    private HelpDeskTicketRepository helpDeskTicketRepository;

    public HelpDeskTicketService(HelpDeskTicketRepository helpDeskTicketRepository) {
        this.helpDeskTicketRepository = helpDeskTicketRepository;
    }

    public HelpDeskTicket createTicket(TicketRequest ticketRequest,String username) {
        HelpDeskTicket helpDeskTicket = HelpDeskTicket.builder()
                .issue(ticketRequest.issue())
                .username(username)
                .status("Open")
                .createdDateTime(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();
       return helpDeskTicketRepository.save(helpDeskTicket);
    }

    public List<HelpDeskTicket> getHelpDeskTicket(String username) {
        return helpDeskTicketRepository.findByUsername(username);
    }
}
