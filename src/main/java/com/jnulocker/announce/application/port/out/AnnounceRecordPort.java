package com.jnulocker.announce.application.port.out;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import java.util.List;

public interface AnnounceRecordPort {
    Announce saveAnnounce(Announce announce);

    void saveAnnounceParticipations(List<AnnounceParticipation> announceParticipations);
}
