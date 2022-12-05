package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User saveUser(User user, String role) {
        User newUser = new User();
        Set<Role> roles;
        if (role.equals("ROLE_ADMIN")) {
            roles = Set.of(new Role(2, "ROLE_USER"), new Role(1, "ROLE_ADMIN"));
        } else {
            roles = Set.of(new Role(2, "ROLE_USER"));
        }
        newUser.setName(user.getName());
        newUser.setLastName(user.getLastName());
        newUser.setAge(user.getAge());
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setId(user.getId());
        return userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void edit(User user, Integer id, String role) {
        Set<Role> rolesToChange;
        if (role.equals("ROLE_ADMIN")) {
            rolesToChange = Set.of(new Role(2, "ROLE_USER"), new Role(1, "ROLE_ADMIN"));
        } else {
            rolesToChange = Set.of(new Role(2, "ROLE_USER"));
        }

        User changedUser = new User();
        changedUser.setName(user.getName());
        changedUser.setLastName(user.getLastName());
        changedUser.setAge(user.getAge());
        changedUser.setRoles(rolesToChange);
        changedUser.setId(id);
        changedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(changedUser);
    }

    @Override
    @Transactional
    public void removeUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.getById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    //Добавление пользователя для теста функционала
   //@PostConstruct
   //public User createTestAdmin() {
   //    if (userRepository.findByUsername("admin") == null) {
   //        User user = new User("admin", "admin", 22);
   //        user.setId(1);
   //        user.setPassword(passwordEncoder.encode("admin"));
   //        user.addRole(new Role(user.getId(), "ROLE_ADMIN"));
   //        user.addRole(new Role(user.getId(), "ROLE_USER"));
   //        return userRepository.save(user);
   //    }
   //    return null;
   //}
}
