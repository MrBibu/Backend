-- Demo Data for AcademiaGo database
-- Assuming initial AUTO_INCREMENT values will be 1 for all tables as they are freshly created.
-- Password for all users is 'password' (BCrypt hash: $2a$10$sWtyy65jwlde/3rcNbYZ3eqpf8AtEwCrMkYPUOVDL2qWnbJInQUvy)

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;

-- 1. Faculty
INSERT INTO `faculty` (`id`, `name`) VALUES
(1, 'Faculty of Science and Technology'),
(2, 'Faculty of Management');

-- 2. Programs
INSERT INTO `programs` (`id`, `name`, `faculty_id`) VALUES
(1, 'Bachelor of Computer Applications (BCA)', 1),
(2, 'Bachelor of Science in Computer Science and Information Technology (BSc. CSIT)', 1),
(3, 'Bachelor of Business Administration (BBA)', 2);

-- 3. Semesters
INSERT INTO `semester` (`id`, `number`, `program_id`) VALUES
(1, 1, 1), (2, 2, 1), (3, 3, 1), (4, 4, 1), (5, 5, 1), (6, 6, 1), (7, 7, 1), (8, 8, 1),
(9, 1, 2), (10, 2, 2),
(11, 1, 3), (12, 2, 3);

-- 4. Users
INSERT INTO `users` (`id`, `enabled`, `first_login`, `temp_password`, `created_at`, `username`, `email`, `password`, `role`) VALUES
-- Admin
(1, b'1', b'0', b'0', '2024-05-01 10:00:00.000000', 'admin', 'admin@academiago.com', '$2a$10$sWtyy65jwlde/3rcNbYZ3eqpf8AtEwCrMkYPUOVDL2qWnbJInQUvy', 'ADMIN'),
-- Teachers
(2, b'1', b'0', b'0', '2024-05-01 10:00:00.000000', 'teacher_ram', 'ram.teacher@academiago.com', '$2a$10$sWtyy65jwlde/3rcNbYZ3eqpf8AtEwCrMkYPUOVDL2qWnbJInQUvy', 'TEACHER'),
(3, b'1', b'0', b'0', '2024-05-01 10:00:00.000000', 'teacher_sita', 'sita.teacher@academiago.com', '$2a$10$sWtyy65jwlde/3rcNbYZ3eqpf8AtEwCrMkYPUOVDL2qWnbJInQUvy', 'TEACHER'),
-- Students
(4, b'1', b'0', b'0', '2024-05-02 10:00:00.000000', 'student_hari', 'hari.student@academiago.com', '$2a$10$sWtyy65jwlde/3rcNbYZ3eqpf8AtEwCrMkYPUOVDL2qWnbJInQUvy', 'STUDENT'),
(5, b'1', b'0', b'0', '2024-05-02 10:00:00.000000', 'student_gita', 'gita.student@academiago.com', '$2a$10$sWtyy65jwlde/3rcNbYZ3eqpf8AtEwCrMkYPUOVDL2qWnbJInQUvy', 'STUDENT');

-- 5. Teacher Profiles (Using user_id)
INSERT INTO `teacher_profile` (`user_id`, `status`, `created_at`, `dob`, `contact_no`, `email`, `employee_id`, `name`, `permanent_address`, `qualifications`, `temporary_address`, `gender`) VALUES
(2, b'1', '2024-05-01 10:00:00.000000', '1985-06-15 00:00:00.000000', '9800000001', 'ram.teacher@academiago.com', 'EMP001', 'Ram Kumar Sharma', 'Kathmandu, Nepal', 'M.Sc. Computer Science', 'Lalitpur, Nepal', 'MALE'),
(3, b'1', '2024-05-01 10:00:00.000000', '1988-08-20 00:00:00.000000', '9800000002', 'sita.teacher@academiago.com', 'EMP002', 'Sita Devi', 'Pokhara, Nepal', 'MBA', 'Kathmandu, Nepal', 'FEMALE');

-- 6. Student Profiles
INSERT INTO `student_profile` (`id`, `user_id`, `date_of_birth`, `program_id`, `roll_number`, `semester_id`, `batch_year`, `full_name`, `permanent_address`, `temporary_address`, `gender`) VALUES
(1, 4, '2001-02-14', 1, 101, 1, '2024', 'Hari Bahadur', 'Bhaktapur, Nepal', 'Kathmandu, Nepal', 'MALE'),
(2, 5, '2002-05-22', 1, 102, 1, '2024', 'Gita Kumari', 'Chitwan, Nepal', 'Lalitpur, Nepal', 'FEMALE');

-- 7. Subjects
INSERT INTO `subjects` (`id`, `semester_id`, `teacher_id`, `code`, `name`) VALUES
(1, 1, 2, 'CACS101', 'Computer Fundamentals and Applications'),
(2, 1, 2, 'CACS102', 'Society and Technology'),
(3, 1, 3, 'CAEN103', 'English I');

-- 8. Assignments
INSERT INTO `assignments` (`id`, `due_date`, `subject_id`, `teacher_id`, `title`) VALUES
(1, '2025-06-10 23:59:00.000000', 1, 2, 'CFA Chapter 1 Notes Submission'),
(2, '2025-06-15 23:59:00.000000', 3, 3, 'English Essay on Society');

-- 9. Submissions
INSERT INTO `submissions` (`id`, `assignment_id`, `student_id`, `submitted_at`, `status`) VALUES
(1, 1, 1, '2025-06-05 14:30:00.000000', 'SUBMITTED'),
(2, 1, 2, '2025-06-08 10:15:00.000000', 'GRADED');

-- 10. Course Materials
INSERT INTO `course_materials` (`id`, `subject_id`, `uploaded_at`, `uploaded_by`, `fileurl`) VALUES
(1, 1, '2024-05-15 08:00:00.000000', 2, 'https://example.com/materials/cfa_chapter1.pdf'),
(2, 3, '2024-05-16 09:00:00.000000', 3, 'https://example.com/materials/english_grammar.pdf');

-- 11. Notices
INSERT INTO `notices` (`id`, `created_at`, `faculty_id`, `program_id`, `semester_id`, `title`, `visible_to`) VALUES
(1, '2024-05-10 10:00:00.000000', NULL, NULL, NULL, 'Welcome to the New Academic Session', 'ALL'),
(2, '2024-05-12 11:00:00.000000', 1, 1, 1, 'First Semester Orientation Tomorrow', 'STUDENTS');

-- 12. Questions (Q&A Forum)
INSERT INTO `questions` (`id`, `created_at`, `student_id`, `subject_id`, `teacher_id`, `text`, `status`) VALUES
(1, '2024-05-20 14:00:00.000000', 1, 1, NULL, 'Can anyone clarify the difference between SRAM and DRAM?', 'OPEN'),
(2, '2024-05-21 16:30:00.000000', 2, 3, NULL, 'What is the syllabus for the first internal exam?', 'ANSWERED');

-- 13. Answers
INSERT INTO `answers` (`id`, `created_at`, `question_id`, `teacher_id`, `text`) VALUES
(1, '2024-05-22 09:00:00.000000', 2, 3, 'The syllabus includes chapters 1, 2, and 3. Please refer to the course material uploaded perfectly.');

-- 14. Notifications
INSERT INTO `notification` (`id`, `is_read`, `created_at`, `user_id`, `message`) VALUES
(1, b'0', '2024-05-20 08:00:00.000000', 4, 'Your assignment has been graded.'),
(2, b'0', '2024-05-22 09:05:00.000000', 5, 'A new notice has been posted.');

COMMIT;
