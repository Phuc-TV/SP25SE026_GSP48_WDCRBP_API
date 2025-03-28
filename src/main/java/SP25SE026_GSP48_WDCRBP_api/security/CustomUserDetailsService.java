package SP25SE026_GSP48_WDCRBP_api.security;

import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
        //allow user to log in by username or email
        User user = userRepository.findUserByEmailOrPhone(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + emailOrPhone));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);

        return new org.springframework.security.core.userdetails.User(emailOrPhone, user.getPassword(), authorities);
    }
}
