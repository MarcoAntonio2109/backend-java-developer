package com.cmanager.app.authentication.service;

import com.cmanager.app.authentication.data.TokenResponse;
import com.cmanager.app.authentication.domain.User;
import com.cmanager.app.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ShowTvService imple{

    @Autowride
    private RestTemplate restTemplate;

    private ShowTvRepository repository;

    public AuthenticationService() {

    }
    @PreAutorize("validarPerfil(codPerfil)")
    public String obtemSingleSearch (String nome, codPerfil){
        var url = "https://api.tvmaze.com/singlesearch/shows?q=";
        return restTemplate.getForObject(url.concat(nome), String.class);
    }

    public Page<DTO> paginacao (Pageable pageable){
        Page<ShowTv> pageEntity = repository.findAll(pageable);

    }



}
