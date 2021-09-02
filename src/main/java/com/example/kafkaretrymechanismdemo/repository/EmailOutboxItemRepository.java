package com.example.kafkaretrymechanismdemo.repository;

import com.example.kafkaretrymechanismdemo.entity.EmailOutboxItem;
import com.example.kafkaretrymechanismdemo.enums.EmailOutboxItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EmailOutboxItemRepository extends JpaRepository<EmailOutboxItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<EmailOutboxItem> findFirstBySendDateIsLessThanEqualAndStatusInOrderByOrderDesc(final Date sendDateLessThan,
                                                                                            final List<EmailOutboxItemStatus> statuses);
}
