package ewm.comment.repository;

import ewm.comment.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    List<Comment> findAllByEventIdAndAuthorId(Long eventId, Long authorId, Pageable pageable);

    List<Comment> findAllByAuthorId(Long authorId, Pageable pageable);
}