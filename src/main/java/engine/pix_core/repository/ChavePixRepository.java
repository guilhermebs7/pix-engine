package engine.pix_core.repository;

import engine.pix_core.entity.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChavePixRepository extends JpaRepository<ChavePix,Long> {
    Optional<ChavePix> findByValorChave(String valorChave);
    boolean existsByValorChave(String valorChave);
}
