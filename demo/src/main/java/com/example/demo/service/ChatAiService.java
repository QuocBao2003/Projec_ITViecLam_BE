package com.example.demo.service;


import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ChatAiService {
    private final OllamaChatModel ollamaChatModel;
    private final JdbcTemplate jdbcTemplate;

    // Danh sách các kỹ năng được hỗ trợ
    private static final Set<String> SUPPORTED_SKILLS = Set.of(
            "REACT.JS", "REACT NATIVE", "VUE.JS", "ANGULAR",
            "NEST.JS", "TYPESCRIPT", "JAVA", "FRONTEND",
            "BACKEND", "FULLSTACK", "JAVA SPRING", "NODE.JS",
            "SPRING BOOT", "PYTHON", "C#", ".NET"
    );

    public ChatAiService(OllamaChatModel ollamaChatModel, JdbcTemplate jdbcTemplate) {
        this.ollamaChatModel = ollamaChatModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    public String processQuestionToJson(String question) {


        // Kiểm tra xem câu hỏi có liên quan đến tìm việc theo kỹ năng không
        Optional<String> detectedSkill = detectSkill(question);
        String jobInfo = "";

        if (detectedSkill.isPresent()) {
            String skillName = detectedSkill.get();
            jobInfo = getJobInfoBySkill(skillName);
        }

        // Tạo prompt cho AI bao gồm cả thông tin job nếu có
        String prompt = createPrompt(question, jobInfo);

        try {
            ChatResponse response = ollamaChatModel.call(new Prompt(prompt));
            String answer = response.getResult().getOutput().getText().trim();
            return formatAnswer(answer);
        } catch (Exception e) {
            return "Lỗi khi xử lý câu hỏi: " + e.getMessage();
        }

    }

    private String createPrompt(String question, String jobInfo) {
        if (!jobInfo.isEmpty()) {
            return """
                Bạn là một trợ lý AI thông minh chuyên về tuyển dụng và công nghệ. 
                Hãy trả lời bằng tiếng Việt, với văn phong thân thiện, chuyên nghiệp.
                
                Câu hỏi: "%s"
                
                Dưới đây là một số công việc liên quan:
                %s
                
                Hãy trả lời câu hỏi và gợi ý các công việc trên nếu phù hợp.
                Viết ngắn gọn trong 3-5 câu.
                """.formatted(question.replace("\"", "\\\""), jobInfo);
        } else {
            return """
                Bạn là một trợ lý AI thông minh. 
                Hãy trả lời bằng tiếng Việt, với văn phong thân thiện, rõ ràng, dễ hiểu.
                
                Câu hỏi: "%s"
                
                Trả lời ngắn gọn trong 3-4 câu.
                """.formatted(question.replace("\"", "\\\""));
        }
    }

    private String getJobInfoBySkill(String skillName) {
        try {
            String sql = "SELECT DISTINCT j.name as job_name " +
                    "FROM jobs j " +
                    "JOIN job_skill js ON j.id = js.job_id " +
                    "JOIN skills s ON js.skill_id = s.id " +
                    "WHERE LOWER(s.name) LIKE LOWER(?) " +
                    "ORDER BY j.name";

            List<Map<String, Object>> data = jdbcTemplate.queryForList(
                    sql,
                    "%" + skillName.toLowerCase() + "%"
            );

            if (!data.isEmpty()) {
                StringBuilder jobsBuilder = new StringBuilder();
                jobsBuilder.append("Các công việc yêu cầu kỹ năng ").append(skillName).append(":\n");
                for (int i = 0; i < data.size(); i++) {
                    String jobName = (String) data.get(i).get("job_name");
                    jobsBuilder.append("- ").append(jobName).append("\n");
                }
                return jobsBuilder.toString();
            }
        } catch (Exception e) {
            return "Lỗi khi tìm kiếm công việc: " + e.getMessage();
        }
        return "Không tìm thấy công việc yêu cầu kỹ năng: " + skillName;
    }

    private Optional<String> detectSkill(String question) {
        // Chuẩn hóa câu hỏi để so sánh
        String normalizedQuestion = question.toUpperCase();
        if (!normalizedQuestion.matches(".*(TÌM|VIỆC|CÔNG VIỆC|JOB|TUYỂN).*")) {
            return Optional.empty();
        }
        // Tìm kỹ năng trong câu hỏi
        for (String skill : SUPPORTED_SKILLS) {
            if (normalizedQuestion.contains(skill)) {
                return Optional.of(skill);
            }

            // Xử lý các biến thể viết thường/viết hoa không đầy đủ
            String simpleSkill = skill.replace(".", "").replace(" ", "");
            if (normalizedQuestion.replace(".", "").replace(" ", "").contains(simpleSkill)) {
                return Optional.of(skill);
            }
        }

        return Optional.empty();
    }
    private String formatAnswer(String answer) {
        // Thay thế các dòng trống liên tiếp bằng một dòng trống duy nhất
        answer = answer.replaceAll("(\n\\s*)+", "\n");

        // Loại bỏ dòng trống ở đầu và cuối
        answer = answer.trim();

        // Thay thế các dấu xuống dòng còn lại bằng khoảng trắng
        answer = answer.replace("\n", " ");

        // Loại bỏ khoảng trắng thừa
        answer = answer.replaceAll("\\s+", " ");

        return answer;
    }
}