package com.sww.ddorangddorang.domain.user.api;

import com.sww.ddorangddorang.auth.dto.AuthenticatedUser;
import com.sww.ddorangddorang.auth.dto.TokenClaims;
import com.sww.ddorangddorang.auth.service.JwtService;
import com.sww.ddorangddorang.domain.user.dto.HintDto;
import com.sww.ddorangddorang.domain.user.dto.UsersGetRes;
import com.sww.ddorangddorang.domain.user.dto.UsersHomeInfoGetRes;
import com.sww.ddorangddorang.domain.user.dto.UsersLoginPostRes;
import com.sww.ddorangddorang.domain.user.dto.UsersMoreinfoPutReq;
import com.sww.ddorangddorang.domain.user.dto.UsersSignupPostReq;
import com.sww.ddorangddorang.domain.user.dto.UsersSignupPostRes;
import com.sww.ddorangddorang.domain.user.dto.UsersSsafyinfoPutReq;
import com.sww.ddorangddorang.domain.user.dto.UsersTodayinfoPostReq;
import com.sww.ddorangddorang.domain.user.dto.UsersTokenInfo;
import com.sww.ddorangddorang.domain.user.entity.User;
import com.sww.ddorangddorang.domain.user.exception.UserNotFoundException;
import com.sww.ddorangddorang.domain.user.service.UserService;
import com.sww.ddorangddorang.global.common.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserApi {

    private final UserService userService;
    private final JwtService jwtService;
    private final static String SUCCESS = "SUCCESS";

    @PostMapping("/login")
    public CommonResponse<UsersLoginPostRes> login(
        @RequestHeader("Authorization") String authorizationHeader, HttpServletResponse response)
        throws Exception {
        log.info("UserApi_login starts: {}", authorizationHeader);

        Jwt jwt = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .build().decode(authorizationHeader.substring(7));

        log.info("Claims: {}", jwt.getClaims());
        String email = jwt.getClaim("email");
        log.info("UserApi_login, email: {}", email);

        User user = userService.findByEmail(email).orElseThrow(UserNotFoundException::new);

        String refreshToken = jwtService.createRefreshToken();
        String accessToken = jwtService.createAccessToken(TokenClaims.builder()
            .id(user.getId())
            .email(user.getEmail())
            .build());

        userService.saveRefreshToken(UsersTokenInfo.builder()
            .email(user.getEmail())
            .refreshToken(refreshToken)
            .build());

        log.info("UserApi_login ends\n accessToken: {}\n refreshToken: {}", accessToken,
            refreshToken);
        return CommonResponse.success(
            UsersLoginPostRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()
        );
    }

    @PostMapping(value = "/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<UsersSignupPostRes> signUp(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestPart("profile") MultipartFile profile,
        @RequestPart("signupInfo") UsersSignupPostReq usersSignupPostReq
    ) throws Exception {
        log.info("UserApi_signup starts");

        log.info("UserApi_signup, header: {}", authorizationHeader.substring(7));
        Jwt jwt = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .build().decode(authorizationHeader.substring(7));

        String email = jwt.getClaim("email");
        log.info("UserApi_signup, email: {}", email);

        User user = usersSignupPostReq.toUser();
        // TODO: Need to fix if we add other OAuth
        user.addProviderInfo(email, "Google");
        user.authorizeUser();

        userService.signUp(user);

        String refreshToken = jwtService.createRefreshToken();
        String accessToken = jwtService.createAccessToken(TokenClaims.builder()
            .id(user.getId())
            .email(user.getEmail())
            .build());

        userService.saveRefreshToken(UsersTokenInfo.builder()
            .email(email)
            .refreshToken(refreshToken)
            .build());

        userService.upload(user.getId(), profile);

        log.info("UserApi_signup ends");
        return CommonResponse.success(
            UsersSignupPostRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()
        );
    }

    @GetMapping
    public CommonResponse<UsersGetRes> getUserInfo(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return CommonResponse.success(userService.getUserInfo(authenticatedUser.getId()));
    }

    @GetMapping("/user-info-list")
    public CommonResponse<List<UsersGetRes>> getUserInfoList(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return CommonResponse.success(userService.getUserInfoList(authenticatedUser.getId()));
    }

//    @GetMapping("/jwt-test")
//    public String jwtTest(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
//        log.info("jwt-test start: {}", authenticatedUser);
//        log.info("id: {}", authenticatedUser.getId());
//        log.info("providerType: {}", authenticatedUser.getProviderType());
//        log.info("email: {}", authenticatedUser.getEmail());
//        log.info("username: {}", authenticatedUser.getUsername());
//        log.info("role: {}", authenticatedUser.getRole());
//        log.info("authorities: {}", authenticatedUser.getAuthorities());
//        log.info("password: {}", authenticatedUser.getPassword());
//
//        return "jwtTest 요청 성공";
//    }

    @PostMapping("/s3-test")
    public CommonResponse<String> uploadFile(@RequestParam("profile") MultipartFile profile) {
        log.info(profile.getContentType());
        log.info(profile.getOriginalFilename());
        log.info(profile.getName());
        log.info(String.valueOf(profile.getSize()));
        userService.upload(2L, profile);
        return CommonResponse.success("Success");
    }

    @PutMapping("/todayinfo")
    public CommonResponse<String> todayinfo(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
        @RequestBody UsersTodayinfoPostReq usersTodayinfoPostReq) {
        log.info("UserApi_todayInfo starts");
        userService.todayInfo(authenticatedUser.getId(), usersTodayinfoPostReq);
        log.info("UserApi_todayInfo ends");
        return CommonResponse.success(SUCCESS);
    }

    @PutMapping("/ssafyinfo")
    public CommonResponse<String> ssafyInfo(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
        @RequestBody UsersSsafyinfoPutReq usersSsafyinfoPutReq) {
        log.info("UserApi_ssafyInfo starts");
        userService.ssafyInfo(authenticatedUser.getId(), usersSsafyinfoPutReq);
        log.info("UserApi_ssafyInfo ends");
        return CommonResponse.success(SUCCESS);
    }

    @PutMapping("/moreinfo")
    public CommonResponse<String> moreInfo(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
        @RequestBody UsersMoreinfoPutReq usersMoreinfoPutReq) {
        log.info("UserApi_moreInfo starts");
        userService.moreInfo(authenticatedUser.getId(), usersMoreinfoPutReq);
        log.info("UserApi_moreInfo ends");
        return CommonResponse.success(SUCCESS);
    }

    @GetMapping("/manitohint")
    public CommonResponse<HintDto> getManitoHint(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        log.info("UserApi_getManitoHint starts");
        HintDto hintDto = userService.getManitoHint(authenticatedUser.getId());
        log.info("UserApi_getManitoHint ends");
        return CommonResponse.success(hintDto);
    }

    @GetMapping("/state")
    public CommonResponse<Long> getUserState(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        log.info("UserApi_getUserState starts");
        Long userState = userService.getUserState(authenticatedUser.getId());
        log.info("UserApi_getUserState ends: userState - {}", userState);
        return CommonResponse.success(userState);
    }

    @GetMapping("/home")
    public CommonResponse<UsersHomeInfoGetRes> getHomeInfo(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        log.info("RoomApi_getHomeInfo start");
        UsersHomeInfoGetRes usersHomeInfoGetRes = userService.getHomeInfo(
            authenticatedUser.getId());
        log.info("RoomApi_getHomeInfo end");
        return CommonResponse.success(usersHomeInfoGetRes);
    }
}