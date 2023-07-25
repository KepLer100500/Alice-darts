package com.kepler.service;

import com.kepler.model.User;
import com.kepler.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    /**
     * Check, is the user already registered?
     * @param userId from dialog
     * @return True if user not found in DB
     */
    public boolean isNewUser(String userId) {
        return !userRepo.existsById(userId);
    }

    public String getNickName(String userId) {
        User user = userRepo.findById(userId).orElse(null);
        assert user != null;
        return user.getNickName();
    }

    @Transactional
    public void registerUser(String userId, String nickName) {
        userRepo.save(
                User.builder().id(userId).nickName(nickName).maxScopes(0).build()
        );
    }

    @Transactional
    public void updateMaxScopes(String id, Integer maxScopes) {
        User user = userRepo.findById(id).orElse(null);
        if(user != null) {
            user.setMaxScopes(maxScopes);
            userRepo.save(user);
        }
    }

    @Transactional
    public void renameUser(String id, String nickName) {
        User user = userRepo.findById(id).orElse(null);
        if(user != null) {
            user.setNickName(nickName);
            userRepo.save(user);
        }
    }
}
