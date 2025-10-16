package com.jnulocker.ai.domain;

import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_documents")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // 저장된 파일명

    @Column(nullable = false)
    private String originalFileName; // 원본 파일명

    @Column(nullable = false)
    private Long fileSize; // bytes

    @Column(nullable = false)
    private String fileType; // PDF, TXT

    private String category; // 선택적 카테고리

    @Column(nullable = false)
    private Integer chunkCount; // Vector Store에 저장된 청크 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private Member uploadedBy; // 업로드한 회원

    @Column(columnDefinition = "TEXT")
    private String vectorStoreIds; // JSON array: ["id1", "id2", ...]

    public static AiDocument create(
            String fileName,
            String originalFileName,
            Long fileSize,
            String fileType,
            String category,
            Integer chunkCount,
            Member uploadedBy,
            String vectorStoreIds) {
        return AiDocument.builder()
                .fileName(fileName)
                .originalFileName(originalFileName)
                .fileSize(fileSize)
                .fileType(fileType)
                .category(category)
                .chunkCount(chunkCount)
                .uploadedBy(uploadedBy)
                .vectorStoreIds(vectorStoreIds)
                .build();
    }
}
