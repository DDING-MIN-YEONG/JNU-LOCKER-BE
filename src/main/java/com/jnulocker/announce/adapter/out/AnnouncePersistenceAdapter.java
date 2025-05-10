package com.jnulocker.announce.adapter.out;

import com.jnulocker.announce.application.port.out.AnnounceRecordPort;
import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.common.annotation.PersistenceAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnnouncePersistenceAdapter implements AnnounceRecordPort {
    private final AnnounceRepository announceRepository;
    private final AnnounceParticipationRepository announceParticipationRepository;

    @Override
    public Announce saveAnnounce(Announce announce) {
        return announceRepository.save(announce);
    }

    @Override
    public void saveAnnounceParticipations(List<AnnounceParticipation> announceParticipations) {
        announceParticipationRepository.saveAll(announceParticipations);
    }
}
