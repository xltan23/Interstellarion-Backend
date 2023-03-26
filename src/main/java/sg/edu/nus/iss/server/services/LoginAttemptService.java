package sg.edu.nus.iss.server.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {

    private static final int MAXIMUM_NUMBER_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> loginAttemptCache;

    // CONSTRUCTOR
    // Expires after 15 minutes
    // Allow 100 User records at any time
    public LoginAttemptService() {
        super();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        // Default attempt value tied to key: 0
                        return 0;
                    }
                });
    }

    public void removeUserFromLoginAttemptCache(String email) {
        loginAttemptCache.invalidate(email);
    }

    public void addUserToLoginAttemptCache(String email) {
        int attempts = 0;
        try {
        // 1 + attempt value tied to key, update the attempt value each time method is called
        attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(email);
        loginAttemptCache.put(email, attempts);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public boolean hasExceedMaxAttempt(String email) {
        try {
            return loginAttemptCache.get(email) >= MAXIMUM_NUMBER_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
