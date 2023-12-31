package com.sww.ddorangddorang.domain.room.dto;

import com.sww.ddorangddorang.domain.room.entity.Room;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomGetRes {
    Integer duration;
    Integer minMember;
    Integer maxMember;
    Integer roomKey;
    Integer memberCount;

    @Builder
    public RoomGetRes(Integer duration, Integer minMember, Integer maxMember, Integer roomKey, Integer memberCount) {
        this.duration = duration;
        this.minMember = minMember;
        this.maxMember = maxMember;
        this.roomKey = roomKey;
        this.memberCount = memberCount;
    }

    public static RoomGetRes toDto(Room room) {
        return RoomGetRes.builder()
            .duration(room.getDuration())
            .minMember(room.getMinMember())
            .maxMember(room.getMaxMember())
            .roomKey(room.getAccessCode())
            .memberCount(room.getHeadCount())
            .build();
    }
}
