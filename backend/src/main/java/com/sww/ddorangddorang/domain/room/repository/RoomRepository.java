package com.sww.ddorangddorang.domain.room.repository;

import com.sww.ddorangddorang.domain.room.entity.Room;
import com.sww.ddorangddorang.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByAccessCodeAndStartedAtIsNullAndDeletedAtIsNull(Integer accessCode);

    List<Room> findAllByStartedAtIsNullAndDeletedAtIsNull();

    Optional<Room> findByAdminAndStartedAtIsNullAndDeletedAtIsNull(User admin);

    @EntityGraph(attributePaths = {"participants"})
    List<Room> findByStartedAtBeforeAndDeletedAtIsNull(LocalDateTime time);

    List<Room> findAllByStartedAtIsNotNullAndDeletedAtIsNull();
}
