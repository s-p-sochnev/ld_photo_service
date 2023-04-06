package org.spsochnev.photo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;

@Configuration
public class IntegrationConfig {

    @Bean
    DefaultFtpSessionFactory defaultFtpSessionFactory(
            @Value("${ftp.host}") String host,
            @Value("${ftp.port}") int port,
            @Value("${ftp.username}") String username,
            @Value("${ftp.password}") String pw,
            @Value("${ftp.encoding}") String encoding) {
        DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
        defaultFtpSessionFactory.setHost(host);
        defaultFtpSessionFactory.setPort(port);
        defaultFtpSessionFactory.setPassword(pw);
        defaultFtpSessionFactory.setUsername(username);
        defaultFtpSessionFactory.setControlEncoding(encoding);
        return defaultFtpSessionFactory;
    }

    @Bean
    FtpRemoteFileTemplate ftpRemoteFileTemplate(DefaultFtpSessionFactory dsf) {
        return new FtpRemoteFileTemplate(dsf);
    }

}
