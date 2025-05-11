package com.jnulocker.announce.adapter.out;

import com.jnulocker.announce.application.port.out.AnnounceLoadPort;
import com.jnulocker.announce.application.port.out.AnnounceRecordPort;
import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.organization.domain.Department;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnnouncePersistenceAdapter implements AnnounceRecordPort, AnnounceLoadPort {
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

    @Override
    public Page<Announce> getAllAnnouncesByDepartment(Department department, Pageable pageable) {
        return announceRepository.findAllByDepartment(department, pageable);
    }

    @Override
    public List<Announce> getAnnouncesByParticipationDepartment(Department department) {
        return announceRepository.findAllByAnnounceParticipations_Department(department);
    }

    @Override
    public Optional<Announce> getById(Long announceId) {
        return announceRepository.findById(announceId);
    }

    @Override
    public void deleteAnnounce(Announce announce) {
        announceParticipationRepository.deleteAllByAnnounce(announce);
        announceRepository.delete(announce);
    }

    @Override
    public void deleteAnnounceParticipationsByAnnounce(Announce announce) {
        announceParticipationRepository.deleteAllByAnnounce(announce);
    }
}
