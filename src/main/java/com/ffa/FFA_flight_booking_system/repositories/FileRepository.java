package com.ffa.FFA_flight_booking_system.repositories;

import com.ffa.FFA_flight_booking_system.models.File;
import com.ffa.FFA_flight_booking_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional
public interface FileRepository extends JpaRepository<File, Long> {
    File findByFileName(String fileName);
    Collection<File> findAllByUser(User user);
}