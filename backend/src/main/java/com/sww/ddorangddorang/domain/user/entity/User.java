package com.sww.ddorangddorang.domain.user.entity;

import com.sww.ddorangddorang.domain.room.entity.Room;
import com.sww.ddorangddorang.domain.user.dto.UsersMoreinfoPutReq;
import com.sww.ddorangddorang.domain.user.dto.UsersSsafyinfoPutReq;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

@DynamicInsert
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;                    // BIGINT

    private String name;                // VARCHAR(255) "유저 이름"
    private String email;               // VARCHAR(255) "유저 이메일"
    private String providerType;        // VARCHAR(255) "소셜 로그인 타입 -GOOGLE, KAKAO" - for OAuth2
    private String providerId;          // VARCHAR(255) "소셜 로그인 식별자 값"            - for OAuth2
    private String password;            // VARCHAR(255)                                 - for security
    private String role;                // VARCHAR(255)                                 - for security
    private String refreshToken;        // VARCHAR(255) "리프레시 토큰"                   - for jwt
    private Integer generation;         // INT
    private Byte isMajor;               // TINYINT
    private Byte gender;                // TINYINT
    private Integer campus;             // INT
    @Column(name = "class")
    private Integer classes;            // INT
    private Integer floor;              // INT
    private String profileImage;        // TEXT "프로필 이미지"
    private String likes;                // VARCHAR(255)
    private String hate;                // VARCHAR(255)
    private String mbti;                // VARCHAR(255)
    private String worry;               // TEXT
    private Integer reportCount;        // INT
    @JoinColumn(name = "participate")
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;           // BIGINT - FK(ROOM)
    private Long status;                // BIGINT - FK(MASTER_CODE)
    private LocalDateTime deletedAt;    // TIMESTAMP

    @Builder(builderMethodName = "signup", builderClassName = "Signup")
    public User(Long id, String name, String email, String password, String role, Byte gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
    }

    @Builder(builderMethodName = "auth", builderClassName = "Auth")
    public User(String email, String providerType, String providerId) {
        this.email = email;
        this.providerType = providerType;
        this.providerId = providerId;
        this.role = "ROLE_GUEST";
    }

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = "ROLE_USER";
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updatedRefreshToken) {
        this.refreshToken = updatedRefreshToken;
    }

    public void updateSsafyInfo(UsersSsafyinfoPutReq usersSsafyinfoPutReq) {
        this.generation = usersSsafyinfoPutReq.getGeneration();
        this.profileImage = usersSsafyinfoPutReq.getProfileImage();
        this.campus = usersSsafyinfoPutReq.getCampus();
        this.classes = usersSsafyinfoPutReq.getClasses();
        this.isMajor = usersSsafyinfoPutReq.getIsMajor();
        this.floor = usersSsafyinfoPutReq.getFloor();
    }

    public void updateMoreInfo(UsersMoreinfoPutReq usersMoreinfoPutReq) {
        this.likes = usersMoreinfoPutReq.getLikes();
        this.hate = usersMoreinfoPutReq.getHate();
        this.mbti = usersMoreinfoPutReq.getMbti();
        this.worry = usersMoreinfoPutReq.getWorry();
    }

    public void updateRoom(Room room) {
        this.room = room;
    }

    public void updateStatus(Long status) {
        this.status = status;
    }
}
