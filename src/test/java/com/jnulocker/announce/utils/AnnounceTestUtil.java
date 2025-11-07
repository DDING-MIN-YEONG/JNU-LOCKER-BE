package com.jnulocker.announce.utils;

import static announce.domain.AnnounceTestDataBuilder.announceBuilder;
import static organization.domain.DepartmentTestDataBuilder.departmentBuilder;
import static organization.domain.OrganizationTestDataBuilder.organizationBuilder;

import com.jnulocker.announce.adapter.out.AnnounceParticipationRepository;
import com.jnulocker.announce.adapter.out.AnnounceRepository;
import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.organization.adapter.out.DepartmentRepository;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnnounceTestUtil {

    @Autowired private AnnounceRepository announceRepository;

    @Autowired private AnnounceParticipationRepository announceParticipationRepository;

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private DepartmentRepository departmentRepository;

    public void createAnnounceWithParticipationDepartment(Department department) {
        // 공지사항 생성 및 저장
        Announce savedAnnounce = createAndSaveAnnounce(department);

        // 공지사항 참여 학과 정보 생성 및 저장
        AnnounceParticipation announceParticipation =
                AnnounceParticipation.create(savedAnnounce, department);
        announceParticipationRepository.save(announceParticipation);
    }

    private Announce createAndSaveAnnounce(Department department) {

        Announce announce = announceBuilder().withDepartment(department).build();
        return announceRepository.save(announce);
    }

    public void deleteAll() {
        announceRepository.deleteAll();
        announceParticipationRepository.deleteAll();
    }

    public void createAnnounce() {
        // 조직 생성 및 저장
        Organization organization = organizationBuilder().build();
        Organization savedOrganization = organizationRepository.save(organization);

        // 학과 생성 및 저장
        Department department = departmentBuilder().withOrganization(savedOrganization).build();
        Department savedDepartment = departmentRepository.save(department);

        // 공지사항 생성 및 저장
        createAndSaveAnnounce(savedDepartment);
    }
}
