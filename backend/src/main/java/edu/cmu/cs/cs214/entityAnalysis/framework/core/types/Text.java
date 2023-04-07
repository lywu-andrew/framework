package main.java.edu.cmu.cs.cs214.entityAnalysis.framework.core.types;

import java.time.LocalDateTime;

public class Text {
    private String content;
    private LocalDateTime datetime;

    public Text(String content, LocalDateTime datetime) {
        this.content = content;
        this.datetime = datetime;
    }

    /**
     * Gets the content of this text.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Gets the datetime.
     */
    public LocalDateTime getDatetime() {
        return this.datetime;
    }
}