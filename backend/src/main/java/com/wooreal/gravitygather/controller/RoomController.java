package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.service.RoomService;
import com.wooreal.gravitygather.utils.SHA256Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Room Controller", description = "룸 컨트롤러")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;
    private final WebSocketHandler webSocketHandler;

    public RoomController(RoomService roomService, WebSocketHandler webSocketHandler) {
        this.roomService = roomService;
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/get")
    @ApiOperation(value = "방 가져오는 api")
    public ResponseEntity<?> getRoom() {
        List<Room> rooms = roomService.getRooms();
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomResponses, HttpStatus.OK);
    }

    @PostMapping("/enter")
    @ApiOperation(value = "방 입장 api")
    public ResponseEntity<?> canEnterRoom( @RequestBody RoomRequest roomRequest) {
        RoomResponse room = new RoomResponse(roomService.canEnterRoom(roomRequest));
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ApiOperation(value = "방 생성 api")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest roomRequest) {
        roomService.createRoom(roomRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{roomId}/participants")
    @ApiOperation(value = "방 참여자 가져오는 api")
    public ResponseEntity<?> getRoomParticipants(@PathVariable("roomId") int roomId) {
        List<UserResponse> participants = roomService.getRoomParticipants(roomId);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    // 인증
    @PostMapping("/{roomId}/delete")
    @ApiOperation(value = "방 삭제 api")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") int roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test")
    public void test(@RequestParam String test) throws IOException {
        String salt = SHA256Util.generateSalt();
        String hashed = SHA256Util.generateHashWithSalt(test, salt);
        System.out.println(hashed);
    }

}
