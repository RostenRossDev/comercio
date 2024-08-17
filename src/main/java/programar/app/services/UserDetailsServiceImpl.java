package programar.app.services;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import programar.app.entities.CustomUser;
import programar.app.entities.Role;
import programar.app.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser usuario=userRepository.findByUsername(username);

        if(usuario==null) {
            log.error("Error login: no existe el usuario '"+username+"'");
            throw new UsernameNotFoundException("username "+username+" no existe en el sistema" );
        }

        List<GrantedAuthority> authorities= new ArrayList<>();
        for (Role role  : usuario.getRoles()) {
            log.info("Role: "+role.getAuthority());
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        if(authorities.isEmpty()) {
            log.error("Error login: usuario '"+username+"' no tiene roles asignados");
            throw new UsernameNotFoundException("Error login: usuario '"+username+"' no tiene roles asignados");
        }
        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }
}