package com.ChaTop.Backend.Repositories;

import com.ChaTop.Backend.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {


}
