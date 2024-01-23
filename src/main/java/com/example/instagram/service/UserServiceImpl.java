package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;
import com.example.instagram.dto.inputs.EnableUserInput;
import com.example.instagram.dto.inputs.UserSignupInput;
import com.example.instagram.dto.internal.EmailDetails;
import com.example.instagram.dto.projections.SearchUserProjection;
import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.dto.response.SearchUserResponse;
import com.example.instagram.dto.response.UserFollowDTO;
import com.example.instagram.dto.response.UserProfileResponse;
import com.example.instagram.exception.ResourceNotFoundException;
import com.example.instagram.mappers.ModelMapper;
import com.example.instagram.model.Follow;
import com.example.instagram.model.User;
import com.example.instagram.repository.FollowRepository;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

    @Autowired
    private EmailService emailService;

    @Value("${server.url}")
    private String serverUrl;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }

    @Override
    @Transactional
    public ResponseMessage saveUser(UserSignupInput userSignupInput) {
        userSignupInput.setUsername(userSignupInput.getUsername().toLowerCase());
        if(userRepository.existsByEmail(userSignupInput.getEmail())){
            throw new ResourceNotFoundException("User already exist with Email: "+ userSignupInput.getEmail());
        }
        if(userRepository.existsByUsername(userSignupInput.getUsername())){
            throw new ResourceNotFoundException("User already exist with Username: "+ userSignupInput.getUsername());
        }
        User user = new User();
        user.setEmail(userSignupInput.getEmail());
        user.setUsername(userSignupInput.getUsername());
        user.setFullName(userSignupInput.getFullName());
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(userSignupInput.getPassword()));
        user.setEnabled(false);
        user.setOtp(UUID.randomUUID().toString());
        userRepository.save(user);
        return sendSignupMail(user);
    }

    @Override
    @Transactional
    public ResponseMessage enableUser(EnableUserInput enableUserInput) {
        User user = userRepository.findByEmail(enableUserInput.getEmail());
        if(user==null){
            throw new ResourceNotFoundException("User not found with email: "+enableUserInput.getEmail());
        }
        if(!user.getOtp().equals(enableUserInput.getOtp())){
            throw new ResourceNotFoundException("Invalid OTP!");
        }
        user.setOtp(null);
        user.setEnabled(true);
        userRepository.save(user);
        return new ResponseMessage("User Enabled!");
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
        userProfileResponse.setProfilePic(getBase64ImageFromImagePath(user.getProfilePic()));
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

    @Override
    public List<SearchUserResponse> searchUserByUsername(String searchUsername, String authorization) {
        User user = getUserFromAuthorizationToken(authorization);
        List<SearchUserProjection> searchUserProjectionList = userRepository.findByUsernameStartsWithAndUsernameNot(searchUsername, user.getUsername());
        List<SearchUserResponse> searchUserResponseList = new ArrayList<>();
        for(SearchUserProjection searchUserProjection : searchUserProjectionList){
            SearchUserResponse searchUserResponse = ModelMapper.searchUserProjectionToSearchUserResponse(searchUserProjection);
            searchUserResponse.setProfilePic(getBase64ImageFromImagePath(searchUserProjection.getProfilePic()));
            searchUserResponse.setFollowing(followRepository.existsByFollowerUser_IdAndFollowingToUser_Id(user.getId(), searchUserProjection.getId()));
            searchUserResponseList.add(searchUserResponse);
        }
        return searchUserResponseList;
    }

    @Override
    public ResponseMessage followUser(String followUsername, String authorization) {
        User user = getUserFromAuthorizationToken(authorization);
        User followingToUser = userRepository.findByUsername(followUsername);
        if(followingToUser==null){
            throw new ResourceNotFoundException("User not found with username: "+followUsername);
        }
        if(user.getId()==followingToUser.getId()){
            throw new IllegalArgumentException("You cannot follow yourself.");
        }
        if(followRepository.existsByFollowerUser_IdAndFollowingToUser_Id(user.getId(), followingToUser.getId())){
            throw new IllegalArgumentException("Already following.");
        }
        Follow follow = new Follow();
        follow.setFollowerUser(user);
        follow.setFollowingToUser(followingToUser);
        followRepository.save(follow);
        return new ResponseMessage("Followed: "+followUsername);
    }

    @Override
    public ResponseMessage unfollowUser(String followUsername, String authorization) {
        User user = getUserFromAuthorizationToken(authorization);
        User followingToUser = userRepository.findByUsername(followUsername);
        if(followingToUser==null){
            throw new ResourceNotFoundException("User not found with username: "+followUsername);
        }
        if(user.getId()==followingToUser.getId()){
            throw new IllegalArgumentException("You cannot unfollow yourself.");
        }
        Follow follow = followRepository.findByFollowerUser_IdAndFollowingToUser_Id(user.getId(), followingToUser.getId());
        if(follow==null){
            throw new IllegalArgumentException("You are not following: "+followUsername);
        }
        followRepository.delete(follow);
        return new ResponseMessage("UnFollowed: "+followUsername);
    }

    @Override
    public List<UserFollowDTO> followersList(String username, String authorization) {
        long tokenUserId = getUserIdFromAuthorizationToken(authorization);
        User searchUser = userRepository.findByUsername(username);
        List<Follow> followersList = followRepository.findAllByFollowingToUser_Id(searchUser.getId());
        List<UserFollowDTO> followers = new ArrayList<>();
        for(Follow follower : followersList){
            UserFollowDTO userFollowDTO = ModelMapper.userToUserFollowDTO(follower.getFollowerUser());
            userFollowDTO.setProfilePic(getBase64ImageFromImagePath(userFollowDTO.getProfilePic()));
            userFollowDTO.setFollowing(followRepository.existsByFollowerUser_IdAndFollowingToUser_Id(tokenUserId,follower.getFollowerUser().getId()));
            userFollowDTO.setSelfUser(follower.getFollowerUser().getId() == tokenUserId);
            followers.add(userFollowDTO);
        }
        return followers;
    }

    @Override
    public List<UserFollowDTO> followingList(String username, String authorization) {
        long tokenUserId = getUserIdFromAuthorizationToken(authorization);
        User searchUser = userRepository.findByUsername(username);
        List<Follow> followingList = followRepository.findAllByFollowerUser_Id(searchUser.getId());
        List<UserFollowDTO> following = new ArrayList<>();
        for(Follow follower : followingList){
            UserFollowDTO userFollowDTO = ModelMapper.userToUserFollowDTO(follower.getFollowingToUser());
            userFollowDTO.setProfilePic(getBase64ImageFromImagePath(userFollowDTO.getProfilePic()));
            userFollowDTO.setFollowing(followRepository.existsByFollowerUser_IdAndFollowingToUser_Id(tokenUserId,follower.getFollowingToUser().getId()));
            userFollowDTO.setSelfUser(follower.getFollowingToUser().getId() == tokenUserId);
            following.add(userFollowDTO);
        }
        return following;
    }

    private List<Follow> getFollowersList(String authorization){
        long userId = getUserIdFromAuthorizationToken(authorization);
        return followRepository.findAllByFollowingToUser_Id(userId);
    }

    //todo : use this in get post list
    private List<Follow> getFollowingList(String authorization){
        long userId = getUserIdFromAuthorizationToken(authorization);
        return followRepository.findAllByFollowerUser_Id(userId);
    }

    private long getUserIdFromAuthorizationToken(String authorization){
        String strId = jwtTokenUtil.extractClaimValue(authorization, JwtTokenUtil.JWT_ID);
        return Long.parseLong(strId);
    }
    private User getUserFromAuthorizationToken(String authorization){
        long id = getUserIdFromAuthorizationToken(authorization);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }

    private String getBase64ImageFromImagePath(String imageName){
        if(imageName!=null) {
            try {
                String base64Image = imageService.getBase64ImageByName(imageName, ImageType.PROFILE_PIC);
                if (base64Image != null) {
                    return "data:image/jpeg;base64," + base64Image;
                }
            } catch (IOException ioException) {
                logger.error(ioException.toString());
            }
        }
        return null;
    }

    private ResponseMessage sendSignupMail(User user){
        try {
            Path newAccountTemplateFile = Path.of(UserServiceImpl.class.getResource("/templates/signup-template.html").getPath());
            String template = Files.readString(newAccountTemplateFile);

            template = template.replace("{{fullName}}", user.getFullName());
            template = template.replace("{{username}}", user.getUsername());
            template = template.replace("{{email}}", user.getEmail());
            template = template.replace("{{otp}}", user.getOtp());
            template = template.replaceAll("appURL", serverUrl);

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(user.getEmail());
            emailDetails.setMsgBody(template);
            emailDetails.setSubject("Instagram User Registration");
            return new ResponseMessage(emailService.sendMail(emailDetails));
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResourceNotFoundException("Email Template Not Found.");
        }
    }
}
