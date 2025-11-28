package com.puxinxiaolin.adopt.utils;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.google.gson.Gson;
import com.puxinxiaolin.adopt.config.SmsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsSenderUtil {

    private final SmsConfig smsConfig;

    // TODO [YCcLin 2025/11/28]: 待完善
    public boolean sendSmsCodeAsync() throws ExecutionException, InterruptedException {
        // HttpClient Configuration
//        HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
//                .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout time, the default is 10 seconds
//                .responseTimeout(Duration.ofSeconds(10)) // Set the response timeout time, the default is 20 seconds
//                .maxConnections(128) // Set the connection pool size
//                .maxIdleTimeOut(Duration.ofSeconds(50)) // Set the connection pool timeout, the default is 30 seconds
//                // Configure the proxy
//                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
//                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
//                // If it is an https connection, you need to configure the certificate, or ignore the certificate(.ignoreSSL(true))
//                .x509TrustManagers(new X509TrustManager[]{})
//                .keyManagers(new KeyManager[]{})
//                .ignoreSSL(false)
//                .build();

        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
                .accessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"))
                .build());

        try (AsyncClient client = AsyncClient.builder()
                .region("ap-southeast-1")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dypnsapi.aliyuncs.com")
                )
                .build()) {
            SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                    .signName("速通互联验证码")
                    .templateCode("100001")
                    .phoneNumber("13063184972")
                    .templateParam("{\"code\":\"##code##\",\"min\":\"5\"}")
                    .build();
            CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
            SendSmsVerifyCodeResponse resp = response.get();
            log.info("发送短信成功, 响应结果: {}", new Gson().toJson(resp));

            return true;
        } catch (Exception e) {
            log.error("发送短信失败", e);
            return false;
        }
    }

//    public boolean sendLoginCode(String phone, String code) {
//        try {
//            if (isConfigured()) {
//                Config config = new Config()
//                        .setEndpoint("dysmsapi.aliyuncs.com")
//                        .setAccessKeyId(smsConfig.getAccessKeyId())
//                        .setAccessKeySecret(smsConfig.getAccessKeySecret());
//                Client client = new Client(config);
//                SendSmsRequest req = new SendSmsRequest()
//                        .setPhoneNumbers(phone)
//                        .setSignName(smsConfig.getSignName())
//                        .setTemplateCode(smsConfig.getTemplateCode().getLogin())
//                        .setTemplateParam("{\"code\":\"" + code + "\"}");
//                SendSmsResponse resp = client.sendSms(req);
//                log.info("[SMS] 阿里云发送结果: {}", resp.getBody());
//                return resp.getBody() != null && "OK".equalsIgnoreCase(resp.getBody().getCode());
//            }
//            // 未配置, 模拟发送, 保证系统可跑
//            log.warn("[SMS] 未配置阿里云密钥, 模拟发送登录验证码到 {}: code={}", phone, code);
//            return true;
//        } catch (Exception e) {
//            log.error("发送短信失败", e);
//            return false;
//        }
//    }

    private boolean isConfigured() {
        return smsConfig.getAccessKeyId() != null && !smsConfig.getAccessKeyId().isEmpty()
                && smsConfig.getAccessKeySecret() != null && !smsConfig.getAccessKeySecret().isEmpty();
    }

}