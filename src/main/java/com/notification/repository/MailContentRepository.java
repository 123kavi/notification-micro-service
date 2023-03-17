package com.notification.repository;

import com.notification.entity.MailContent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailContentRepository extends JpaRepository<MailContent, Long> {

    @Cacheable(key = "#templateName", value = "firstByTemplateName")
    MailContent findFirstByTemplateName(String templateName);

    boolean existsByTemplateNameAndContentLike(String templateName, String contentPattern);

    MailContent findByTemplateNameEquals(String templateName);
}
