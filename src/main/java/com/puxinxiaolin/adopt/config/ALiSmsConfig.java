package com.puxinxiaolin.adopt.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ALiSmsConfig {
    private final SmsProperty smsProperty;

    @Bean
    public AsyncClient asyncClient() {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(smsProperty.getAccessKey())
                .accessKeySecret(smsProperty.getSecretKey())
                .build());

        return AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create().setEndpointOverride("dysmsapi.aliyuncs.com")
                ).build();
    }

}
