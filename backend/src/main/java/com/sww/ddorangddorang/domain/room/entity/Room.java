package com.sww.ddorangddorang.domain.room.entity;

import com.sww.ddorangddorang.domain.participant.entity.Participant;
import com.sww.ddorangddorang.domain.room.dto.RoomInfoReq;
import com.sww.ddorangddorang.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //BIGINT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @NotNull
    private User admin; //BIGINT

    @NotNull
    private Boolean isOpen = true;  //TINYINT(1)

    @NotNull
    private Integer accessCode;  //INT

    private Integer campus; //INT

    //    @Column(name = "min_member")
    @NotNull
    private Integer minMember = 1;  //INT

    /**
     * 현재 참여 인원 방 생성 시 참여 인원은 방 생성자 혼자이므로 기본값 = 1
     */
//    @Column(name = "head_count")
    @NotNull
    private Integer headCount = 1;  //INT

    //    @Column(name = "max_member")
    @NotNull
    private Integer maxMember = Integer.MAX_VALUE;  //INT

    //    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now().plusHours(9L);    //DATETIME

    //    @Column(name = "started_at")
    private LocalDateTime startedAt;    //DATETIME

    @NotNull
    private Integer duration;    //INT

    //    @Column(name = "profile_image")
    private String profileImage;    //TEXT

    //    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;    //DATETIME

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    public void startGame() {
        this.startedAt = LocalDateTime.now().plusHours(9L);
    }

    public void deleteRoom() {
        this.deletedAt = LocalDateTime.now().plusHours(9L);
    }

    public void joinMember() {
        ++this.headCount;
    }

    public void removeMember() {
        --this.headCount;
    }

    public void updateHeadCount(Integer headCount) {
        this.headCount = headCount;
    }

    public void updateRoomInfo(RoomInfoReq roomInfoReq) {
        this.maxMember = roomInfoReq.getMaxMember();
        this.minMember = roomInfoReq.getMinMember();
        this.isOpen = roomInfoReq.getIsOpen();
        this.duration = roomInfoReq.getDuration();
    }

    //getEndDate()의 날짜 00:00:00 000부터 게임은 종료됨
    public LocalDate getEndDate() {
        return this.startedAt.minusNanos(1L).plusDays(this.duration + 1).toLocalDate();
    }

    public Boolean isEnded() {
        return !LocalDateTime.now().plusHours(9L).toLocalDate().isBefore(getEndDate());
    }

    //Guess API 호출 가능시점
    public Boolean isGuessable() {
        return LocalDateTime.now().plusHours(9L)
            .isAfter(LocalDateTime.of(getEndDate(), LocalTime.MIDNIGHT).minusDays(3L))
            && !isEnded();
    }

    //결과 화면을 볼 수 있는 시기
    public Boolean isShowable() {
        return LocalDateTime.now().plusHours(9L).toLocalDate().equals(getEndDate());
    }

    @Builder
    public Room(User admin, Integer accessCode, Integer minMember, Integer maxMember,
        Integer duration, String profileImage) {
        this.admin = admin;
        this.accessCode = accessCode;
        this.campus = admin.getCampus();
        this.minMember = minMember;
        this.maxMember = maxMember;
        this.duration = duration;
        this.profileImage = profileImage;
    }
}
