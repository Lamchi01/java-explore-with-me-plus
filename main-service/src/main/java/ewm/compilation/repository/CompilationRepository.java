package ewm.compilation.repository;

import ewm.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
