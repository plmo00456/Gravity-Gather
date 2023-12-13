package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.LoginRequired;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.service.RoomService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(info = @Info(title = "Room Controller", version = "v1", description = "미팅 룸 컴트롤러"))
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/get")
    @Operation(summary = "미팅 룸 점보 조회", description = "모든 미팅 방 을 조회합니다.")
    public ResponseEntity<?> getRoom() {
        List<Room> rooms = roomService.getRooms();
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomResponses, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/enter")
    @Operation(summary = "방 입장 api")
    public ResponseEntity<?> canEnterRoom(
        @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody RoomRequest roomRequest ) {
        RoomResponse room = new RoomResponse(roomService.canEnterRoom(roomRequest));
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/create")
    @Operation(summary = "방 생성 api")
    public ResponseEntity<?> createRoom(
        @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody RoomRequest roomRequest) {
        RoomResponse room = roomService.createRoom(roomRequest);

        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/{roomId}/get")
    @Operation(summary = "특정 방 정보 조회 api")
    public ResponseEntity<?> getRoom(@PathVariable("roomId") int roomId) {
        RoomResponse room = new RoomResponse(roomService.getRoomBySeq(roomId));
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping("/{roomId}/participants")
    @Operation(summary = "방 참여자 가져오는 api")
    public ResponseEntity<?> getRoomParticipants(@PathVariable("roomId") int roomId) {
        List<UserResponse> participants = roomService.getRoomParticipants(roomId);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/{roomId}/delete")
    @Operation(summary = "방 삭제 api")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") int roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/isInTheRoom")
    @Operation(summary = "현재 방 안에 있는지 확인 api")
    public ResponseEntity<?> isInTheRoom() {
        UserResponse room = roomService.isInTheRoom();
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/kick")
    @Operation(summary = "추방 api")
    public ResponseEntity<?> kick(@RequestBody RoomRequest roomRequest) {
        roomService.kick(roomRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/out")
    @Operation(summary = "방 나가기 api")
    public ResponseEntity<?> out() {
        roomService.outTheRoom();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
