package com.example.social.media.util.somethingElse;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EnumUpdater {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void updateEnum() {
        try {
            // Kiểm tra xem enum có chứa giá trị DELETE chưa
            String checkSql = "SELECT column_type FROM information_schema.columns " +
                    "WHERE table_name = 'post' AND column_name = 'visibility'";

            String enumValues = jdbcTemplate.queryForObject(checkSql, String.class);

            if (enumValues != null && !enumValues.contains("DELETE")) {
                // Nếu chưa có DELETE, thêm giá trị mới vào enum
                String sql = "ALTER TABLE post MODIFY COLUMN visibility ENUM('PUBLIC', 'PRIVATE', 'FRIEND', 'DELETE') NOT NULL";
                jdbcTemplate.execute(sql);
                System.out.println("Enum visibility đã được cập nhật!");
            } else {
                System.out.println("Enum visibility đã có giá trị DELETE, không cần cập nhật.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật ENUM: " + e.getMessage());
        }
    }
}