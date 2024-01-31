package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;
import com.example.instagram.dto.inputs.PostInput;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.exception.ResourceNotFoundException;
import com.example.instagram.mappers.ModelMapper;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import com.example.instagram.repository.LikeRepository;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ImageService imageService;

    @Autowired
    private LikeService likeService;

    @Override
    public List<PostResponse> getAllPostsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        List<Post> postList = postRepository.findAllByUser_IdOrderByCreateDateTimeDesc(user.getId());
        List<PostResponse> postResponseList = new ArrayList<>();
        for(Post post : postList){
            PostResponse postResponse = ModelMapper.postToPostResponse(post);
            postResponse.setImage(getBase64ImageFromImagePath(post.getImageName(), ImageType.POST));
            postResponseList.add(postResponse);
        }
        return postResponseList;
    }

    @Override
    public PostResponse getPostById(Long id, String authorization) {
        User user = getUserFromAuthorizationToken(authorization);
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found with id:"+id));
        PostResponse postResponse = ModelMapper.postToPostResponse(post);
        postResponse.setImage(getBase64ImageFromImagePath(post.getImageName(), ImageType.POST));
        postResponse.setLikes(likeRepository.countAllByPost_Id(post.getId()));
        postResponse.setLikedByCurrentUser(likeRepository.existsByPost_IdAndUser_Id(post.getId(), user.getId()));
        postResponse.getUser().setProfilePic(getBase64ImageFromImagePath(postResponse.getUser().getProfilePic(), ImageType.PROFILE_PIC));
        return postResponse;
    }

    @Override
    @Transactional
    public PostResponse createPost(PostInput postInput, MultipartFile postPic, String authorization) throws IOException {
        User user = getUserFromAuthorizationToken(authorization);
        Post post = ModelMapper.postInputToPost(postInput);
        post.setImageName(imageService.uploadImage(postPic, ImageType.POST));
        post.setUser(user);
        post = postRepository.save(post);
        return ModelMapper.postToPostResponse(post);
    }

    @Override
    public ResponseMessage likePost(long postId, String authorization) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: "+postId));
        return likeService.likePost(post,authorization);
    }

    @Override
    public ResponseMessage unlikePost(long postId, String authorization) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: "+postId));
        return likeService.unlikePost(post,authorization);
    }

    private long getUserIdFromAuthorizationToken(String authorization){
        String strId = jwtTokenUtil.extractClaimValue(authorization, JwtTokenUtil.JWT_ID);
        return Long.parseLong(strId);
    }
    private User getUserFromAuthorizationToken(String authorization){
        long id = getUserIdFromAuthorizationToken(authorization);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }

    private String getBase64ImageFromImagePath(String imageName, ImageType imageType){
        if(imageName!=null) {
            try {
                String base64Image = imageService.getBase64ImageByName(imageName, imageType);
                if (base64Image != null) {
                    return "data:image/jpeg;base64," + base64Image;
                }
            } catch (IOException ioException) {
                logger.error(ioException.toString());
            }
        }
        return null;
    }
}
