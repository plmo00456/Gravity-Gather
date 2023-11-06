package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.service.RoomService;
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
    @ApiOperation(value = "룸 가져오는 api")
    public ResponseEntity<?> getRoom() {
        List<Room> rooms = roomService.getRooms();
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomResponses, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ApiOperation(value = "룸 생성 api")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest roomRequest) {
        roomService.createRoom(roomRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test")
    public void test(@RequestParam String test) throws IOException {
        webSocketHandler.sendMessageToAll(test);
    }
}
