package com.ffa.FFA_flight_booking_system.repositories;

import com.ffa.FFA_flight_booking_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Override
    <S extends User> S save(S entity);

    @Override
    <S extends User> List<S> saveAll(Iterable<S> entities);

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByUsername(String username);

    @Override
    List<User> findAll();

    @Override
    boolean existsById(Long aLong);

    @Override
    long count();

    @Override
    void deleteById(Long aLong);

    @Override
    void delete(User entity);

    @Override
    void deleteAllById(Iterable<? extends Long> longs);

    @Override
    void deleteAll(Iterable<? extends User> entities);

    @Override
    void deleteAll();
}
