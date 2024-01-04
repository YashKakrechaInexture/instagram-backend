package com.example.instagram.service;

import com.example.instagram.dto.inputs.UserInput;
import com.example.instagram.dto.response.UserProfileResponse;
import com.example.instagram.exception.ResourceNotFoundException;
import com.example.instagram.mappers.ModelMapper;
import com.example.instagram.model.User;
import com.example.instagram.repository.FollowRepository;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id:"+id));
    }

    @Override
    public User saveUser(UserInput userInput) {
//        if(!Boolean.parseBoolean(jwtTokenUtil.getClaimFromToken(authorization.substring(7), claims -> claims.get(JwtTokenUtil.JWT_ROLE)).toString())){
//            throw new RuntimeException("You do not have admin permissions!");
//        }
        User user = new User();
        user.setEmail(userInput.getEmail());
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserProfileResponse getUserProfile(String authorization) {
        String strId = jwtTokenUtil.extractClaimValue(authorization, JwtTokenUtil.JWT_ID);
        long id = Long.parseLong(strId);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id:"+id));
        UserProfileResponse userProfileResponse = ModelMapper.userToUserProfileResponse(user);
        long postCount = postRepository.countByUser_Id(id);
        long followers = followRepository.countByFollowingToUser_Id(id);
        long following = followRepository.countByFollowerUser_Id(id);
        userProfileResponse.setPostCount(postCount);
        userProfileResponse.setFollowers(followers);
        userProfileResponse.setFollowing(following);
        return userProfileResponse;
    }
}
