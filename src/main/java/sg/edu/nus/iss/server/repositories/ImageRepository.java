package sg.edu.nus.iss.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class ImageRepository {
    
    @Value("${spaces.endpoint.bucket}")
    private String spacesBucket;

    @Value("${spaces.endpoint.url}")
    private String spacesEndpointUrl;

    @Autowired
    private AmazonS3 s3;

    public String uploadImage(String planetName, MultipartFile image) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(spacesBucket, planetName, image.getInputStream(), metadata);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putObjectRequest);
        } catch (Exception ex) {
            System.out.println();
        }
        String imageUrl = "https://%s.%s/%s".formatted(spacesBucket,spacesEndpointUrl,planetName);
        return imageUrl;
    }
}
