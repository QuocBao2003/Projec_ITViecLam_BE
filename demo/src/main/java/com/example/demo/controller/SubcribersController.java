package com.example.demo.controller;


import com.example.demo.domain.Subsciber;
import com.example.demo.service.SubscribersService;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class SubcribersController {
    private final SubscribersService subscribersService;

    public SubcribersController(SubscribersService subscribersService) {
        this.subscribersService = subscribersService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("create a subcribers")
    public ResponseEntity<Subsciber> createSubscriber(@Valid @RequestBody Subsciber subsciber) throws IdInvalidException {
//        check email
        boolean isEmailExits = this.subscribersService.existsByEmail(subsciber.getEmail());
        if(isEmailExits == true){
            throw new IdInvalidException("Email đã tồn tại");
        }
        return ResponseEntity.ok().body(this.subscribersService.createSubsciber(subsciber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("update subcribers")
    public ResponseEntity<Subsciber> updateSubscriber(@Valid @RequestBody Subsciber subsciber) throws IdInvalidException {
        Subsciber currentSubsciberDB = this.subscribersService.getSubsciberById(subsciber.getId());
        if (currentSubsciberDB ==null){
            throw new IdInvalidException("Subscibers không tồn tại");
        }
        return ResponseEntity.ok().body(this.subscribersService.updateSubsciber(currentSubsciberDB,subsciber));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscribers's skill")
    public ResponseEntity<Subsciber> getSubscribersSkill() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        return ResponseEntity.ok().body(this.subscribersService.findByEmail(email));
    }
}
