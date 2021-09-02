package com.example.kafkaretrymechanismdemo.entity;

import com.example.kafkaretrymechanismdemo.enums.EmailOutboxItemStatus;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "email_outbox_items_id_seq", allocationSize = 1, initialValue = 1)
@Table(name = "email_outbox_items")
@ToString
public class EmailOutboxItem {
    @Id
    @GeneratedValue(generator = "email_outbox_items_id_seq", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "\"to\"")
    private String to;
    private String bcc;
    private String cc;
    private String templateName;
    private String parameters;
    private String errorMessage;

    @Builder.Default
    @Column(name = "\"order\"")
    private int order = 0;

    @Builder.Default
    private Date insertDate = DateTime.now().toDate();

    private Date sendDate;

    @Enumerated(EnumType.STRING)
    private EmailOutboxItemStatus status;
}
