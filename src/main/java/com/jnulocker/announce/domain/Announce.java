package com.jnulocker.announce.domain;

import com.jnulocker.announce.exception.OnlyManagerCanDeleteAnnounceException;
import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.organization.domain.Department;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announce extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announce_id")
    private Long id;

    private String title;

    private String content;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "announce", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<AnnounceParticipation> announceParticipations = new ArrayList<>();

    public static Announce create(
            String title, String content, String writer, Department department) {
        return Announce.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .department(department)
                .build();
    }

    public void validateDeletable(Department department) {
        if (!this.department.equals(department)) {
            throw OnlyManagerCanDeleteAnnounceException.EXCEPTION;
        }
    }
}
