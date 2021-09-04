package com.example.kafkaretrymechanismdemo.repository;

import com.example.kafkaretrymechanismdemo.entity.EmailOutboxItem;
import com.example.kafkaretrymechanismdemo.enums.EmailOutboxItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EmailOutboxItemRepository extends JpaRepository<EmailOutboxItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="-2")})
    Optional<EmailOutboxItem> findFirstBySendDateIsLessThanEqualAndStatusInOrderByOrderDesc(final Date sendDateLessThan,
                                                                                            final List<EmailOutboxItemStatus> statuses);
}
