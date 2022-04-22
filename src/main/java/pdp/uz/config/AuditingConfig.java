package pdp.uz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pdp.uz.entity.User;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    AuditorAware<User> auditorAware(){
        return new SpringSecurityAuditAwareImpl();
    }
}
