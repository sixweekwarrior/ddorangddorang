package com.sww.ddorangddorang.domain.mission.dto;

import com.sww.ddorangddorang.domain.mission.entity.MissionPerform;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionPerformsInfoRes {

    private Long missionId;
    private String title;
    private String content;
    private Boolean isComplete;
    private Long missionType;

    public static MissionPerformsInfoRes of(MissionPerform missionPerform) {
        // Fetch Join이나 Entity Graph 안쓰면 N+1 문제가 발생한다.

        return MissionPerformsInfoRes.builder()
            .missionId(missionPerform.getId())
            .title(missionPerform.getMission().getTitle())
            .content(missionPerform.getMission().getContent())
            .isComplete(missionPerform.getPerformedAt() != null)
            .missionType(missionPerform.getMission().getMissionType())
            .build();
    }

    public static List<MissionPerformsInfoRes> listOf(List<MissionPerform> missionPerforms) {
        return missionPerforms.stream()
            .map(MissionPerformsInfoRes::of)
            .collect(Collectors.toList());
    }

}