package com.jnulocker.announce.application.port.in;

import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;

public interface AnnounceCommand {
    void createAnnounce(CreateAnnounceRequest request);

    void deleteAnnounce(Long announceId);
}
