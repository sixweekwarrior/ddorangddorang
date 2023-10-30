package com.sww.ddorangddorang.domain.room.service;

import com.sww.ddorangddorang.domain.participant.entity.Participant;
import com.sww.ddorangddorang.domain.participant.entity.Prefix;
import com.sww.ddorangddorang.domain.participant.entity.Suffix;
import com.sww.ddorangddorang.domain.participant.repository.ParticipantRepository;
import com.sww.ddorangddorang.domain.participant.repository.PrefixRepository;
import com.sww.ddorangddorang.domain.participant.repository.SuffixRepository;
import com.sww.ddorangddorang.domain.room.dto.RoomInfoReq;
import com.sww.ddorangddorang.domain.room.dto.ShowUsersRes;
import com.sww.ddorangddorang.domain.room.entity.Room;
import com.sww.ddorangddorang.domain.room.repository.RoomRepository;
import com.sww.ddorangddorang.domain.user.entity.User;
import com.sww.ddorangddorang.domain.user.repository.UserRepository;
import com.sww.ddorangddorang.global.util.RedisUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final PrefixRepository prefixRepository;
    private final SuffixRepository suffixRepository;
    private final RedisUtil redisUtil;

    public Integer createRoom(Long userId, RoomInfoReq roomInfoReq) {
        //사용자가 현재 참여중인 방이 있는지
        //선택하지 않은 옵션
        //정상
        log.info("RoomServiceImpl_createRoom start");
        User user = null;

        try {
            user = userRepository.getReferenceById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user.getStatus() != 1L) {
            return -1;
        }

        if (roomInfoReq.getMinMember() < 1
            || roomInfoReq.getMinMember() > roomInfoReq.getMaxMember()) {
            return -1;
        }

        Integer accessCode = generateAccessCode();
        log.info("accessCode = " + accessCode);

        user.updateStatus(2L);
        Room room = Room.builder()
            .admin(user)
            .accessCode(accessCode)
            .minMember(roomInfoReq.getMinMember())
            .maxMember(roomInfoReq.getMaxMember())
            .duration(roomInfoReq.getDuration())
            .build();

        roomRepository.save(room);

        Participant participant = Participant.builder()
            .room(room)
            .user(user)
            .build();

        participantRepository.save(participant);

        log.info("RoomServiceImpl_createRoom end: " + room.getAccessCode());
        return room.getAccessCode();
    }

    private Integer generateAccessCode() {
        Integer accessCode = redisUtil.getAccessCode();

        if (accessCode == -1) {
            Boolean[] accessCodeStatusList = new Boolean[10000];
            List<Room> roomList = roomRepository.findAllByStartedAtAndDeletedAt(null, null);

            for (Room room : roomList) {
                accessCodeStatusList[room.getAccessCode()] = true;
            }

            accessCode = redisUtil.initAccessCode(accessCodeStatusList);
        }

        return accessCode;
    }

    public Boolean joinRoom(Long userId, Integer accessCode) {
        log.info("RoomServiceImpl_joinRoom start: " + userId + " " + accessCode);

        User user = null;

        try {
            user = userRepository.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        //TODO: 참여 중인 방이 이미 있는데 새로운 방에 참가하는 건 안 됨 -> 거절 신호 필요
        if (user.getStatus() != 1L) {
            return false;
        }

        Room room = null;

        try {
            room = roomRepository.findByAccessCodeAndStartedAtAndDeletedAt(accessCode, null,
                null);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        if (room.getHeadCount() >= room.getMaxMember()) {
            return false;
        }

        room.joinMember();

//        TODO: User에서 상태를 변경하는 함수 필요
        user.updateStatus(3L);
        Participant participant = Participant.builder()
            .room(room)
            .user(user)
            .build();

        participantRepository.save(participant);

        log.info("RoomServiceImpl_joinRoom done");
        return true;
    }

    //TODO: 미션 부여는 아직 안 됨
    public void startGame(Room room) {
        List<Participant> participantList = participantRepository.findAllByRoomAndIsWithdrawalAndDeletedAt(
            room, false, null);

        Map<Integer, Participant> indexToParticipant = new HashMap<>();
        Map<Participant, Integer> participantToIndex = new HashMap<>();
        Integer count = 0;
        Integer size = participantList.size() + 1;
        List<Participant> maleList = new ArrayList<>();
        List<Participant> femaleList = new ArrayList<>();
        List<Participant>[] wishManitoList = new List[size];
        Integer[] matchedManito = new Integer[size];
        Boolean[] searched = new Boolean[size];

        for (int i = 0; i < participantList.size(); ++i) {
            Participant participant = participantList.get(i);

            indexToParticipant.put(i + 1, participant);
            participantToIndex.put(participant, i + 1);

            //우선 이성끼리 매칭
            if (participant.getUser().getGender() == 1) {
                maleList.add(participant);
            } else {
                femaleList.add(participant);
            }
        }

        for (int i = 1; i < size; ++i) {
            Participant participant = indexToParticipant.get(i);

            //추후 차단 로직 구현 시 여기서 차단한 사용자는 간선에서 뺴기
            if (participant.getUser().getGender() == 1) {
                wishManitoList[i] = List.copyOf(femaleList);
            } else {
                wishManitoList[i] = List.copyOf(maleList);
            }

            Collections.shuffle(wishManitoList[i]);
        }

        for (int i = 1; i < size; ++i) {
            Arrays.fill(searched, false);
            matchManito(i, searched, matchedManito, wishManitoList[i], participantToIndex);
        }

        Boolean[] matched = new Boolean[size];

        for (int i = 1; i < size; ++i) {
            if (matchedManito[i] > 0) {
                matched[matchedManito[i]] = true;
                ++count;
            }
        }

        List<Participant> unmatchedList = new ArrayList<>();
        //매칭이 되지 않은 회원끼리는 성별과 관계 없이 매칭
        for (int i = 1; i < size; ++i) {
            if (!matched[i]) {
                unmatchedList.add(indexToParticipant.get(i));
            }
        }

        for (int i = 1; i < size; ++i) {
            if (!matched[i]) {
                Participant me = indexToParticipant.get(i);
                wishManitoList[i] = new ArrayList<>();

                //추후 차단한 사용자는 배제하는 로직 추가
                //Set을 활용하면 좋을 것 같음
                for (Participant participant : unmatchedList) {
                    if (!participant.equals(me)) {
                        wishManitoList[i].add(participant);
                    }
                }

                Collections.shuffle(wishManitoList[i]);
            }
        }

        for (int i = 1; i < size; ++i) {
            if (!matched[i]) {
                Arrays.fill(searched, false);
                matchManito(i, searched, matchedManito, wishManitoList[i], participantToIndex);
            }
        }

        count = 0;
        for (int i = 1; i < size; ++i) {
            if (matchedManito[i] > 0) {
                matched[matchedManito[i]] = true;
                ++count;
                Participant manito = indexToParticipant.get(matchedManito[i]);
                Participant maniti = indexToParticipant.get(i);

                manito.matchManiti(maniti);
                maniti.matchManito(manito);
            }
        }

        List<Prefix> prefixList = prefixRepository.findAll();
        List<Suffix> suffixList = suffixRepository.findAll();
        Integer prefixListSize = prefixList.isEmpty() ? 0 : prefixList.size();
        Integer suffixListSize = suffixList.isEmpty() ? 0 : suffixList.size();
        Boolean[][] nicknameUsage = new Boolean[prefixListSize][suffixListSize];

        for (Participant participant : participantList) {
            if (participant.getManito() != null) {
                Integer prefixSelected = -1;
                Integer suffixSelected = -1;

                do {
                    Integer randomNumber = (int) (Math.random() * prefixListSize * suffixListSize);
                    prefixSelected = randomNumber / suffixListSize;
                    suffixSelected = randomNumber % suffixListSize;
                } while (nicknameUsage[prefixSelected][suffixSelected] != true);

                participant.allocateNickname(
                    prefixList.get(prefixSelected) + " " + suffixList.get(suffixSelected));
//                해당 사용자들은 게임을 시작한 상태로 변경
                participant.getUser().updateStatus(4L);
            } else {
//                이 사용자들은 매칭이 되지 않은 관계로 강퇴
                participant.deleteParticipant();
            }
        }

        room.updateHeadCount(count);
        room.startGame();
    }

    Boolean matchManito(Integer current, Boolean[] searched, Integer[] matchedManito,
        List<Participant> wishManitoList, Map<Participant, Integer> participantToIndex) {
        for (Participant participant : wishManitoList) {
            Integer next = participantToIndex.get(participant);

            if (!searched[next]) {
                searched[next] = true;

                if (matchedManito[next] == 0) {
                    matchedManito[next] = current;
                    return true;
                } else if (
                    matchManito(next, searched, matchedManito, wishManitoList, participantToIndex)
                        && matchedManito[current] != next) {
                    matchedManito[next] = current;
                    return true;
                }
            }
        }

        return false;
    }

    public Boolean deleteGame(Long userId) {
        User admin = null;

        try {
            admin = userRepository.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        if (admin.getStatus() != 2L) {
            return false;
        }

        Room room = null;
        try {
            room = roomRepository.findByAdminAndStartedAtAndDeletedAt(admin, null, null);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        List<User> userList = userRepository.findAllByRoom(room);

        for (User user : userList) {
            user.updateStatus(1L);
            user.updateRoom(null);
        }

        room.deleteRoom();

        return true;
    }

    public Boolean withdrawalRoom(Long userId) {
        log.info("RoomServiceImpl_withdrawalRoom start");

        User user = null;

        try {
            user = userRepository.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        if (user.getStatus() != 3L) {
            log.info("RoomServiceImpl_withdrawalRoom end: " + false);
            return false;
        }

        Room room = user.getRoom();

        user.updateStatus(1L);
        user.updateRoom(null);
        room.removeMember();

        log.info("RoomServiceImpl_withdrawalRoom end: " + true);
        return true;
    }

    public Boolean updateRoom(Long userId, RoomInfoReq roomInfoReq) {
        log.info("RoomServiceImpl_updateRoom start");
        User user = null;

        try {
            user = userRepository.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        if (user.getStatus() != 2L) {
            return false;
        }

        if (roomInfoReq.getMinMember() < 1
            || roomInfoReq.getMinMember() > roomInfoReq.getMaxMember()) {
            return false;
        }

        Room room = null;

        try {
            room = roomRepository.findByAdminAndStartedAtAndDeletedAt(user, null, null);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        Integer currentCount = room.getHeadCount();
        //현재 인원보다 최소 인원이 많거나, 현재 인원보다 최대 인원이 적은 경우: false 반환
        if (roomInfoReq.getMinMember() < currentCount
            || currentCount > roomInfoReq.getMaxMember()) {
            return false;
        }

        room.updateRoomInfo(roomInfoReq);

        log.info("RoomServiceImpl_updateRoom end: " + true);
        return true;

    }

    public void checkAndRunIfRoomShouldStart(Long userId) {
        User user = userRepository.getReferenceById(userId);

        Room room = user.getRoom();

        if (room.getHeadCount() == room.getMaxMember()) {
            startGame(room);
        }
    }

    public List<ShowUsersRes> showUsers(Long userId) {
        log.info("RoomServiceImpl_showUsers start");

        User user = null;
        List<ShowUsersRes> showUsersResList = new ArrayList<>();

        try {
            user = userRepository.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        if (user.getStatus() != 1L) {
            Room room = user.getRoom();
            List<User> userList = userRepository.findAllByRoom(room);

            for (User registedUser : userList) {
                ShowUsersRes showUsersRes = ShowUsersRes.builder()
                    .name(registedUser.getName())
                    .generation(registedUser.getGeneration())
                    .classes(registedUser.getGeneration())
                    .profileImage(registedUser.getProfileImage())
                    .build();

                showUsersResList.add(showUsersRes);
            }
        }

        log.info("RoomServiceImpl_showUsers end");
        return showUsersResList;
    }
}
