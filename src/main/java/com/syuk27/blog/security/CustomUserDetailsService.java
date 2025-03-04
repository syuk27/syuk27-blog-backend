package com.syuk27.blog.security;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.user.model.UserRole;
import com.syuk27.blog.domain.user.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("usernotfound"));
		
		
		
		return null;
	}
	
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("syuk27")
                                .password("{noop}1234")
                                .authorities("read")
                                .roles("USER")
                                .build();
        /**
         {
		  	"username" : "syuk27",
		  	"password" : "1234"
		 }
		 return "token": "eyJraWQiOiIyMTdkOGExOC1hYTkzLTRjNjEtODVjMy1mNTU2MzZlM2JiYzQiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoic3l1azI3IiwiZXhwIjoxNzM5NzA1MTAyLCJpYXQiOjE3Mzk2OTk3MDIsInNjb3BlIjoiUk9MRV9VU0VSIn0.PWzclLeSFS41EDsizdLwK9F_Y6goWgqI2pFVbmc9eDiSpW2QMJi59L3RiLj7NGbrWE10relnGmHMHuS90vLNiqvW3gOcS-Hy_KjZvxD42QNn6ZjueRs56ZZeFUCYEV6Dxp65jJ1DjQC9X1k5Qiuxuxu6iC0nUdBg12VnttaNDHoy_uQC769MbPNdyJ-wMTXpcLr0BgrmAWJ09EzjESYUZ-l5sPgGl05pBDMLcGAPtWqSm3UZHCayAL1jYhdn1eqmUzi4ERygoGz37Uvs7W9TeCY7Ze4t2pV0FOkUCVKL3XmIb-iNO8Hv7sAAUOIGQ2Q7p3slxyjtLZWn2p6k6hwVOQ"
		*/

        return new InMemoryUserDetailsManager(user);
    }

}
