package com.example.newsapp.modules.account.service;

import com.example.newsapp.modules.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {
  private final UserRepository repo;
  @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var u = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not found"));
    return org.springframework.security.core.userdetails.User
      .withUsername(u.getEmail())
      .password(u.getPasswordHash())
      .authorities("ROLE_USER")
      .accountLocked(false).disabled(false).build();
  }
}
