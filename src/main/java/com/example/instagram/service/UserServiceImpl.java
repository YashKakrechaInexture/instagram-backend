package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;
import com.example.instagram.dto.inputs.UserInput;
import com.example.instagram.dto.response.ResponseMessage;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private ImageService imageService;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id:"+id));
    }

    @Override
    public ResponseMessage saveUser(UserInput userInput) {
        if(userRepository.existsByEmail(userInput.getEmail())){
            throw new ResourceNotFoundException("User already exist with Email : "+userInput.getEmail());
        }
        if(userRepository.existsByUsername(userInput.getUsername())){
            throw new ResourceNotFoundException("User already exist with Username : "+userInput.getUsername());
        }
        User user = new User();
        user.setEmail(userInput.getEmail());
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        userRepository.save(user);
        return new ResponseMessage("Email Sent in your email address.");
    }

    @Override
    public UserProfileResponse getUserProfile(String authorization) {
        User user = getUserFromAuthorizationToken(authorization);
        UserProfileResponse userProfileResponse = ModelMapper.userToUserProfileResponse(user);
        long postCount = postRepository.countByUser_Id(user.getId());
        long followers = followRepository.countByFollowingToUser_Id(user.getId());
        long following = followRepository.countByFollowerUser_Id(user.getId());
        userProfileResponse.setPostCount(postCount);
        userProfileResponse.setFollowers(followers);
        userProfileResponse.setFollowing(following);
        try {
            String base64Image = imageService.getBase64ImageByName(user.getProfilePic(), ImageType.PROFILE_PIC);
            if(base64Image!=null){
                userProfileResponse.setProfilePic("data:image/jpeg;base64,"+base64Image);
            }
        }catch(IOException ioException){
            System.out.println(ioException);
        }
        return userProfileResponse;
    }

    @Override
    public ResponseMessage updateProfilePic(MultipartFile profilePic, String authorization) {
        User user = getUserFromAuthorizationToken(authorization);
        String imageName = null;
        try {
            imageName = imageService.uploadImage(profilePic, ImageType.PROFILE_PIC);
        }catch(IOException ioException){
            return new ResponseMessage("Exception Occurred: " + ioException);
        }
        if(user.getProfilePic()!=null){
            if(!imageService.deleteImage(user.getProfilePic(), ImageType.PROFILE_PIC)){
                new ResponseMessage("Old Profile-Pic Not Found.");
            }
        }
        user.setProfilePic(imageName);
        userRepository.save(user);
        return new ResponseMessage("Profile-Pic Updated Successfully.");
    }

    private User getUserFromAuthorizationToken(String authorization){
        String strId = jwtTokenUtil.extractClaimValue(authorization, JwtTokenUtil.JWT_ID);
        long id = Long.parseLong(strId);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }
}
