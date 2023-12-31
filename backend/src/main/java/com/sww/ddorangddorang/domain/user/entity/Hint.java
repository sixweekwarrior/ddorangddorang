package com.sww.ddorangddorang.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hint {

    @Id
    @GeneratedValue
    private Long id;
    private String content;
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column(name = "master_code_id")
//    @ManyToOne(fetch = FetchType.LAZY)
    private Long masterCode;


    public void updateContent(String content) {
        this.content = content;
    }

    @Builder
    public Hint(String content, User user, Long masterCode) {
        this.content = content;
        this.user = user;
        this.masterCode = masterCode;
    }
}
