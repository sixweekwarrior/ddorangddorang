package com.sww.ddorangddorang.domain.room.api;

import com.sww.ddorangddorang.domain.room.dto.RoomInfoReq;
import com.sww.ddorangddorang.domain.room.dto.ShowUsersRes;
import com.sww.ddorangddorang.domain.room.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomApi {

    private final RoomService roomService;

    @PostMapping("/")
    public Integer createRoom(@RequestHeader Long userId, @RequestBody RoomInfoReq roomInfoReq) {
        log.info("RoomApi_createRoom start");
        Integer accessCode = roomService.createRoom(userId, roomInfoReq);
        log.info("RoomApi_createRoom end: " + accessCode);
        return accessCode;
    }

    @PostMapping("/join")
    public Boolean joinRoom(@RequestHeader Long userId, @RequestBody Integer accessCode) {
        log.info("RoomApi_joinRoom start");
        Boolean joinRoomResponse = roomService.joinRoom(userId, accessCode);

        log.info("RoomApi_joinRoom end: " + joinRoomResponse);

        if (joinRoomResponse) {
            roomService.checkAndRunIfRoomShouldStart(userId);
        }

        return joinRoomResponse;
    }

    @PutMapping("/")
    public Boolean updateRoom(@RequestHeader Long userId, @RequestBody RoomInfoReq roomInfoReq) {
        log.info("RoomApi_updateRoom start");
        Boolean updateRoomResponse = roomService.updateRoom(userId, roomInfoReq);
        log.info("RoomApi_updateRoom end: " + updateRoomResponse);

        if (updateRoomResponse) {
            roomService.checkAndRunIfRoomShouldStart(userId);
        }

        return updateRoomResponse;
    }

    //방장이 방을 삭제하는 API
    //방장을 포함한 전원이 탈퇴
    @DeleteMapping("/admin/")
    public Boolean deleteRoom(@RequestHeader Long userId) {
        log.info("RoomApi_deleteRoom start");

        Boolean deleteRoomResponse = roomService.deleteGame(userId);

        log.info("RoomApi_deleteRoom end: " + deleteRoomResponse);

        return deleteRoomResponse;
    }

    //방 참여 인원이 방을 나가는 API
    //회원 혼자만이 탈퇴
    //방장은 방 탈퇴가 불가능
    @DeleteMapping("/")
    public Boolean withdrawalRoom(@RequestHeader Long userId) {
        log.info("RoomApi_withdrawalRoom start");

        Boolean withdrawalRoomResponse = roomService.withdrawalRoom(userId);

        log.info("RoomApi_withdrawalRoom end: " + withdrawalRoomResponse);

        return withdrawalRoomResponse;
    }

    @GetMapping("/{roomId}")
    public List<ShowUsersRes> showUsers(@RequestHeader Long userId) {
        log.info("RoomApi_showUsers start");

        List<ShowUsersRes> showUsersResList = roomService.showUsers(userId);

        log.info("RoomApi_showUsers end");

        return showUsersResList;
    }

}
