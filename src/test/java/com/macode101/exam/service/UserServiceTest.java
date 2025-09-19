package com.macode101.exam.service;

import com.macode101.exam.exception.UserNotFoundException;
import com.macode101.exam.mapper.UserMapper;
import com.macode101.exam.model.User;
import com.macode101.exam.model.UserDto;
import com.macode101.exam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDto testUserDto;
    private List<User> testUsers;
    private List<UserDto> testUserDtos;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setUsername("johndoe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPhone("123-456-7890");
        testUser.setWebsite("www.johndoe.com");

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setName("John Doe");
        testUserDto.setUsername("johndoe");
        testUserDto.setEmail("john.doe@example.com");
        testUserDto.setPhone("123-456-7890");
        testUserDto.setWebsite("www.johndoe.com");

        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setName("Jane Smith");
        testUser2.setUsername("janesmith");
        testUser2.setEmail("jane.smith@example.com");
        testUser2.setPhone("098-765-4321");
        testUser2.setWebsite("www.janesmith.com");

        UserDto testUserDto2 = new UserDto();
        testUserDto2.setId(2L);
        testUserDto2.setName("Jane Smith");
        testUserDto2.setUsername("janesmith");
        testUserDto2.setEmail("jane.smith@example.com");
        testUserDto2.setPhone("098-765-4321");
        testUserDto2.setWebsite("www.janesmith.com");

        testUsers = Arrays.asList(testUser, testUser2);
        testUserDtos = Arrays.asList(testUserDto, testUserDto2);
    }

    @Test
    void getAllUsersShouldReturnAllUsersAsDtos() {
        when(userRepository.findAll()).thenReturn(testUsers);
        when(userMapper.toDto(testUsers)).thenReturn(testUserDtos);

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(testUserDtos);
        
        verify(userRepository).findAll();
        verify(userMapper).toDto(testUsers);
    }

    @Test
    void getAllUsersShouldReturnEmptyList_WhenNoUsersExist() {
        List<User> emptyUserList = Arrays.asList();
        List<UserDto> emptyUserDtoList = Arrays.asList();
        when(userRepository.findAll()).thenReturn(emptyUserList);
        when(userMapper.toDto(emptyUserList)).thenReturn(emptyUserDtoList);

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(userRepository).findAll();
        verify(userMapper).toDto(emptyUserList);
    }

    @Test
    void getUserByIdShouldReturnUserDto_WhenUserExists() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.getUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getUsername()).isEqualTo("johndoe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        
        verify(userRepository).findById(userId);
        verify(userMapper).toDto(testUser);
    }

    @Test
    void getUserByIdShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: " + userId);
        
        verify(userRepository).findById(userId);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    void createUserShouldCreateAndReturnNewUser_WithGeneratedId() {
        UserDto inputDto = new UserDto();
        inputDto.setName("New User");
        inputDto.setUsername("newuser");
        inputDto.setEmail("new.user@example.com");
        inputDto.setPhone("555-123-4567");
        inputDto.setWebsite("www.newuser.com");

        User inputUser = new User();
        inputUser.setName("New User");
        inputUser.setUsername("newuser");
        inputUser.setEmail("new.user@example.com");
        inputUser.setPhone("555-123-4567");
        inputUser.setWebsite("www.newuser.com");

        User savedUser = new User();
        savedUser.setId(3L); // Generated ID
        savedUser.setName("New User");
        savedUser.setUsername("newuser");
        savedUser.setEmail("new.user@example.com");
        savedUser.setPhone("555-123-4567");
        savedUser.setWebsite("www.newuser.com");

        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(3L);
        savedUserDto.setName("New User");
        savedUserDto.setUsername("newuser");
        savedUserDto.setEmail("new.user@example.com");
        savedUserDto.setPhone("555-123-4567");
        savedUserDto.setWebsite("www.newuser.com");

        when(userMapper.toEntity(inputDto)).thenReturn(inputUser);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);

        UserDto result = userService.createUser(inputDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("New User");
        assertThat(result.getUsername()).isEqualTo("newuser");
        assertThat(result.getEmail()).isEqualTo("new.user@example.com");
        
        verify(userMapper).toEntity(inputDto);
        verify(userRepository).save(argThat(user -> user.getId() == null)); // Verify ID is set to null
        verify(userMapper).toDto(savedUser);
    }

    @Test
    void updateUserShouldUpdateAndReturnExistingUser() {
        Long userId = 1L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updated.user@example.com");
        updateDto.setPhone("555-999-8888");
        updateDto.setWebsite("www.updateduser.com");

        User updateUser = new User();
        updateUser.setName("Updated Name");
        updateUser.setUsername("updateduser");
        updateUser.setEmail("updated.user@example.com");
        updateUser.setPhone("555-999-8888");
        updateUser.setWebsite("www.updateduser.com");

        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setName("Updated Name");
        savedUser.setUsername("updateduser");
        savedUser.setEmail("updated.user@example.com");
        savedUser.setPhone("555-999-8888");
        savedUser.setWebsite("www.updateduser.com");

        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(userId);
        savedUserDto.setName("Updated Name");
        savedUserDto.setUsername("updateduser");
        savedUserDto.setEmail("updated.user@example.com");
        savedUserDto.setPhone("555-999-8888");
        savedUserDto.setWebsite("www.updateduser.com");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userMapper.toEntity(updateDto)).thenReturn(updateUser);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);

        UserDto result = userService.updateUser(userId, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getUsername()).isEqualTo("updateduser");
        assertThat(result.getEmail()).isEqualTo("updated.user@example.com");
        
        verify(userRepository).existsById(userId);
        verify(userMapper).toEntity(updateDto);
        verify(userRepository).save(argThat(user -> user.getId().equals(userId)));
        verify(userMapper).toDto(savedUser);
    }

    @Test
    void updateUserShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        Long userId = 999L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updated.user@example.com");

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.updateUser(userId, updateDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: " + userId);
        
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUserShouldDeleteExistingUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUserShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: " + userId);
        
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
