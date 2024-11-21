-- 관리자 계정
INSERT INTO member (member_id, login_id, password, name, email, phone_number, address, birth_date, gender, role, status)
VALUES (1, 'admin', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '관리자', 'admin@spomatch.com', '01012345678', '서울시 강남구', '1990-01-01', 'M', 'ADMIN', 'ACTIVE');

-- 일반 사용자 계정들
INSERT INTO member (member_id, login_id, password, name, email, phone_number, address, birth_date, gender, role, status)
VALUES
    (2, 'user01', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '김철수', 'user01@gmail.com', '01011112222', '서울시 서초구', '1992-03-15', 'M', 'USER', 'ACTIVE'),
    (3, 'user02', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '이영희', 'user02@gmail.com', '01022223333', '서울시 강서구', '1995-07-22', 'F', 'USER', 'ACTIVE'),
    (4, 'user03', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '박지민', 'user03@gmail.com', '01033334444', '경기도 성남시', '1988-12-05', 'M', 'USER', 'ACTIVE'),
    (5, 'user04', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '최수진', 'user04@gmail.com', '01044445555', '인천시 부평구', '1993-09-30', 'F', 'USER', 'ACTIVE'),
    (6, 'user05', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '정민수', 'user05@gmail.com', '01055556666', '부산시 해운대구', '1991-05-17', 'M', 'USER', 'ACTIVE'),
    (7, 'user06', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '강미라', 'user06@gmail.com', '01066667777', '대구시 수성구', '1994-11-08', 'F', 'USER', 'ACTIVE'),
    (8, 'user07', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '조현우', 'user07@gmail.com', '01077778888', '광주시 서구', '1987-08-25', 'M', 'USER', 'ACTIVE'),
    (9, 'user08', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '윤서연', 'user08@gmail.com', '01088889999', '대전시 유성구', '1996-02-14', 'F', 'USER', 'ACTIVE'),
    (10, 'user09', '$2a$10$vDX8vXz8pf3dFzQsGpJW6exQr2OBrDzTASd2Rk27zWpnY97jxfyKm', '송재현', 'user09@gmail.com', '01099990000', '울산시 남구', '1989-04-03', 'M', 'USER', 'ACTIVE');