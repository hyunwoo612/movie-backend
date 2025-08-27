package com.cocoh.movie.repository;

import com.cocoh.movie.Entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsername(String username);
    Optional<RefreshToken> findRefreshTokenByRefreshToken(String refreshToken);
}
