package com.sww.ddorangddorang.domain.participant.repository;

import com.sww.ddorangddorang.domain.participant.entity.Participant;
import com.sww.ddorangddorang.domain.room.entity.Room;
import com.sww.ddorangddorang.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByRoomAndIsWithdrawalFalseAndDeletedAtIsNull(Room room);

    Optional<Participant> findByUserAndRoomAndIsWithdrawalFalse(User user, Room room);
}