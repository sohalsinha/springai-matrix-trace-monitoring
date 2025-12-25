package com.learning.openai.repository;

import com.learning.openai.entity.HelpDeskTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket,Long> {

    List<HelpDeskTicket> findByUsername(String username);
}
