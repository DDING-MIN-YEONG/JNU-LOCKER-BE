package com.jnulocker.announce.application.port.in;

import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import com.jnulocker.announce.application.port.in.request.UpdateAnnounceRequest;

public interface AnnounceCommand {
    void createAnnounce(CreateAnnounceRequest request);

    void deleteAnnounce(Long announceId);

    void updateAnnounce(Long announceId, UpdateAnnounceRequest request);
}
