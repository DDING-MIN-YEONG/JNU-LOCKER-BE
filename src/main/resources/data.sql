-- Organization 데이터 (단과대학 및 위원회)
INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (1, '농업생명과학대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (2, '사회과학대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (3, '수의과대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (4, '치의학전문대학원', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (5, '경영대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (6, '인문대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (7, 'AI융합대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (8, '본부직할', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (9, '사범대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (10, '예술대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (11, '공과대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (12, '간호대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (13, '의과대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (14, '생활과학대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (15, '약학대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (16, '자연과학대학', 'COUNCIL', NOW(), NOW());

INSERT INTO organization (id, name, type, created_at, updated_at)
VALUES (17, '도서관자치위원회', 'COMMITTEE', NOW(), NOW());

-- Department 데이터 (각 단과대학에 대한 '해당없음' 부서와 학과)
-- 농업생명과학대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (1, 1, '해당없음', '농업생명과학대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (2, 1, '응용식물학과', '응용식물학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (3, 1, '원예생명공학과', '원예생명공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (4, 1, '응용생물학과', '응용생물학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (5, 1, '산림자원학과', '산림자원학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (6, 1, '임산공학과', '임산공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (7, 1, '농생명화학과', '농생명화학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (8, 1, '식품공학과', '식품공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (9, 1, '분자생명공학과', '분자생명공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (10, 1, '동물자원학부', '동물자원학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (11, 1, '동물자원학부-동물자원과학전공', '동물자원학부-동물자원과학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (12, 1, '동물자원학부-동물생명과학전공', '동물자원학부-동물생명과학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (13, 1, '조경학과', '조경학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (14, 1, '농업경제학과', '농업경제학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (15, 1, '바이오에너지공학과', '바이오에너지공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (16, 1, '지역·바이오시스템공학과', '지역·바이오시스템공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (17, 1, '융합바이오시스템기계공학과', '융합바이오시스템기계공학과 학생회', null, null, NOW(), NOW());

-- 사회과학대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (18, 2, '해당없음', '사회과학대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (19, 2, '정치외교학과', '정치외교학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (20, 2, '행정학과', '행정학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (21, 2, '사회학과', '사회학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (22, 2, '심리학과', '심리학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (23, 2, '문헌정보학과', '문헌정보학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (24, 2, '미디어커뮤니케이션학과', '미디어커뮤니케이션학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (25, 2, '지리학과', '지리학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (26, 2, '문화인류고고학과', '문화인류고고학과 학생회', null, null, NOW(), NOW());

-- 수의과대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (27, 3, '해당없음', '수의과대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (28, 3, '수의예과', '수의예과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (29, 3, '수의학과', '수의학과 학생회', null, null, NOW(), NOW());

-- 치의학전문대학원 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (30, 4, '해당없음', '치의학전문대학원 학생회', null, null, NOW(), NOW());

-- 경영대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (31, 5, '해당없음', '경영대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (32, 5, '경영학부', '경영학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (33, 5, '경영학부-경영학전공', '경영학부-경영학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (34, 5, '경영학부-회계학전공', '경영학부-회계학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (35, 5, '경제학부', '경제학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (36, 5, '경제학부-경제학전공', '경제학부-경제학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (37, 5, '경제학부-지역개발학전공', '경제학부-지역개발학전공 학생회', null, null, NOW(), NOW());

-- 인문대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (38, 6, '해당없음', '인문대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (39, 6, '국어국문학과', '국어국문학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (40, 6, '불어불문학과', '불어불문학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (41, 6, '영어영문학과', '영어영문학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (42, 6, '중어중문학과', '중어중문학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (43, 6, '독일언어문학과', '독일언어문학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (44, 6, '사학과', '사학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (45, 6, '일어일문학과', '일어일문학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (46, 6, '철학과', '철학과 학생회', null, null, NOW(), NOW());

-- AI융합대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (47, 7, '해당없음', 'AI융합대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (48, 7, '인공지능학부', '인공지능학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (49, 7, '인공지능학부-인공지능전공', '인공지능학부-인공지능전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (50, 7, '인공지능학부-소프트웨어전공', '인공지능학부-소프트웨어전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (51, 7, '인공지능학부-정보보안전공', '인공지능학부-정보보안전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (52, 7, '빅데이터융합학과', '빅데이터융합학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (53, 7, '미래모빌리티학과', '미래모빌리티학과 학생회', null, null, NOW(), NOW());

-- 본부직할 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (54, 8, '해당없음', '본부직할 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (55, 8, '자율전공학부', '자율전공학부 학생회', null, null, NOW(), NOW());

-- 사범대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (56, 9, '해당없음', '사범대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (57, 9, '가정교육과', '가정교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (58, 9, '교육학과', '교육학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (59, 9, '국어교육과', '국어교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (60, 9, '물리교육과', '물리교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (61, 9, '생물교육과', '생물교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (62, 9, '수학교육과', '수학교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (63, 9, '역사교육과', '역사교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (64, 9, '영어교육과', '영어교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (65, 9, '유아교육과', '유아교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (66, 9, '윤리교육과', '윤리교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (67, 9, '음악교육과', '음악교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (68, 9, '지구과학교육과', '지구과학교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (69, 9, '지리교육과', '지리교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (70, 9, '체육교육과', '체육교육과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (71, 9, '특수교육학부', '특수교육학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (72, 9, '특수교육학부-초등특수교육전공', '특수교육학부-초등특수교육전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (73, 9, '특수교육학부-중등특수교육전공', '특수교육학부-중등특수교육전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (74, 9, '특수교육학부-유아특수교육전공', '특수교육학부-유아특수교육전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (75, 9, '화학교육과', '화학교육과 학생회', null, null, NOW(), NOW());

-- 예술대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (76, 10, '해당없음', '예술대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (77, 10, '국악학과', '국악학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (78, 10, '디자인학과', '디자인학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (79, 10, '미술학과', '미술학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (80, 10, '음악학과', '음악학과 학생회', null, null, NOW(), NOW());

-- 공과대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (81, 11, '해당없음', '공과대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (82, 11, '건축학부', '건축학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (83, 11, '건축학부-건축공학전공', '건축학부-건축공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (84, 11, '건축학부-건축·도시설계전공', '건축학부-건축·도시설계전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (85, 11, '고분자융합소재공학부', '고분자융합소재공학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (86, 11, '고분자융합소재공학부-고분자공학전공', '고분자융합소재공학부-고분자공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (87, 11, '고분자융합소재공학부-융합섬유공학전공', '고분자융합소재공학부-융합섬유공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (88, 11, '기계공학부', '기계공학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (89, 11, '기계공학부-기계공학전공', '기계공학부-기계공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (90, 11, '기계공학부-자동차공학전공', '기계공학부-자동차공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (91, 11, '산업공학과', '산업공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (92, 11, '생물공학과', '생물공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (93, 11, '신소재공학부', '신소재공학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (94, 11, '신소재공학부-금속재료공학전공', '신소재공학부-금속재료공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (95, 11, '신소재공학부-에너지나노재료전공', '신소재공학부-에너지나노재료전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (96, 11, '신소재공학부-광·전자재료전공', '신소재공학부-광·전자재료전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (97, 11, '에너지자원공학과', '에너지자원공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (98, 11, '전기공학과', '전기공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (99, 11, '전자컴퓨터공학부', '전자컴퓨터공학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (100, 11, '전자컴퓨터공학부-전자공학전공', '전자컴퓨터공학부-전자공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (101, 11, '전자컴퓨터공학부-컴퓨터정보통신공학전공', '전자컴퓨터공학부-컴퓨터정보통신공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (102, 11, '전자컴퓨터공학부-시스템반도체공학전공', '전자컴퓨터공학부-시스템반도체공학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (103, 11, '전자공학과', '전자공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (104, 11, '컴퓨터정보통신공학과', '컴퓨터정보통신공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (105, 11, '소프트웨어공학과', '소프트웨어공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (106, 11, '토목공학과', '토목공학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (107, 11, '화학공학부', '화학공학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (108, 11, '화학공학부-화공소재전공', '화학공학부-화공소재전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (109, 11, '화학공학부-화공안전전공', '화학공학부-화공안전전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (110, 11, '화학공학부-화학공정전공', '화학공학부-화학공정전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (111, 11, '환경에너지공학과', '환경에너지공학과 학생회', null, null, NOW(), NOW());

-- 간호대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (112, 12, '해당없음', '간호대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (113, 12, '간호학과', '간호학과 학생회', null, null, NOW(), NOW());

-- 의과대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (114, 13, '해당없음', '의과대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (115, 13, '의예과', '의예과 학생회', null, null, NOW(), NOW());

-- 생활과학대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (116, 14, '해당없음', '생활과학대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (117, 14, '생활복지학과', '생활복지학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (118, 14, '식품영양과학부', '식품영양과학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (119, 14, '식품영양과학부-식품학전공', '식품영양과학부-식품학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (120, 14, '식품영양과학부-영양학전공', '식품영양과학부-영양학전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (121, 14, '의류학과', '의류학과 학생회', null, null, NOW(), NOW());

-- 약학대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (122, 15, '해당없음', '약학대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (123, 15, '약학부', '약학부 학생회', null, null, NOW(), NOW());

-- 자연과학대학 departments
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (124, 16, '해당없음', '자연과학대학 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (125, 16, '물리학과', '물리학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (126, 16, '생명과학기술학부', '생명과학기술학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (127, 16, '생물학과', '생물학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (128, 16, '수학과', '수학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (129, 16, '지구환경과학부', '지구환경과학부 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (130, 16, '지구환경과학부-지질환경전공', '지구환경과학부-지질환경전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (131, 16, '지구환경과학부-해양환경전공', '지구환경과학부-해양환경전공 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (132, 16, '통계학과', '통계학과 학생회', null, null, NOW(), NOW());

INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (133, 16, '화학과', '화학과 학생회', null, null, NOW(), NOW());

-- 도서관자치위원회 department
INSERT INTO department (id, organization_id, name, nickname, email, phone_number, created_at, updated_at)
VALUES (134, 17, '해당없음', '도서관자치위원회', null, null, NOW(), NOW());
