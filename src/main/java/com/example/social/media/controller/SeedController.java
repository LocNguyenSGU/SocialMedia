package com.example.social.media.controller;

import com.example.social.media.service.SeedService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class SeedController {

    SeedService service;

    @PostMapping("/clear")
    public ResponseEntity<String> clear(){
        service.clearSeeds();
        return ResponseEntity.ok("Xóa thành công");
    }


    @PostMapping("/default")
    public ResponseEntity<String> defaultMock (){
        service.defaultSeeds();
        return ResponseEntity.ok("tạo mặc định thành công");
    }

    @PostMapping("/seeds")
    public ResponseEntity<String> seeds (){
        service.mockSeeds();
        return ResponseEntity.ok("tạo thành công");
    }

}
