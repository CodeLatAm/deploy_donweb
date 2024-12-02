package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.user.UserBasicInfoResponse;
import com.abel.sistema_gestion.dto.user.UserStatusResponse;
import com.abel.sistema_gestion.dto.user.UserUpdateRequest;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.exception.UserNotFoundException;
import com.abel.sistema_gestion.mapper.UserMapper;
import com.abel.sistema_gestion.repository.UserRepository;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private final VendorService vendorService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;

        this.userMapper = userMapper;

    }

    @Override
    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Override
    public User getUserByUserId(Integer userId) {

        return userRepository.findById(userId).orElseThrow(() ->{
           return new UserNotFoundException("Usuario no encontrado");

        });
    }

    @Override
    public UserBasicInfoResponse getInfoBasicUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User no encontrado con id: " + userId));
        return userMapper.mapToBasicUserResponse(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        Optional<User> user = userRepository.findByUsername(userEmail);
        if(user.isEmpty()){
            throw new UserNotFoundException("Usuario no encontrado con email: " + userEmail);
        }
        return user.get();
    }

    @Override
    public List<User> getPremiumUsers() {
        return userRepository.findByIsPremium(true);
    }

    @Override
    public UserStatusResponse getStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));
        return  userMapper.mapToUserStatusResponse(user);

    }

    @Override
    public List<UserBasicInfoResponse> getAllUsersByAdmin() {
        List<User> users = userRepository.findAllOrderByName();
        return userMapper.mapToListUsers(users);
    }

    @Override
    public Long countUsers() {
        return userRepository.count();
    }

    @Transactional
    @Override
    public void update(Integer id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Usuario no encontrado con id: " + id));
        user.setName(request.getName());
        user.setUserStatus(request.getUserStatus());
        user.setCompanyName(request.getCompanyName());
        user.setUsername(request.getUsername());
        userRepository.save(user);
    }


}
