package service.user_service.repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.user_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT u FROM User u WHERE u.name LIKE :x ")
    Page<User> searchByName(@Param("x") String s, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber")
    Page<User> searchByPhoneNumber(@Param("phoneNumber") String phoneNumber, Pageable pageable);


    boolean existsByUsername(String username);
    User findByUsername(String username);

}
