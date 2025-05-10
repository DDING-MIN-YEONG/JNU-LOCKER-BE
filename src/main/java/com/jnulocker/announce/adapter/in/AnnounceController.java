package com.jnulocker.announce.adapter.in;

import com.jnulocker.announce.adapter.in.docs.AnnounceApi;
import com.jnulocker.announce.application.port.in.AnnounceCommand;
import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/announces")
public class AnnounceController implements AnnounceApi {

    private final AnnounceCommand announceCommand;

    @Override
    @PostMapping
    public ResponseEntity<Void> createAnnounce(@Valid @RequestBody CreateAnnounceRequest request) {
        announceCommand.createAnnounce(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
