package com.fincons.taskmanager.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long attachmentId;

    @Column(nullable = false)
    private String attachmentName;

    @Column(name = "file64", length = 7026356)
    private String file64;

    @Column(nullable = false)
    private String extension;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Attachment(String attachmentName, String file64, String extension, Task task) {
        this.attachmentName = attachmentName;
        this.file64 = file64;
        this.extension = extension;
        this.task = task;
    }

    public Attachment(String attachmentName, Task task) {
        this.attachmentName = attachmentName;
        this.task = task;
    }


}
