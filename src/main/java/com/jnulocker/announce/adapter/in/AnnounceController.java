package com.jnulocker.announce.adapter.in;

import com.jnulocker.announce.adapter.in.docs.AnnounceApi;
import com.jnulocker.announce.application.port.in.AnnounceCommand;
import com.jnulocker.announce.application.port.in.AnnounceQuery;
import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.AnnouncePageable;
import com.jnulocker.announce.application.port.in.response.MyAnnounceResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/announces")
public class AnnounceController implements AnnounceApi {

    private final AnnounceCommand announceCommand;
    private final AnnounceQuery announceQuery;

    @Override
    @PostMapping
    public ResponseEntity<Void> createAnnounce(@Valid @RequestBody CreateAnnounceRequest request) {
        announceCommand.createAnnounce(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping
    public ResponseEntity<AnnounceCustomPage> getAnnounces(
            @Valid @ParameterObject AnnouncePageable announcePageable) {
        Pageable pageable = announcePageable.toPageable();
        return ResponseEntity.ok(announceQuery.getAllAnnounces(pageable));
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<List<MyAnnounceResponse>> getMyAnnounces() {
        return ResponseEntity.ok(announceQuery.getMyAnnounces());
    }

    @DeleteMapping("/{announce-id}")
    public ResponseEntity<Void> deleteAnnounce(@PathVariable("announce-id") Long announceId) {
        announceCommand.deleteAnnounce(announceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
