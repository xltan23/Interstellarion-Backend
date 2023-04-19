package sg.edu.nus.iss.server.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AppConfig {

    // AMAZON S3 CLOUD
    @Value("${spaces.secret.key}")
    private String spacesSecretKey;
    @Value("${spaces.access.key}")
    private String spacesAccessKey;
    @Value("${spaces.endpoint.url}")
    private String spacesEndpointUrl;
    @Value("${spaces.endpoint.region}")
    private String spacesEndpointRegion;

    // SET UP BEAN
    @Bean
    public AmazonS3 initS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(spacesAccessKey, spacesSecretKey);
        EndpointConfiguration endpointConfig = new EndpointConfiguration(spacesEndpointUrl, spacesEndpointRegion);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfig)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
